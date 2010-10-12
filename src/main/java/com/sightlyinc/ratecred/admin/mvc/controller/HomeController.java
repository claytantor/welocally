package com.sightlyinc.ratecred.admin.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/admin/home")
public class HomeController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		return "admin_home";
	}

}
