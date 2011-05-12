package com.sightlyinc.ratecred.service;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.test.BaseTest;

public class PlaceServiceTest extends BaseTest {
	
	static Logger logger = 
		Logger.getLogger(PlaceServiceTest.class);
	
	@Autowired
	@Qualifier("ebeEventHarvester")
	private PlaceManagerService placeManagerService;
	
	
	@Before
	public void setup(){
		logger.debug("setup");
	}
	
	
	@Test
	public void testUpdatePlaceInfo() {
		logger.debug("testUpdatePlaceInfo");
		try {
			//Place place = placeManagerService.findPlaceByPrimaryKey(1992l);
			placeManagerService.saveNewLocationInfo(2007l);
			
		} catch (BLServiceException e) {
			Assert.fail(e.getMessage());
		}
			
	}
	


}
