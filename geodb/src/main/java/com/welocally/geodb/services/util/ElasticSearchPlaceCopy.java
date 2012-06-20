package com.welocally.geodb.services.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.conn.scheme.Scheme;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.welocally.geodb.services.app.CommandException;
import com.welocally.geodb.services.app.CommandSupport;
import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.JsonDatabase;
import com.welocally.geodb.services.db.JsonDatabase.StatusType;
import com.welocally.geodb.services.jmx.LoadMonitor;
import com.welocally.signpost4j.client.ClientResponse;
import com.welocally.signpost4j.client.SimpleHttpClient;

@Component
public class ElasticSearchPlaceCopy implements CommandSupport {
    
    static Logger logger = 
        Logger.getLogger(ElasticSearchPlaceCopy.class);
    
    @Autowired WelocallyJSONUtils welocallyJSONUtils;
    
    @Autowired LoadMonitor loadMonitor;
    
    @Autowired SimpleHttpClient simpleClient;
    
    @Autowired
    @Qualifier("dynamoJsonDatabase")
    private JsonDatabase jsonDatabase;
      

    @Override
    public void doCommand(JSONObject command) throws CommandException {
        try {           
            loadMonitor.reset();
            JSONArray urls = command.getJSONArray("sourceUrls");

            String[] sourceUrls = new String[urls.length()];
            for (int i = 0; i < urls.length(); i++) {
                sourceUrls[i] = urls.getString(i);           
            }
            JSONArray placesAll = getPlaces(sourceUrls);
            loadES(placesAll, 
                    command.getString("targetIndex"), 
                    command.getString("targetCollection"), 
                    JsonDatabase.EntityType.PLACE);
            
            loadDynamo(
                    placesAll, 
                    1000, 
                    command.getString("targetCollection"));
            
        
        } catch (DbException e) {
            logger.error(e);
            throw new CommandException(e);
        } catch (JSONException e) {
            logger.error(e);
            throw new CommandException(e);
        }
    }
    
    public void loadDynamo(JSONArray places, int maxRecords, String collectionName) throws DbException, JSONException {
        logger.debug("starting dynamo load");      

        for (int j = 0; j < places.length(); j++) {
              JSONObject place = places.getJSONObject(j);
              jsonDatabase.put(place,collectionName, place.getString("_id"), JsonDatabase.EntityType.PLACE, StatusType.PUBLISHED);         
        }
        logger.debug("dynamo load done");
    }
    
    public JSONArray getPlaces(String[] sourceUrls) throws JSONException{
        JSONArray placesAll = new JSONArray();
        for (int i = 0; i < sourceUrls.length; i++) {
            String url = sourceUrls[i];
            JSONArray places = getPlacesForURL(url);
            for (int j = 0; j < places.length(); j++) {
                JSONObject place = places.getJSONObject(j);
                placesAll.put(place);
            }
           
        }
        return placesAll;
    }

    public void loadES(JSONArray places, String targetIndex, String collectionName, JsonDatabase.EntityType type) 
    throws DbException, JSONException {

        logger.debug("starting es load");
        for (int j = 0; j < places.length(); j++) {
                JSONObject place = places.getJSONObject(j);
                String indexEndpoint = targetIndex+place.getString("_id");
                put(place,indexEndpoint);                    
        }
        logger.debug("es load done");
                

    }
    
    
    public JSONArray getPlacesForURL(String url) throws JSONException{
        ClientResponse response = simpleClient.get(url, null, null);
        if(response.getCode() == 200){
                return new JSONArray(new String(response.getResponse()));
        } else {
            throw new RuntimeException("cannot get response");
        }
        
    }
    
    public void put(JSONObject doc, String url) throws JSONException{
        HttpClient httpclient = new HttpClient();
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        PutMethod put = new PutMethod(url);
        
        RequestEntity placeRequest = new ByteArrayRequestEntity(doc.toString().getBytes(),"text/plain; charset=UTF-8");
        
        put.setRequestEntity(placeRequest);
        
        try{
            int result = httpclient.executeMethod(put);
            
            logger.debug("Response status code: " + result+" Response body: "+put.getResponseBodyAsString());
            
        } catch (HttpException he) {
            logger.error("Http error connecting to '" + url + "'");
        } catch (IOException ioe){
            logger.error("Http error connecting to '" + url + "'");
        }
        
    }
	

}
