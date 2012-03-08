package com.welocally.geodb.services.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.DeleteItemRequest;
import com.amazonaws.services.dynamodb.model.DeleteItemResult;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.PutItemRequest;
import com.amazonaws.services.dynamodb.model.PutItemResult;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.welocally.geodb.services.db.DbException.Type;

@Component("dynamoJsonDatabase")
public class DynamoJsonDatabase implements JsonDatabase {

	static Logger logger = Logger.getLogger(DynamoJsonDatabase.class);

	private AmazonDynamoDBClient dynamoDB;

	@Autowired
	private DynamoJsonObjectFactory dynamoJsonObjectFactory;

	@Value("${AWSCredentials.userKey:foo}")
	private String awsUserKey;

	@Value("${AWSCredentials.secretKey:bar}")
	private String awsSecretKey;

	private Evictor evictorInstance;
	
	private class Evictor implements Runnable{
		
		private DynamoJsonDatabase dbListener;
		
		public Evictor(DynamoJsonDatabase dbListener){
			this.dbListener = dbListener;
		}
		
		public void run() { 
			dbListener.evict();
			dbListener.load();
		}
		
	}
	
	private Map<String, String[]> classifiers;
	
	private ScheduledExecutorService scheduler = Executors
	        .newScheduledThreadPool(1);
	
	private ScheduledFuture<?> evictorHandle;

	@PostConstruct
	private void initDynamo() {
		
		AWSCredentials credentials = 
			new BasicAWSCredentials(awsUserKey, awsSecretKey);
		
		dynamoDB = new AmazonDynamoDBClient(credentials);
		
		classifiers = new HashMap<String, String[]>();
		
		evictorInstance = new Evictor(this);

		evictorHandle = scheduler.scheduleAtFixedRate(
				evictorInstance, 0, 24, TimeUnit.HOURS);
		
		scheduler.scheduleAtFixedRate(evictorInstance, 1, 12, TimeUnit.HOURS);
	}
	
	public void evict(){
		classifiers.clear();
	}
	
	public synchronized void load() {
		if(classifiers.isEmpty()){
		 ScanRequest scanRequest = new ScanRequest("classifiers");
	     ScanResult scanResult = dynamoDB.scan(scanRequest); 
	     
	     for (Map<String, AttributeValue> items : scanResult.getItems()) {
	    	 String[] row = new String[] {
	    			 items.get("type").getS().toString(),
	    			 items.get("category").getS().toString(),
	    			 items.get("subcategory").getS().toString(),
	    			 
	    	 };
	    	 classifiers.put(items.get("_id").getS().toString(), row);	      
         }
		}
	}
	

	@Override
	public DbPage findAll(String collectionName, int pageNum)
	        throws DbException {
		// TODO Auto-generated method stub
		throw new RuntimeException("NO IMPL");
	}

	@Override
	public DbPage findByExample(String collectionName, int pageNumber,
	        JSONObject example) throws DbException {
		// TODO Auto-generated method stub
		throw new RuntimeException("NO IMPL");
	}

	@Override
	public DbPage findByExampleIncluding(String collectionName, int pageNumber,
	        JSONObject example, JSONObject inclusions) throws DbException {
		// TODO Auto-generated method stub
		throw new RuntimeException("NO IMPL");
	}

	@Override
	public JSONObject findById(String collectionName, String id)
	        throws DbException {

		try {
			
			Key primaryKey = new Key()
			        .withHashKeyElement(new AttributeValue(id));
			GetItemRequest request = new GetItemRequest().withTableName(
			        collectionName).withKey(primaryKey);

			GetItemResult result = dynamoDB.getItem(request);

			if (result.getItem() == null || result.getItem().isEmpty())
				throw new DbException(Type.OBJECT_NOT_FOUND);
			else {
				return new JSONObject(result.getItem().get("document").getS());
			}

		} catch (AmazonServiceException e) {
			throw new DbException(Type.DB_ERROR);
		} catch (AmazonClientException e) {
			throw new DbException(Type.DB_ERROR);
		} catch (JSONException e) {
			throw new DbException(Type.DB_ERROR);
		}

	}
	
	

	@Override
    public void delete(String collectionName, String id) throws DbException {
	    try {
            Key primaryKey = new Key()
            .withHashKeyElement(new AttributeValue(id));
            DeleteItemRequest deleteItemRequest = new DeleteItemRequest(collectionName, primaryKey);
            DeleteItemResult result = dynamoDB.deleteItem(deleteItemRequest);
        } catch (AmazonServiceException e) {
            throw new DbException(Type.DB_ERROR);
        } catch (AmazonClientException e) {
            throw new DbException(Type.DB_ERROR);
        }

    }
	

    @Override
    public void deleteAll(String collectionName) throws DbException {
        
        
        // TODO Auto-generated method stub
        throw new RuntimeException("NO IMPL");
    }
    
    

    @Override
	public JSONArray findDistinct(String collectionName, String key,
	        JSONObject query) throws DbException {
		// TODO Auto-generated method stub
		throw new RuntimeException("NO IMPL");
	}

	public JSONArray findTypes() throws DbException {
		load();
		JSONArray typesArray = new JSONArray();
		Set<String> types = new HashSet<String>();
		for (String[] row : classifiers.values()) 
	        types.add(row[0]);
		for (String item : types) {
			typesArray.put(item);
        }
		
		return typesArray;
	}
	
	public JSONArray findCatgories(String type) throws DbException {
		load();
		JSONArray oArray = new JSONArray();
		Set<String> result = new HashSet<String>();
		for (String[] row : classifiers.values()) {
			if(row[0].equals(type))
				result.add(row[1]);
		}
		for (String item : result) {
			oArray.put(item);
        }
		return oArray;
	}
	
	public JSONArray findSubcatgories(String type, String category) throws DbException {
		load();
		JSONArray oArray = new JSONArray();
		Set<String> result = new HashSet<String>();
		for (String[] row : classifiers.values()) {
			if(row[0].equals(type) && row[1].equals(category))
				result.add(row[2]);
		}
		for (String item : result) {
			oArray.put(item);
        }

		return oArray;
	}

	@Override
	public void put(JSONObject doc, String collectionName, String id, EntityType type)
	        throws DbException {

		try {

			switch(type){
			case PLACE:{
			    putItem(dynamoJsonObjectFactory.makePlace(doc, "published"),
                        collectionName);
			    break;
			}
            case CLASSIFER: {
                putItem(dynamoJsonObjectFactory.makeClassifier(doc),
                        collectionName);
                break;
            }
            case DEAL: {
                putItem(dynamoJsonObjectFactory.makeDeal(doc, "published"),
                        collectionName);
                break;
            }
			}
            
			
			
			
		} catch (JSONException e) {
			logger.error("cant put object to dynamo store", e);
			throw new DbException(DbException.Type.DB_ERROR);
		}

	}

	private void putItem(Map<String, AttributeValue> item, String collectionName) {
		PutItemRequest putItemRequest = new PutItemRequest(collectionName, item);
		PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
		logger.debug(putItemResult.getConsumedCapacityUnits()
		        + "units used, result:" + putItemResult.toString());
	}


}
