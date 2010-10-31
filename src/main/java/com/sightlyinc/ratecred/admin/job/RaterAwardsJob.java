package com.sightlyinc.ratecred.admin.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
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
	
	@Autowired
	@Qualifier("RatingManagerService")
	private RatingManagerService ratingManagerService;
	
	@Autowired
	@Qualifier("offerPoolService")
	private OfferPoolService offerPoolService;
	
	@Autowired
	private AwardManagerService awardManagerService;
	
	@Autowired
	RaterAwardsService raterAwardsService;
	
	@Value("${raterAwardsJob.millisSince}")
	private Long millisSince;

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			logger.debug("running rules job");
			Long t1 = Calendar.getInstance().getTimeInMillis();
			RuleBase ruleBase = RuleBaseLoader
					.loadFromInputStream(TestRulesController.class
							.getResourceAsStream("/rules/rater_awards.java.drl"));

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			boolean dynamic = true;
			
			Set<Rater> allRaters = new HashSet<Rater>();
			
			List<Rating> ratingsSince = 
				ratingManagerService.findRatingsSince(millisSince);
			
			//what we are doing here is getting the top ten raters
			//in all rated areas for the period
			for (Rating rating : ratingsSince) {
				allRaters.add(rating.getOwner());
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
				workingMemory.assertObject( raterAwards, dynamic );
			}

			for (PlaceCityState placeCityState : allcs.values()) {
				CityStateEvaluator cseval = new CityStateEvaluator(
						placeCityState, new ArrayList<Rater>(allRaters));
				workingMemory.assertObject(cseval, dynamic);
			}

			workingMemory.fireAllRules();
			
			//save those awards for everyone
			for (RaterAwards raterAwards : raList) {
				raterAwardsService.proccessAwardsForRater(raterAwards);
			}
			
			
			

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

}
