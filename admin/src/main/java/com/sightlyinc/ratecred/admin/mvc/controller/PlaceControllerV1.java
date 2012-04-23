package com.sightlyinc.ratecred.admin.mvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/place/1_0")
public class PlaceControllerV1 {

	static Logger logger = Logger.getLogger(PlaceControllerV1.class);

	@RequestMapping(value = "/iplace", method = RequestMethod.GET)
    public String place(@RequestParam ("id") String id, Model model,
            HttpServletRequest request) {
	    logger.debug("id:"+id);
	    model.addAttribute("id", id);
        return "place/iplace";
    }
	
	@RequestMapping(value = "/finder", method = RequestMethod.GET)
    public String finder(Model model,HttpServletRequest request) {
	    
        return "place/finder";
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model,HttpServletRequest request) {       
        return "place/edit";
    }

}
