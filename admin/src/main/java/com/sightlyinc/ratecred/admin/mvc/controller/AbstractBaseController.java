package com.sightlyinc.ratecred.admin.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sightlyinc.ratecred.admin.model.Configuration;

public class AbstractBaseController {
    
    @Autowired
    Configuration configuration;
    
    @ModelAttribute("config")
    public Configuration getForm() {
        return configuration;
    }
    
    

}
