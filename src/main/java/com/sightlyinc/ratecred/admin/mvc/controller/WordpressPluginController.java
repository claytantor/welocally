package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.admin.model.wordpress.JsonModelProcessor;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.PublisherService;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/wpp")
public class WordpressPluginController {
	
	static Logger logger = Logger.getLogger(WordpressPluginController.class);


    @Autowired
    private PublisherService publisherService;
    @Autowired
    private JsonModelProcessor jsonModelProcessor;

    /**
     * Handles requests sent by the Wordpress plugin when users publish a post.
     * Sends blog, post, place and SimpleGeo feature info in a JSON object.
     *
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public String handlePublish(@RequestBody String requestJSON, 
    		@RequestHeader("publisher-key") String publisherKey, 
    		@RequestHeader("welocally-baseurl") String baseurl,
    		@RequestHeader("wp-action") String wpaction)
    throws JSONException {

    	logger.debug(requestJSON);
        JSONObject requestJSONObject = new JSONObject(requestJSON);

        if (publisherKey != null) {
            String[] keys = publisherKey.split("\\x2e");

            // look up the selected publisher
            Publisher publisher = publisherService.findByNetworkKeyAndPublisherKey(keys[0], keys[1]);
            if(!requestJSONObject.isNull("post")) {
            	JSONObject jsonPost = requestJSONObject.getJSONObject("post");
            	
            	//determine if this is an event or article post
            	if(jsonModelProcessor.isArticlePost(jsonPost)){
            		jsonModelProcessor.saveArticleAndPlaceFromPostJson(jsonPost, publisher, wpaction);
            	} else if(jsonModelProcessor.isEventPost(jsonPost)) {
            		jsonModelProcessor.saveEventAndPlaceFromPostJson(jsonPost, publisher, wpaction);
            	}
            	              
            }
        }
        
        return "";
    }

    public void setPublisherService(PublisherService publisherService) {
        this.publisherService = publisherService;
    }
}
