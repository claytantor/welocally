package com.sightlyinc.ratecred.admin.jms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Patron;

@Component("saveNewAwardMessageProducer")
public class SaveNewAwardMessageProducer {
	
	static Logger logger = Logger.getLogger(SaveNewAwardMessageProducer.class);
	   
    @Autowired
    @Qualifier("saveNewAwardJmsTemplate")
    private JmsTemplate saveNewAwardJmsTemplate;
    
    @Autowired
    @Qualifier("jacksonMapper")
    private ObjectMapper jacksonMapper;

    public void generateMessage(Award award, AwardType awardType, Patron r) 
    	throws JMSException, JsonGenerationException, JsonMappingException, IOException {
    		logger.debug("generating message");
    		
    		Map<String,Object> awardToSaveData = new HashMap<String,Object>();
    		awardToSaveData.put("raterId", r.getId());
    		awardToSaveData.put("award", award);
    		awardToSaveData.put("awardTypeId", awardType.getId());
    		
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		
    		jacksonMapper.writeValue(baos, awardToSaveData);
    		
            final String text = baos.toString();

            saveNewAwardJmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(text);                 
                    return message;
                }
            });
    }


}
