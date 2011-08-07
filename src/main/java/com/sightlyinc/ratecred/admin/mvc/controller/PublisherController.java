package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.PublisherService;

@Controller
@RequestMapping(value="/publisher/publisher")
public class PublisherController {
		
	static Logger logger = Logger.getLogger(PublisherController.class);

	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private UserPrincipalService userPrincipalService;
	
	@Autowired
	private NetworkMemberService networkMemberService;
	
	@ModelAttribute("member")
    public NetworkMember getPublisherMember() {
		UserDetails details = 
			(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		try {			
			UserPrincipal principal = userPrincipalService.loadUser(details.getUsername());
			return networkMemberService.findMemberByUserPrincipal(principal);		
		} catch (UserPrincipalServiceException e) {
			logger.error("", e);
			return null;
		} catch (UserNotFoundException e) {
			logger.error("", e);
			return null;
		}
    }
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		Publisher publisherForm = new Publisher(); 
		model.addAttribute("publisherForm",publisherForm);
		return "publisher/edit";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid Publisher form, BindingResult result, Model model) {
		logger.debug("got post action");
		Publisher p = new Publisher();
		try {	
			if(form.getId() != null)
				p = publisherService.findByPrimaryKey(form.getId());
			
			if(p!= null)
			{
				p.setMonthlyPageviews(form.getMonthlyPageviews());
				p.setSiteName(form.getSiteName());
				p.setDescription(form.getDescription());
				p.setSummary(form.getSummary());
				p.setUrl(form.getUrl());
				p.setMapIconUrl(form.getMapIconUrl());
				p.setIconUrl(form.getIconUrl());
				p.setKey(getPublisherKeyFromName(form.getSiteName()));
				
				//set the member
				//prepopulate member
				UserDetails details = 
				(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
							
				UserPrincipal principal = userPrincipalService.loadUser(details.getUsername());
				NetworkMember member = networkMemberService.findMemberByUserPrincipal(principal);
				p.setNetworkMember(member);
							
				Long id = publisherService.save(p);
				return "redirect:/publisher/publisher/"+id.toString();
				
			} else {
				model.addAttribute("publisher", p);
				return "publisher/edit";
			}
		
		} catch (UserPrincipalServiceException e) {
			logger.error("", e);
			return null;
		} catch (UserNotFoundException e) {
			logger.error("", e);
			return null;
		}

	}
	
	private String getPublisherKeyFromName(String siteName) {
    	return siteName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "")
			.replaceAll(" ", "-");
    }
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getPublisherById(@PathVariable Long id, Model model) {
		logger.debug("view");
		model.addAttribute("publisher", publisherService.findByPrimaryKey(id));
		return "publisher/view";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String editPublisher(@PathVariable Long id, Model model) {
		logger.debug("edit");
		model.addAttribute(
				"publisherForm",
				publisherService.findByPrimaryKey(id));
		return "publisher/edit";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deletePublisher(@PathVariable Long id, Model model) {
		logger.debug("delete");
		Publisher p = publisherService.findByPrimaryKey(id);
		
		publisherService.delete(p);
		return "redirect:/home";
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String getList(Model model) {
		logger.debug("list");
		//see if the user is a member
		UserDetails details = 
			(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			
			UserPrincipal principal = userPrincipalService.loadUser(details.getUsername());
			model.addAttribute("publishers", publisherService.findByUserPrincipal(principal));
			return "publisher/list";
			
		} catch (UserPrincipalServiceException e) {
			logger.error("", e);
			model.addAttribute("error", e);
			return "error";
		} catch (UserNotFoundException e) {
			logger.error("", e);
			model.addAttribute("error", e);
			return "error";
		}
		
	}

    @RequestMapping("/search")
    public String searchByName(@RequestParam("siteName") String siteName, Model model) {
        logger.debug("search by siteName");
        List<Publisher> publisers = publisherService.findBySiteNameLike(siteName);
        model.addAttribute("publishers", publisers);
        return "publisher/list_json";
    }
}
