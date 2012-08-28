package com.sightlyinc.ratecred.admin.mvc.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sightlyinc.ratecred.admin.util.CollectionUtils;
import com.sightlyinc.ratecred.admin.util.HttpHelperUtils;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.Publisher.PublisherStatus;
import com.sightlyinc.ratecred.service.GeodbProvisionManager;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.PublisherService;
import com.welocally.admin.security.UserNotFoundException;
import com.welocally.admin.security.UserPrincipal;
import com.welocally.admin.security.UserPrincipalService;
import com.welocally.admin.security.UserPrincipalServiceException;

@Controller
@RequestMapping(value="/publisher")
public class PublisherController {
		
	static Logger logger = Logger.getLogger(PublisherController.class);

	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private UserPrincipalService userPrincipalService;
	
	@Autowired
	private NetworkMemberService networkMemberService;
	
    @Autowired
    private HttpHelperUtils httpHelperUtils;
    
    @Autowired GeodbProvisionManager geodbProvisionManager;
    
//    @Value("${geodb.endpoint:http://localhost:8082/geodb}")
//    private String geodbEnpoint;
//    
//    @Value("${geodb.provisionRequestMapping:/user/1_0/exists/}")
//    private String provisionExistsRequestMapping;
	
	
	@ModelAttribute("member")
    public NetworkMember getPublisherMember() {
		UserDetails details = 
			(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		try {			
			UserPrincipal principal = userPrincipalService.loadUser(details.getUsername());
			//return networkMemberService.findMemberByUserPrincipal(principal);	
			throw new RuntimeException("NO IMPL");
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
		
		List<String> subscriptionTypes = 
			CollectionUtils.toStringList(Publisher.PublisherStatus.class);
		
		return (String[]) subscriptionTypes.toArray(new String[subscriptionTypes
                .size()]);
		
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

				//this should not be editable without a process
				p.setKey(form.getKey());			
				p.setJsonToken(form.getJsonToken());
				p.setName(form.getName());
				p.setDescription(form.getDescription());
				p.setMapIconUrl(form.getMapIconUrl());
				p.setIconUrl(form.getIconUrl());
				
				if(form.getSubscriptionStatus()!= null)
					p.setSubscriptionStatus(form.getSubscriptionStatus());
				
				UserPrincipal principal=null;
				try {
					principal = userPrincipalService.loadUser(p.getKey());
					//exists
					throw new RuntimeException("DONT WANT THIS TO HAPPEN");
					//principal.setPassword(form.getJsonToken());
					//userPrincipalService.saveUserPrincipal(principal);
				} catch (UserNotFoundException e) {
					//create the principal if missing
					//update the password to be the key
					principal = new UserPrincipal();
					principal.setUsername(p.getKey());
				} 

				
				String hashedPass = userPrincipalService.makeMD5Hash(form.getJsonToken());					
				principal.setPassword(hashedPass);
				
				if(p.getSubscriptionStatus().equals(PublisherStatus.SUBSCRIBED))
					userPrincipalService.activateWithRoles(principal, Arrays.asList("ROLE_USER", "ROLE_PUBLISHER"));
				else
					userPrincipalService.deactivate(principal);
//				//set the member
//				//prepopulate member
				UserDetails adminDetails = 
				(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
				UserPrincipal networkPrincipal = userPrincipalService.loadUser(adminDetails.getUsername());
				//NetworkMember member = networkMemberService.findMemberByUserPrincipal(networkPrincipal);
//				p.setNetworkMember(member);
//				
//							
//				Long id = publisherService.save(p);
//				return "redirect:/publisher/"+id.toString();
				throw new RuntimeException("NO IMPL");
				
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
	
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getPublisherById(@PathVariable Long id, Model model) {
		logger.debug("view");
		
		Publisher p = publisherService.findByPrimaryKey(id);
		model.addAttribute("publisher", p);
		
		UserDetails authUser = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		try {
            for (int i = 0; i < authUser.getAuthorities().size(); i++) {
                GrantedAuthority auth = new ArrayList<GrantedAuthority>(authUser.getAuthorities()).get(i);
                if(auth.toString().equals("ROLE_ADMIN")){
                    
//                    GeodbProvisionStatus status = geodbProvisionManager.status(p.getUserPrincipal(), 
//                            authUser.getUsername(), 
//                            authUser.getPassword());
                    //model.addAttribute("provisionStatus", status.toString());
                    throw new RuntimeException("NO IMPL");
                    
                }            
            };
        } 
//		catch (NoSuchAlgorithmException e) {
//            logger.error("", e);
//            model.addAttribute("error", e);
//            return "error";
//        } catch (JSONException e) {
//            logger.error("", e);
//            model.addAttribute("error", e);
//            return "error";
//        } 
        catch (Exception e) {
            logger.error("", e);
            model.addAttribute("error", e);
            return "error";
        }
			
		
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
//		UserDetails details = 
//			(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//try {
			
			//UserPrincipal principal = userPrincipalService.loadUser(details.getUsername());
			//model.addAttribute("publishers", publisherService.findByUserPrincipal(principal));
			List<Publisher> all = publisherService.findAll();
			model.addAttribute("publishers", all);
			return "publisher/list";
			
//		} catch (UserPrincipalServiceException e) {
//			logger.error("", e);
//			model.addAttribute("error", e);
//			return "error";
//		} catch (UserNotFoundException e) {
//			logger.error("", e);
//			model.addAttribute("error", e);
//			return "error";
//		}
		
	}

    @RequestMapping("/search")
    public String searchByName(@RequestParam("siteName") String siteName, Model model) {
    	throw new RuntimeException("NO IMPL");
    }
    
    
}
