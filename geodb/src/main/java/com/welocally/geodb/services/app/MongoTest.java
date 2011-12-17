package com.welocally.geodb.services.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class MongoTest {
	
	static Logger logger = 
		Logger.getLogger(MongoTest.class);

	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Map<String,String> model =
			makeArgsModel(args) ;
		
		if(model.get("action")==null)
			throw new RuntimeException("an action of load or delete is required");
		if(model.get("collection")==null)
			throw new RuntimeException("specify which collection");
		if(model.get("action").equals("load") && model.get("file")==null )
			throw new RuntimeException("a file for loading is required");
				
		MongoTest test = new MongoTest(model.get("collection"));
		
		if(model.get("action").equals("load")){
			test.load(model.get("file"));
		} else if(model.get("action").equals("delete")) {
			test.delete();
		}

	}
	
	private Mongo m = null;

	private DB db = null;
	
	private DBCollection mongoCollection = 
		null;

	private SecureRandom random = new SecureRandom();	
	
	public MongoTest(String collectionName) {
		super();
		try {
			m = new Mongo( "localhost" , 27017 );
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (MongoException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		db = m.getDB( "geodb" );
		
		mongoCollection = 
			db.getCollection(collectionName);
	}

	public String nextId()
	{
	    return new BigInteger(130, random).toString(32);
	}
		
	
	public void delete(){
		mongoCollection.drop();	
	}
	
	public void load(String fileName) {
		try {
			
			String[] files = fileName.split(",");
			for (String file : files) {
				FileReader reader = 
					new FileReader(new File(file));
				
				BufferedReader br = new BufferedReader(reader); 
				String s = null; 
				while((s = br.readLine()) != null) { 
					
					JSONObject place = 
						new JSONObject(s);
					
					//place.put("sgid", place.getString("id"));
					place.put("_id", place.getString("id").replaceAll("SG_", "WL_"));
					place.put("owner", "welocally");
					place.put("type", "Place");
					place.remove("id");
					JSONObject properties = place.getJSONObject("properties");
					properties.remove("href");
					
					logger.debug("adding document:"+place.getString("_id"));
					DBObject placeJson = (DBObject)JSON.parse(place.toString());				
			        mongoCollection.insert(placeJson);
					
				} 
				reader.close(); 
				
				logger.debug("collection size:"+mongoCollection.getCount());
			}
			
		
				
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public static Map<String,String> makeArgsModel(String[] args) throws RuntimeException {
		Map<String,String> model = new HashMap<String,String>();
		for (int i = 0; i < args.length; i++) {
			String[] nv = args[i].split("=");
			model.put(nv[0].replace("--", ""), nv[1]);
		}
		return model;
		
	}
	

}
