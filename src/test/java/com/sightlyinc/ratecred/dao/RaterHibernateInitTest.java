package com.sightlyinc.ratecred.dao;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class RaterHibernateInitTest extends TestCase {
	
	static Logger logger = 
		Logger.getLogger(RaterHibernateInitTest.class);
	
	public void testInitConfig()
	{
		
		try {
			SessionFactory sf = new Configuration()
			.configure("hibernate-mysql.cfg.xml")
			.buildSessionFactory();
		} catch (HibernateException e) {
			logger.error("cannot init sf",e);
			fail();
		}

	}
	
	

}
