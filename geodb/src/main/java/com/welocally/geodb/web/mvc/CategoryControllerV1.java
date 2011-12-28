package com.welocally.geodb.web.mvc;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.welocally.geodb.services.db.DbException;
import com.welocally.geodb.services.db.DbPage;
import com.welocally.geodb.services.db.IdGen;
import com.welocally.geodb.services.db.JsonDatabase;


@Controller
@RequestMapping("/category/1_0")
public class CategoryControllerV1 extends AbstractJsonController {
	
	static Logger logger = 
		Logger.getLogger(PlaceControllerV1.class);
	
	@Autowired JsonDatabase jsonDatabase;
	
	@Autowired IdGen idGen; 
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONObject category = jsonDatabase.findById("categories", id);
			mav.addObject("mapperResult", category.toString());
			
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		return mav;
	}
	
	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public ModelAndView getByType(Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONObject example = new JSONObject();
			example.put("type", "Type");
			
			makePagedCategories(example); 
			
//			DbPage dbpage = jsonDatabase.findByExample("categories", 1, example);			
//			JSONArray allCats = dbpage.getObjects();
//			int resultCount = 0;
//			int maxDocs = 100;
//			if (dbpage.getCount() > dbpage.getObjects().length()) {
//				int pages = dbpage.getCount() / dbpage.getPageSize();
//				if (dbpage.getCount() % dbpage.getPageSize() > 0)
//					pages = pages + 1;
//				for (int pageNumIndex = 2; pageNumIndex <= pages && resultCount<maxDocs; pageNumIndex++) {
//					dbpage = jsonDatabase.findByExample("categories", 1, example);
//					resultCount = resultCount + dbpage.getObjects().length();
//					for (int i = 0; i < dbpage.getObjects().length(); i++) {
//						allCats.put(dbpage.getObjects().get(i));
//					}
//				}
//			}
			
			
			mav.addObject("mapperResult", makePagedCategories(example));
			
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (JSONException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		return mav;
	}
	
	
	private String makePagedCategories(JSONObject example) throws DbException, JSONException{
		DbPage dbpage = jsonDatabase.findByExample("categories", 1, example);			
		JSONArray allCats = dbpage.getObjects();
		int resultCount = 0;
		int maxDocs = 100;
		if (dbpage.getCount() > dbpage.getObjects().length()) {
			int pages = dbpage.getCount() / dbpage.getPageSize();
			if (dbpage.getCount() % dbpage.getPageSize() > 0)
				pages = pages + 1;
			for (int pageNumIndex = 2; pageNumIndex <= pages && resultCount<maxDocs; pageNumIndex++) {
				dbpage = jsonDatabase.findByExample("categories", pageNumIndex, example);
				resultCount = resultCount + dbpage.getObjects().length();
				for (int i = 0; i < dbpage.getObjects().length(); i++) {
					allCats.put(dbpage.getObjects().get(i));
				}
			}
		}
		return allCats.toString();
	}
	
	//@RequestParam String type,
	@RequestMapping(value = "/children", method = RequestMethod.GET)
	public ModelAndView getChildren(@RequestParam String parentId, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONObject example = new JSONObject();
			example.put("parentId", parentId);
									
			mav.addObject("mapperResult", makePagedCategories(example));
			
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (JSONException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		return mav;
	}

}
