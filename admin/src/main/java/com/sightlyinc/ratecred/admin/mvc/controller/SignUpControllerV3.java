package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.ui.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.model.AjaxError;
import com.sightlyinc.ratecred.admin.model.AjaxErrors;
import com.sightlyinc.ratecred.admin.model.Errors;
import com.sightlyinc.ratecred.admin.model.SiteInfoModel;
import com.sightlyinc.ratecred.admin.model.UserPrincipalForm;
import com.sightlyinc.ratecred.admin.util.JsonObjectSerializer;
import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.OrderManager;
import com.sightlyinc.ratecred.service.PublisherManager;
import com.sightlyinc.ratecred.service.PublisherService;

/**
 * @author clay
 * @version $Id$
 */
@Controller
@RequestMapping("/signup/3_0")
public class SignUpControllerV3 {

    private static Logger logger = Logger.getLogger(SignUpControllerV3.class);

    @Autowired
    private UserPrincipalService userService;
    

    @Autowired
    private PublisherService publisherService;
    
    
    @Autowired
    private OrderManager orderManager;
    
    @Autowired
    private PublisherManager publisherManager;
    
	
	@Autowired
	JsonObjectSerializer jsonObjectSerializer;

	
	@Value("${admin.adminEmailAccountFrom:mailer@welocally.com}")
	private String adminEmailAccountFrom;
    

    @ModelAttribute("signup")
    public UserPrincipalForm getForm() {
        return new UserPrincipalForm();
    }

    @RequestMapping("/success")
    public String success() {
        return "signup_success";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSignUpForm() {
        return "signup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postSignUpForm(@ModelAttribute("signup") UserPrincipalForm form, BindingResult errors) {
        String viewName = "signup";

        // validate email
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "emailRequired", "Please provide an email");

        if (!errors.hasErrors()) {
            if (!com.noi.utility.validation.ValidationUtils.isEmailAddressValid(form.getEmail())) {
                errors.rejectValue("email", "invalidEmail", "Please provide a valid email");
            }
        }

        // validate username
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "usernameRequired", "Please provide a username");

