package com.sightlyinc.ratecred.job;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionException;

import com.noi.utility.spring.test.AbstractBeanFactoryTestCase;
import com.sightlyinc.ratecred.service.BusinessManagerService;

public class BusinessMetricsJobTest extends AbstractBeanFactoryTestCase {
	static Logger logger = 
		Logger.getLogger(BusinessMetricsJobTest.class);

	@Override
	protected String[] getSpringResources() {
		return new String[] {
				"/com/noi/rater/job/BusinessMetricsJobTest-configurer-beans.xml",
				"/applicationSessionFactory-beans.xml",
				"/hibernateProperties-beans.xml",
				"/localDataSource-beans.xml",
				"/springDao-beans.xml",
				"/businessManagerService-beans.xml",
				
		};
	}

	private static Session session;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		/*session = SessionFactoryUtils.getSession(
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"), true);
		
		TransactionSynchronizationManager.bindResource(
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"),
				new SessionHolder(session));*/
	}



	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		/*TransactionSynchronizationManager.unbindResource(
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"));
		SessionFactoryUtils.releaseSession(
				session, 
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"));*/
		
	}



	public void testExecute()
	{

		
		BusinessMetricsJob job = new BusinessMetricsJob();
		job.setBusinessManagerService(
				(BusinessManagerService)super.getBeanFactory().getBean("BusinessManagerService"));
		job.setSessionFactory(
				(SessionFactory)super.getBeanFactory().getBean("ApplicationSessionFactory"));
		
		try {
			job.execute();
		} catch (JobExecutionException e) {
			logger.error("cannot execute job", e);
			fail("problem "+e.getMessage());
		}
		

	
	}

}
