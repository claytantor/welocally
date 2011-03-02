package com.sightlyinc.ratecred.admin.job;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.sightlyinc.ratecred.admin.jms.UpdateAwardOfferMessageProducer;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.service.AwardManagerService;

@Component("raterAwardsUpdateOffersJob")
public class RaterAwardsUpdateOffersJob extends QuartzJobBean {
	
	static Logger logger = Logger.getLogger(RaterAwardsUpdateOffersJob.class);
	
	private AwardManagerService awardManagerService;
	
	private UpdateAwardOfferMessageProducer updateAwardOfferMessageProducer;
	
	private SessionFactory sessionFactory;
	


	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			logger.debug("running offer update rules job:"+Calendar.getInstance().getTimeInMillis());
			
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
			
			//find awards whose offers are expired only on bl failure
        	//really should fire working memory rules against rater that has expired 
        	//awards

			List<AwardOffer> expired = awardManagerService.findExpiredAwardOffers();
			
			logger.debug("number of expired offers:"+expired.size());
			
			for (AwardOffer awardOffer : expired) {
				//logger.debug(awardOffer.getOffer().getExpireDateMillis());
				
				if(awardOffer.getAward() != null)
				{
					//dont throw if one has a problem
					try {
						updateAwardOfferMessageProducer
						.generateMessage(
								awardOffer.getAward(), 
								awardOffer.getAwardType(), 
								awardOffer.getAward().getOwner());
					} catch (JsonGenerationException e) {
						logger.error("problem udating expired offer", e);
					} catch (JsonMappingException e) {
						logger.error("problem udating expired offer", e);
					} catch (JMSException e) {
						logger.error("problem udating expired offer", e);
					} catch (IOException e) {
						logger.error("problem udating expired offer", e);
					}
				} else { //delete orphaned award offers
					awardManagerService.deleteAwardOffer(awardOffer);					
				}
				
				
			}
			
			
			
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem updating expired offers", e);
			throw new JobExecutionException(e);
		} catch (Exception e) {
			logger.error("problem updating expired offers", e);
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

	public void setAwardManagerService(AwardManagerService awardManagerService) {
		this.awardManagerService = awardManagerService;
	}

	public void setUpdateAwardOfferMessageProducer(
			UpdateAwardOfferMessageProducer updateAwardOfferMessageProducer) {
		this.updateAwardOfferMessageProducer = updateAwardOfferMessageProducer;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}



}
