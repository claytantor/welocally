package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.noi.utility.date.DateUtils;
import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.wordpress.JsonModelProcessor;
import com.sightlyinc.ratecred.admin.util.GoogleSpreadsheetUtils;
import com.sightlyinc.ratecred.client.geo.GeoEventClient;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.EventService;
import com.sightlyinc.ratecred.service.PublisherService;
import com.simplegeo.client.SimpleGeoPlacesClient;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

@Controller
@RequestMapping("publisher/event")
public class EventController {

	static Logger logger = Logger.getLogger(EventController.class);
	
    
	public static String sgoauthkey = "ZdGSMVGmne9ccTn6dykyGffHU8AXCAaC";
	
	public static String sgoauthsecret = "kmbYEGBVbhA6473Y2ms3SwMS5SYYuWux";

	@Autowired
	private EventService eventService;

	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	JsonModelProcessor jsonModelProcessor; 
	
	
	@Autowired
	@Qualifier("geoEventClient")
	private GeoEventClient geoEventClient;
	
	

	@RequestMapping(method = RequestMethod.GET)
	public String addEvent(@RequestParam Long publisherId, Model model) {
		Publisher publisher = publisherService.findByPrimaryKey(publisherId);
		model.addAttribute("publisher", publisher);
		Event e = new Event();
		e.setPublisher(publisher);
		model.addAttribute("eventForm", e);
		return "event/edit";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editEvent(@PathVariable Long id, Model model) {
		logger.debug("edit");
		Event e = eventService.findByPrimaryKey(id);
		model.addAttribute("publisher", e.getPublisher());
		model.addAttribute("eventForm", e);
		return "event/edit";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String saveEvent(@Valid Event event) {
		logger.debug("got post action");
		Long id = eventService.save(event);
		return "redirect:/publisher/event/" + id.toString();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getEventById(@PathVariable Long id, Model model) {
		logger.debug("view");
		Event e = eventService.findByPrimaryKey(id);
		model.addAttribute("publisher", e.getPublisher());
		model.addAttribute("event", e);
		return "event/view";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteEvent(@PathVariable Long id) {
		logger.debug("delete");
		Event event = eventService.findByPrimaryKey(id);
		Long publisherId = event.getPublisher().getId();
		eventService.delete(event);
		return "redirect:/publisher/event/list?publisherId="+publisherId;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam Long publisherId, Model model) {
		logger.debug("list");
		Publisher publisher = publisherService.findByPrimaryKey(publisherId);
		model.addAttribute("publisher", publisher);
		model.addAttribute("events", publisher.getEvents());
		return "event/list";
	}

	@RequestMapping(value = "/load/google/{key}", method = RequestMethod.GET)
	public String loadEventsFromGoogle(@PathVariable String key, @RequestParam Long publisherId, Model model) {
		logger.debug("load");
		String spreadsheetUrl = "https://spreadsheets.google.com/tq?tqx=out:csv&key="
				+ key + "&hl=en";
		try {
			List<String[]> rows = GoogleSpreadsheetUtils
					.getSpreadsheetData(spreadsheetUrl);
			Publisher p = publisherService.findByPrimaryKey(publisherId);
			findAndSaveEventsAsTable(rows);
			return "redirect:/publisher/event/list?publisherId="+publisherId;
		} catch (IOException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (JSONException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (GeoPersistenceException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (NoSuchAlgorithmException e) {
			model.addAttribute("error", e);
			return "error";
		}

	}
	
	//?json=get_category_posts&get_post_meta&custom_fields=_EventVenue,_EventStartDate,_EventEndDate,_EventAddress,_EventCity,_EventState,_EventZip,_EventCost,_EventPhone&category_id=41
	@RequestMapping(value = "/load/wordpress", method = RequestMethod.GET)
	public String loadEventsFromWordpress(
			@RequestParam String baseUrl,
			@RequestParam Integer categoryId, 
			@RequestParam String publisherKey, 
			Model model) {
		logger.debug("load");
		Publisher publisher = null;
		if (publisherKey != null) {
        	String[] keys = publisherKey.split("\\x2e");
        	
            // look up the selected publisher
        	publisher = publisherService.findByNetworkKeyAndPublisherKey(
        		keys[0], keys[1]);
        	
        	SimpleHttpClient client = new SimpleHttpClient();
        	Map<String,String> params = new HashMap<String,String>();
        	params.put("json", "get_category_posts");
        	params.put("get_post_meta", "");
        	params.put("custom_fields", "_EventVenue,_EventStartDate,_EventEndDate,_EventAddress,_EventCity,_EventState,_EventZip,_EventCost,_EventPhone");
        	params.put("category_id", categoryId.toString());
        	
        	//so lame!
        	for (int j = 0; j < 7; j++) {
        		
            	
            	params.put("page", ""+j);
            	
            	
            	
            	try {
    				ClientResponse response = 
    					client.get("http://"+baseUrl, params, null);
    				
    				JSONObject postsResponse = new JSONObject(new String(response.getResponse()));
    				
    				jsonModelProcessor.saveJsonEventsAsPostsForPublisher(postsResponse, publisher);
    				
    				
    			} catch (Exception e) {
    				logger.error("problem get events", e);
    			}
			}
        	
        	return "redirect:/publisher/event/list?publisherId="+publisher.getId();
         }

		return "error";

	}

	protected void findAndSaveEventsAsTable(List<String[]> lines) throws IOException, JSONException,
			GeoPersistenceException, BLServiceException, NoSuchAlgorithmException {

		double lat = 37.811373;
		double lon = -122.265698;

		double radiusInKMeters = 10.00;

		SimpleGeoPlacesClient client = SimpleGeoPlacesClient.getInstance();
		client.getHttpClient().setToken(sgoauthkey, sgoauthsecret);

		/*
		 * readLine is a bit quirky : it returns the content of a line MINUS the
		 * newline. it returns null only for the END of the stream. it returns
		 * an empty String if two newlines appear in a row.
		 */

		for (String[] fields : lines) {
			////Timestamp0,publisherKey1,eventName2,hidden3,url4,phone5,
			//placeName6,description7,address8,city9,
			//state10,starts_date11,ends_date12,length13,status14
			
			String publisherKey = fields[1].trim();	
			String[] keys = publisherKey.split("\\.");

            // look up the selected publisher
			Publisher p = null;
			try {
				p = publisherService.findByNetworkKeyAndPublisherKey(
						keys[0], keys[1]);
			} catch (Exception e) {
				logger.error("problem with keys", e);
			}
			
			String name = fields[2].trim();	
			String status = fields[14].trim();	
			String url= fields[4].trim();	
			String phone = fields[5].trim();	
			String placeName = fields[6].trim();	
			String description = fields[7].trim();	
			String address = fields[8].trim();	
			String city = fields[9].trim();	
			String state = fields[10].trim();	
			Date ts = DateUtils.stringToDate(fields[11].trim(), "MM/dd/yyyy HH:mm:ss");
			Date te = DateUtils.stringToDate(fields[12].trim(), "MM/dd/yyyy HH:mm:ss");
			
			String idVal = url.substring(url.lastIndexOf("=")+1);

			logger.debug("trying to load place:" + placeName);

			if (status.equalsIgnoreCase("ACTIVE") && p != null) {

				FeatureCollection collection = client.search(lat, lon,
						placeName.trim(), "", radiusInKMeters);

				ArrayList<Feature> features = collection.getFeatures();
				// if (features.size() == 1) {
				for (Feature feature : features) {

					String fname = (String) feature.getProperties().get("name");
					logger.debug("found:" + fname);

					// this is lame, should probably use distance or something
					// better
					if (fname.equals(placeName)
							&& feature.getProperties().get("city").toString()
									.contains(city)) {
						
						Event e = geoEventClient.makeEventFromFeature(
								 feature, 
								 name,
								 status,
								 url,
								 phone,
								 placeName,	
								 description,
								 address,	
								 city,
								 state,	
								 ts.getTime(),	
								 te.getTime(),
								 p);
						
						eventService.save(e);
					}

				}

			} 

		}

	}


}
