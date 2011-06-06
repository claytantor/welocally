package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
import com.sightlyinc.ratecred.admin.model.UserPrincipalForm;
import com.sightlyinc.ratecred.authentication.Role;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;

@Controller
@RequestMapping(value="/admin/user")
public class UserController {
	
	
	static Logger logger = Logger.getLogger(UserController.class);


	@Autowired
	private UserPrincipalService userPrincipalService;

	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
			UserPrincipalForm form = new UserPrincipalForm();					
			model.addAttribute("userPrincipalForm",form);
			
			String[] availableRoles = { "ROLE_PATRON", "ROLE_MEMBER",  "ROLE_USER", "ROLE_ADMIN" };
			model.addAttribute("availableRoles",availableRoles);
			
			return "user/edit";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid UserPrincipalForm form, BindingResult result, Model model) {
		logger.debug("got post action");
		UserPrincipal up = new UserPrincipal();
		
		try {
			if(form.getId() != null)
				up = userPrincipalService.findUserByPrimaryKey(form.getId());
			
			if(up!= null)
			{
				up.setEmail(form.getEmail());
				up.setUsername(form.getUsername());
				up.setPassword(form.getPassword());
				up.setExpired(false);
				up.setEnabled(true);
				up.setUserClass("MEMBER");
				up.setLocked(false);
				up.setAuthGuid(UUID.randomUUID().toString());
				Set<Role> newRoles = new HashSet<Role>();
				for (String role : form.getRoles()) {
					Role r = new Role();
					r.setRole(role);
					r.setUser(up);
					r.setRoleGroup(role);
					newRoles.add(r);
				}
				
				if(newRoles.size()>0)
					up.setRoles(newRoles);
				
				Long id = userPrincipalService.saveUserPrincipal(up);
				return "redirect:/admin/user/"+id.toString();
			} else {
				model.addAttribute("userPrincipal", up);
				return "user/edit";
			}
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (UserPrincipalServiceException e) {
			model.addAttribute("error", e);
			return "error";
		}

	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getUserPrincipalById(@PathVariable Long id, Model model) {
		logger.debug("view");
		try{ 
			model.addAttribute("userPrincipal", userPrincipalService.findUserByPrimaryKey(id));
			return "user/view";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		} 
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String editUserPrincipal(@PathVariable Long id, Model model) {
		logger.debug("edit");
		try{
			model.addAttribute(
					"userPrincipalForm",
					new UserPrincipalForm(userPrincipalService.findUserByPrimaryKey(id)));
			return "user/edit";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		} 
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deleteUserPrincipal(@PathVariable Long id, Model model) {
		logger.debug("delete");
		try{
			UserPrincipal p = userPrincipalService.findUserByPrimaryKey(id);
			userPrincipalService.deleteUserPrincipal(p);
			return "redirect:/admin/user/list";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (UserPrincipalServiceException e) {
			model.addAttribute("error", e);
			return "error";
		} 
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String getList(Model model) {
		logger.debug("list");
		try{
			model.addAttribute("userPrincipals", userPrincipalService.findAll());
			return "user/list";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		} 
	}

}
