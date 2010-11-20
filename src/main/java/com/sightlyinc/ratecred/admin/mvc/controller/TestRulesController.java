package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.drools.FactException;
import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.CityStateEvaluator;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.RaterMetrics;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.AwardsUtils;
import com.sightlyinc.ratecred.service.OfferPoolService;
import com.sightlyinc.ratecred.service.RaterAwardsService;
import com.sightlyinc.ratecred.service.RatingManagerService;

@Controller
@RequestMapping(value="/admin/rules")
public class TestRulesController {
	
	
	static Logger logger = Logger.getLogger(TestRulesController.class);

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
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String getHome(Model model) {
		return "rules_home";
	}
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String getAward(Model model) {
		
		try {
			RuleBase ruleBase = 
				RuleBaseLoader.loadFromInputStream(
						TestRulesController.class.getResourceAsStream("/rules/offer.java.drl"));
			
			WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
			
			List<Offer> offersRaw = offerPoolService.getOfferPool();
			boolean dynamic = true;
			for (Offer offer : offersRaw) {
				workingMemory.assertObject( offer, dynamic );
			}
			
			workingMemory.fireAllRules( );
			model.addAttribute("offers", offersRaw);
			
		} catch (IntegrationException e) {
			logger.error("IntegrationException", e);
		} catch (FactException e) {
			logger.error("FactException", e);
		} catch (SAXException e) {
			logger.error("SAXException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
		
		return "offers";
	}
	
	@RequestMapping(value="/target/{rater}", method=RequestMethod.GET)
	public String getAward(@PathVariable("rater") String rater, Model model) {
		
		try {
			RuleBase ruleBase = 
				RuleBaseLoader.loadFromInputStream(
						TestRulesController.class.getResourceAsStream("/rules/rater_awards.java.drl"));
			
			WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
			boolean dynamic = true;
			
			Rater r = ratingManagerService.findRaterByUsername(rater);
			RaterMetrics rm = ratingManagerService.findMetricsByRater(r);
			r.setMetrics(rm);
			RaterAwards ra = new RaterAwards(r);
			workingMemory.assertObject( ra, true );
			
			workingMemory.fireAllRules( );
			
			raterAwardsService.proccessAwardsForRater(ra);
			
			model.addAttribute("raterAwards",ra);
			model.addAttribute("rater",r);
			
			
			
		} catch (IntegrationException e) {
			logger.error("IntegrationException", e);
		} catch (FactException e) {
			logger.error("FactException", e);
		} catch (SAXException e) {
			logger.error("SAXException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} catch (BLServiceException e) {
			logger.error("BLServiceException", e);
		}
		
		return "rater_awards";
	}
	
	
	@RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
	public String getRatersByStatus(@PathVariable("status") String status, Model model, HttpServletRequest request) {
		try {
			
			RuleBase ruleBase = 
				RuleBaseLoader.loadFromInputStream(
						TestRulesController.class.getResourceAsStream("/rules/raters.java.drl"));
			
			WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
			
			List<Rater> raters = ratingManagerService.findRatersByStatus(status);
			for (Rater rater : raters) {
				
			}
			
			workingMemory.fireAllRules( );
			
			model.addAttribute("raters", raters);
			
			
		} catch (IntegrationException e) {
			logger.error("IntegrationException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (FactException e) {
			logger.error("FactException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (SAXException e) {
			logger.error("SAXException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (IOException e) {
			logger.error("IOException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			model.addAttribute("error", e);
			return "error";
		} 
		
		return "raters";
			
	}
		
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String getRaters(Model model, HttpServletRequest request) {
		try {
			Long t1 = Calendar.getInstance().getTimeInMillis();
			RuleBase ruleBase = RuleBaseLoader
					.loadFromInputStream(TestRulesController.class
							.getResourceAsStream("/rules/rater_awards.java.drl"));

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			boolean dynamic = true;

			List<Rater> allRaters = ratingManagerService.findAllRaters();
			Map<String, PlaceCityState> allcs = new HashMap<String, PlaceCityState>();
			List<RaterAwards> raList = new ArrayList<RaterAwards>();
			
			
			for (Rater rater : allRaters) {
				RaterMetrics rm = ratingManagerService.findMetricsByRater(rater);
				rater.setMetrics(rm);
				RaterAwards ra = new RaterAwards(rater);
				List<PlaceCityState> cities = AwardsUtils.getCitiesRated(rater);
				AwardsUtils.addCitiesToMap(cities, allcs);
				raList.add(ra);
			}
			
			
			for (RaterAwards raterAwards : raList) {
				workingMemory.assertObject( raterAwards, dynamic );
			}

			//

			for (PlaceCityState placeCityState : allcs.values()) {
				CityStateEvaluator cseval = new CityStateEvaluator(
						placeCityState, allRaters);
				workingMemory.assertObject(cseval, dynamic);
			}

			workingMemory.fireAllRules();
			
			//save those awards for everyone
			for (RaterAwards raterAwards : raList) {
				raterAwardsService.proccessAwardsForRater(raterAwards);
			}
			
			List<Rater> raters = ratingManagerService.findRatersByStatus("USER");
			model.addAttribute("raters", raters);
			
			Long t2 = Calendar.getInstance().getTimeInMillis();
			
			Long delta = t2-t1;
			model.addAttribute("delta", delta);
			return "raters";
			

		} catch (IntegrationException e) {
			logger.error("IntegrationException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (FactException e) {
			logger.error("FactException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (SAXException e) {
			logger.error("SAXException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (IOException e) {
			logger.error("IOException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			model.addAttribute("error", e);
			return "error";
		}

	}
	
	@RequestMapping(value = "/since/{millis}", method = RequestMethod.GET)
	public String getRatersSince(
			@PathVariable("millis") Long millis, 
			Model model, 
			HttpServletRequest request) {
		
		try {
			Long t1 = Calendar.getInstance().getTimeInMillis();
			RuleBase ruleBase = RuleBaseLoader
					.loadFromInputStream(TestRulesController.class
							.getResourceAsStream("/rules/rater_awards.java.drl"));

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			boolean dynamic = true;
			
			Set<Rater> allRaters = new HashSet<Rater>();
			
			List<Rating> ratingsSince = 
				ratingManagerService.findRatingsSince(millis);
			
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
				List<PlaceCityState> cities = AwardsUtils.getCitiesRated(rater);
				AwardsUtils.addCitiesToMap(cities, allcs);
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
			
			List<Rater> raters = ratingManagerService.findRatersByStatus("USER");
			model.addAttribute("raters", raters);
			
			Long t2 = Calendar.getInstance().getTimeInMillis();
			
			Long delta = t2-t1;
			model.addAttribute("delta", delta);
			return "raters";
			

		} catch (IntegrationException e) {
			logger.error("IntegrationException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (FactException e) {
			logger.error("FactException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (SAXException e) {
			logger.error("SAXException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (IOException e) {
			logger.error("IOException", e);
			model.addAttribute("error", e);
			return "error";
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			model.addAttribute("error", e);
			return "error";
		}

	}
	
	
	
	

	public void setOfferPoolService(OfferPoolService offerPoolService) {
		this.offerPoolService = offerPoolService;
	}
	


}
