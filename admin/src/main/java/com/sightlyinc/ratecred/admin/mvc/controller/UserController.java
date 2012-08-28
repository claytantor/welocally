package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	static Logger logger = Logger.getLogger(UserController.class);
	
	@PostConstruct
	public void init(){
	    logger.debug("foo");
	    logger.setLevel(Level.DEBUG);
	    Enumeration<org.apache.log4j.Logger> loggers = LogManager.getCurrentLoggers();
	    while (loggers.hasMoreElements()) {
	        Logger l = loggers.nextElement();
	        l.setLevel(Level.DEBUG);
	        logger.debug(l.getName());            
        }
	}
  
    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String home(Model model) {
        
        logger.debug("home");
        
        //see if the user is a member
        UserDetails user = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        model.addAttribute("user", user);    
        
        return "home";
    }


    /** Returns the user ID for the given HTTP servlet request. */
    protected  String getUserId(HttpServletRequest req) throws ServletException, IOException{
        UserDetails user = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

}
