package com.sightlyinc.ratecred.service;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.noi.utility.spring.test.AbstractBeanFactoryTestCase;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.compare.PobabilisticNameAndLocationPlaceComparitor;
import com.sightlyinc.ratecred.dao.PlaceDao;
import com.sightlyinc.ratecred.model.Place;

public class ComparePlaceTest extends AbstractBeanFactoryTestCase {
	
	static Logger logger = 
		Logger.getLogger(ComparePlaceTest.class);

	@Override
	protected String[] getSpringResources() {
		return new String[] {
				"/com/noi/rater/service/ComparePlaceTest-configurer-beans.xml",
				"/applicationSessionFactory-beans.xml",
				"/hibernateProperties-beans.xml",
				"/localDataSource-beans.xml",
				"/springDao-beans.xml",
				"/businessManagerService-beans.xml"
				
		};
	}
	
private static Session session;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		session = SessionFactoryUtils.getSession(
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"), true);
		
		TransactionSynchronizationManager.bindResource(
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"),
				new SessionHolder(session));
	}



	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		TransactionSynchronizationManager.unbindResource(
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"));
		SessionFactoryUtils.releaseSession(
				session, 
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"));
		
	}
	
	public void testComparePlaces()
	{
		logger.debug("testComparePlaces");
		
		Place p1 = (Place)super.getBeanFactory().getBean("placeTestComparePlace1");
		logger.debug(p1.getName());
		
		Place p2 = (Place)super.getBeanFactory().getBean("placeTestComparePlace2");
		logger.debug(p2.getName());
		
		Double name1 = StringUtils.compareStrings(p1.getName(), p2.getName());
		logger.debug(name1.toString());
		
		assertTrue(name1>0.5);
		
		
	}
	
	public void testComparePlacesByRegion()
	{
		logger.debug("testComparePlacesByRegion");
		
		Place p3 = (Place)super.getBeanFactory().getBean("placeTestComparePlace3");
		
		PlaceDao placeDao = (PlaceDao)super.getBeanFactory().getBean("PlaceDao"); 
		
		List<Place> places = 
			placeDao.findByGeoBounding(
					p3.getLatitude() - 0.1, 
					p3.getLongitude() - 0.1, 
					p3.getLatitude() + 0.1, 
					p3.getLongitude() + 0.1);
		logger.debug("places found:"+places.size());

		Collections.sort(places,new PobabilisticNameAndLocationPlaceComparitor(p3));
		Place closest = places.get(0);
		assertTrue(closest.getId().equals(2385l));
		
		
		
	}
	
	
	
	//places = 
	//placeDao.findByNamePrefix(
	//		entity.getPlace().getName().substring(0, 3));
	public void testComparePlacesByNameCityState()
	{
		logger.debug("testComparePlacesByRegion");
		
		Place p3 = (Place)super.getBeanFactory().getBean("placeTestComparePlace3");
		
		PlaceDao placeDao = (PlaceDao)super.getBeanFactory().getBean("PlaceDao"); 
		
		List<Place> places = 
		placeDao.findByNamePrefix(
				p3.getName().substring(0, 3));
		logger.debug("places found:"+places.size());

		Collections.sort(places,new PobabilisticNameAndLocationPlaceComparitor(p3));
		Place closest = places.get(0);
		assertTrue(closest.getId().equals(2385l));
		
		
		
	}
	
	
}
