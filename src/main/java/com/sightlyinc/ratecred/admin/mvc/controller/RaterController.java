package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.IteratorTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.noi.utility.xml.JsonEncoder;
import com.sightlyinc.ratecred.compare.ModelSort;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.service.PatronManagerService;

@Controller
@RequestMapping(value = "/admin/rater")
@SessionAttributes("accessToken")
public class RaterController {

	static Logger logger = Logger.getLogger(RaterController.class);

//	@Autowired
//	@Qualifier("RatingManagerService")
//	private RatingManagerService ratingManagerService;
	
	@Autowired
	private PatronManagerService patronManagerService;


	@RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
	public String getAllRaters(@PathVariable("status") String status, Model model, HttpServletRequest request) {
		try {
			List<Patron> raters = patronManagerService.findPatronByStatus(status);
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


			Patron r = patronManagerService.findPatronByUsername(uname);
			
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
			
			
			Patron rater = patronManagerService.findPatronByPrimaryKey(id);
			
			
			model.addAttribute("ratings", new ArrayList());

			/*rater.getAwards().addAll(
					ratingManagerService.findAwardsLocalByRater(rater));*/

			model.addAttribute("rater", rater);
			model.addAttribute("raterMetrics", patronManagerService.findMetricsByPatron(rater));
			
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
			Patron rater = patronManagerService
					.findPatronByUsername(twitterScreenName);

			/*rater.getAwards().addAll(
			  ratingManagerService.findAwardsLocalByRater(rater));*/
			
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
			List<Patron> leaders = patronManagerService
					.findPatronsByScoreDesc(size);
			
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
