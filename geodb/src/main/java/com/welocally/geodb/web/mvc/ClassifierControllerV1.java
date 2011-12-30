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
import com.welocally.geodb.services.db.IdGen;
import com.welocally.geodb.services.db.JsonDatabase;


@Controller
@RequestMapping("/classifier/1_0")
public class ClassifierControllerV1 extends AbstractJsonController {
	
	static Logger logger = 
		Logger.getLogger(PlaceControllerV1.class);
	
	@Autowired JsonDatabase jsonDatabase;
	
	@Autowired IdGen idGen; 
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONObject classifier = jsonDatabase.findById("classifiers", id);
			mav.addObject("mapperResult", classifier.toString());
			
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		return mav;
	}
	
	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public ModelAndView getTypes(Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONObject example = new JSONObject();
			example.put("type", "Type");
			
			JSONArray types = jsonDatabase.findDistinct("classifiers", "type", null);

			mav.addObject("mapperResult", types.toString());
			
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (JSONException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		return mav;
	}
	
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ModelAndView getCategoriese(@RequestParam String type, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONObject example = new JSONObject();
			example.put("type", type);
			
			JSONArray types = jsonDatabase.findDistinct("classifiers", "category", example);

			mav.addObject("mapperResult", types.toString());
			
		} catch (DbException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} catch (JSONException e) {
			logger.error("could not get results");
			mav.addObject("mapperResult", makeErrorsJson(e));
		} 
		return mav;
	}
	
	@RequestMapping(value = "/subcategories", method = RequestMethod.GET)
	public ModelAndView getSubCategories(@RequestParam String type, @RequestParam String category, Model m){
		ModelAndView mav = new ModelAndView("mapper-result");
		try {
			JSONObject example = new JSONObject();
			example.put("type", type);
			example.put("category", category);
			
			JSONArray types = jsonDatabase.findDistinct("classifiers", "subcategory", example);

			mav.addObject("mapperResult", types.toString());
			
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
