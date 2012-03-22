package com.sightlyinc.ratecred.admin.mvc;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.velocity.tools.generic.NumberTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sightlyinc.ratecred.admin.velocity.PublisherRegistrationGenerator;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.service.OrderService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "classpath*:**/*-beans.xml",
        "classpath:/com/sightlyinc/ratecred/admin/OrderTest-test-config.xml"})
@Transactional
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback = false)
public class OrderEmailTest {
    
    static Logger logger = Logger.getLogger(OrderEmailTest.class);
    
    @Autowired OrderService orderService;
    
    @Test
    public void testGenerateOrderText() { 
        System.out.println("log4j:"+System.getProperty("log4j.configuration"));
        DOMConfigurator.configure(System.getProperty("log4j.configuration"));
        
        logger.debug("testGenerateOrderText");
        
        for (Order o : orderService.findAll()) {
            Map<String,Object> model = new HashMap<String,Object>();
            model.put("order", o);
            model.put("number", new NumberTool());
            
            PublisherRegistrationGenerator generator = 
                new PublisherRegistrationGenerator(model); 
           
            if(generator.makeDisplayString().contains("quantityOrig")){
                logger.debug(generator.makeDisplayString());
                logger.debug("quantityOrig:"+o.getOrderLines().iterator().next().getQuantityOrig());
                logger.debug("done");
            }
            Assert.assertEquals(false, generator.makeDisplayString().contains("quantityOrig"));
            Assert.assertEquals(false, generator.makeDisplayString().contains("0E-8"));
                      
        }
            
    }

}
