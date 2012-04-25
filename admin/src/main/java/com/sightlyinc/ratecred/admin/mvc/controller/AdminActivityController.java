package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sightlyinc.ratecred.admin.util.HttpHelperUtils;
import com.sightlyinc.ratecred.admin.util.JsonObjectSerializer;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.PublisherService;

/**
 * special activities restricted to an admin
 * 
 * @author claygraham
 *
 */
@Controller
@RequestMapping(value="/administrator")
public class AdminActivityController {
		
	static Logger logger = Logger.getLogger(AdminActivityController.class);


	@Autowired
	private UserPrincipalService userPrincipalService;
	
	@Autowired
    private PublisherService publisherService;
	
	@Autowired
    private JsonObjectSerializer jsonObjectSerializer;
	
	@Autowired
	private HttpHelperUtils httpHelperUtils;
    
	@Value("${geodb.endpoint:http://localhost:8082/geodb}")
    private String geodbEnpoint;
    
	@Value("${geodb.provisionRequestMapping:/user/1_0/create}")
    private String provisionRequestMapping;
    
    
	
    @ModelAttribute("authenticatedUser")
    public UserDetails getAdminUser() {
        UserDetails details = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return details;
    }
    
    
	
	@RequestMapping(value="/publisher/provision/{id}", method=RequestMethod.GET)
	public String provisionUser(@PathVariable Long id, Model model) {
	    	    
	    try {
	        Publisher p = publisherService.findByPrimaryKey(id);
	       	        
            UserPrincipal up = p.getUserPrincipal();
            
            JSONObject upObject = 
                new JSONObject(jsonObjectSerializer.serialize(up));
            
            String url = geodbEnpoint+provisionRequestMapping;
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            UserDetails details = 
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();            
            
            String wireToken = new String(md.digest(details.getPassword().getBytes()));
            httpHelperUtils.put(upObject, details.getUsername(), wireToken, url);
            
            
        } catch (JSONException e) {
            logger.error("problem provision in user", e);
            model.addAttribute("error", e);
            return "error";
        } catch (IOException e) {
            logger.error("problem provision in user", e);
            model.addAttribute("error", e);
            return "error";
        } catch (NoSuchAlgorithmException e) {
            logger.error("problem provision in user", e);
            model.addAttribute("error", e);
            return "error";
        }
	    
	    return "redirect:/publisher/"+id;
    }
	
//	private void put(JSONObject doc, String key, String token, String url) throws JSONException{
//        HttpClient httpclient = new HttpClient();
//        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
//        //String url = "http://localhost:9200/place/"+doc.getString("id");
//        PutMethod put = new PutMethod(url);
//        put.setRequestHeader("key", key);
//        put.setRequestHeader("token", token);
//               
//        RequestEntity placeRequest = new ByteArrayRequestEntity(doc.toString().getBytes(),"application/json; charset=UTF-8");
//
//        put.setRequestEntity(placeRequest);
//        
//        try{
//            int result = httpclient.executeMethod(put);
//            
//            logger.debug("Response status code: " + result+" Response body: "+put.getResponseBodyAsString());
//            
//        } catch (HttpException he) {
//            logger.error("Http error connecting to '" + url + "'");
//        } catch (IOException ioe){
//            logger.error("Http error connecting to '" + url + "'");
//        } finally {
//            put.releaseConnection();
//        } 
//        
//    }
//	
//	private JSONObject get(String key, String token, String url) throws Exception {
//        HttpClient httpclient = new HttpClient();
//        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
//        //String url = "http://localhost:9200/place/"+doc.getString("id");
//        GetMethod get = new GetMethod(url);
//        get.setRequestHeader("key", key);
//        get.setRequestHeader("token", token);
//               
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        
//        try {
//            
//            logger.debug(get.getURI().toString());
//            
//            int returnCode = httpclient.executeMethod(get);
//        
//            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
//                logger.error("The GET method is not implemented by this URI");
//                throw new RuntimeException("problem with endpoint");
//            } else {
//                IOUtils.copy(
//                        get.getResponseBodyAsStream(), baos);                
//                baos.flush();
//                return new JSONObject(baos.toByteArray());
//                
//            }
//        } catch (Exception e) {
//            logger.error("Http error connecting to '" + url + "'");
//            throw e;
//        } finally {
//            get.releaseConnection();
//        } 
//
//        
//    }
	
	
	
	

}
