package com.sightlyinc.ratecred.admin.mvc.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.model.AjaxLogin;
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
	
	@ModelAttribute("subscriptionStatusTypes")
    public String[] getSubscriptionStatusTypes() {
		String[] subscriptionTypes = new String[]{ "KEY_ASSIGNED", "SUBSCRIBER","CANCELLED","SUBSCRIPTION FAILURE","SUSPENDED" };
		return subscriptionTypes;
    }
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		Publisher publisherForm = new Publisher(); 
		
		model.addAttribute("publisherForm",publisherForm);
		return "publisher/edit";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid Publisher form, BindingResult result, Model model) {
		logger.debug("got post action");
		Publisher p = new Publisher();
		try {	
			if(form.getId() != null)
				p = publisherService.findByPrimaryKey(form.getId());
			
			
			if(p!= null)
			{
				p.setMonthlyPageviews(form.getMonthlyPageviews());
				
				//this should not be editable without a process
				p.setKey(form.getKey());
				
				p.setJsonToken(form.getJsonToken());
				p.setSiteName(form.getSiteName());
				p.setDescription(form.getDescription());
				p.setSummary(form.getSummary());
				p.setUrl(form.getUrl());
				p.setMapIconUrl(form.getMapIconUrl());
				p.setIconUrl(form.getIconUrl());
				
				if(!StringUtils.isEmpty(form.getSubscriptionStatus()))
					p.setSubscriptionStatus(form.getSubscriptionStatus());
				

				
							
				UserPrincipal principal=null;
				try {
					principal = userPrincipalService.loadUser(p.getKey());
					//exists
					principal.setPassword(form.getJsonToken());
					userPrincipalService.saveUserPrincipal(principal);
				} catch (UserNotFoundException e) {
					//create the principal if missing
					//update the password to be the key
					principal = new UserPrincipal();
					principal.setUsername(p.getKey());
				} catch (org.springframework.security.userdetails.UsernameNotFoundException e){
					principal = new UserPrincipal();
					principal.setUsername(p.getKey());
				}

				
				String hashedPass = userPrincipalService.makeMD5Hash(form.getJsonToken());					
				principal.setPassword(hashedPass);
				
				if(p.getSubscriptionStatus().equals("SUBSCRIBER"))
					userPrincipalService.activateWithRoles(principal, Arrays.asList("ROLE_USER", "ROLE_PUBLISHER"));
				else
					userPrincipalService.deactivate(principal);
//				//set the member
//				//prepopulate member
				UserDetails adminDetails = 
				(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
				UserPrincipal networkPrincipal = userPrincipalService.loadUser(adminDetails.getUsername());
				NetworkMember member = networkMemberService.findMemberByUserPrincipal(networkPrincipal);
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
		} catch (NoSuchAlgorithmException e) {
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
	
	/*@RequestMapping(value="/plugin/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loginPublisher(@RequestBody String requestJson) {
		Map<String, Object> response = new HashMap<String, Object>();
        List<String> errors = new ArrayList<String>();
		logger.debug("login");
		
		String publisherKey = null;
        String publisherToken = null;

        try {
            JSONObject jsonObject = new JSONObject(requestJson);

            publisherKey = jsonObject.getString("publisherKey");
            publisherToken = jsonObject.getString("publisherToken");
            
        } catch (JSONException e) {
            logger.error("problem parsing json", e);
            errors.add("Unable to parse request, please check the format of your data");
        }
        
        if (errors.isEmpty() && 
        		!StringUtils.isEmpty(publisherToken) &&
        		!StringUtils.isEmpty(publisherKey)) {
        	Publisher p = publisherService.findByPublisherKeyAndToken(publisherKey, publisherToken);
        	AjaxLogin ajaxLogin = new AjaxLogin("SUCCEED", "http://www.yahoo.com");
        	response.put("ajaxLogin", ajaxLogin);
        	
        } else {
        	errors.add("required information missing");
        }
        	
        response.put("errors", errors);
		return response;
	}*/
	
	
	
	
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
