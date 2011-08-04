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

import com.sightlyinc.ratecred.service.ArticleService;
import com.sightlyinc.ratecred.service.EventService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.client.geo.GeoPersistable;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.client.geo.GeoStoragePersistor;
import com.sightlyinc.ratecred.dao.MerchantDao;
import com.sightlyinc.ratecred.dao.NetworkMemberDao;
import com.sightlyinc.ratecred.dao.PublisherDao;
import com.sightlyinc.ratecred.interceptor.PersistenceActivity;
import com.sightlyinc.ratecred.interceptor.PersistenceObservable;
import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Merchant;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Geometry;
import com.simplegeo.client.types.Layer;
import com.simplegeo.client.types.Point;
import com.simplegeo.client.types.Record;


@Component("persistenceActivityMessageListener")
public class PersistenceMessageListener implements MessageListener,GeoStoragePersistor { 

	static Logger logger = Logger.getLogger(PersistenceMessageListener.class);
	
	@Autowired 
	private ArticleService articleService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired 
	private PublisherDao publisherDao;
	
	@Autowired 
	private MerchantDao merchantDao;
	
	@Autowired 
	private NetworkMemberDao networkMemberDao;
    
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

    // TODO figure out why this getting invoked twice for create messages - sam
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

                    switch (activity.getActivity()) {
                        case PersistenceActivity.ACTIVITY_CREATE:
                        case PersistenceActivity.ACTIVITY_UPDATE:
                            if(clazz.getName().equals(Article.class.getName())){
                                Article article = articleService.findByPrimaryKey(activity.getEntityId());
                                saveGeoEntityToStorage((GeoPersistable)article) ;
        						articleService.save(article);
                                //not working
                                article.setPublished(true);
                            } else if(clazz.getName().equals(Event.class.getName())){
                                Event event = eventService.findByPrimaryKey(activity.getEntityId());
                                saveGeoEntityToStorage((GeoPersistable)event) ;
        						eventService.save(event);
                                event.setPublished(true);
                            } else if(clazz.getName().equals(Publisher.class.getName())){
                                Publisher publisher = publisherDao.findByPrimaryKey(activity.getEntityId());
                                createLayersForKey(getPublisherLayerPrefix(publisher),
                                        new String[] {
                                                Article.class.getSimpleName().toLowerCase(),
                                                Event.class.getSimpleName().toLowerCase()});

                            } else if(clazz.getName().equals(Merchant.class.getName())){
                                Merchant merchant =
                                    merchantDao.findByPrimaryKey(activity.getEntityId());
                                saveGeoEntityToStorage((GeoPersistable)merchant) ;
                                logger.debug("saved merchant:"+merchant.getName());

                            } else if(clazz.getName().equals(NetworkMember.class.getName())){
                                NetworkMember member =
                                    networkMemberDao.findByPrimaryKey(activity.getEntityId());
                                createLayersForKey(member.getMemberKey(),
                                        new String[] {
                                            Merchant.class.getSimpleName().toLowerCase()});
                            }
                            break;
                        case PersistenceActivity.ACTIVITY_DELETE:
                            deleteGeoEntityFromStorage(activity);
                            // TODO handle delete of publisher (delete layers?)
                            // TODO handle delete of network member (delete layers?)
                            break;
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

    private String getPublisherLayerPrefix(Publisher publisher) {
    	return publisher.getNetworkMember().getMemberKey()+"."+
			publisher.getKey();
    }
    
//    private String getMemberLayerPrefix(NetworkMember member) {
//    	return member.getMemberKey()+".merchants";
//    }
    
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
		
		if(geoEntity == null)
			logger.debug("geo is null");
		if(geoEntity.getMemberKey() == null)
			logger.debug("member key is null");
		
		String layername = geoEntity.getMemberKey()+"."+
			geoEntity.getClass().getSimpleName().toLowerCase();
		logger.debug("layer:"+layername+" saving:"+baosArticle.toString());
		JSONObject jEntity = new JSONObject(baosArticle.toString());
		model.put("entity", jEntity);
				
		r.setProperties(model);
		Geometry g = new Geometry();
		Point p = new Point();
		p.setLat(geoEntity.getGeoPlace().getLatitude());
		p.setLon(geoEntity.getGeoPlace().getLongitude());
		g.setPoint(p);	
		r.setLayer(layername);
		if(geoEntity.getExpiration() != -1l)
			r.setExpiration(geoEntity.getExpiration());
			
		r.setRecordId(geoEntity.getGeoRecordId());
		r.setGeometry(g);	
		client.addOrUpdateRecord(r);
    } 

    private void deleteGeoEntityFromStorage(PersistenceActivity activity) throws GeoPersistenceException, IOException {
        String shortName = activity.getClazzName().substring(activity.getClazzName().lastIndexOf('.')+1).toLowerCase();
        String layername = activity.getMemberKey()+"."+shortName;
        logger.debug("layer:"+layername+" deleting:"+activity.getEntityId());
        client.deleteRecord(layername, activity.getEntityId().toString());
    }


    public void createLayersForKey(String key, String[] types) throws IOException, JSONException {
//		String[] types = {
//				Merchant.class.getSimpleName().toLowerCase()};
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
