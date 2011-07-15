package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.xml.JsonEncoder;
import com.sightlyinc.ratecred.admin.model.Feature;
import com.sightlyinc.ratecred.client.geo.SimpleGeoPlaceManager;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.service.PlaceManagerService;


@Controller
@RequestMapping("/publisher/place")
public class PlaceController {

	static Logger logger = Logger.getLogger(PlaceController.class);

    
	@Autowired
	private PlaceManagerService placeManagerService;
	
    @Autowired
    @Qualifier("jacksonMapper")
    private ObjectMapper jacksonMapper;
    
    @ModelAttribute("encoder")
    public JsonEncoder getJsonEncoder() {
		return JsonEncoder.getInstance();
    }
	
		
	@RequestMapping(method= RequestMethod.GET)
	public String addPlace(Model model) {
		model.addAttribute("placeForm",new Place());
		return "place/edit";
	}	
	
    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editReview(@PathVariable Long id, Model model) {
        logger.debug("edit");
        model.addAttribute("placeForm", placeManagerService.findPlaceByPrimaryKey(id));
        return "place/edit";
    }

    /**
     * ok we are having to do some backflips because it looks like 
     * the jackson deserializer is breaking for some reason, if you can get 
     * this working with a simpler implementation then by all means
     * 
     * @param f
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value="/feature", method=RequestMethod.POST)
    public String createFromFeature(@RequestBody Feature f, Model model, HttpServletResponse response) {
    	logger.debug("got post action");
   	
    	try {
			//try to find the place
			Place p = placeManagerService.findBySimpleGeoId(f.getId());
			if(p==null)
				p= new Place();
			transformFeature(f, p);
			placeManagerService.savePlace(p);

			model.addAttribute("place", p);
	    	model.addAttribute("status", "SUCCESS");
	    	response.setStatus(200);			
		} catch (BLServiceException e) {
			logger.error("problem saving place", e);
			response.setStatus(500);	
		}
    	
    	return "save-place";
    }
    
    
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid Place placeForm, BindingResult result) {
		logger.debug("got post action");
		try {			
			placeManagerService.savePlace(placeForm);
			return "redirect:/publisher/place/" + placeForm.getId();

		} catch (BLServiceException e) {
			logger.error("problem saving place", e);
			return "error";
		}

	}
	
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public String getPlaceById(@PathVariable Long id, Model model) {
        logger.debug("view");
        model.addAttribute("place", placeManagerService.findPlaceByPrimaryKey(id));
        return "place/view";
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
    public String deletePlace(@PathVariable Long id) {
        logger.debug("delete");
        Place place = placeManagerService.findPlaceByPrimaryKey(id);
        try {
            placeManagerService.deletePlace(place);
        } catch (BLServiceException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/publisher/place/list";
    }

    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(Model model) {
        logger.debug("list");
        try {
            model.addAttribute("places", placeManagerService.findAllPlaces());
        } catch (BLServiceException e) {
            throw new RuntimeException(e);
        }
        return "place/list";
    }

    @RequestMapping("/search")
    public String searchByName(@RequestParam("name") String name, Model model) {
        logger.debug("search by name");
        List<Place> places;
        try {
            places = placeManagerService.findPlacesByNamePrefix(name);
        } catch (BLServiceException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("places", places);
        return "place/list_json";
    }
 
/**
 * so the reason we do this instead of using the service is that
 * we have serialized this to a sightly type, it isnt the simple geo
 * type so we need a different transform, this could be cleaned up later
 *     
 * @param f
 * @param p
 */
	public static void transformFeature(Feature f, Place p ){
		
		//Place p = new Place();
		p.setSimpleGeoId(f.getId());
		
		p.setName(f.getProperties().get("name").toString());
		p.setAddress(f.getProperties().get("address").toString());
		p.setLatitude(f.getGeometry().getCoordinates()[1]);
		p.setLongitude(f.getGeometry().getCoordinates()[0]);
		if(f.getProperties().get("city") != null)
			p.setCity(f.getProperties().get("city").toString());
		if(f.getProperties().get("postalcode") != null)
			p.setZip(f.getProperties().get("postalcode").toString());
		if(f.getProperties().get("province") != null)
			p.setState(f.getProperties().get("province").toString());
		if(f.getProperties().get("phone") != null)
			p.setPhone(f.getProperties().get("phone").toString());
		if(f.getProperties().get("url") != null && f.getProperties().get("website").toString().startsWith("http"))
			p.setUrl(f.getProperties().get("website").toString().toLowerCase());
		else if(f.getProperties().get("website") != null)
			p.setUrl("http://"+f.getProperties().get("website").toString().toLowerCase());
		else if(f.getProperties().get("menulink") != null)
			p.setUrl(f.getProperties().get("menulink").toString().toLowerCase());
		
		
	}
    
    
}
