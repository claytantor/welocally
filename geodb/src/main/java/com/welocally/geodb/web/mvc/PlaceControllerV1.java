package com.welocally.geodb.web.mvc;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.JsonDatabase;
import com.welocally.geodb.services.index.DirectoryException;
import com.welocally.geodb.services.spatial.Point;
import com.welocally.geodb.services.spatial.SpatialSearchException;
import com.welocally.geodb.services.spatial.SpatialSearchService;

@Controller
@RequestMapping("/place/1_0")
public class PlaceControllerV1 extends AbstractJsonController {
	
	static Logger logger = 
		Logger.getLogger(PlaceControllerV1.class);
	
	@Autowired 
	SpatialSearchService searchService;
	
	@Autowired JsonDatabase jsonDatabase;
	

		
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ModelAndView put(@PathVariable String id, @RequestBody String requestJson){
		ModelAndView mav = new ModelAndView("mapper-result");
		
		return mav;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONArray results = new JSONArray();
			results.put(jsonDatabase.findById("places", id));
			mav.addObject(
					"mapperResult", 
					results.toString());
			
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		return mav;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String q, @RequestParam String loc,  @RequestParam Double radiusKm,  Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		
		try {
			String[] parts = loc.split("_");
			
			Point p = new Point(Double.parseDouble(parts[0]),Double.parseDouble(parts[1]));
			
			JSONArray results = 
				searchService.find(q, p, radiusKm, "places");
			
			mav.addObject(
					"mapperResult", 
					results.toString());
			
			
		} catch (SpatialSearchException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (DirectoryException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (Exception e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		}	
		
		return mav;
	}
	


}
