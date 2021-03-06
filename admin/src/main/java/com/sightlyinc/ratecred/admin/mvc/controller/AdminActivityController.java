package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.GeodbProvisionManager;
import com.sightlyinc.ratecred.service.PublisherService;
import com.welocally.admin.security.UserPrincipal;

/**
 * special activities restricted to an admin
 * 
 * @author claygraham
 *
 */
@Controller
@RequestMapping(value="/administrator")
public class AdminActivityController {
		
	static Logger logger = Logger.getLogger(AdminActivityController.class);

//
//	@Autowired
//	private UserPrincipalService userPrincipalService;
	
	@Autowired
    private PublisherService publisherService;
		
	@Autowired GeodbProvisionManager geodbProvisionManager;
    	
    @ModelAttribute("authenticatedUser")
    public UserDetails getAdminUser() {
        UserDetails details = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return details;
    }
    
    
	
	@RequestMapping(value="/publisher/provision/{id}", method=RequestMethod.GET)
	public String provisionUser(@PathVariable Long id, Model model) {
	    	    
	    try {
	        Publisher p = publisherService.findByPrimaryKey(id);
            UserPrincipal up = null;
 
            //only allow admins to provision
            UserDetails admin = getAdminUser();
            for (int i = 0; i < admin.getAuthorities().size(); i++) {
                GrantedAuthority auth = new ArrayList<GrantedAuthority>(admin.getAuthorities()).get(i);
                if(auth.getAuthority().equals("ROLE_ADMIN")){
                    geodbProvisionManager.provision(up, admin.getUsername(), admin.getPassword());
                }
            }
            
            

            
            
        } catch (JSONException e) {
            logger.error("problem provision in user", e);
            model.addAttribute("error", e);
            return "error";
        } catch (IOException e) {
            logger.error("problem provision in user", e);
            model.addAttribute("error", e);
            return "error";
        } catch (NoSuchAlgorithmException e) {
            logger.error("problem provision in user", e);
            model.addAttribute("error", e);
            return "error";
        }
	    throw new RuntimeException("NO IMPL");
	    //return "redirect:/publisher/"+id;
    }

	
	
	
	

}
