package com.sightlyinc.ratecred.admin.jms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.admin.model.Article;

@Component("saveArticleLocationMessageProducer")
public class SaveArticleLocationMessageProducer extends BaseMessageProducer {
	
	static Logger logger = Logger.getLogger(SaveArticleLocationMessageProducer.class);
	   
    @Autowired
    @Qualifier("saveArticleLocationJmsTemplate")
    private JmsTemplate saveArticleLocationJmsTemplate;
    
    @Autowired
    @Qualifier("jacksonMapper")
    private ObjectMapper jacksonMapper;

    public void generateMessage(Article article) 
    	throws JMSException, JsonGenerationException, JsonMappingException, IOException {
    		logger.debug("generating message");
    		
    		
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		
    		jacksonMapper.writeValue(baos, article);
    		
            final String text = baos.toString();

            super.generateMessage(text, saveArticleLocationJmsTemplate);

    }

    

}
