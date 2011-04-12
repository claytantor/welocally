package com.sightlyinc.ratecred.admin.jms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.noi.utility.data.Base64;
import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.rosaloves.net.shorturl.bitly.Bitly;
import com.rosaloves.net.shorturl.bitly.BitlyFactory;
import com.rosaloves.net.shorturl.bitly.url.BitlyUrl;
import com.sightlyinc.ratecred.admin.model.Article;
import com.sightlyinc.ratecred.client.offers.Location;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Geometry;
import com.simplegeo.client.types.Point;
import com.simplegeo.client.types.Record;


@Component("saveArticleLocationMessageListener")
public class SaveArticleLocationMessageListener implements MessageListener { 

	static Logger logger = Logger.getLogger(SaveArticleLocationMessageListener.class);

   /* @Autowired
    @Qualifier("saveArticleLocationJmsTemplate")
    private JmsTemplate saveArticleLocationJmsTemplate;*/
    
    @Autowired
    @Qualifier("jacksonMapper")
    private ObjectMapper jacksonMapper;
	
	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	private String ratecredConsumerKey;
	
	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	private String ratecredConsumerSecret;
	
	@Value("${simpleGeo.articleLayer.prefix}")
	private String articleLayerPrefix="com.ratecred.geo.article";
	
	@Value("${simpleGeo.articleLayer.suffix}")
	private String articleLayerSuffix="dev";
	
	@Value("${twitter.rateCredService.bitlyUserName}")
	private String bitlyUserName;
	
	@Value("${twitter.rateCredService.bitlyKey}")
	private String bitlyKey;
	
	
	//Qh3x7erV34FE3UXznOhwVubn2bEG1pmiAfQMqEJae4D4vXjUMuWKFq4MwmeADWV.mOTx
	@Value("${article.geocodingUrl}")
	private String geocodingUrl="http://where.yahooapis.com/geocode?q=[QUERY]&flags=J&appid=dj0yJmk9RFZyMmFFSk1TdXZWJmQ9WVdrOVVWSjFRbmRJTlRJbWNHbzlOemszTWpnNE9UWXkmcz1jb25zdW1lcnNlY3JldCZ4PTI0";
	
	private Integer sleepInSeconds = 5;
	
	@PostConstruct
	public void postConstruct() {
		logger.debug("listener is created");
	}
	
    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
    	
    	logger.debug("saving article to location storage");
    	
        try {   
 	    	//we are gong to do a thread sleep here to deal with rate limiting
	    	//this is expected to be processed in a synchronous queue
	    	//with a single worker
	    	Thread.sleep(sleepInSeconds*1000);
    	
        	if (message instanceof TextMessage) {
        		TextMessage tm = (TextMessage)message;
    			//what we are sending to storage
    			JSONObject jArticle = new JSONObject(new String(tm.getText().getBytes()));
            	
            	//deserialize the json
            	Article article = 
            		jacksonMapper.readValue(
            				new ByteArrayInputStream(
            						tm.getText().getBytes()), Article.class);
    			
    			//now reverse geocode it
            	ClientResponse response = 
        			SimpleHttpClient.get(geocodingUrl.replace("[QUERY]", article.getAddress1().replace(" ", "+")), null, null);
            	JSONObject jLocation = new JSONObject(new String(response.getResponse()));
            	Location loc = getLocation(jLocation);
            	if(loc == null)
            		throw new RuntimeException("location cannot be empty, problem geocoding");
            	            	
            	SimpleGeoStorageClient client = SimpleGeoStorageClient.getInstance();	
        		client.getHttpClient().setToken(ratecredConsumerKey, ratecredConsumerSecret);
        		Record r = new Record();
    			       			
    			HashMap<String,Object> model = new HashMap<String,Object>();
    			model.put("article", jArticle);
    			r.setProperties(model);
    			Geometry g = new Geometry();
    			Point p = new Point();
    			p.setLat(loc.getLat());
    			p.setLon(loc.getLng());
    			g.setPoint(p);	
    			r.setLayer(articleLayerPrefix+"."+article.getReferrerId()+"."+articleLayerSuffix);
    			
    			Bitly bitly = 
    				BitlyFactory.newInstance(bitlyUserName, bitlyKey);
    			
    			BitlyUrl burl = 
    				bitly.shorten(article.getUrl());
    			
    			r.setRecordId(burl.getHash());
    			r.setGeometry(g);	
    			logger.debug("writing to storage:"+article.getUrl()+" to layer "+articleLayerPrefix+"."+article.getReferrerId()+"."+articleLayerSuffix);
    			client.addOrUpdateRecord(r);
            	            	
            }        	
        	        	
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		} 
    }
    
    /**
     * {
    "ResultSet": {
        "version": "1.0",
        "Error": 0,
        "ErrorMessage": "No error",
        "Locale": "us_US",
        "Quality": 87,
        "Found": 1,
        "Results": [
            {
                "quality": 85,
                "latitude": "38.898717",
                "longitude": "-77.035974",
                "offsetlat": "38.898590",
                "offsetlon": "-77.035971",
                "radius": 500,
                "name": "",
                "line1": "1600 Pennsylvania Ave NW",
                "line2": "Washington, DC  20006",
                "line3": "",
                "line4": "United States",
                "house": "1600",
                "street": "Pennsylvania Ave NW",
                "xstreet": "",
                "unittype": "",
                "unit": "",
                "postal": "20006",
                "neighborhood": "",
                "city": "Washington",
                "county": "District of Columbia",
                "state": "District of Columbia",
                "country": "United States",
                "countrycode": "US",
                "statecode": "DC",
                "countycode": "DC",
                "uzip": "20006",
                "hash": "B42121631CCA2B89",
                "woeid": 12765843,
                "woetype": 11
            }
        ]
    }
}
     * @param geocodingResponse
     * @return
     * @throws JSONException 
     */
    private Location getLocation(JSONObject geocodingResponse) throws JSONException {
    	Location loc = new Location();
    	JSONObject jResultSet = geocodingResponse.getJSONObject("ResultSet");
    	Integer error = jResultSet.getInt("Error");
    	if(error == 0 && !jResultSet.isNull("Results")) {
    		JSONArray jResults = jResultSet.getJSONArray("Results");
    		if(jResults.length()>0) {
    			JSONObject jfirstLoc = jResults.getJSONObject(0);
    			loc.setAddressOne(jfirstLoc.getString("line1"));
    			loc.setCity(jfirstLoc.getString("city"));
    			loc.setState(jfirstLoc.getString("state"));
    			loc.setPostalCode(jfirstLoc.getString("postal"));
    			loc.setLat(jfirstLoc.getDouble("latitude"));
    			loc.setLng(jfirstLoc.getDouble("longitude"));
    			return loc;
    			
    		} else
    			return null;
    		
    	}
    	return null;
    }
    
    
}
