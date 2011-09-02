package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sightlyinc.ratecred.admin.model.wordpress.JsonModelProcessor;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import com.sightlyinc.ratecred.service.PublisherService;
import com.simplegeo.client.types.Feature;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/wpp")
public class WordpressPluginController {
	
	static Logger logger = Logger.getLogger(WordpressPluginController.class);


	@Autowired
	private PlaceManagerService placeManagerService;
	
    @Autowired
    private PublisherService publisherService;
    
    @Autowired
    private JsonModelProcessor jsonModelProcessor;
    
    @Autowired
    private ObjectMapper jacksonMapper;



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
    
    @RequestMapping(value="/add",  method = RequestMethod.POST)
	public ModelAndView chooserAddPlace(
			@RequestBody String addPlaceJSON,
			Model m,
			HttpServletResponse response) 
    {
		logger.debug("adding place from place chooser");
        ModelAndView modelAndView = new ModelAndView("add-feature");
        
        try {
			JSONObject requestJSONObject = new JSONObject(addPlaceJSON);
			Feature f = jsonModelProcessor.saveNewPlaceAsFeatureFromPostJson(requestJSONObject);
			StringWriter sw = new StringWriter();   // serialize
			MappingJsonFactory jsonFactory = new MappingJsonFactory();
			JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
			jacksonMapper.writeValue(jsonGenerator, f);
			sw.flush();
			sw.close();
			modelAndView.addObject("feature", sw.getBuffer().toString());

		} catch (JSONException e) {
			response.setStatus(500);
		} catch (IOException e) {
			response.setStatus(500);
		}
      
        return modelAndView;
	}

    public void setPlaceManagerService(PlaceManagerService placeManagerService) {
		this.placeManagerService = placeManagerService;
	}

	public void setJsonModelProcessor(JsonModelProcessor jsonModelProcessor) {
		this.jsonModelProcessor = jsonModelProcessor;
	}

	public void setPublisherService(PublisherService publisherService) {
        this.publisherService = publisherService;
    }
}
