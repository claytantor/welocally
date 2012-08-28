package com.welocally.admin.spreadsheet.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.welocally.admin.security.UserPrincipal;
import com.welocally.admin.spreadsheet.template.Entry;
import com.welocally.admin.spreadsheet.template.JsonSpreadsheetDeserializationTemplate;
import com.welocally.admin.spreadsheet.template.SpreadsheetFeed;
import com.welocally.admin.spreadsheet.template.WorksheetCellFeed;

@Service
@Scope(value = "session")
public class GoogleOAuthSpreadsheetClient {
    
    private static final Logger logger = Logger.getLogger(GoogleOAuthSpreadsheetClient.class);
    
    public static final String SCOPE = "https://spreadsheets.google.com/feeds";
    
	private OAuthService service;
	
	private String key;   
    private String authUrl;
    private Token authToken;
    private Token accessToken;
    
    @Value("${google.oauth2.clientId}")
    String apiKey;
    @Value("${google.oauth2.clientSecret}")
    String apiSecret;
    
    @Autowired JsonSpreadsheetDeserializationTemplate jsonSpreadsheetDeserializationTemplate;
    
    
    @PostConstruct
    public synchronized void init(){
        try {
            Thread.sleep(1);
            this.key = "auth-"+Calendar.getInstance().getTimeInMillis();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }       
    }
    
    /**
     * dont like this should be in service
     * 
     * @throws ServletException
     * @throws IOException
     */
    public void setUserAuthInfo(UserPrincipal up) throws ServletException, IOException{
            
            if(up.getAuthUrl() != null){
                this.setAuthUrl(up.getAuthUrl());
            }
        
            if(up.getAuthKey() != null){
                this.setKey(up.getAuthKey());
            }
            
            if(up.getAuthToken() != null && up.getAuthSecret() != null){
                Token auth = new Token(up.getAuthToken(), up.getAuthSecret() );
                this.setAuthToken(auth);
            } 
            
            if(up.getAccessToken() != null && up.getAccessSecret() != null){
                Token access = new Token(up.getAccessToken(), up.getAccessSecret() );
                this.setAccessToken(access);
            } 
  
    }

    public String getOAuthorizationURL(String forUrl) {
        service = new ServiceBuilder().provider(GoogleApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope(SCOPE)
                .callback(forUrl)
                .build();
        authToken = service.getRequestToken();
        authUrl = service.getAuthorizationUrl(authToken);
        return authUrl;
    }
    public Token getAuthToken() {
        return authToken;
    }
    public void authorizeWith(String oauth_verifier) {
        accessToken = service.getAccessToken(authToken, new Verifier(oauth_verifier));      
    }
    public Token getAccessToken() {
        return accessToken;
    }
    
    public SpreadsheetFeed retrieveSpreadsheetFeed(String spreadsheetName) throws JSONException, JsonParseException, JsonMappingException, IOException {
         return jsonSpreadsheetDeserializationTemplate.feed(retrieveSpreadsheetFeedAsJSON(spreadsheetName));      
    }
    
    public JSONObject retrieveSpreadsheetFeedAsJSON(String spreadsheetName) throws JSONException, JsonParseException, JsonMappingException, IOException {       
        if(service == null && authUrl != null){
            service = new ServiceBuilder().provider(GoogleApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope(SCOPE)
                .callback(authUrl)
                .build();
        }
        
        if(service != null){
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://spreadsheets.google.com/feeds/worksheets/" + spreadsheetName + "/private/full?alt=json");
            service.signRequest(accessToken, request);
            Response response = request.send();
            JSONObject body = new JSONObject(response.getBody());
            logger.debug(body.toString());
            return body;   
        } else
            throw new RuntimeException("Could not init service");
   
   }
    
    public JSONObject retrieveWorksheetFeedAsJSON(String worksheetFeedUrl) throws JSONException, JsonParseException, JsonMappingException, IOException {       
        if(service == null && authUrl != null){
            service = new ServiceBuilder().provider(GoogleApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope(SCOPE)
                .callback(authUrl)
                .build();
        }
        
        if(service != null){
            OAuthRequest request = new OAuthRequest(Verb.GET, worksheetFeedUrl+"?alt=json");
            service.signRequest(accessToken, request);
            Response response = request.send();
            JSONObject body = new JSONObject(response.getBody());
            logger.debug(body.toString());
            return body;   
        } else
            throw new RuntimeException("Could not init service");
   
   }
    
    
    public WorksheetCellFeed retrieveWorksheetCellFeed(String worksheetFeedUrl) 
    throws JsonParseException, JsonMappingException, IOException, JSONException{
        return jsonSpreadsheetDeserializationTemplate.cellfeed(
                retrieveWorksheetFeedAsJSON(worksheetFeedUrl));
    }
    
    
    
    
    public OAuthService getService() {
        return service;
    }



    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("key: "+key.toString());
        if(accessToken != null)
            buf.append(" accessToken: "+accessToken.toString());
        if(authUrl != null)
            buf.append(" authUrl: "+authUrl.toString());
        if(authToken != null)
            buf.append(" authToken: "+authToken.toString());
        return buf.toString();
    }

    public String getKey() {
        return key;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAuthToken(Token authToken) {
        this.authToken = authToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }
	

}
