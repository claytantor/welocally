package com.sightlyinc.ratecred.admin.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.drools.FactException;
import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;
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
import org.xml.sax.SAXException;

import com.sightlyinc.ratecred.admin.model.CityStateEvaluator;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.admin.mvc.controller.TestRulesController;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.RaterMetrics;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.AwardsRulesUtils;
import com.sightlyinc.ratecred.service.OfferPoolService;
import com.sightlyinc.ratecred.service.RaterAwardsService;
import com.sightlyinc.ratecred.service.RatingManagerService;

@Component("raterAwardsJob")
public class RaterAwardsJob extends QuartzJobBean {
	
	static Logger logger = Logger.getLogger(RaterAwardsJob.class);
	
	private SessionFactory sessionFactory;
	
	private RatingManagerService ratingManagerService;
	
	private OfferPoolService offerPoolService;
	
	private AwardManagerService awardManagerService;
	
	RaterAwardsService raterAwardsService;
	
	private Long millisSince;

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			logger.debug("running rules job");
			
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
			
			
			RuleBase ruleBase = RuleBaseLoader
					.loadFromInputStream(TestRulesController.class
							.getResourceAsStream("/rules/rater_awards.java.drl"));

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			boolean dynamic = true;
			
			Set<Rater> allRaters = new HashSet<Rater>();
			Long since = Calendar.getInstance().getTimeInMillis()-millisSince;
			logger.debug("finding ratings since:"+new Date(since));
			List<Rating> ratingsSince = 
				ratingManagerService.findRatingsSince(millisSince);
			
			logger.debug("number found:"+ratingsSince.size());
			
			//what we are doing here is getting the top ten raters
			//in all rated areas for the period
			for (Rating rating : ratingsSince) {
				
				Rater r =  null;
				try {
					r = rating.getOwner();
					allRaters.add(r);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				PlaceCityState pcs = 
					new PlaceCityState(
							rating.getPlace().getCity(), 
							rating.getPlace().getState(), 
							null);
				
				allRaters.addAll(
						ratingManagerService.findRatersByCityStateScoreDesc(pcs, 10));
			}
						
			Map<String, PlaceCityState> allcs = new HashMap<String, PlaceCityState>();
			List<RaterAwards> raList = new ArrayList<RaterAwards>();
						
			for (Rater rater : allRaters) {
				RaterMetrics rm = ratingManagerService.findMetricsByRater(rater);
				rater.setMetrics(rm);
				RaterAwards ra = new RaterAwards(rater);
				List<PlaceCityState> cities = AwardsRulesUtils.getCitiesRated(rater);
				AwardsRulesUtils.addCitiesToMap(cities, allcs);
				raList.add(ra);
			}
			
			
			for (RaterAwards raterAwards : raList) {
				logger.debug("adding rater:"+raterAwards.getRater().getUserName());
				workingMemory.assertObject( raterAwards, dynamic );
			}

			for (PlaceCityState placeCityState : allcs.values()) {
				logger.debug("adding cs:"+placeCityState.getCity()+placeCityState.getState());
				CityStateEvaluator cseval = new CityStateEvaluator(
						placeCityState, new ArrayList<Rater>(allRaters));
				workingMemory.assertObject(cseval, dynamic);
			}

			workingMemory.fireAllRules();
			
			//save those awards for everyone
			for (RaterAwards raterAwards : raList) {
				logger.debug("processing rater:"+raterAwards.getRater().getUserName()+" add:"+raterAwards.getAwards().size()+" remove:"+raterAwards.getRemoveAwards().size());
				raterAwardsService.proccessAwardsForRater(raterAwards);
			}
			
			//unbind
			SessionHolder sessionHolder = (SessionHolder) 
	        TransactionSynchronizationManager.unbindResource(sessionFactory);
	        if(!FlushMode.MANUAL.equals(sessionHolder.getSession().getFlushMode())) {
	           sessionHolder.getSession().flush(); 
	        }
	        SessionFactoryUtils.closeSession(sessionHolder.getSession());
			

		} catch (IntegrationException e) {
			logger.error("IntegrationException", e);
			throw new JobExecutionException(e);
		} catch (FactException e) {
			logger.error("FactException", e);
			throw new JobExecutionException(e);
		} catch (SAXException e) {
			logger.error("SAXException", e);
			throw new JobExecutionException(e);
		} catch (IOException e) {
			logger.error("IOException", e);
			throw new JobExecutionException(e);
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			throw new JobExecutionException(e);
		}
				
	}

	public void setMillisSince(Long millisSince) {
		this.millisSince = millisSince;
	}

	public void setRatingManagerService(RatingManagerService ratingManagerService) {
		this.ratingManagerService = ratingManagerService;
	}

	public void setOfferPoolService(OfferPoolService offerPoolService) {
		this.offerPoolService = offerPoolService;
	}

	public void setAwardManagerService(AwardManagerService awardManagerService) {
		this.awardManagerService = awardManagerService;
	}

	public void setRaterAwardsService(RaterAwardsService raterAwardsService) {
		this.raterAwardsService = raterAwardsService;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	

}
