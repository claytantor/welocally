package com.sightlyinc.ratecred.admin.mvc.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.service.NetworkMemberService;

@Controller
@RequestMapping(value="/home")
public class HomeController {
	
	static Logger logger = Logger.getLogger(HomeController.class);
	
	@Autowired
	UserPrincipalService userPrincipalService;
	
	@Autowired
	NetworkMemberService networkMemberService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Model model) {
		
		//see if the user is a member
		UserDetails details = 
			(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			UserPrincipal principal = 
				userPrincipalService.loadUser(details.getUsername());
			NetworkMember member =
				networkMemberService.findMemberByUserPrincipal(principal); 
			model.addAttribute("member", member);
			
		} catch (UserPrincipalServiceException e) {
			logger.error("", e);
			model.addAttribute("error", e);
			return "error";
		} catch (UserNotFoundException e) {
			logger.error("", e);
			model.addAttribute("error", e);
			return "error";
		}
		
		
		return "home";
	}

}
