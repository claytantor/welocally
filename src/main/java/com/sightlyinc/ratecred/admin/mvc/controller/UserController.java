package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.IteratorTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.UserPrincipalForm;
import com.sightlyinc.ratecred.authentication.Role;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;

@Controller
@RequestMapping(value = "/admin/user")
public class UserController {

	static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserPrincipalService userPrincipalService;

	@RequestMapping(method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		UserPrincipalForm form = new UserPrincipalForm();
		model.addAttribute("userPrincipalForm", form);
		String[] availableRoles = { "ROLE_PATRON", "ROLE_MEMBER",
				"ROLE_PUBLISHER", "ROLE_MERCHANT", "ROLE_AFFILIATE",
				"ROLE_USER", "ROLE_ADMIN" };
		model.addAttribute("availableRoles", availableRoles);
		return "user/edit";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid UserPrincipalForm form, BindingResult result,
			Model model) {
		logger.debug("got post action");

		try {
			UserPrincipal up = form.getEntity();
			if(form.getId() != null)
				up = userPrincipalService.findUserByPrimaryKey(form.getId());
			
			
			//PUT THIS IN BL
			//dont do anything if the user did not set roles
			if(form.getRoleNames() != null) {
				userPrincipalService.saveUserPrincipalRoles(up, form.getRoleNames());
			} 		
			
			//make sure user is enabled
			up.setEnabled(form.getEnabled());
			//why do I have to do this!
			up.setCredentialsExpired(form.getCredentialsExpired());
			up.setLocked(form.getLocked());
			up.setPassword(form.getPassword());
			
			Long id = userPrincipalService.saveUserPrincipal(up);

			return "redirect:/admin/user/" + id.toString();
		} catch (UserPrincipalServiceException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		}

	}

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String getUserPrincipalById(@PathVariable Long id, Model model) {
		logger.debug("view");
		try {
			model.addAttribute("userPrincipal", new UserPrincipalForm(
					userPrincipalService.findUserByPrimaryKey(id)));
			return "user/view";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		}
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editUserPrincipal(@PathVariable Long id, Model model) {
		logger.debug("edit");
		try {
			model.addAttribute("userPrincipalForm", new UserPrincipalForm(
					userPrincipalService.findUserByPrimaryKey(id)));

			String[] availableRoles = { "ROLE_PATRON", "ROLE_MEMBER",
					"ROLE_PUBLISHER", "ROLE_MERCHANT", "ROLE_AFFILIATE",
					"ROLE_USER", "ROLE_ADMIN" };
			model.addAttribute("availableRoles", availableRoles);

			return "user/edit";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteUserPrincipal(@PathVariable Long id, Model model) {
		logger.debug("delete");
		try {
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getList(Model model) {
		logger.debug("list");
		try {
			model.addAttribute(
					"userPrincipals", 
					userPrincipalService.findAll());
			return "user/list";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		}
	}

	@RequestMapping("/search")
	public String searchByName(@RequestParam("userName") String userName,
			Model model) {
		logger.debug("search by userName");
		List<UserPrincipal> users =  
			userPrincipalService.findByUserNameLike(userName);
		model.addAttribute("itool", new IteratorTool());
		model.addAttribute("userPrincipals", users);
		return "user-list";
	}

}
