package com.sightlyinc.ratecred.client.simplegeo;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sightlyinc.ratecred.client.geo.GeoPlacesClient;
import com.sightlyinc.ratecred.client.geo.SimpleGeoLocationClient;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.test.BaseTest;

public class SimpleGeoLocationClientTest extends BaseTest {
	
	static Logger logger = 
		Logger.getLogger(SimpleGeoLocationClient.class);
	
	@Autowired
//	@Qualifier("locationPlacesClient")
	private GeoPlacesClient locationClient;
	
	@Before
	public void setup(){
		logger.debug("setup");
	}
	
	@Test
	public void testGetPlaces() {
		double lat = 37.797933;
		double lon = -122.423937;
		double radiusInKMeters = 0.25;
		
		List<Place> places = 
			locationClient.findPlaces(lat, lon, radiusInKMeters);
		for (Place place : places) {
			logger.debug("place:"+place.toString());
		}
		
		Assert.assertNotNull(places);
		
		
	}

}
