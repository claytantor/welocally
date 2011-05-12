package com.sightlyinc.ratecred.admin.geocoding;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sightlyinc.ratecred.admin.harvest.EventHarvester;
import com.sightlyinc.ratecred.model.Events;
import com.sightlyinc.ratecred.model.Location;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:/com/sightlyinc/ratecred/admin/geocoding/PublishEventsTest-beans.xml"})
public class PublishEventsTest {
	
	static Logger logger = Logger.getLogger(PublishEventsTest.class);
	
	@Autowired
	private EventHarvester eventHarvester;
	
	@Autowired 
	private Geocoder geocoder;
	
	@Test
	public void testEventHarvest() {
		Events events=null;
		try {
			events = eventHarvester.havestEvents();
		} catch (Exception e) {
			logger.error(e);
		}
		Assert.assertNotNull(events);
	}
	
	@Test
	public void testGeocoder() {
		Location loc=null;
		try {
			loc = geocoder.geocode("6363 Aspinwall Rd., Oakland CA 94611");
		} catch (GeocoderException e) {
			logger.error(e);
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(loc);
	}

}
