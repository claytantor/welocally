package com.sightlyinc.ratecred.admin.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/logoutfoo")
public class LogoutController {
	
	
	static Logger logger = Logger.getLogger(LogoutController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String logout(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response)
	{
			request.getSession().invalidate();
			return "redirect:/index.html";		
	}
	
	
	

}
