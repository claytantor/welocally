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

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.PatronAwardsService;



public class UpdateAwardOfferMessageListener implements MessageListener { 

	static Logger logger = Logger.getLogger(UpdateAwardOfferMessageListener.class);

	//private SessionFactory sessionFactory;
	
	private ObjectMapper mapper;
	
	private PatronAwardsService raterAwardsService;
	
	//private RatingManagerService ratingManagerService;

	//private PatronManagerService patronManagerService;

	private AwardManagerService awardManagerService;
	
	private Integer sleepInSeconds;
    
    /**
     * Implementation of <code>MessageListener</code>.
     * 
	 * This message should only be consumed by a queue, that's because it makes calls
	 * to rate limited activities that should be in a single worker queue that can be
	 * controlled
	 * 
	 * Secondly we should re evaluate how we run the targeting for offers, right now
	 * we are targeting per award which works, but a better way would be to look at raters who 
	 * have expired offers and then put all their awards and potential offers into a single
	 * working memory, fire rules against the entire model. then then expert systems could 
	 * be applied better. THE CURRENT APPROACH DOES NOT SCALE WELL  
	 * 
	 * send this to queue daily by a quartz job
	 * 
     */
    public void onMessage(Message message) {
    	
        try {   
        	logger.debug("==== onMessage");
        	        	
        	//we are gong to do a thread sleep here to deal with rate limiting
        	//this is expected to be processed in a synchronous queue
        	//with a single worker
        	Thread.sleep(sleepInSeconds*1000);
        	
        	if (message instanceof TextMessage) {
            	TextMessage tm = (TextMessage)message;
            	//deserialize the json
            	String msgText = tm.getText();
            	logger.debug(msgText);
            	
            	Map<String,Object> userData = 
            		mapper.readValue(
            				new ByteArrayInputStream(
            						msgText.getBytes()), Map.class);
            	
               	Long awardTypePk = 
            		new Long(userData.get("awardTypeId").toString());
               	Long raterPk = 
            		new Long(userData.get("raterId").toString());
            	
            	AwardType awardType = awardManagerService.findAwardTypeByPrimaryKey(awardTypePk);
            	//Patron r = patronManagerService.findPatronByPrimaryKey(raterPk);            	
            	Map<String,Object> awardModel = (Map<String,Object>)userData.get("award");
            	Award award = awardManagerService.findAwardByPrimaryKey(new Long(awardModel.get("id").toString()));
            
            	//really should fire working memory rules against rater that has expired 
            	//awards APPROACH DOES NOT SCALE WELL
            	//raterAwardsService.saveUpdateAwardOffer(award, awardType, r);
            	
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
			

		}
    }


//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}


	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}


	public void setRaterAwardsService(PatronAwardsService raterAwardsService) {
		this.raterAwardsService = raterAwardsService;
	}


	public void setAwardManagerService(AwardManagerService awardManagerService) {
		this.awardManagerService = awardManagerService;
	}


	public void setSleepInSeconds(Integer sleepInSeconds) {
		this.sleepInSeconds = sleepInSeconds;
	}
    
    
}
