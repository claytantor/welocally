package com.welocally.admin.spreadsheet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.welocally.admin.spreadsheet.template.JsonSpreadsheetDeserializationTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { 
        "classpath:test-config-beans.xml",
        "classpath:admin-beans.xml"})
@Transactional
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback = false)
public class SpreadsheetDeserializationTest {
	
	static Logger logger = 
		Logger.getLogger(SpreadsheetDeserializationTest.class);
	
	@Autowired JsonSpreadsheetDeserializationTemplate jsonSpreadsheetDeserializationTemplate;

	@Before
	public void setup(){
		logger.debug("setup");
	}

	@Test
	public void testDeserializeSpreadsheet() {
		logger.debug("testDeserializeSpreadsheet");
		try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(
                    SpreadsheetDeserializationTest
                    .class.getResourceAsStream("/data/spreadsheet.json"), 
                    baos);
            logger.debug(baos.toString());
            JSONObject ofeed = new JSONObject(baos.toString());
            jsonSpreadsheetDeserializationTemplate.feed(ofeed);
            
        } catch (Exception e) {
            logger.error("problem", e);
            Assert.fail(e.getMessage());
        }
		
	}
	
	@Test
    public void testDeserializeWorksheet() {
        logger.debug("testDeserializeWorksheet");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(
                    SpreadsheetDeserializationTest
                    .class.getResourceAsStream("/data/worksheet_cells2.json"), 
                    baos);
            logger.debug(baos.toString());
            JSONObject ofeed = new JSONObject(baos.toString());
            
            jsonSpreadsheetDeserializationTemplate.cellfeed(ofeed);
            
        } catch (Exception e) {
            logger.error("problem", e);
            Assert.fail(e.getMessage());
        }
        
    }

}

