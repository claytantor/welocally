package com.sightlyinc.ratecred.client.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.model.RatingCheckinAction;

@Component
public class CheckinMessageProducer {
	
	static Logger logger = Logger.getLogger(CheckinMessageProducer.class);
	
    protected static final String PROVIDER_TYPE= "providerType";

    @Autowired
    @Qualifier("checkinJmsTemplate")
    private JmsTemplate checkinJmsTemplate;

    public void generateMessage(final RatingCheckinAction checkin) throws JMSException {
    		logger.debug("generating message:"+checkin.getProvider());
    	   	
            final String text = checkin.getRatingString();

            checkinJmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(text);
                    message.setStringProperty(PROVIDER_TYPE, checkin.getProvider());                    
                    return message;
                }
            });
    }


}
