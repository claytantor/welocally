package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.authentication.Role;
import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.PublisherService;

@Controller
@RequestMapping(value="/admin/member")
public class NetworkMemberController {
	
	
	static Logger logger = Logger.getLogger(NetworkMemberController.class);
	
	@Autowired
	private NetworkMemberService networkMemberService;
	
	@Autowired
	private UserPrincipalService userPrincipalService;
	
	@Autowired
	private PublisherService publisherService;

	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("networkMemberForm",new NetworkMember());
		
		//member types
		String[] memberTypes = { "MERCHANT", "AFFILIATE", "PUBLISHER" };
		model.addAttribute("memberTypes",memberTypes);
		
		try {
			List<UserPrincipal> principals = userPrincipalService.findAll();
			StringBuffer buf = new StringBuffer();
			for (UserPrincipal userPrincipal : principals) {
				buf.append(userPrincipal.getName()+",");
			}
			model.addAttribute("users", buf.toString());
		} catch (BLServiceException e) {
			logger.error("problem", e);
			model.addAttribute("error", e);
			return "error";
		}
		
		return "member/edit";
	}
	
	
	// @TODO should be refactored to not use form object 
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid NetworkMember form, BindingResult result, Model model) {
		logger.debug("got post action");
		if(result.hasErrors()) {
			model.addAttribute("networkMemberForm",form);
			return "member/edit";
		}
		Long id = networkMemberService.save(form);
		return "redirect:/admin/member/"+id.toString();
		

	}
	
	private Boolean principalHasRole(UserPrincipal up, String roleName)
	{
		for (Role r : up.getRoles()) {
			if(r.getRole().equals(roleName))
				return true;
		}
		return false;
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getNetworkMemberById(@PathVariable Long id, Model model) {
		logger.debug("view");
		model.addAttribute("member", networkMemberService.findByPrimaryKey(id));
		return "member/view";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String editNetworkMember(@PathVariable Long id, Model model) {
		logger.debug("edit");
		model.addAttribute(
				"networkMemberForm",
				networkMemberService.findByPrimaryKey(id));
		return "member/edit";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deleteNetworkMember(@PathVariable Long id, Model model) {
		logger.debug("delete");
		NetworkMember p = networkMemberService.findByPrimaryKey(id);
		networkMemberService.delete(p);
		return "redirect:/admin/member/list";
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String getList(Model model) {
		logger.debug("list");
		model.addAttribute("members", networkMemberService.findAll());
		return "member/list";
	}

}
