package com.welocally.geodb.services.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mongodb.MongoException;
import com.welocally.geodb.services.app.CommandException;
import com.welocally.geodb.services.app.CommandSupport;
import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.JsonDatabase;
import com.welocally.geodb.services.jmx.LoadMonitor;

@Component
public class DynamoLoader implements CommandSupport {
	
	static Logger logger = 
		Logger.getLogger(DynamoLoader.class);
	
	@Autowired WelocallyJSONUtils welocallyJSONUtils;
	
	@Autowired LoadMonitor loadMonitor;
	
	@Autowired
	@Qualifier("dynamoJsonDatabase")
	private JsonDatabase jsonDatabase;

	@Override
	public void doCommand(JSONObject command) throws CommandException {
		try {			
			loadMonitor.reset();
			load(command.getString("file"), command.getInt("maxRecords"),
			        command.getString("collection"));
	
		} catch (DbException e) {
			logger.error(e);
			throw new CommandException(e);
		} catch (JSONException e) {
			logger.error(e);
			throw new CommandException(e);
        }

	}

	public void load(String fileName, int maxRecords, String collectionName) throws DbException {
		try {
			logger.debug("starting load");
			String[] files = fileName.split(",");
			for (String file : files) {
				FileReader reader = 
					new FileReader(new File(file));
				
				BufferedReader br = new BufferedReader(reader); 
				String s = null; 
				int count=0;
				while((s = br.readLine()) != null && count<maxRecords) { 
					
					JSONObject doc = 
						new JSONObject(s);
					
					//welocallyJSONUtils.updatePlaceToWelocally(doc);
					
					logger.debug("adding document:"+doc.getString("_id"));
					jsonDatabase.put(doc,collectionName, doc.getString("_id"));
					loadMonitor.increment();
			        count++;
					
				} 
				reader.close(); 
			
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
