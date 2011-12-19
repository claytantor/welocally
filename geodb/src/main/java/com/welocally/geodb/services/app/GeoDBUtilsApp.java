package com.welocally.geodb.services.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class GeoDBUtilsApp {
	
	static Logger logger = 
		Logger.getLogger(GeoDBUtilsApp.class);

	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		

		
		StringBuffer buf = new StringBuffer();
					
		try {
	        BufferedReader br = 
	          new BufferedReader(new InputStreamReader(System.in));
	        
	     // read stdin buffer until EOF (or skip)
	    	while(br.ready()){
	    		buf.append( br.readLine());
	    	}
	    	
	    	if(!buf.toString().isEmpty()){ 
	    		
	    		ApplicationContext ctx = new ClassPathXmlApplicationContext(
	    			    new String[] {"geodb-applicationContext.xml"});
	    	    		
	    		JSONObject harness = new JSONObject(buf.toString());
	    		
	    		setSystemProperties(harness.getJSONArray("properties"));
	    		
	    		logger.debug("doing command:"+harness.getString("bean"));
	    		
	    		CommandSupport beanCommand = (CommandSupport)ctx.getBean(harness.getString("bean"));
	    		beanCommand.doCommand(harness.getJSONObject("command"));
    		
	    	}
	        
	        
	    } catch (IOException e) {
	        logger.error("problem reading input", e);
	    } catch (JSONException e) {
	    	logger.error("error parsing input", e);
		} 
//	    catch (ClassNotFoundException e) {
//			logger.error("error finding command", e);
//		} catch (InstantiationException e) {
//			logger.error("error instantiating command", e);
//		} catch (IllegalAccessException e) {
//			logger.error("error instantiating command", e);
//		} 
		catch (CommandException e) {
			logger.error("cannot execute command", e);
		}
	    


	}
	
	private static void setSystemProperties(JSONArray properties) 
	throws CommandException, JSONException{
		for (int i = 0; i < properties.length(); i++) {
			String property=properties.getString(i);
			String[] prop = property.split("=");
			System.setProperty(prop[0], prop[1]);
		}
	}
	
	
//	private Mongo m = null;
//
//	private DB db = null;
//	
//	private DBCollection mongoCollection = 
//		null;
//
//	private SecureRandom random = new SecureRandom();	
	
//	public GeoDBUtilsApp(String collectionName) {
//		super();
//		try {
//			m = new Mongo( "localhost" , 27017 );
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} catch (MongoException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//
//		db = m.getDB( "geodb" );
//		
//		mongoCollection = 
//			db.getCollection(collectionName);
//	}

//	public String nextId()
//	{
//	    return new BigInteger(130, random).toString(32);
//	}
//		
//	
//	public void delete(){
//		mongoCollection.drop();	
//	}
	
//	public void load(String fileName) {
//		try {
//			
//			String[] files = fileName.split(",");
//			for (String file : files) {
//				FileReader reader = 
//					new FileReader(new File(file));
//				
//				BufferedReader br = new BufferedReader(reader); 
//				String s = null; 
//				while((s = br.readLine()) != null) { 
//					
//					JSONObject place = 
//						new JSONObject(s);
//					
//					//place.put("sgid", place.getString("id"));
//					place.put("_id", place.getString("id").replaceAll("SG_", "WL_"));
//					place.put("owner", "welocally");
//					place.put("type", "Place");
//					place.remove("id");
//					JSONObject properties = place.getJSONObject("properties");
//					properties.remove("href");
//					
//					logger.debug("adding document:"+place.getString("_id"));
//					DBObject placeJson = (DBObject)JSON.parse(place.toString());				
//			        mongoCollection.insert(placeJson);
//					
//				} 
//				reader.close(); 
//				
//				logger.debug("collection size:"+mongoCollection.getCount());
//			}
//			
//		
//				
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (MongoException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//	}

//	public static Map<String,String> makeArgsModel(String[] args) throws RuntimeException {
//		Map<String,String> model = new HashMap<String,String>();
//		for (int i = 0; i < args.length; i++) {
//			String[] nv = args[i].split("=");
//			model.put(nv[0].replace("--", ""), nv[1]);
//		}
//		return model;
//		
//	}
	

}
