package com.sightlyinc.ratecred.test;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:basic-test-beans.xml"})
public class ListContainsTest {
	
	static Logger logger = 
		Logger.getLogger(ListContainsTest.class);
	
	@Test
	public void testContains(){
		
		logger.debug("test contains");
		
		List<String> list = Arrays.asList(new String[] {"foo", "bar", "roo", "cos" });
		Assert.assertTrue(list.contains("cos"));		
	}
	
	@Test
    public void testNotContains(){
        
        logger.debug("test not contains");
        
        List<String> list = Arrays.asList(new String[] {"foo", "bar", "roo", "cos" });
        Assert.assertTrue(!list.contains("lemon"));        
    }

}
