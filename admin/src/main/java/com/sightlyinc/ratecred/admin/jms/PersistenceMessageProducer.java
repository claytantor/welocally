package com.sightlyinc.ratecred.admin.jms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.interceptor.PersistenceActivity;
import com.sightlyinc.ratecred.interceptor.PersistenceObserver;

@Component("persistenceInformerListener")
public class PersistenceMessageProducer extends BaseMessageProducer implements PersistenceObserver {
	
	static Logger logger = Logger.getLogger(PersistenceMessageProducer.class);
	   
    @Autowired
    @Qualifier("persistenceMessageJmsTemplate")
    private JmsTemplate persistenceMessageJmsTemplate;
    
    @Autowired
    @Qualifier("jacksonMapper")
    private ObjectMapper jacksonMapper;
    
    @Override
	public void inform(PersistenceActivity activity)  {
    		logger.debug("generating message");  		
    		    		   				
    		try {
    			ByteArrayOutputStream baos = new ByteArrayOutputStream();   
				jacksonMapper.writeValue(baos, activity);
				final String text = baos.toString();
		        super.generateMessage(text, persistenceMessageJmsTemplate);
			} catch (JsonGenerationException e) {
				logger.error("JsonGenerationException", e);
			} catch (JsonMappingException e) {
				logger.error("JsonMappingException", e);
			} catch (IOException e) {
				logger.error("IOException", e);
			}    		
           
    }

    

}
