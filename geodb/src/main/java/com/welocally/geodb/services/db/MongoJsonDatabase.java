package com.welocally.geodb.services.db;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@Deprecated
/**
 * obsolete
 */
public class MongoJsonDatabase implements JsonDatabase {

	@Override
    public void delete(String collectionName, String id) throws DbException {
        // TODO Auto-generated method stub
        throw new RuntimeException("NO IMPL");
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
	    // TODO Auto-generated method stub
	    throw new RuntimeException("NO IMPL");
    }

	@Override
    public JSONArray findDistinct(String collectionName, String key,
            JSONObject query) throws DbException {
	    // TODO Auto-generated method stub
	    throw new RuntimeException("NO IMPL");
    }

	@Override
    public void put(JSONObject doc, String collectionName, String id)
            throws DbException {
	    // TODO Auto-generated method stub
	    throw new RuntimeException("NO IMPL");
    }
	
	
	
//	static Logger logger = 
//		Logger.getLogger(MongoJsonDatabase.class);
//	
//	private Mongo mongo;
//
//	private DB db;
//		
//	@Value("${jsonDatabase.pageSize:10}")
//	private int pageSize;
//	
//	@PostConstruct
//	public void initDb(){
//		try {
//			mongo = new Mongo( "localhost" , 27017 );
//			db = mongo.getDB( "geodb" );
//		} catch (UnknownHostException e) {
//			logger.error("problem init of db:"+e.getMessage(), e);
//		} catch (MongoException e) {
//			logger.error("problem init of db:"+e.getMessage(), e);
//		}
//	}
//	
//	
//	@Override
//	public void put(JSONObject doc, String collectionName, String id) throws DbException {		
//		try {
//			DBCollection coll = 
//				db.getCollection(collectionName);
//			
//			doc.put("_id", id);				
//			logger.debug("adding document:"+doc.getString("_id"));
//			DBObject placeJson = (DBObject)JSON.parse(doc.toString());				
//			coll.insert(placeJson);
//			
//		} catch (MongoException e) {
//			logger.error("db problem", e);
//			throw new DbException(DbException.Type.DB_ERROR,e);
//		} catch (JSONException e) {
//			logger.error("json problem", e);
//			throw new DbException(DbException.Type.DB_ERROR,e);
//		}
//		
//	}
//
//
//	/* (non-Javadoc)
//	 * @see com.welocally.geodb.services.db.JsonDatabase#findById(java.lang.String, java.lang.String)
//	 */
//	public JSONObject findById(String collectionName, String id) throws DbException {
//		
//		JSONObject dbObjectJson = null;
//		
//		try {
//			DBCollection coll = 
//				db.getCollection(collectionName);
//			
//			BasicDBObject query = new BasicDBObject();
//			query.put("_id", id);
//
//			DBObject obj = coll.findOne(query);
//			
//			if(obj == null)
//				throw new DbException(DbException.Type.OBJECT_NOT_FOUND,new RuntimeException("object not found"));
//			
//			dbObjectJson = new JSONObject(obj.toString());
//			
//		} catch (MongoException e) {
//			throw new DbException(DbException.Type.DB_ERROR,e);
//		} catch (JSONException e) {
//			throw new DbException(DbException.Type.DB_ERROR,e);
//		}
//
//		return dbObjectJson;
//	}
//
//	/**
//	 * function printStudents(pageNumber, nPerPage) {
//   print("Page: " + pageNumber);
//   db.students.find().skip((pageNumber-1)*nPerPage).limit(nPerPage).forEach( function(student) { print(student.name + "<p>"); } );
//}
//	 */
//	@Override
//	public DbPage findAll(String collectionName, int pageNumber) throws DbException {		
//		return findByExample( collectionName,  pageNumber, null);
//	}
//	
//	public JSONArray findDistinct(String collectionName, String key, JSONObject query)
//	throws DbException {
//		
//		JSONArray result = new JSONArray();
//		List<Object> names = new ArrayList<Object>();
//		
//		try {
//			DBCollection coll = 
//				db.getCollection(collectionName);
//			
//			if(query != null){
//				DBObject example = makeDBObjectFromJSON(query);
//				names = coll.distinct(key, example);
//								
//			} else {
//				names = coll.distinct(key);
//			}
//		} catch (JSONException e) {
//			throw new DbException(DbException.Type.DB_ERROR,e);
//		}
//		
//		for (Object object : names) {
//			result.put(object.toString());
//		}
//		
//		return result;
//	}
//	
//	public DbPage findByExample(String collectionName, int pageNumber, JSONObject example)
//	throws DbException {
//		
//		return findByExampleIncluding( collectionName,  pageNumber,  example, null);
//	}
//	
//	public DbPage findByExampleIncluding(String collectionName, int pageNumber, JSONObject example, JSONObject inclusions)
//	throws DbException {
//		DbPage pageResult = new DbPage();
//		try {
//			DBCollection coll = 
//				db.getCollection(collectionName);
//			
//			DBCursor cur = null;
//			
//			if(example == null){
//				cur = 
//					coll.find().skip((pageNumber-1)*this.pageSize).limit(pageSize);
//			} else if(example != null && inclusions == null) {
//				cur = 
//					coll.find(makeDBObjectFromJSON(example)).skip((pageNumber-1)*this.pageSize).limit(pageSize);	
//			} else {
//				cur = 
//					coll.find(makeDBObjectFromJSON(example), makeDBObjectFromJSON(inclusions) ).skip((pageNumber-1)*this.pageSize).limit(pageSize);					
//			}
//					
//			int total = cur.count();
//			pageResult.setCount(total);
//			
//			pageResult.setPageNum(pageNumber);
//
//	        while(cur.hasNext()) {
//	            DBObject obj = cur.next();
//	            pageResult.getObjects().put(new JSONObject(obj.toString()));
//	        }
//	        
//	        pageResult.setPageSize(pageResult.getObjects().length());
//			
//		} catch (MongoException e) {
//			throw new DbException(DbException.Type.DB_ERROR,e);
//		} catch (JSONException e) {
//			throw new DbException(DbException.Type.DB_ERROR,e);
//		}
//		return pageResult;
//	}
//	
//	protected DBObject makeDBObjectFromJSON(JSONObject example) throws JSONException{
//		String[] names = JSONObject.getNames(example);
//		DBObject obj = new BasicDBObject();	
//		for (int i = 0; i < names.length; i++) {
//			obj.put(names[i], example.get(names[i]));
//		}		
//		return obj;
//	}
	
	

}
