package com.sightlyinc.ratecred.admin.mvc.controller;

import javax.validation.Valid;

import com.sun.tools.internal.ws.resources.ModelerMessages;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.Merchant;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.service.MerchantService;
import com.sightlyinc.ratecred.service.NetworkMemberService;

@Controller
@RequestMapping("/association/merchant")
public class MerchantController {

    static Logger logger = Logger.getLogger(MerchantController.class);


    @Autowired
    private MerchantService merchantService;
    
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

	
    @RequestMapping(method = RequestMethod.GET)
    public String addMerchant(@ModelAttribute("member") NetworkMember member, Model model) {
        //set the member
        //prepopulate member
        Merchant merchant = new Merchant();
        merchant.setNetworkMember(member);

        model.addAttribute("merchantForm",merchant);

        return "merchant/edit";
	        
    }

    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editMerchant(@PathVariable Long id, Model model) {
        logger.debug("edit");
        Merchant a = merchantService.findByPrimaryKey(id);
        //model.addAttribute("publisher", a.getPublisher());
        model.addAttribute("member", a.getNetworkMember());
        model.addAttribute("merchantForm", a);
        return "merchant/edit";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String saveMerchant(@Valid Merchant merchant) {

        Long id = merchantService.save(merchant);
        return "redirect:/association/merchant/"+id.toString();

    }
	
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public String getMerchantById(@PathVariable Long id, Model model) {
        logger.debug("view");
        Merchant a = merchantService.findByPrimaryKey(id);
        //model.addAttribute("publisher", a.getPublisher());
        model.addAttribute("member", a.getNetworkMember());
        model.addAttribute("merchant", a);
        return "merchant/view";
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
    public String deleteMerchant(@PathVariable Long id) {
        logger.debug("delete");
        Merchant merchant = merchantService.findByPrimaryKey(id);
        merchantService.delete(merchant);
        return "redirect:/association/merchant/list";
    }
	
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(@RequestParam Long publisherId, Model model) {
        logger.debug("list");

        try {	
	    	//set the member
			//prepopulate member
			UserDetails details = 
			(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
						
			UserPrincipal principal = userPrincipalService.loadUser(details.getUsername());
			NetworkMember member = networkMemberService.findMemberByUserPrincipal(principal);
			model.addAttribute("merchants", member.getMerchants());	        
	        return "merchant/list";
	        
    	} catch (UserPrincipalServiceException e) {
			logger.error("", e);
			return "error";
		} catch (UserNotFoundException e) {
			logger.error("", e);
			return "error";
		}
        
        
        
    }
}
