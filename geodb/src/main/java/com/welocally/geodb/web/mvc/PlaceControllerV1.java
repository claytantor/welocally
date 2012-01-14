package com.welocally.geodb.web.mvc;

import org.apache.log4j.Logger;
import org.apache.lucene.search.IndexSearcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.IdGen;
import com.welocally.geodb.services.db.JsonDatabase;
import com.welocally.geodb.services.index.DirectoryException;
import com.welocally.geodb.services.spatial.Point;
import com.welocally.geodb.services.spatial.SpatialConversionUtils;
import com.welocally.geodb.services.spatial.SpatialIndexException;
import com.welocally.geodb.services.spatial.SpatialIndexService;
import com.welocally.geodb.services.spatial.SpatialSearchException;
import com.welocally.geodb.services.spatial.SpatialSearchService;

@Controller
@RequestMapping("/place/1_0")
public class PlaceControllerV1 extends AbstractJsonController {
	
	static Logger logger = 
		Logger.getLogger(PlaceControllerV1.class);
	
	@Autowired SpatialSearchService searchService;
	
	@Autowired JsonDatabase jsonDatabase;
	
	@Autowired IdGen idGen; 
	
	@Autowired SpatialConversionUtils spatialConversionUtils; 
	
	@Autowired 
	@Qualifier("luceneMongoSpatialIndexService")
	SpatialIndexService spatialIndexService;
	
		
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView put(@RequestBody String requestJson){
		ModelAndView mav = new ModelAndView("mapper-result");
		
		try {
			JSONObject place = 
				new JSONObject(requestJson);
			place.put("owner", "welocally");
			Point p = spatialConversionUtils.getJSONPoint(place);
			if(p != null){		
				jsonDatabase.put(place, "new_places", idGen.genPoint(p));
				spatialIndexService.indexPlace(place);
			}		
		} catch (JSONException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		catch (SpatialIndexException e) {
			logger.error("could not index results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		}

		
		
		return mav;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		
		try {
			JSONArray places = new JSONArray();
			JSONObject place = jsonDatabase.findById("places", id);
			places.put(place);
			mav.addObject("mapperResult", places.toString());
		} catch (DbException e) {
			logger.error("could not get results");
			if(e.getExceptionType() == DbException.Type.OBJECT_NOT_FOUND)
			{
				mav.addObject("mapperResult", new JSONArray().toString());
			} else {
				mav.addObject("mapperResult", makeErrorsJson(e));
			}

		} 
		return mav;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String q, @RequestParam String loc,  @RequestParam Double radiusKm,  Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		
		try {
			String[] parts = loc.split("_");
			
			Point p = new Point(Double.parseDouble(parts[0]),Double.parseDouble(parts[1]));
			IndexSearcher searcher = searchService.getPlaceSearcher();
			JSONArray results = 
				searchService.find(searcher, p, radiusKm, q);
			
			mav.addObject(
					"mapperResult", 
					results.toString());
					
		} 
		catch (SpatialSearchException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		catch (Exception e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		}	
		
		return mav;
	}

	public void setJsonDatabase(JsonDatabase jsonDatabase) {
		this.jsonDatabase = jsonDatabase;
	}

	public void setSearchService(SpatialSearchService searchService) {
		this.searchService = searchService;
	}

	public void setIdGen(IdGen idGen) {
		this.idGen = idGen;
	}

	public void setSpatialConversionUtils(
			SpatialConversionUtils spatialConversionUtils) {
		this.spatialConversionUtils = spatialConversionUtils;
	}
	


}
