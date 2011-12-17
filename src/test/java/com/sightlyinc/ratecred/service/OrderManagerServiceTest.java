package com.sightlyinc.ratecred.service;


import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.hibernate.GUIDGenerator;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
		"classpath*:**/*-beans.xml",
		"classpath*:**/servicesTest-placeholder.xml"})
@Transactional
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback = false)
public class OrderManagerServiceTest {
	
	static Logger logger = 
		Logger.getLogger(OrderManagerServiceTest.class);
	
	@Autowired
	private OrderManagerService orderManagerService;
		
	@Before
	public void setup(){
		logger.debug("setup");
	}
	
	
	@Test
	public void testSaveOrder() {
		logger.debug("testSaveOrder");
		try {
			// check that paymentStatus=Completed
			// check that txnId has not been previously processed
			Order  o = orderManagerService.findOrderByTxId("abcd");
			
			//need a check on supported product ids
			if(o == null)
			{
								
				// process payment
				 logger.debug("new order:"+"abcd");
				 o = new Order();
				 o.setExternalTxId("abcd");
				 
			}
			
			 o.setStatus("PAID");
			 o.setPrice(Float.valueOf("5.99"));
			 o.setExternalOrderItemCode("ERY4");
			 o.setSku("92920210saksis");
			 o.setBuyerEmail("joe@foo.com");
			 o.setQuantity(1);
			 o.setBuyerKey(GUIDGenerator.createId());
			 
			 orderManagerService.saveOrder(o);
		} catch (NumberFormatException e) {
			logger.error("cannot save", e);
			Assert.fail(e.getMessage());
		} catch (BLServiceException e) {
			logger.error("cannot save", e);
			Assert.fail(e.getMessage());
		}
		 
			
	}
	


}
