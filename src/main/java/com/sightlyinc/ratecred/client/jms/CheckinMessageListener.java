package com.sightlyinc.ratecred.client.jms;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.noi.utility.data.Base64Utils;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.service.CheckinService;



public class CheckinMessageListener implements MessageListener { 

	static Logger logger = Logger.getLogger(CheckinMessageListener.class);

	private CheckinService checkinService;

	private SessionFactory sessionFactory;
    
    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
    	
    	
    	Session session = null;
        try {   
        	
        	//bind to thread			
			session = 
				SessionFactoryUtils.getSession(sessionFactory, true);
        	
        	TransactionSynchronizationManager.bindResource(
					sessionFactory,
					new SessionHolder(session));
        	
            String provider = message.getStringProperty(CheckinMessageProducer.PROVIDER_TYPE);
            logger.debug("==== onMessage:"+provider);
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage)message;
                String msg = tm.getText();
                Rating r = (Rating)Base64Utils.fromString(msg);
                logger.debug("provider:"+provider);
                checkinService.checkinRating(provider, r);

            }
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (BLServiceException e) {
			logger.error(e.getMessage(), e);
		} finally {
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			if(session != null)
				SessionFactoryUtils.releaseSession(
						session, 
						sessionFactory);
		}
    }

	public void setCheckinService(CheckinService checkinService) {
		this.checkinService = checkinService;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
    
    
}
