package com.sightlyinc.ratecred.client.geo;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.pojo.Events;
import com.sightlyinc.ratecred.service.EventService;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.Record;

@Component("geoEventClient")
public class SimpleGeoEventClient implements GeoEventClient {
	
	static Logger logger = 
		Logger.getLogger(SimpleGeoLocationClient.class);
	
	
	private SimpleGeoStorageClient client;

	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	private String ratecredConsumerKey;

	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	private String ratecredConsumerSecret;
	
	@Autowired
	private PlaceManagerService placeManagerService;
	
	@Autowired
	private EventService eventService; 
	
	@Autowired
	@Qualifier("locationPlacesClient")
	private SimpleGeoPlaceManager locationPlacesClient;
	


    @PostConstruct
    public void init() {
		client = SimpleGeoStorageClient.getInstance();
		client.getHttpClient().setToken(ratecredConsumerKey, ratecredConsumerSecret);
	}


	@Override
	public Event findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Events findEvents(Double lat, Double lng, Float radiusInKm,
			String sourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void publishEvents(Events e, String sourceId) {
		// TODO Auto-generated method stub
		
	}
	
	public void publishEvent(Event e, String sourceId) {
		SimpleGeoStorageClient client = SimpleGeoStorageClient.getInstance();	
		client.getHttpClient().setToken(ratecredConsumerKey, ratecredConsumerSecret);
		Record r = new Record();
		       			
		/*HashMap<String,Object> model = new HashMap<String,Object>();
		model.put("article", jArticle);
		r.setProperties(model);
		Geometry g = new Geometry();
		Point p = new Point();
		p.setLat(loc.getLat());
		p.setLon(loc.getLng());
		g.setPoint(p);	
		r.setLayer(articleLayerPrefix+"."+article.getReferrerId()+"."+articleType.toLowerCase()+"."+articleLayerSuffix);
		
		Bitly bitly = 
			BitlyFactory.newInstance(bitlyUserName, bitlyKey);
		
		BitlyUrl burl = 
			bitly.shorten(article.getUrl());
		
		r.setRecordId(burl.getHash());
		r.setGeometry(g);	
		logger.debug("writing to storage:"+article.getUrl()+" to layer "+articleLayerPrefix+"."+article.getReferrerId()+"."+articleLayerSuffix);
		client.addOrUpdateRecord(r);*/
    	
	}
	
	public Event makeEventFromFeature(Feature f,
			String name,
			String status,
			String url,
			String phone,
			String placeName,	
			String description,
			String address,	
			String city,
			String state,	
			Long timeStarts,	
			Long timeEnds,
			Publisher pub)  {
		
		Event event = eventService.findByUrl(url);

		if (event == null) {
			event = new Event();
			event.setUrl(url);
		}
		

		// dont add a place that is already in the database
		Place p = null;
		try {
			p = placeManagerService.findBySimpleGeoId(f.getSimpleGeoId());			
		} catch (BLServiceException ev) {
			logger.error("cannot find place", ev);
		}

		try {
			if (p == null) {
				p = new Place();
				locationPlacesClient.transformFeature(f, p);
				placeManagerService.savePlace(p);
			}
		} catch (BLServiceException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		event.setDescription(description);
		event.setName(name);
		event.setPhone(phone);
		event.setPlace(p);
		event.setPublisher(pub);
		event.setTimeCreated(Calendar.getInstance().getTimeInMillis());
		event.setTimeStarts(timeStarts);
		event.setTimeEnds(timeEnds);
		
		return event;
	}


}
