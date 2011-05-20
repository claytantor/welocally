package com.sightlyinc.ratecred.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath*:**/*-beans.xml",
		"classpath:placeholder.xml"})
@Transactional
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback = false)		
public class BaseTest {

}