        // validate password
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "passwordRequired", "Please provide a password");

        // password requirements?
        if (!errors.hasErrors()) {
            // create account
            try {
                // TODO hard coding the role to publisher for now, will need to figure out a better way of doing this later
                userService.signUp(form.getEntity(), Arrays.asList("ROLE_USER", "ROLE_PUBLISHER"));
                // send user to success page
                viewName = "redirect:/signup/success";
            } catch (UserPrincipalServiceException e) {
                if (e.getMessage().toLowerCase().contains("email")) {
                    errors.rejectValue("email", "emailUsed", e.getMessage());
                } else if (e.getMessage().toLowerCase().contains("username")) {
                    errors.rejectValue("username", "usernameUsed", e.getMessage());
                } else {
                    errors.reject("error", e.getMessage());
                }
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
                errors.reject("error", "Something went wrong saving your account information, please try again");
            }
        }

        return viewName;
    }
    
    
    @RequestMapping(value="/plugin/verify", method=RequestMethod.GET)
    public Map<String, Object> verify(@RequestBody String requestJson, HttpServletRequest request) {
        
        Map<String, Object> response = new HashMap<String, Object>();
        Errors eajax = new AjaxErrors();
        try {
            String key = request.getParameter("site-key");
            String token = request.getParameter("site-token");
            JSONObject jsonObject = new JSONObject(requestJson);

            Publisher p = publisherService.findByPublisherKey(key);
            if(p.getJsonToken().equals(token)){
                UserPrincipal up = userService.loadUser(key);
                Map<String,String> data = new HashMap<String,String>();
                data.put("command", jsonObject.getString("command"));
                response.put("mapperResult",
                        jsonObjectSerializer.serialize(publisherManager.isAllowed(data, p, up)));
            } else {
                eajax.getErrors().add(
                        new AjaxError(AjaxError.AUTH_ERROR, "Problem with token match."));
            }
            
        } catch (JSONException e) {
            logger.error("problem with request", e);
            eajax.getErrors().add(
                    new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));
        } catch (UserPrincipalServiceException e) {
            logger.error("problem with user", e);
            eajax.getErrors().add(
                    new AjaxError(AjaxError.AUTH_ERROR, "Problem with user."));
        } catch (UserNotFoundException e) {
            logger.error("problem with user", e);
            eajax.getErrors().add(
                    new AjaxError(AjaxError.AUTH_ERROR, "Problem with user."));
        } catch (IOException e) {
            logger.error("problem serializing", e);
            eajax.getErrors().add(
                    new AjaxError(AjaxError.AUTH_ERROR, "Problem with serialize."));
        }
        
     // if errors send them instead
        if (eajax.getErrors().size() > 0) {
            try {
                response.put("mapperResult",
                        jsonObjectSerializer.serialize(eajax));
                //mav.setViewName("mapper-result");
            } catch (IOException e) {
                logger.error("cannot serialize message", e);
            }
        }

        return response;
    }
    
    @RequestMapping(value="/plugin/login", method=RequestMethod.GET)
	public String homePublisher(@RequestParam String siteKey, @RequestParam String siteToken, HttpServletRequest request, Model model) {
    	logger.debug("home");
		
			
		// Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(siteKey, siteToken);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = ((AuthenticationProvider)userService).authenticate(token);
        logger.debug("Logging in with [{}]"+ authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/home";
	}

    @RequestMapping(value = "/plugin/key")
    public ModelAndView signUpFromPluginForKey(@RequestParam(required=false) String callback, HttpServletRequest request) {
        ModelAndView mav = null;
        if(StringUtils.isEmpty(callback))
            mav = new ModelAndView("mapper-result");
        else {
            mav = new ModelAndView("jsonp-mapper-result");
            mav.addObject(
                    "callback", 
                    callback);
        }
        
        Map<String, Object> response = new HashMap<String, Object>();
        Errors eajax = new AjaxErrors();

        try {    
            
            SiteInfoModel site = transformModel(new JSONObject(request.getParameterMap()));
                        
            // validate input
            if (StringUtils.isBlank(site.getHome())) {
                //errors.add("Please provide the URL for your site");
                eajax.getErrors().add(
                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide the URL for your site."));
            }
            
            if (StringUtils.isBlank(site.getName())) {
                eajax.getErrors().add(
                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide the name for your site."));
            }
            
            if (StringUtils.isBlank(site.getEmail())) {
                eajax.getErrors().add(
                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide a real email address."));
            }

            if(site.getKey() == null || site.getKey().isEmpty() || site.getName().equals("delete")){
                String newkey = UUID.randomUUID().toString();
                newkey = newkey.substring(newkey.lastIndexOf('-') + 1);
                site.setKey(newkey);               
                response.put("site", site);
                response.put("subscriptionStatus", "KEY_ASSIGNED");               
            } else {
                Publisher p = publisherService.findByPublisherKey(site.getKey());
                if(p == null){
                    String newkey = UUID.randomUUID().toString();
                    newkey = newkey.substring(newkey.lastIndexOf('-') + 1);
                    site.setKey(newkey);  
                    response.put("site", site);
                    response.put("subscriptionStatus", "KEY_ASSIGNED");  
                } 
                else if(p!= null && (p.getOrders()==null || p.getOrders().size()==0)){

                    eajax.getErrors().add(
                            new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Something is wrong with your order, please contact welocally."));
                    
                }
                else {
                    response.put("site", site);
                    response.put("subscriptionStatus", p.getSubscriptionStatus()); 
                }               
            }      

        } catch (Exception e) {
            //errors.add("Unable to parse request, please check the format of your data");
        	logger.error("problem with request", e);
            eajax.getErrors().add(
                    new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));
        }
        
        //if errors send them       
        try {
            if(eajax.getErrors().size()>0){
                mav.addObject("mapperResult", jsonObjectSerializer.serialize(eajax));
            } else {
                mav.addObject("mapperResult", jsonObjectSerializer.serialize(response));
            }
            
            
        } catch (IOException e) {
            mav.addObject("mapperResult", makeErrorsJson(e));   
        }
   
        return mav;
    }
    
    private SiteInfoModel transformModel(JSONObject jsonQueryString) throws JSONException{
        SiteInfoModel info = new SiteInfoModel();
        if(!jsonQueryString.isNull("siteKey")){
            info.setKey(((String[])jsonQueryString.get("siteKey"))[0].toString());
        }

        if(!jsonQueryString.isNull("siteEmail")){
            info.setEmail(((String[])jsonQueryString.get("siteEmail"))[0].toString());
        }
        
        if(!jsonQueryString.isNull("siteHome")){
            info.setHome(((String[])jsonQueryString.get("siteHome"))[0].toString());
        }
        
        if(!jsonQueryString.isNull("siteName")){
            info.setName(((String[])jsonQueryString.get("siteName"))[0].toString());
        }
        
        if(!jsonQueryString.isNull("siteToken")){

            boolean empty = StringUtils.isEmpty(((String[])jsonQueryString.get("siteToken"))[0].toString());
            if(!((String[])jsonQueryString.get("siteToken"))[0].toString().equals("null") && !empty)
                info.setToken(((String[])jsonQueryString.get("siteToken"))[0].toString());
        }
        
        return info;
    }
    
    @RequestMapping(value = "/plugin/register")
    public ModelAndView register(@RequestParam(required=false) String callback,  HttpServletRequest request) {
        
        ModelAndView mav = null;
        if(StringUtils.isEmpty(callback))
            mav = new ModelAndView("mapper-result");
        else {
            mav = new ModelAndView("jsonp-mapper-result");
            mav.addObject(
                    "callback", 
                    callback);
        }
        
    	Map<String, Object> response = new HashMap<String, Object>();
    	
		Errors eajax = new AjaxErrors();
    	
    	
    	//all we do here is gen the token and send it by email
    	try {
    	    
    	    SiteInfoModel site = transformModel(new JSONObject(request.getParameterMap()));
    	    // validate input
            if (StringUtils.isBlank(site.getHome())) {
                //errors.add("Please provide the URL for your site");
                eajax.getErrors().add(
                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide the URL for your site."));
            }
            
            if (StringUtils.isBlank(site.getName())) {
                eajax.getErrors().add(
                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide the name for your site."));
            }
            
            if (StringUtils.isBlank(site.getEmail())) {
                eajax.getErrors().add(
                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide a real email address."));
            }


            if( eajax.getErrors().size()==0){
                
                Boolean verified = false;
                if(site.getHome().equals(request.getHeader("referer"))){
                    verified = true;
                } 
                
                Publisher publisher = orderManager.processPublisherRegistration(
                        site.getEmail(), 
                        site.getKey(), 
                        site.getToken(),
                        site.getName(), 
                        site.getHome(), 
                        verified,
                        adminEmailAccountFrom);
                
                site.setKey(publisher.getKey());
                //site.setToken(publisher.getJsonToken());
                response.put("site", site);
                response.put("subscriptionStatus", publisher.getSubscriptionStatus());
                
            }

			

		} catch (JSONException e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));

		} catch (BLServiceException e) {
            logger.debug("business logic problem request:"+e.getMessage());

            eajax.getErrors().add(
                    new AjaxError(AjaxError.SUBSCRIPTION_INVALID,"Problem with order please contact welocally:"+e.getMessage()));
        }
		catch (Exception e) {
			logger.error("problem with request", e);
			eajax.getErrors().add(
					new AjaxError(AjaxError.WL_SERVER_ERROR,
							"Problem back at Welocally please report: "
									+ e.getStackTrace()[0].toString().replace(
											".", " ")));
		}
		 //if errors send them       
        try {
            if(eajax.getErrors().size()>0){
                mav.addObject("mapperResult", jsonObjectSerializer.serialize(eajax));
            } else {
                mav.addObject("mapperResult", jsonObjectSerializer.serialize(response));
            }
            
            
        } catch (IOException e) {
            mav.addObject("mapperResult", makeErrorsJson(e));   
        }
   
        return mav;
    }
    
    protected String makeErrorsJson(Exception e){
        try {
            return jsonObjectSerializer.serialize(makeErrors(e));
        } catch (IOException e1) {
            return "[{\"errorMessage\":\"server error\",\"errorCode\":106 }]";
        }
    }
    
    protected AjaxErrors makeErrors(Exception e){
        AjaxErrors errors = new AjaxErrors();
        AjaxError error = new AjaxError(AjaxError.WL_SERVER_ERROR,e.getMessage());
        errors.getErrors().add(error);
        return errors;
    }
    public void setUserService(UserPrincipalService userService) {
        this.userService = userService;
    }
}
