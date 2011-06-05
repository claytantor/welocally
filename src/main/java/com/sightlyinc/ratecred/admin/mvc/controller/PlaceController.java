package com.sightlyinc.ratecred.admin.mvc.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.service.PlaceManagerService;

@Controller
@RequestMapping("admin/place")
public class PlaceController {

	static Logger logger = Logger.getLogger(PlaceController.class);

    
	@Autowired
	private PlaceManagerService placeManagerService;
	
		
	@RequestMapping(method= RequestMethod.GET)
	public String addPlace(Model model) {
		model.addAttribute("placeForm",new Place());
		return "place/edit";
	}
	
    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editReview(@PathVariable Long id, Model model) {
        logger.debug("edit");
        try {
            model.addAttribute("placeForm", placeManagerService.findPlaceByPrimaryKey(id));
        } catch (BLServiceException e) {
            throw new RuntimeException(e);
        }
        return "place/edit";
    }

	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid Place placeForm, BindingResult result) {
		logger.debug("got post action");
		try {
			
//			Place p =
//				placeManagerService.findPlaceByPrimaryKey(placeForm.getPlaceId());
//			p.setEmail(placeForm.getEmail());
			//p.setBusinessServices(placeForm.getBusinessServices());
//			placeManagerService.savePlace(p);
			placeManagerService.savePlace(placeForm);
//			return "redirect:/admin/place/" + p.getId();
			return "redirect:/admin/place/" + placeForm.getId();

		} catch (BLServiceException e) {
			logger.error("problem saving place", e);
			return "error";
		}

	}
	
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public String getPlaceById(@PathVariable Long id, Model model) {
        logger.debug("view");
        try {
            model.addAttribute("place", placeManagerService.findPlaceByPrimaryKey(id));
        } catch (BLServiceException e) {
            throw new RuntimeException(e);
        }
        return "place/view";
    }

    @RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
    public String deletePlace(@PathVariable Long id) {
        logger.debug("delete");
        Place place;
        try {
            place = placeManagerService.findPlaceByPrimaryKey(id);
            placeManagerService.deletePlace(place);
        } catch (BLServiceException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/admin/place/list";
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
}
