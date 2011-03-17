package com.sightlyinc.ratecred.admin.job;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Synchronization;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.noi.utility.date.DateUtils;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.RaterAwardsService;
import com.sightlyinc.ratecred.service.RatingManagerService;

@Component("unlockOffersJob")
public class UnlockOffersJob extends QuartzJobBean {
	
	static Logger logger = Logger.getLogger(UnlockOffersJob.class);
	
	private AwardManagerService awardManagerService;
	
	private RatingManagerService ratingManagerService;
	
	private RaterAwardsService raterAwardsService;
		
	private SessionFactory sessionFactory;
	
	@Value("${twitter.rateCredOAuth.appConsumerKey}")
	private String ratecredTwitterConsumerKey;
	
	@Value("${twitter.rateCredOAuth.appSecretKey}")
	private String ratecredTwitterConsumerSecret;
	
	
	

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			logger.debug("running unlockOffersJob:"+Calendar.getInstance().getTimeInMillis());
			
			//bind session to thread
			Session session = null;   
			try { 
              session = SessionFactoryUtils.getSession(sessionFactory, false); 
			}
			// If not already bound the Create and Bind it! 
			catch (java.lang.IllegalStateException ex) { 
              session = SessionFactoryUtils.getSession(sessionFactory, true);  
              TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session)); 
			}
			session.setFlushMode(FlushMode.AUTO);
			
			System.setProperty("twitter4j.debug","true");
			System.setProperty("twitter4j.loggerFactory","twitter4j.internal.logging.Log4JLoggerFactory");

			Twitter twitter = 
				new TwitterFactory().getOAuthAuthorizedInstance(
						ratecredTwitterConsumerKey, ratecredTwitterConsumerSecret);	 

			Query query = new Query();
			
			//YYYY-MM-DD			
			query.setSince(
					DateUtils.dateToString(
							Calendar.getInstance().getTime(),
							DateUtils.DESC_SIMPLE_FORMAT));
						
			query.setQuery("#ratecred_deal");
			QueryResult result = twitter.search(query);
			List<Tweet> tweets = result.getTweets();
			
			Map<Long,OfferUnlockTweetHolder> holder = 
				new HashMap<Long,OfferUnlockTweetHolder>();
			
			//make a lookup to verify that that both tweet and retweet exist
			populateHolder(holder, tweets);
			
			//now iterate through
			//create awards
			for(Long offerId: holder.keySet()) {
				OfferUnlockTweetHolder holderItem =
					holder.get(offerId);
				if(holderItem.isUnlocked()) {
					Tweet request = holderItem.getOfferUnlockRequest();
					
					AwardOffer offerUnlocked = 
						awardManagerService.findAwardOfferByPrimaryKey(offerId);
					
					Rater raterWhoRequested = 
						ratingManagerService.findRaterByUsername(request.getFromUser());
					
					List<Award> offerGiven =
						awardManagerService.findAwardByOfferRater(offerUnlocked, raterWhoRequested);
					
					//dont award unlock more than once 
					if(offerGiven.size() == 0) {
						
						AwardType awardType = 
							awardManagerService.findAwardTypeByKey("unlocked");
						Award unlockAward = new Award();
						unlockAward.setGiveOffer(true);
						unlockAward.setStatus("GIVEN");
						unlockAward.setNotes("@"+request.getToUser()+" has unlocked an offer for you.");
						Long now = Calendar.getInstance().getTimeInMillis();
						unlockAward.setTimeCreatedMills(now);
						
						raterAwardsService.saveNewAward(
								unlockAward, awardType, raterWhoRequested, offerUnlocked);
						
					}
					
					
				}
			}
		
			
			
			
			
		} /*catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem updating expired offers", e);
			throw new JobExecutionException(e);
		}*/ catch (TwitterException e) {
			logger.error("problem getting getting unlocks", e);
			throw new JobExecutionException(e);
		} catch (Exception e) {
			logger.error("problem getting getting unlocks", e);
			throw new JobExecutionException(e);
		} finally {
			//unbind
			SessionHolder sessionHolder = (SessionHolder) 
	        TransactionSynchronizationManager.unbindResource(sessionFactory);
	        if(!FlushMode.MANUAL.equals(sessionHolder.getSession().getFlushMode())) {
	           sessionHolder.getSession().flush(); 
	        }
	        SessionFactoryUtils.closeSession(sessionHolder.getSession());
		}
				
	}
	
	private void populateHolder(Map<Long,OfferUnlockTweetHolder> holder, List<Tweet> tweets) {
		
		//make a lookup to verify that that both tweet and retweet exist
		for (Tweet tweet : tweets) {
			//see if there is a holder
			Long offerId = getOfferIdFromTweet(tweet);
			
			OfferUnlockTweetHolder holderItem =
				holder.get(offerId);
			
			if(holderItem == null)
				holderItem = new OfferUnlockTweetHolder();
			
			if(isOfferUnlockRetweet(tweet))
				holderItem.setOfferUnlockRetweet(tweet);
			else if(isOfferRequestUnlockTweet(tweet))
				holderItem.setOfferUnlockRequest(tweet);
			
			//check to see if the user who is requesting alreday has
			//the offer, if not then put it in the map
			//awardManagerService.findAwardByOfferRater(offer, r)
			
			holder.put(offerId, holderItem);
			
			
			logger.debug(tweet.getText());
		}
	}
	
	private Boolean isOfferUnlockRetweet(Tweet t) {
		
		if(!StringUtils.isEmpty(t.getFromUser()) 
				&& StringUtils.isEmpty(t.getToUser()) &&
				t.getText().contains("RT"))
			return true;
		else
			return false;
	}
	
	private Boolean isOfferRequestUnlockTweet(Tweet t) {
		
		if(!StringUtils.isEmpty(t.getFromUser()) 
				&& !StringUtils.isEmpty(t.getToUser()))
			return true;
		else
			return false;
	}
	
	private AwardOffer getAwardOfferForTweet(Tweet t) {
		try {
			AwardOffer unlockedOffer = 
				awardManagerService.findAwardOfferByPrimaryKey(getOfferIdFromTweet(t));
			
			return unlockedOffer;
			
		} catch (NumberFormatException e) {
			logger.error("problem getting unlock offer", e);
			
		} catch (BLServiceException e) {
			logger.error("problem getting unlock offer", e);
		} catch (Exception e) {
			logger.error("problem getting unlock offer", e);
		}
		
		return null;
	}
	

	
	private Long getOfferIdFromTweet(Tweet t) throws NumberFormatException {
		try {
			int hashTagPos = t.getText().indexOf("#ratecred_deal")+"#ratecred_deal".length()+1;
			int nextSpace = t.getText().indexOf(' ', hashTagPos);
			String offerIdString = t.getText().substring(hashTagPos, nextSpace);
			return Long.parseLong(offerIdString);
		} catch (NumberFormatException e) {
			logger.error("problem getting offer id", e);
			throw e;
		}
	}

	public void setAwardManagerService(AwardManagerService awardManagerService) {
		this.awardManagerService = awardManagerService;
	}

	public void setRatingManagerService(RatingManagerService ratingManagerService) {
		this.ratingManagerService = ratingManagerService;
	}
	

	public RaterAwardsService getRaterAwardsService() {
		return raterAwardsService;
	}

	public void setRaterAwardsService(RaterAwardsService raterAwardsService) {
		this.raterAwardsService = raterAwardsService;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private class OfferUnlockTweetHolder {
		
		private Tweet offerUnlockRequest;
		private Tweet offerUnlockRetweet;
		private Long awardOfferId;
		
		public Tweet getOfferUnlockRequest() {
			return offerUnlockRequest;
		}
		public void setOfferUnlockRequest(Tweet offerUnlockRequest) {
			this.offerUnlockRequest = offerUnlockRequest;
		}
		public Tweet getOfferUnlockRetweet() {
			return offerUnlockRetweet;
		}
		public void setOfferUnlockRetweet(Tweet offerUnlockRetweet) {
			this.offerUnlockRetweet = offerUnlockRetweet;
		}
		public Long getAwardOfferId() {
			return awardOfferId;
		}
		public void setAwardOfferId(Long awardOfferId) {
			this.awardOfferId = awardOfferId;
		}
		
		
		public boolean isUnlocked() {
			if(offerUnlockRequest != null && offerUnlockRetweet != null)
				return true;
			else
				return false;
		}
		
		
	}

	public void setRatecredTwitterConsumerKey(String ratecredTwitterConsumerKey) {
		this.ratecredTwitterConsumerKey = ratecredTwitterConsumerKey;
	}

	public void setRatecredTwitterConsumerSecret(
			String ratecredTwitterConsumerSecret) {
		this.ratecredTwitterConsumerSecret = ratecredTwitterConsumerSecret;
	}



}
