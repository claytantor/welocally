package com.sightlyinc.ratecred.admin.mvc.controller;

import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.xml.JsonEncoder;
import com.sightlyinc.ratecred.admin.velocity.AwardTool;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.service.PatronManagerService;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.IteratorTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/patron/{id}")
public class PatronController {

    private static final Logger logger = Logger.getLogger(PatronController.class);

    @Autowired
    private PatronManagerService patronManagerService;

    @Value("${applicationProperties.ratecredMediaUrl}")
    private String awardRepoUrlPrefix;

    @RequestMapping(value = "/awards")
    public ModelAndView getAwards(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            Patron patron = patronManagerService.findPatronByPrimaryKey(id);

            Set<Award> awards = patron.getAwards();

            modelAndView.setViewName("patron-awards");
            modelAndView.addObject("awardList", awards);
            modelAndView.addObject("total", awards.size());
            modelAndView.addObject("itool", new IteratorTool());
            modelAndView.addObject("awardTool", new AwardTool(awardRepoUrlPrefix));
            modelAndView.addObject("encoder", JsonEncoder.getInstance());

        } catch (BLServiceException e) {
            logger.error("problem getting rater", e);
            modelAndView.setViewName("error");
            modelAndView.addObject("error", e);
        }
        return modelAndView;
    }

}
