package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import com.sightlyinc.ratecred.service.PublisherService;

@Controller
@RequestMapping("/place/1_0")
public class PlaceControllerV1 {

	static Logger logger = Logger.getLogger(PlaceControllerV1.class);
	
	@Autowired
	private PublisherService publisherService;

	@RequestMapping(value = "/iplace", method = RequestMethod.GET)
    public String place(
            @RequestParam String id, 
            @RequestParam String key, 
            @RequestParam String token, 
            Model model,
            HttpServletRequest request) {
	    logger.debug("id:"+id);
	    	    
	    model.addAttribute("key", key);
	    model.addAttribute("token", token);	    
	    model.addAttribute("id", id);
        return "place/iplace";
    }
	
	@RequestMapping(value = "/finder", method = RequestMethod.GET)
    public String finder(Model model,HttpServletRequest request) {
	    
        return "place/finder";
    }
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model,HttpServletRequest request) {       
        return "place/edit";
    }
	
	@RequestMapping(value = "/user/upload", method = RequestMethod.POST)
    public String uploadUserPlaces(
            @RequestParam("file") MultipartFile file, 
            HttpServletRequest request, 
            HttpServletResponse response) {  

	    logger.debug("upload");
	    
        try {
            if (file == null) {
                 // hmm, that's strange, the user did not upload anything
            } else {
                InputStreamReader isr = new InputStreamReader(file.getInputStream());
                
                CSVReader in = new CSVReader(isr);
                List<String[]> rows = in.readAll();
                JSONArray places = new JSONArray();
                for (String[] placeRow : rows) {
                    places.put(makePlaceFromCSVRow(placeRow));
                }
                
                
             
                in.close();
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }
	
	private JSONObject makePlaceFromCSVRow(String[] placeRow){
	    JSONObject place = new JSONObject();
	    return place;
	}

}
