package com.sightlyinc.ratecred.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath:service-beans.xml",
		"classpath:thirdparty-service-beans.xml",
		"classpath:applicationSessionFactory-beans.xml",
		"classpath:awardManagerService-beans.xml",
		"classpath:businessManagerService-beans.xml",
		"classpath:checkinService-beans.xml",
		"classpath:hibernateProperties-beans.xml",
		"classpath:jacksonObjectMapper-beans.xml",
		"classpath:linkClient-beans.xml",
		"classpath:linkManagerService-beans.xml",
		"classpath:localDataSource-beans.xml",
		"classpath:offerPoolService-beans.xml",
		"classpath:orderManagerService-beans.xml",
		"classpath:placeManagerService-beans.xml",
		"classpath:placeSearchEngine-beans.xml",
		"classpath:ratingManagerService-beans.xml",
		"classpath:ratingSearchEngine-beans.xml",
		"classpath:springDao-beans.xml",
		"classpath:placeholder-beans.xml"})
@Transactional
@TransactionConfiguration(transactionManager="ApplicationTransactionManager",defaultRollback = false)		
public class BaseTest {

}
