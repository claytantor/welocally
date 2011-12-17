package com.sightlyinc.ratecred.admin.mvc.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sightlyinc.ratecred.authentication.UserPrincipalService;

/**
 * special activities restricted to an admin
 * 
 * @author claygraham
 *
 */
@Controller
@RequestMapping(value="/admin/activity")
public class AdminActivityController {
	
	
	static Logger logger = Logger.getLogger(AdminActivityController.class);


	@Autowired
	private UserPrincipalService userPrincipalService;

	
//	@RequestMapping(method=RequestMethod.GET)
//	public String getCreateForm(Model model) {
//			UserPrincipalForm form = new UserPrincipalForm();					
//			model.addAttribute("userPrincipalForm",form);
//			
//			String[] availableRoles = { "ROLE_PATRON", "ROLE_MEMBER",  "ROLE_USER", "ROLE_ADMIN" };
//			model.addAttribute("availableRoles",availableRoles);
//			
//			return "user/edit";
//	}
//	
//	@RequestMapping(method=RequestMethod.POST)
//	public String create(@Valid UserPrincipalForm form, BindingResult result, Model model) {
//		logger.debug("got post action");
//		UserPrincipal up = new UserPrincipal();
//		
//		try {
//			if(form.getId() != null)
//				up = userPrincipalService.findUserByPrimaryKey(form.getId());
//			
//			if(up!= null)
//			{
//				up.setEmail(form.getEmail());
//				up.setUsername(form.getUsername());
//				up.setPassword(form.getPassword());
//				up.setExpired(false);
//				up.setEnabled(true);
//				up.setUserClass("MEMBER");
//				up.setLocked(false);
//				up.setAuthGuid(UUID.randomUUID().toString());
//				Set<Role> newRoles = new HashSet<Role>();
//				for (String role : form.getRoles()) {
//					Role r = new Role();
//					r.setRole(role);
//					r.setUser(up);
//					r.setRoleGroup(role);
//					newRoles.add(r);
//				}
//				
//				if(newRoles.size()>0)
//					up.setRoles(newRoles);
//				
//				Long id = userPrincipalService.saveUserPrincipal(up);
//				return "redirect:/admin/user/"+id.toString();
//			} else {
//				model.addAttribute("userPrincipal", up);
//				return "user/edit";
//			}
//		} catch (BLServiceException e) {
//			model.addAttribute("error", e);
//			return "error";
//		} catch (UserPrincipalServiceException e) {
//			model.addAttribute("error", e);
//			return "error";
//		}
//
//	}
//	
//	@RequestMapping(value="{id}", method=RequestMethod.GET)
//	public String getUserPrincipalById(@PathVariable Long id, Model model) {
//		logger.debug("view");
//		try{ 
//			model.addAttribute("userPrincipal", userPrincipalService.findUserByPrimaryKey(id));
//			return "user/view";
//		} catch (BLServiceException e) {
//			model.addAttribute("error", e);
//			return "error";
//		} 
//	}
//	
//	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
//	public String editUserPrincipal(@PathVariable Long id, Model model) {
//		logger.debug("edit");
//		try{
//			model.addAttribute(
//					"userPrincipalForm",
//					new UserPrincipalForm(userPrincipalService.findUserByPrimaryKey(id)));
//			return "user/edit";
//		} catch (BLServiceException e) {
//			model.addAttribute("error", e);
//			return "error";
//		} 
//	}
//	
//	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
//	public String deleteUserPrincipal(@PathVariable Long id, Model model) {
//		logger.debug("delete");
//		try{
//			UserPrincipal p = userPrincipalService.findUserByPrimaryKey(id);
//			userPrincipalService.deleteUserPrincipal(p);
//			return "redirect:/admin/user/list";
//		} catch (BLServiceException e) {
//			model.addAttribute("error", e);
//			return "error";
//		} catch (UserPrincipalServiceException e) {
//			model.addAttribute("error", e);
//			return "error";
//		} 
//	}
//	
//	@RequestMapping(value="/list", method=RequestMethod.GET)
//	public String getList(Model model) {
//		logger.debug("list");
//		try{
//			model.addAttribute("userPrincipals", userPrincipalService.findAll());
//			return "user/list";
//		} catch (BLServiceException e) {
//			model.addAttribute("error", e);
//			return "error";
//		} 
//	}
//	
//	@RequestMapping("/search")
//    public String searchByName(@RequestParam("userName") String userName, Model model) {
//        logger.debug("search by userName");
//        List<UserPrincipal> users = userPrincipalService.findByUserNameLike(userName);
//        model.addAttribute("users", users);
//        return "user/list_json";
//    }

}
