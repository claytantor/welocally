package com.welocally.geodb.web.mvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.search.geo.GeoDistance;
import org.elasticsearch.search.SearchHit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.IdGen;
import com.welocally.geodb.services.db.JsonDatabase;
import com.welocally.geodb.services.spatial.Point;
import com.welocally.geodb.services.spatial.SpatialConversionUtils;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Controller
@RequestMapping("/place/es")
public class ESPlaceController extends AbstractJsonController {
	
	static Logger logger = 
		Logger.getLogger(ESPlaceController.class);
		
	@Autowired 
	@Qualifier("dynamoJsonDatabase")
	JsonDatabase jsonDatabase;
	
	@Value("${placesDatabase.collectionName:places}")
	String placesCollection;
	
	@Autowired IdGen idGen; 
	
	@Autowired SpatialConversionUtils spatialConversionUtils; 	
	
	TransportClient transportClient = null;
	
	public ESPlaceController() {
		Settings settings = ImmutableSettings.settingsBuilder()
			.put("cluster.name", "es-welocally").build();
		transportClient = new TransportClient(settings);
		transportClient.addTransportAddress(new InetSocketTransportAddress("ec2-107-22-80-168.compute-1.amazonaws.com", 9300));
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id, @RequestParam(required=false) String callback, Model m){
	    ModelAndView mav = null;
        if(StringUtils.isEmpty(callback))
            mav = new ModelAndView("mapper-result");
        else {
            mav = new ModelAndView("jsonp-mapper-result");
            mav.addObject(
                    "callback", 
                    callback);
        }
		
		try {
			JSONArray places = new JSONArray();
			JSONObject place = jsonDatabase.findById(placesCollection, id);
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
	public ModelAndView search(@RequestParam String q, @RequestParam String loc,  
			@RequestParam Double radiusKm, @RequestParam(required=false) String callback, HttpServletRequest req){
		
	    ModelAndView mav = null;
	    if(StringUtils.isEmpty(callback))
	        mav = new ModelAndView("mapper-result");
	    else {
	        mav = new ModelAndView("jsonp-mapper-result");
	        mav.addObject(
                    "callback", 
                    callback);
	    }
        	    
		try {
			String[] parts = loc.split("_");

			QueryBuilder searchQuery = filteredQuery(
					termQuery("search", q),
					geoDistanceFilter("location")
					.point(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]))
					.distance(radiusKm, DistanceUnit.KILOMETERS));
			
			SearchResponse response = transportClient.prepareSearch("geodb").setTypes("place").
			setQuery(searchQuery).execute().actionGet();

			
			JSONArray results = new JSONArray();
			//get items from db 
			for (SearchHit hit: response.getHits()) {
                String id = hit.getId();
                JSONObject place = jsonDatabase.findById(placesCollection, id);
                
                if(place != null){
//                	place.put("distance", id.getDouble("_dist_"));
                	results.put(place);
                }
            }
							
			mav.addObject(
					"mapperResult", 
					results.toString());
					
		} 
		catch (Exception e) {
			logger.error("could not get results",e);
			mav.addObject("mapperResult", makeErrorsJson(e));
		}	
			
		return mav;
	}

	public void setJsonDatabase(JsonDatabase jsonDatabase) {
		this.jsonDatabase = jsonDatabase;
	}

	public void setIdGen(IdGen idGen) {
		this.idGen = idGen;
	}

	public void setSpatialConversionUtils(
			SpatialConversionUtils spatialConversionUtils) {
		this.spatialConversionUtils = spatialConversionUtils;
	}
	
	


}
