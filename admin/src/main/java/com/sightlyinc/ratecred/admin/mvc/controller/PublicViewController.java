package com.sightlyinc.ratecred.admin.mvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import twitter4j.TwitterException;

@Controller
@RequestMapping(value = "/view")
public class PublicViewController {

	static Logger logger = Logger.getLogger(PublicViewController.class);

	@RequestMapping(value = "/{view}", method = RequestMethod.GET)
	public String getView(@PathVariable("view") String view, Model model,
			HttpServletRequest request) {
		
		return view;
	}

}
