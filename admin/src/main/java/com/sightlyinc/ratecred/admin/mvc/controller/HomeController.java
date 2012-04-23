package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sightlyinc.ratecred.authentication.Role;
import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.OrderService;
import com.sightlyinc.ratecred.service.PublisherService;

@Controller
@RequestMapping(value="/home")
public class HomeController extends AbstractBaseController {
	
	static Logger logger = Logger.getLogger(HomeController.class);
	
	@Autowired
	UserPrincipalService userPrincipalService;
	
	@Autowired
	NetworkMemberService networkMemberService;
	
	@Autowired
	PublisherService publisherService;
	
	@Autowired
	OrderService orderService;
	
	@Value("${HomeController.order.daysTrailing:7}")
	private Integer daysTrailing;
		
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Model model) {
		
		//see if the user is a member
		UserDetails details = 
			(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			UserPrincipal principal = 
				userPrincipalService.loadUser(details.getUsername());
			
			if(userPrincipalService.hasUserRole(principal, "ROLE_PUBLISHER")){
				Publisher p = publisherService.findByPublisherKey(details.getUsername());
				model.addAttribute("publisher", p);
				
			} else if(userPrincipalService.hasUserRole(principal, "ROLE_MEMBER")) {
				NetworkMember member =
					networkMemberService.findMemberByUserPrincipal(principal);				
				model.addAttribute("member", member); 
				model.addAttribute("orders", orderService.findByDaysTrailing(daysTrailing)); 
				
			}
			
			
			
			Set<Role> roles = principal.getRoles();
			
			
			
			
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
