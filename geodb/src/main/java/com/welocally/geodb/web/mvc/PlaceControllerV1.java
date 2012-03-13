package com.welocally.geodb.web.mvc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import com.welocally.geodb.services.db.JsonDatabase.EntityType;
import com.welocally.geodb.services.spatial.Point;
import com.welocally.geodb.services.spatial.SpatialConversionUtils;
import com.welocally.geodb.services.spatial.SpatialSearchException;
import com.welocally.geodb.services.spatial.SpatialSearchService;
import com.welocally.geodb.services.util.JsonStoreLoader;

@Controller
@RequestMapping("/place/1_0")
public class PlaceControllerV1 extends AbstractJsonController {
	
	static Logger logger = 
		Logger.getLogger(PlaceControllerV1.class);
	
	@Qualifier("solrSearchService")
	@Autowired SpatialSearchService searchService;
	
	@Autowired 
	@Qualifier("dynamoJsonDatabase")
	JsonDatabase jsonDatabase;
	
	@Autowired IdGen idGen; 
	
	@Autowired SpatialConversionUtils spatialConversionUtils; 	
	
	@Value("${placesDatabase.collectionName:places}")
	String placesCollection;
	
	@Value("${userDatabase.collectionName:user}")
	String userCollection;
	
    @Value("${SolrSearchService.places.endpoint:http://localhost:8983/solr/select/}")
    private String searchEndpoint;
    
    @Value("${SolrPlaceLoader.places.endpoint:http://localhost:8983/solr/update/json}")
    private String updateEndpoint;
   
	
	//solrDealLoader, solrPlaceLoader
    @Qualifier("solrPlaceLoader")
	@Autowired JsonStoreLoader loader;
	
		
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView put(@RequestBody String requestJson, HttpServletRequest req){
		ModelAndView mav = new ModelAndView("mapper-result");
		
		try {
			JSONObject place = 
				new JSONObject(requestJson);
			
			String owner = "anonymous";
			if(req.getHeader("site-key") != null)
				owner = req.getHeader("site-key");
			
			place.put("owner", owner);			
			
			Point p = spatialConversionUtils.getJSONPoint(place);
			String id=idGen.genPoint("WL_",p);
			if(p != null){		
				place.put("_id", id);
				jsonDatabase.put(place, placesCollection, id, JsonDatabase.EntityType.PLACE);
				jsonDatabase.put(place, userCollection, id, JsonDatabase.EntityType.PLACE);
				StringWriter sw = new StringWriter();
				//now add it to the index
				loader.loadSingle(place, 1, 1, sw, updateEndpoint);
				sw.flush();
				sw.close();
								
				Map<String, Object> result = new HashMap<String,Object>();
				result.put("id", id);
				result.put("status", "SUCCEED");
				
				mav.addObject("mapperResult", makeModelJson(result));
			}		
		} catch (JSONException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (IOException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
        } 
		
		return mav;
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, Model m){
	    return delete(  id,  m);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ModelAndView delete(@PathVariable String id, Model m){
        ModelAndView mav = new ModelAndView("mapper-result");
        
        try {

            jsonDatabase.delete(placesCollection, id);
            StringWriter sw = new StringWriter();
            //now add it to the index
            loader.deleteSingle(id, 1, 1, sw, updateEndpoint);
            sw.flush();
            sw.close();
            
            
            Map<String, Object> result = new HashMap<String,Object>();
            result.put("id", id);
            result.put("status", "SUCCEED");
            
            mav.addObject("mapperResult", makeModelJson(result));
            
        } catch (DbException e) {
            logger.error("could not get results");
            if(e.getExceptionType() == DbException.Type.OBJECT_NOT_FOUND)
            {
                mav.addObject("mapperResult", new JSONArray().toString());
            } else {
                mav.addObject("mapperResult", makeErrorsJson(e));
            }

        } catch (JSONException e) {
            mav.addObject("mapperResult", makeErrorsJson(e));
        } catch (IOException e) {
            mav.addObject("mapperResult", makeErrorsJson(e));
        } 
        return mav;
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		
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
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT)
	public ModelAndView edit(@PathVariable String id, @RequestBody String requestJson, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONObject place = 
					new JSONObject(requestJson);
			jsonDatabase.put(place, placesCollection, id, EntityType.PLACE);
			mav.addObject("mapperResult", "OK");
		} catch (JSONException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		catch (DbException e) {
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
		ModelAndView mav = new ModelAndView("mapper-result");
		
		try {
			String[] parts = loc.split("_");
			
			Point p = new Point(Double.parseDouble(parts[0]),Double.parseDouble(parts[1]));

			JSONArray resultIds = 
				searchService.find(p, radiusKm, q, 0, 25, searchEndpoint);
			JSONArray results = new JSONArray();
			//get items from db 
			for (int i = 0; i < resultIds.length(); i++) {
                JSONObject id = resultIds.getJSONObject(i);
                JSONObject place = jsonDatabase.findById(placesCollection, id.getString("_id"));
                
                if(place != null){
                	place.put("distance", id.getDouble("_dist_"));
                	results.put(place);
                }
            }
							
			mav.addObject(
					"mapperResult", 
					results.toString());
					
		} 
		catch (SpatialSearchException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
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
