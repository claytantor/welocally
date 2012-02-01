package com.sightlyinc.ratecred.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sightlyinc.ratecred.etl.PublisherToSiteMigrate_20120127191918;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath*:**/test-beans.xml",
		"classpath*:**/applicationSessionFactory-beans.xml",
		"classpath*:**/localDataSource-beans.xml",
		"classpath*:**/placeSearchEngine-beans.xml",		
		"classpath:placeholder.xml"})
@Transactional
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback = false)		
public class UpgradeGeneratorTest {
	
	static Logger logger = 
		Logger.getLogger(UpgradeGeneratorTest.class);
	
	@Autowired PublisherToSiteMigrate_20120127191918 publisherToSite;
	
	@Test
	public void testPublisherToSiteMigrate(){
		
		String result = publisherToSite.migrate();
		logger.debug(result);
		
		
	}

}
