package com.sightlyinc.ratecred.admin.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class BaseMessageProducer {
	
	public void generateMessage(final String messageBody, JmsTemplate jmsTemplate) {
		jmsTemplate.send(new MessageCreator() {
		  public Message createMessage(Session session) throws JMSException {
		      TextMessage message = session.createTextMessage(messageBody);                 
		      return message;
		  }
		});
	}


}
