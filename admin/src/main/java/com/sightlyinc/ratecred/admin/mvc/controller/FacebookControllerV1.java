package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sightlyinc.ratecred.authentication.UserPrincipalService;

/**
 * @author clay
 * @version $Id$
 */
@Controller
@RequestMapping("/facebook/1_0")
public class FacebookControllerV1 {

    private static final Logger LOGGER = Logger.getLogger(FacebookControllerV1.class);
    
    @Autowired
    private UserPrincipalService userService;
   
    @RequestMapping(value="/auth", method=RequestMethod.GET)
	public String homePublisher(@RequestParam String code, HttpServletRequest request, Model model) {
    	LOGGER.debug("code:"+code);
    	HttpClient client = new HttpClient();
    	
    	/*
    	 * https://graph.facebook.com/oauth/access_token?
                client_id=YOUR_APP_ID
               &redirect_uri=YOUR_REDIRECT_URI
               &client_secret=YOUR_APP_SECRET
               &code=CODE_GENERATED_BY_FACEBOOK
               welocally
            App ID: 212416905466586
            App Secret: 1594a6e9abc4adbea7ce5e649207eb1a
    	 */
    	HttpMethod method = new GetMethod("https://graph.facebook.com/oauth/access_token?" +
    			"client_id=212416905466586&" +
    			"redirect_uri=http://gaudi.welocally.com/admin/facebook/1_0/auth&"+
    			"client_secret=1594a6e9abc4adbea7ce5e649207eb1a&" +
    			"code="+code);
			
		// Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
        /*UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(siteKey, siteToken);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = ((AuthenticationProvider)userService).authenticate(token);
        LOGGER.debug("Logging in with [{}]"+ authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);*/
    	
    	try {
    	      // Execute the method.
    	      int statusCode = client.executeMethod(method);

    	      if (statusCode != HttpStatus.SC_OK) {
    	        System.err.println("Method failed: " + method.getStatusLine());
    	      }

    	      // Read the response body.
    	      byte[] responseBody = method.getResponseBody();

    	      // Deal with the response.
    	      // Use caution: ensure correct character encoding and is not binary data
    	      LOGGER.debug(new String(responseBody));

    	    } catch (HttpException e) {
    	        LOGGER.error("Fatal protocol violation: " + e.getMessage());
    	    } catch (IOException e) {
    	        LOGGER.error("Fatal transport error: " + e.getMessage());
    	    } finally {
    	      // Release the connection.
    	      method.releaseConnection();
    	    } 
		
		return "redirect:/home";

	}
    
    @RequestMapping(value="/finished", method=RequestMethod.POST)
    public String finished(Model model,HttpServletRequest request) {
        LOGGER.debug("finished");
        
      
        
        return "facebook/finished";

    }

 
}
