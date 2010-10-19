package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.IteratorTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import twitter4j.TwitterException;
import twitter4j.http.AccessToken;

import com.noi.utility.date.DateUtils;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.noi.utility.xml.JsonEncoder;
import com.sightlyinc.ratecred.compare.ModelSort;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.User;
import com.sightlyinc.ratecred.service.RatingManagerService;

@Controller
@RequestMapping(value = "/admin/rater")
@SessionAttributes("accessToken")
public class RaterController {

	static Logger logger = Logger.getLogger(RaterController.class);

	@Autowired
	@Qualifier("RatingManagerService")
	private RatingManagerService ratingManagerService;


	@RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
	public String getAllRaters(@PathVariable("status") String status, Model model, HttpServletRequest request) {
		try {
			List<Rater> raters = ratingManagerService.findRatersByStatus(status);
			model.addAttribute("raters", raters);
			return "raters";
			
			
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			model.addAttribute("error", e);
			return "error";
		} 
			
	}
	
	@RequestMapping(value = "/profile/uname/{uname}", method = RequestMethod.GET)
	public String getProfileByUsername(@PathVariable("uname") String uname, Model model, HttpServletRequest request) {
		try {


			Rater r = ratingManagerService.findRaterByUsername(uname);
			
			if(r != null)
				return getProfile(r.getId(),  model, request);
			else
			{
				model.addAttribute("error", new Exception("cannot find rater"));
				return "error";
			}
			
			
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			model.addAttribute("error", e);
			return "error";
		} 
			
	}
	

	@RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
	public String getProfile(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
		try {
			
			
			Rater rater = ratingManagerService.findRaterByPrimaryKey(id);
			
			
			model.addAttribute("ratings", ModelSort.getInstance().sortRating(
					rater.getRatings()));

			rater.getAwards().addAll(
					ratingManagerService.findAwardsLocalByRater(rater));

			model.addAttribute("rater", rater);
			model.addAttribute("raterMetrics", ratingManagerService.findMetricsByRater(rater));
			
			return "rater_profile";
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			model.addAttribute("error", e);
			return "error";
		} 
	}


	

	@RequestMapping(value = "/{twitterScreenName}", method = RequestMethod.GET)
	public String getRaterByTwitterScreenName(
			@PathVariable("twitterScreenName") String twitterScreenName,
			Model model) {
		
		logger.debug("getRaterByTwitterScreenName");

		try {
			Rater rater = ratingManagerService
					.findRaterByUsername(twitterScreenName);

			rater.getAwards().addAll(
			  ratingManagerService.findAwardsLocalByRater(rater));
			
			model.addAttribute("itool", new IteratorTool());
			model.addAttribute("encoder", JsonEncoder.getInstance());
			model.addAttribute("ratingsSorted", ModelSort.getInstance().sortRating(rater.getRatings()));
			model.addAttribute("rater", rater);
			
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
		}
		return "rater";

	}
	
	@RequestMapping(value = "/leaders", method = RequestMethod.GET)
	public String getLeaders(
			@RequestParam(value="size",required=false) Integer size, 
			Model model) {
		try {
			
			if (size == null)
				size = 10;

			// get the leaders
			List<Rater> leaders = ratingManagerService
					.findRatersByScoreDesc(size);
			
			model.addAttribute("raters", leaders);
			model.addAttribute("itool", new IteratorTool());
						
			return "leaders";
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			model.addAttribute("error", e);
			return "error";
		}
	}
	
	
	//------------ exception handlers -------------//
	@ExceptionHandler(HttpSessionRequiredException.class)
	public String handleHttpSessionRequiredException(
			HttpSessionRequiredException ex, HttpServletRequest request) {
		return "error";
	}
	
	
	


}
