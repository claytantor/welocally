package com.sightlyinc.ratecred.admin.client.geo;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sightlyinc.ratecred.client.geo.GeoPersistable;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.client.geo.GeoStoragePersistor;
import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Review;
import com.sightlyinc.ratecred.service.ArticleService;
import com.sightlyinc.ratecred.service.EventService;
import com.sightlyinc.ratecred.service.ReviewService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath*:**/*-beans.xml",
		"classpath*:**/GeoPersistableTest-beans.xml"})
@Transactional
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback = false)
public class GeoPersistableTest {
	
	static Logger logger = 
		Logger.getLogger(GeoPersistableTest.class);
	
	@Autowired
	@Qualifier("persistenceActivityMessageListener")
	private GeoStoragePersistor geoPersistor;
	
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private EventService eventService;
	
	
	@Before
	public void setup(){
		logger.debug("setup");
		List<Review> review = reviewService.findAll();
		try {
			geoPersistor.createLayersForKey(((GeoPersistable)review.get(0)).getMemberKey());
		} catch (JsonGenerationException e) {
			logger.debug("problem",e);
		} catch (JsonMappingException e) {
			logger.debug("problem",e);
		} catch (IOException e) {
			logger.debug("problem",e);
		} catch (JSONException e) {
			logger.debug("problem",e);
		} catch (GeoPersistenceException e) {
			// TODO Auto-generated catch block
			logger.debug("problem",e);
		}			
	}
	
	
	@Test
	public void testPersistReview() {
		logger.debug("testPersistReview");
		List<Review> review = reviewService.findAll();
		try {
			geoPersistor.saveGeoEntityToStorage((GeoPersistable)review.get(0));
		} catch (JsonGenerationException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (JsonMappingException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (IOException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (JSONException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (GeoPersistenceException e) {
			logger.debug("problem",e);
			Assert.fail();
		}			
	}
	
	@Test
	public void testPersistArticle() {
		logger.debug("testPersistArticle");
		List<Article> all = articleService.findAll();
		try {
			geoPersistor.saveGeoEntityToStorage((GeoPersistable)all.get(0));
		} catch (JsonGenerationException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (JsonMappingException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (IOException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (JSONException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (GeoPersistenceException e) {
			logger.debug("problem",e);
			Assert.fail();
		}			
	}

	@Test
	public void testPersistEvent() {
		logger.debug("testPersistEvent");
		List<Event> all = eventService.findAll();
		try {
			geoPersistor.saveGeoEntityToStorage((GeoPersistable)all.get(0));
		} catch (JsonGenerationException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (JsonMappingException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (IOException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (JSONException e) {
			logger.debug("problem",e);
			Assert.fail();
		} catch (GeoPersistenceException e) {
			logger.debug("problem",e);
			Assert.fail();
		}			
	}
	
	
	
	
	


}
