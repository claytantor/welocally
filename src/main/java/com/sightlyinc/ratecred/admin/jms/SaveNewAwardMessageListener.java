package com.sightlyinc.ratecred.admin.jms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.RaterAwardsService;
import com.sightlyinc.ratecred.service.RatingManagerService;



public class SaveNewAwardMessageListener implements MessageListener { 

	static Logger logger = Logger.getLogger(SaveNewAwardMessageListener.class);

	private SessionFactory sessionFactory;
	
	private ObjectMapper mapper;
	
	private RaterAwardsService raterAwardsService;
	
	private RatingManagerService ratingManagerService;

	private AwardManagerService awardManagerService;
	
	private Integer sleepInSeconds;
	
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
        	logger.debug("==== onMessage");
        	
        	//we are gong to do a thread sleep here to deal with rate limiting
        	//this is expected to be processed in a synchronous queue
        	//with a single worker
        	Thread.sleep(sleepInSeconds*1000);
        	
            if (message instanceof TextMessage) {
            	TextMessage tm = (TextMessage)message;
            	//deserialize the json
            	Map<String,Object> userData = 
            		mapper.readValue(
            				new ByteArrayInputStream(
            						tm.getText().getBytes()), Map.class);
            	
               	Long awardTypePk = 
            		new Long(userData.get("awardTypeId").toString());
               	Long raterPk = 
            		new Long(userData.get("awardTypeId").toString());
            	
            	AwardType awardType = awardManagerService.findAwardTypeByPrimaryKey(awardTypePk);
            	Rater r = ratingManagerService.findRaterByPrimaryKey(raterPk);
            	Award award = (Award)userData.get("award");
            	
            	
            	raterAwardsService.saveNewAward(award, awardType, r);
            	
            }
        	
        	        	
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
        	logger.error(e.getMessage(), e);
		} catch (BLServiceException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} finally {
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			if(session != null)
				SessionFactoryUtils.releaseSession(
						session, 
						sessionFactory);
		}
    }


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}


	public void setRaterAwardsService(RaterAwardsService raterAwardsService) {
		this.raterAwardsService = raterAwardsService;
	}


	public void setRatingManagerService(RatingManagerService ratingManagerService) {
		this.ratingManagerService = ratingManagerService;
	}


	public void setAwardManagerService(AwardManagerService awardManagerService) {
		this.awardManagerService = awardManagerService;
	}


	public void setSleepInSeconds(Integer sleepInSeconds) {
		this.sleepInSeconds = sleepInSeconds;
	}
    
    
}
