package com.sightlyinc.ratecred.client.geo;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rosaloves.net.shorturl.bitly.Bitly;
import com.rosaloves.net.shorturl.bitly.BitlyFactory;
import com.rosaloves.net.shorturl.bitly.url.BitlyUrl;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Events;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Geometry;
import com.simplegeo.client.types.Point;
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


}
