package com.sightlyinc.ratecred.dao;

import java.util.ArrayList;
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
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.pojo.PatronBusinessMetrics;

public class RaterBusinessMetricsDaoTest extends AbstractBeanFactoryTestCase {
	
	static Logger logger = 
		Logger.getLogger(RaterBusinessMetricsDaoTest.class);

	@Override
	protected String[] getSpringResources() {
		return new String[] {
				"/com/noi/rater/dao/BasicDaoTest-configurer-beans.xml",
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
	
	
	
}
