package com.welocally.geodb.services.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.SecureRandom;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import com.welocally.geodb.services.app.CommandException;
import com.welocally.geodb.services.app.CommandSupport;

@Component
public class MongoLoader implements CommandSupport {
	
	static Logger logger = 
		Logger.getLogger(MongoLoader.class);
	
	private Mongo m = null;

	private DB db = null;
	
	private DBCollection mongoCollection = 
		null;

	private SecureRandom random = new SecureRandom();
	
	
	
	@Override
	public void doCommand(JSONObject command) throws CommandException {
		try {
			m = new Mongo(command.getString("host"), 27017);
			
			db = m.getDB(command.getString("db"));

			mongoCollection = db.getCollection(command.getString("collection"));
			
			load(command.getString("file"), command.getInt("maxRecords"));
			
		} catch (UnknownHostException e) {
			logger.error(e);
			throw new CommandException(e);
		} catch (MongoException e) {
			logger.error(e);
			throw new CommandException(e);
		} catch (JSONException e) {
			logger.error(e);
			throw new CommandException(e);
		}

		
		
	}



	public void load(String fileName, int maxRecords) {
		try {
			
			String[] files = fileName.split(",");
			for (String file : files) {
				FileReader reader = 
					new FileReader(new File(file));
				
				BufferedReader br = new BufferedReader(reader); 
				String s = null; 
				int count=0;
				while((s = br.readLine()) != null && count<maxRecords) { 
					
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
			        count++;
					
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

}
