package com.sightlyinc.ratecred.admin.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.admin.mvc.controller.AdminActivityController;

@Component
public class HttpHelperUtils {
    
    static Logger logger = Logger.getLogger(AdminActivityController.class);

    public void put(JSONObject doc, String key, String token, String url) throws JSONException{
        HttpClient httpclient = new HttpClient();
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        //String url = "http://localhost:9200/place/"+doc.getString("id");
        PutMethod put = new PutMethod(url);
        put.setRequestHeader("key", key);
        put.setRequestHeader("token", token);
               
        RequestEntity placeRequest = new ByteArrayRequestEntity(doc.toString().getBytes(),"application/json; charset=UTF-8");

        put.setRequestEntity(placeRequest);
        
        try{
            int result = httpclient.executeMethod(put);
            
            logger.debug("Response status code: " + result+" Response body: "+put.getResponseBodyAsString());
            
        } catch (HttpException he) {
            logger.error("Http error connecting to '" + url + "'");
        } catch (IOException ioe){
            logger.error("Http error connecting to '" + url + "'");
        } finally {
            put.releaseConnection();
        } 
        
    }
    
    public JSONObject get(String key, String token, String url) throws Exception {
        HttpClient httpclient = new HttpClient();
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        //String url = "http://localhost:9200/place/"+doc.getString("id");
        GetMethod get = new GetMethod(url);
        get.setRequestHeader("key", key);
        get.setRequestHeader("token", token);
               
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        
        try {
            
            logger.debug(get.getURI().toString());
            
            int returnCode = httpclient.executeMethod(get);
        
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.error("The GET method is not implemented by this URI");
                throw new RuntimeException("problem with endpoint");
            } else {
                IOUtils.copy(
                        get.getResponseBodyAsStream(), baos);                 
                baos.flush();
                String result = new String(baos.toByteArray());
                return new JSONObject(result);
                
            }
        } catch (Exception e) {
            logger.error("Http error connecting to '" + url + "'");
            throw e;
        } finally {
            get.releaseConnection();
        } 

        
    }
    
    
}
