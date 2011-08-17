package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.admin.model.wordpress.JsonModelProcessor;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.PublisherService;
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
    		@RequestHeader("welocally-baseurl") String baseurl)
    throws JSONException {
//        boolean success = false;

        JSONObject requestJSONObject = new JSONObject(requestJSON);

        //String publisherKey = requestJSONObject.getString("publisherKey");
//        String categoryId = requestJSONObject.getString("categoryId");

        if (publisherKey != null) {
            String[] keys = publisherKey.split("\\x2e");

            // look up the selected publisher
            Publisher publisher = publisherService.findByNetworkKeyAndPublisherKey(keys[0], keys[1]);

            jsonModelProcessor.saveEventAndPlaceFromPostJson(requestJSONObject, publisher);

        }

        // empty response
        return "";
    }

    public void setPublisherService(PublisherService publisherService) {
        this.publisherService = publisherService;
    }
}
