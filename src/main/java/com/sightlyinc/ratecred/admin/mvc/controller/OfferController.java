package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.noi.utility.geo.GeoPoint;
import com.noi.utility.geo.GeoPointBounds;
import com.noi.utility.geo.MapUtils;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.model.TargetModel;
import com.sightlyinc.ratecred.client.offers.Location;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.OfferPoolService;
import com.sightlyinc.ratecred.service.RaterAwardsService;

@Controller
@RequestMapping(value="/offer")
public class OfferController {
	
	
	static Logger logger = Logger.getLogger(OfferController.class);

	
	@Autowired
	@Qualifier("offerPoolService")
	private OfferPoolService offerPoolService;
	
	@Autowired
	private AwardManagerService awardManagerService;
	
	@Autowired
	RaterAwardsService raterAwardsService;
	

	
	@RequestMapping(value="/target",method=RequestMethod.GET)
	public String getTargetedOffer(
			@RequestParam(value="city",required=false) String city, 
			@RequestParam(value="state",required=false) String state, 
			@RequestParam(value="keywords",required=false) String keywords, 
			@RequestParam(value="view",required=false) String view, 
			@RequestParam(value="css",required=false) String css, 
			Model model) throws BLServiceException {		
		
		TargetModel tmodel = new TargetModel();
		tmodel.setCity(city);
		tmodel.setState(state);
		List<String> keywordsList = Arrays.asList(keywords.split(","));	
		tmodel.setKeywords(keywordsList);
		
		try {
			
			Offer offer = raterAwardsService.targetOfferByTargetingModel(tmodel);
			model.addAttribute(
					"offer", raterAwardsService.targetOfferByTargetingModel(tmodel));
			model.addAttribute(
					"css", css);
			
			//find the center and points
			//map bounds
			List<GeoPoint> points = new ArrayList<GeoPoint>();

			for (Location location : offer.getAdvertiser().getLocations()) {
				points.add(new GeoPoint(
						location.getLat(), location.getLng()));
			}
			
			if(points.size()>0)
			{
				GeoPointBounds bounds = MapUtils.computeBounds(points);
				model.addAttribute("mapCenter", MapUtils.computeCenter(bounds));	
				model.addAttribute("mapBounds", bounds);	
			}
			
			
		} catch (BLServiceException e) {
			logger.error("problem targeting", e);
			throw e;
		}
		
		if(StringUtils.isEmpty(view))
			return "offer";
		else
			return view;
	}
	
	

}
