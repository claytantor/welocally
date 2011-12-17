package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.PublisherService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/admin/publisher")
public class AdminPublisherController {

    private static Logger LOGGER = Logger.getLogger(AdminPublisherController.class);

    private PublisherService publisherService;

    @RequestMapping("/expiring")
    public ModelAndView publisherExpirationReport() {
        ModelAndView modelAndView = new ModelAndView("publisher/expiring_publishers");

        List<Publisher> expiringPublishers = publisherService.findExpiringPublishers();

        modelAndView.addObject("publishers", expiringPublishers);

        // name, url, service end date
        // publisher key
        // simplegeo json token
        // sort by date
        // make already expired red ... ?

        return modelAndView;
    }

    @Autowired
    public void setPublisherService(PublisherService publisherService) {
        this.publisherService = publisherService;
    }
}
