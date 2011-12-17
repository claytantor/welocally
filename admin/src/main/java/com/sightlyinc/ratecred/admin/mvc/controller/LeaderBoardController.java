package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.service.PatronManagerService;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.IteratorTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/leaders")
public class LeaderBoardController {

    private static final Logger logger = Logger.getLogger(LeaderBoardController.class);

    @Autowired
    private PatronManagerService patronManagerService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
	public String getLeaders(@RequestParam(value="size", required=false) Integer size, Model model) {
		try {

			if (size == null)
				size = 10;

			// get the leaders
			List<Patron> leaders = patronManagerService.findPatronsByScoreDesc(size);

			model.addAttribute("leaders", leaders);
			model.addAttribute("itool", new IteratorTool());

			return "leaders";
		} catch (com.noi.utility.spring.service.BLServiceException e) {
			logger.error("problem getting rater", e);
			model.addAttribute("error", e);
			return "error";
		}
	}

}
