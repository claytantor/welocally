package com.sightlyinc.ratecred.admin.jms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.client.geo.GeoPersistable;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.client.geo.GeoStoragePersistor;
import com.sightlyinc.ratecred.dao.ArticleDao;
import com.sightlyinc.ratecred.dao.EventDao;
import com.sightlyinc.ratecred.dao.PublisherDao;
import com.sightlyinc.ratecred.dao.ReviewDao;
import com.sightlyinc.ratecred.interceptor.PersistenceActivity;
import com.sightlyinc.ratecred.interceptor.PersistenceObservable;
import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.Review;
import com.sightlyinc.ratecred.pojo.Location;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Geometry;
import com.simplegeo.client.types.Layer;
import com.simplegeo.client.types.Point;
import com.simplegeo.client.types.Record;


@Component("persistenceActivityMessageListener")
public class PersistenceMessageListener implements MessageListener,GeoStoragePersistor { 

	static Logger logger = Logger.getLogger(PersistenceMessageListener.class);
	
	@Autowired 
	private ArticleDao articleDao;
	
	@Autowired 
	private ReviewDao reviewDao;
	
	@Autowired 
	private EventDao eventDao;
	
	@Autowired 
	private PublisherDao publisherDao;
    
    @Autowired
    @Qualifier("jacksonMapper")
    private ObjectMapper jacksonMapper;
	
	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	private String ratecredConsumerKey;
	
	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	private String ratecredConsumerSecret;
	
	private SimpleGeoStorageClient client;
		
	@PostConstruct
	public void postConstruct() {
		logger.debug("listener is created");
		client = SimpleGeoStorageClient.getInstance();	
		client.getHttpClient().setToken(ratecredConsumerKey, ratecredConsumerSecret);
	}
			
    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
    	
    	logger.debug("got message");
    	
    	try {   
	    	if (message instanceof TextMessage) {
	    		TextMessage tm = (TextMessage)message;
	    			    		
	    		ByteArrayInputStream bais = new ByteArrayInputStream(tm
						.getText().getBytes());

	    		PersistenceActivity activity = jacksonMapper.readValue(bais,
	    				PersistenceActivity.class);    		
				
				//instantiate the class, I know there is a more elegant way to
	    		//do this driven by the type, but hey I never minded using
	    		//basic machinery
				Class clazz = Class.forName(activity.getClazzName());
				if(clazz.isAnnotationPresent(PersistenceObservable.class)){
					if(clazz.getName().equals(Article.class.getName())){
						Article article = articleDao.findByPrimaryKey(activity.getEntityId());
						saveGeoEntityToStorage((GeoPersistable)article) ;						
						article.setPublished(true);
						articleDao.save(article);												
					} else if(clazz.getName().equals(Review.class.getName())){
						Review review = reviewDao.findByPrimaryKey(activity.getEntityId());
						saveGeoEntityToStorage((GeoPersistable)review) ;						
						review.setPublished(true);
						reviewDao.save(review);						
					} else if(clazz.getName().equals(Event.class.getName())){
						Event event = eventDao.findByPrimaryKey(activity.getEntityId());
						saveGeoEntityToStorage((GeoPersistable)event) ;						
						event.setPublished(true);
						eventDao.save(event);							
					} else if(clazz.getName().equals(Publisher.class.getName())){
						Publisher publisher = publisherDao.findByPrimaryKey(activity.getEntityId());			
						createLayersForKey(publisher.getNetworkMember().getMemberKey());
						
					}
					
				}
				
				
	    	}
    	} catch (JMSException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (JSONException e) {
			logger.error(e.getMessage(), e);
		} catch (GeoPersistenceException e) {
			logger.error(e.getMessage(), e);
		}
    	
    }
    
    public void saveGeoEntityToStorage(GeoPersistable geoEntity) 
	    throws 
	    JsonGenerationException, 
	    JsonMappingException, 
	    IOException, 
	    JSONException, 
	    GeoPersistenceException 
	{
    	
		Record r = new Record();
		   
		//make the model to persist
		HashMap<String,Object> model = new HashMap<String,Object>();
		
		//serialize the article
		ByteArrayOutputStream baosArticle = new ByteArrayOutputStream(); 		
		jacksonMapper.writeValue(baosArticle, geoEntity);
		logger.debug(baosArticle.toString());
		JSONObject jEntity = new JSONObject(baosArticle.toString());
		model.put("entity", jEntity);
				
		r.setProperties(model);
		Geometry g = new Geometry();
		Point p = new Point();
		p.setLat(geoEntity.getGeoPlace().getLatitude());
		p.setLon(geoEntity.getGeoPlace().getLongitude());
		g.setPoint(p);	
		r.setLayer(geoEntity.getMemberKey()+"."+geoEntity.getClass().getSimpleName().toLowerCase());
			
		r.setRecordId(geoEntity.getGeoRecordId());
		r.setGeometry(g);	
		client.addOrUpdateRecord(r);
    } 
    
    public void createLayersForKey(String key) throws IOException, JSONException {
		String[] types = {
				Article.class.getSimpleName().toLowerCase(),
				Review.class.getSimpleName().toLowerCase(),
				Event.class.getSimpleName().toLowerCase()};
    	for (int i = 0; i < types.length; i++) {
			Layer l = null;
			try {
				l = client.getLayer(key+"."+types[i]);
			} catch (com.simplegeo.client.http.exceptions.APIException e) {
				logger.debug("layer not found, creating");
				if(e.getMessage().equals("Not Found")) {
					Layer layer = new Layer(key+"."+types[i]);
					client.createLayer(layer);
				}
					
			} 
		
		}
	}
    

    
    
}
