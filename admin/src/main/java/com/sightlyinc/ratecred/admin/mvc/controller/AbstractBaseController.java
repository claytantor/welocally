package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.UUID;

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
    
    /*https://www.facebook.com/dialog/oauth?
    client_id=YOUR_APP_ID
    &redirect_uri=YOUR_REDIRECT_URI
    &scope=COMMA_SEPARATED_LIST_OF_PERMISSION_NAMES
    &state=SOME_ARBITRARY_BUT_UNIQUE_STRING*/
    @ModelAttribute("facebookRedirect")
    public String getFacebookRedirect() {
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        String redirect = "https://www.facebook.com/dialog/oauth?" +
        		"client_id=YOUR_APP_ID&" +
        		"redirect_uri=YOUR_REDIRECT_URI&" +
        		"scope=ROLE_USER,ROLE_PUBLISHER&" +
        		"state=SOME_ARBITRARY_BUT_UNIQUE_STRING"
        		.replace("212416905466586","")
                .replace("YOUR_REDIRECT_URI","http://gaudi.welocally.com/facebook/1_0/auth")
                .replace("SOME_ARBITRARY_BUT_UNIQUE_STRING",key.substring(0, 8));
        
        return redirect;
    }
    
    

}
