package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.IteratorTool;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sightlyinc.ratecred.admin.geocoding.YahooGeocoder;
import com.sightlyinc.ratecred.admin.model.wordpress.JsonModelProcessor;
import com.sightlyinc.ratecred.client.geo.GeoPlacesClient;
import com.sightlyinc.ratecred.model.Place;
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
    private YahooGeocoder yahooGeocoder;

    @Autowired
    private GeoPlacesClient geoPlacesClient;
    
    @Autowired
    private ObjectMapper jacksonMapper;

    @RequestMapping(value="/query", method = RequestMethod.GET)
	public String getPlacesByQuery(
			@RequestHeader("publisher-key") String publisherKey, 
    		@RequestHeader("welocally-baseurl") String baseurl,
    		@RequestParam("address") String address,
			@RequestParam(value="query", required=false) String query,
			@RequestParam(value="category",required=false) String category,
			@RequestParam(value="radius",required=false) Long radius,
			Model model)
	{
		logger.debug("getPlacesByQuery");
		if (publisherKey != null) {
            String[] keys = publisherKey.split("\\x2e");

            // look up the selected publisher
            Publisher publisher = 
            	publisherService.findByNetworkKeyAndPublisherKey(keys[0], keys[1]);
            if(publisher != null && publisher.getSubscriptionStatus().equals("SUBSCRIBER"))
            {
            	List<Place> places = 
    				geoPlacesClient.findPlacesByQuery(address, query, category, radius);
            	model.addAttribute("places", places);
            	return "places";
            } else {
            	Map<String,String> errorMap = new HashMap<String,String>();
            	if(publisher == null){
            		errorMap.put("errorCode:", "101");
            		errorMap.put("errorMessage:", "Could not find publisher with key:"+publisherKey);
            	} else {
               		errorMap.put("errorCode:", "102");
            		errorMap.put("errorMessage:", "Subscription status invalid for publisher with key:"+publisherKey);
            	}
            	
            	try {
					model.addAttribute("mapperResult", makeJsonString(jacksonMapper, errorMap));
					return "mapper-result";
				} catch (IOException e) {
					logger.error("cannot serialize message", e);
				}
            	
            }
		}
		
		
		return "error";
		
		
	}
	
	@RequestMapping(value="/querytest", method = RequestMethod.GET)
	public String getPlacesByQueryTest(
			@RequestParam("publisher-key") String publisherKey, 
			@RequestParam("welocally-baseurl") String baseurl,
    		@RequestParam("address") String address,
			@RequestParam(value="query", required=false) String query,
			@RequestParam(value="category",required=false) String category,
			@RequestParam(value="radius",required=false) Long radius,
			Model model)
	{
		logger.debug("getPlacesByQuery TEST");
		if (publisherKey != null) {
            String[] keys = publisherKey.split("\\x2e");

            // look up the selected publisher
            Publisher publisher = 
            	publisherService.findByNetworkKeyAndPublisherKey(keys[0], keys[1]);
            if(publisher != null && publisher.getSubscriptionStatus().equals("SUBSCRIBER"))
            {
            	List<Place> places = 
    				geoPlacesClient.findPlacesByQuery(address, query, category, radius);
            	model.addAttribute("places", places);
            	model.addAttribute("itool", new IteratorTool());
            	return "places";
            } else {
            	Map<String,String> errorMap = new HashMap<String,String>();
            	if(publisher == null){
            		errorMap.put("errorCode:", "101");
            		errorMap.put("errorMessage:", "Could not find publisher with key:"+publisherKey);
            	} else {
               		errorMap.put("errorCode:", "102");
            		errorMap.put("errorMessage:", "Subscription status invalid for publisher with key:"+publisherKey);
            	}
            	
            	try {
					model.addAttribute("mapperResult", makeJsonString(jacksonMapper, errorMap));
					return "mapper-result";
				} catch (IOException e) {
					logger.error("cannot serialize message", e);
				}
            	
            }
		}
		
		
		return "error";
		
		
	}
	
	private String makeJsonString(ObjectMapper jacksonMapper, Object serialize) throws IOException{
		StringWriter sw = new StringWriter();   // serialize
		MappingJsonFactory jsonFactory = new MappingJsonFactory();
		JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
		jacksonMapper.writeValue(jsonGenerator, serialize);
		sw.flush();
		sw.close();
		return sw.toString();
	}

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
