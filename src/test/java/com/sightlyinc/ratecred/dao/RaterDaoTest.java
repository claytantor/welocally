package com.sightlyinc.ratecred.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.noi.utility.spring.test.AbstractBeanFactoryTestCase;
import com.sightlyinc.ratecred.model.Patron;

public class RaterDaoTest extends AbstractBeanFactoryTestCase {
	static Logger logger = 
		Logger.getLogger(RaterDaoTest.class);

	@Override
	protected String[] getSpringResources() {
		return new String[] {
				"/com/noi/rater/dao/RaterDaoTest-configurer-beans.xml",
				"/applicationSessionFactory-beans.xml",
				"/hibernateProperties-beans.xml",
				"/localDataSource-beans.xml",
				"/springDao-beans.xml",
				
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



	public void testGetRatersByBusiness()
	{

		logger.debug("testGetRatersByBusiness");
		try {
			BusinessDao businessDao = 
				(BusinessDao)super.getBeanFactory().getBean("BusinessDao");
			PatronDao raterDao = 
				(PatronDao)super.getBeanFactory().getBean("RaterDao");
			
			
			long startTime = 
				Calendar.getInstance().getTimeInMillis() - 
				(86400000l*90);
			
			List<Patron> raters = raterDao.findByBusinessDateRange(
					businessDao.findByPrimaryKey(5l), 
					new Date(startTime), 
					Calendar.getInstance().getTime());
			
			assertTrue(raters.size()>0);
		} catch (Exception e) {
			logger.error("problem getting raters for business", e);
			fail("problem getting raters for business");
		}
	
	}

}
