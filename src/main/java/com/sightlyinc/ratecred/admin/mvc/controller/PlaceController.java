package com.sightlyinc.ratecred.admin.mvc.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.PlaceForm;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.service.PlaceManagerService;

@Controller
@RequestMapping(value="/admin/place")
public class PlaceController {
	
	
	static Logger logger = Logger.getLogger(PlaceController.class);

	@Autowired
	@Qualifier("PlaceManagerService")
	private PlaceManagerService placeManagerService;
	
		
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("placeForm",new PlaceForm());				
		return "place_form";
	}
	
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid PlaceForm placeForm, BindingResult result) {
		logger.debug("got post action");
		try {
			
			Place p = 
				placeManagerService.findPlaceByPrimaryKey(placeForm.getPlaceId());
			p.setEmail(placeForm.getEmail());
			//p.setBusinessServices(placeForm.getBusinessServices());
			placeManagerService.savePlace(p);
			return "redirect:/do/admin/place/" + p.getId();
			
		} catch (BLServiceException e) {
			logger.error("problem saving award", e);
			return "error";
		}

	}
	

	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getAward(@PathVariable Long id, Model model) {
		logger.debug("getAward");
		try {
			Place p = 
				placeManagerService.findPlaceByPrimaryKey(id);
			if (p == null) {
				return "error";
			}
			model.addAttribute("place",p);
		} catch (BLServiceException e) {
			logger.error("problem",e);
			return "error";
		}
		return "place";
	}



}
