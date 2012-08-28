package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sightlyinc.ratecred.admin.model.AjaxError;
import com.sightlyinc.ratecred.admin.model.AjaxErrors;
import com.sightlyinc.ratecred.admin.model.PublisherSignupForm;
import com.sightlyinc.ratecred.admin.util.JsonObjectSerializer;
import com.sightlyinc.ratecred.model.Site;
import com.sightlyinc.ratecred.service.OrderManagerV2;
import com.sightlyinc.ratecred.service.SiteService;
import com.welocally.admin.security.UserPrincipal;
import com.welocally.admin.security.UserPrincipalService;
import com.welocally.admin.security.UserPrincipalServiceException;

/**
 * @author clay
 * @version $Id$
 */
@Controller
@RequestMapping("/signup/4_0")
public class SignUpControllerV4 {

    private static Logger logger = Logger.getLogger(SignUpControllerV4.class);

    @Autowired
    private UserPrincipalService userService;
    

//    @Autowired
//    private PublisherService publisherService;
    
    @Autowired SiteService siteService;
    
    
    @Autowired
    private OrderManagerV2 orderManager;
    
//    @Autowired
//    private PublisherManager publisherManager;
    
	
	@Autowired
	JsonObjectSerializer jsonObjectSerializer;

	
	@Value("${admin.adminEmailAccountFrom:mailer@welocally.com}")
	private String adminEmailAccountFrom;
    

    @ModelAttribute("publisherSignupForm")
    public PublisherSignupForm getForm() {
        return new PublisherSignupForm();
    }

    @RequestMapping("/success")
    public String success() {
        return "signup_success";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSignUpForm(Model m) {
        return "signup";
    }
    
    private class SignupValidator implements Validator{

        @Override
        public boolean supports(Class<?> clazz) {
            return PublisherSignupForm.class.equals(clazz);
        }

        @Override
        public void validate(Object target,
                org.springframework.validation.Errors errors) {
            
            PublisherSignupForm form = (PublisherSignupForm)target;
         // validate email
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "emailRequired", "Please provide an email");

            if (!errors.hasErrors()) {
                if (!com.noi.utility.validation.ValidationUtils.isEmailAddressValid(form.getEmail())) {
                    errors.rejectValue("email", "invalidEmail", "Please provide a valid email");
                }
            }

            // validate 
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "usernameRequired", "Please provide a username");
            if (!errors.hasErrors()) {
                try {
                    UserPrincipal up = userService.findByUserName(form.getUsername());
                    if(up != null){
                        errors.rejectValue("username", "usernameRejected", "That username has already been taken.");
                    }
                    
                } catch (UserPrincipalServiceException e) {
                    errors.rejectValue("username", "usernameRejected", "Username rejected");
                }
            }
            
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "passwordRequired", "Please provide a password");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "siteName", "siteNameRequired", "Please provide a Site Name");
            if (!errors.hasErrors()) {

                    List<Site> sites = siteService.findBySiteUrl(form.getSiteUrl());
                    if(sites != null && sites.size()>0){
                        errors.rejectValue("siteUrl", "siteRejected", "That site URL has already been taken.");
                    }

            }
            
            
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "siteUrl", "siteUrlRequired", "Please provide a valid Site Url");
            
            if (!form.getTermsAgree()) {
                errors.rejectValue("termsAgree", "termsNotAgreed", "You must agree to the terms of service to register.");
            }
            
            if (StringUtils.isNotEmpty(form.getPasswordStatus()) && !form.getPasswordStatus().equals("match")) {
                errors.rejectValue("password", "passwordNotStrong", "Password weak or not match.");
            }
            
            
            logger.debug("validate");
          
        }
        
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postSignUpForm(@Valid PublisherSignupForm publisherSignupForm, BindingResult errors, Model m) {
        
        logger.debug("signup form submitted");
        
        String viewName = "signup";
               
        ValidationUtils.invokeValidator(new SignupValidator(), publisherSignupForm, errors);
        
        // password requirements?
        if (!errors.hasErrors()) {
                      
            try {
                orderManager.processPublisherRegistration(
                        publisherSignupForm.getEmail(), 
                        publisherSignupForm.getUsername(),
                        publisherSignupForm.getPassword(),                       
                        publisherSignupForm.getSiteName(),
                        publisherSignupForm.getSiteUrl(), 
                        false,
                        adminEmailAccountFrom);
                m.addAttribute("activationEmail", publisherSignupForm.getEmail());
                viewName = "activate_success";
                
            } catch (Exception e) {
                errors.rejectValue("username", "usernameRejected", "Problem creating user, try again.");
                logger.debug("error creating user",e);
            }
             
            
        } 

        return viewName;
    }
    
    @RequestMapping(value="/activate", method=RequestMethod.GET)
    public String activate(
            @RequestParam("u") String username,
            @RequestParam("siteName") String siteName,
            @RequestParam("siteUrl") String siteUrl,
            @RequestParam("token") String token, 
            HttpServletRequest request) {
               
        try {
            orderManager.processActivation(
                    username, 
                    siteName, 
                    siteUrl, 
                    token,
                    adminEmailAccountFrom);
        } catch (Exception e) {
            return "error";
        }
        
        
        return "signup_success";
    }
    
    
    
    
//    
//    @RequestMapping(value="/plugin/verify", method=RequestMethod.GET)
//    public Map<String, Object> verify(@RequestBody String requestJson, HttpServletRequest request) {
//        
//        Map<String, Object> response = new HashMap<String, Object>();
//        Errors eajax = new AjaxErrors();
//        try {
//            String key = request.getParameter("site-key");
//            String token = request.getParameter("site-token");
//            JSONObject jsonObject = new JSONObject(requestJson);
//
//            Publisher p = publisherService.findByPublisherKey(key);
//            if(p.getJsonToken().equals(token)){
//                UserPrincipal up = userService.loadUser(key);
//                Map<String,String> data = new HashMap<String,String>();
//                data.put("command", jsonObject.getString("command"));
//                response.put("mapperResult",
//                        jsonObjectSerializer.serialize(publisherManager.isAllowed(data, p, up)));
//            } else {
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.AUTH_ERROR, "Problem with token match."));
//            }
//            
//        } catch (JSONException e) {
//            logger.error("problem with request", e);
//            eajax.getErrors().add(
//                    new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));
//        } catch (UserPrincipalServiceException e) {
//            logger.error("problem with user", e);
//            eajax.getErrors().add(
//                    new AjaxError(AjaxError.AUTH_ERROR, "Problem with user."));
//        } catch (UserNotFoundException e) {
//            logger.error("problem with user", e);
//            eajax.getErrors().add(
//                    new AjaxError(AjaxError.AUTH_ERROR, "Problem with user."));
//        } catch (IOException e) {
//            logger.error("problem serializing", e);
//            eajax.getErrors().add(
//                    new AjaxError(AjaxError.AUTH_ERROR, "Problem with serialize."));
//        }
//        
//     // if errors send them instead
//        if (eajax.getErrors().size() > 0) {
//            try {
//                response.put("mapperResult",
//                        jsonObjectSerializer.serialize(eajax));
//                //mav.setViewName("mapper-result");
//            } catch (IOException e) {
//                logger.error("cannot serialize message", e);
//            }
//        }
//
//        return response;
//    }
//    
//    @RequestMapping(value="/plugin/login", method=RequestMethod.GET)
//	public String homePublisher(@RequestParam String siteKey, @RequestParam String siteToken, HttpServletRequest request, Model model) {
//    	logger.debug("home");
//		
//			
//		// Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(siteKey, siteToken);
//        token.setDetails(new WebAuthenticationDetails(request));
//        Authentication authentication = ((AuthenticationProvider)userService).authenticate(token);
//        logger.debug("Logging in with [{}]"+ authentication.getPrincipal());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//		
//		return "redirect:/home";
//	}
//
//    @RequestMapping(value = "/plugin/key")
//    public ModelAndView signUpFromPluginForKey(@RequestParam(required=false) String callback, HttpServletRequest request) {
//        ModelAndView mav = null;
//        if(StringUtils.isEmpty(callback))
//            mav = new ModelAndView("mapper-result");
//        else {
//            mav = new ModelAndView("jsonp-mapper-result");
//            mav.addObject(
//                    "callback", 
//                    callback);
//        }
//        
//        Map<String, Object> response = new HashMap<String, Object>();
//        Errors eajax = new AjaxErrors();
//
//        try {    
//            
//            SiteInfoModel site = transformModel(new JSONObject(request.getParameterMap()));
//                        
//            // validate input
//            if (StringUtils.isBlank(site.getHome())) {
//                //errors.add("Please provide the URL for your site");
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide the URL for your site."));
//            }
//            
//            if (StringUtils.isBlank(site.getName())) {
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide the name for your site."));
//            }
//            
//            if (StringUtils.isBlank(site.getEmail())) {
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide a real email address."));
//            }
//
//            if(StringUtils.isEmpty(site.getKey()) && StringUtils.isEmpty(site.getToken())){
//                String newkey = UUID.randomUUID().toString();
//                newkey = newkey.substring(newkey.lastIndexOf('-') + 1);
//                site.setKey(newkey);               
//                response.put("site", site);
//                response.put("subscriptionStatus", "KEY_ASSIGNED");               
//            }    
//            else {
//                Publisher p = publisherService.findByPublisherKey(site.getKey());
//                if(p == null){
//                    String newkey = UUID.randomUUID().toString();
//                    newkey = newkey.substring(newkey.lastIndexOf('-') + 1);
//                    site.setKey(newkey);  
//                    response.put("site", site);
//                    response.put("subscriptionStatus", "KEY_ASSIGNED");  
//                } 
//                else if(p!= null && (p.getOrders()==null || p.getOrders().size()==0)){
//                    eajax.getErrors().add(
//                            new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Something is wrong with your order, please contact welocally."));
//                    
//                } 
//                else if(p!= null && StringUtils.isEmpty(site.getToken()) && !StringUtils.isEmpty(p.getJsonToken()) ){                  
//                    orderManager.checkSiteForPublisher(p, site.getEmail(), site.getKey(), site.getToken(), site.getName(), site.getHome());
//                    response.put("site", site);
//                    response.put("subscriptionStatus", "REGISTERED");  
//                    response.put("key1", googleMapsApiKey);
//                }
//                else {
//                    
//                    orderManager.checkSiteForPublisher(p, site.getEmail(), site.getKey(), site.getToken(), site.getName(), site.getHome());                                     
//                    response.put("site", site);
//                    response.put("subscriptionStatus", p.getSubscriptionStatus()); 
//                    if(p.getSubscriptionStatus().equals(Publisher.PublisherStatus.SUBSCRIBED) ||
//                            p.getSubscriptionStatus().equals(Publisher.PublisherStatus.REGISTERED)){
//                        response.put("key1", googleMapsApiKey);
//                    } 
//                } 
//                
//
//            }      
//
//        } 
//        catch (BLServiceException e) {
//            logger.debug("problem with request"+e.getMessage()); 
//            for (BLMessage message : e.getMessages()) {
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.SUBSCRIPTION_INVALID, message.getMessageText()));
//                
//            }
//            
//        }
//        catch (Exception e) {
//            //errors.add("Unable to parse request, please check the format of your data");
//        	logger.error("problem with request", e);
//            eajax.getErrors().add(
//                    new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));
//        }
//        
//        //if errors send them       
//        try {
//            if(eajax.getErrors().size()>0){
//                mav.addObject("mapperResult", jsonObjectSerializer.serialize(eajax));
//            } else {
//                mav.addObject("mapperResult", jsonObjectSerializer.serialize(response));
//            }
//            
//            
//        } catch (IOException e) {
//            mav.addObject("mapperResult", makeErrorsJson(e));   
//        }
//   
//        return mav;
//    }
//    
//    private SiteInfoModel transformModel(JSONObject jsonQueryString) throws JSONException{
//        SiteInfoModel info = new SiteInfoModel();
//        if(!jsonQueryString.isNull("siteKey")){
//            info.setKey(((String[])jsonQueryString.get("siteKey"))[0].toString());
//        }
//
//        if(!jsonQueryString.isNull("siteEmail")){
//            info.setEmail(((String[])jsonQueryString.get("siteEmail"))[0].toString());
//        }
//        
//        if(!jsonQueryString.isNull("siteHome")){
//            info.setHome(((String[])jsonQueryString.get("siteHome"))[0].toString());
//        }
//        
//        if(!jsonQueryString.isNull("siteName")){
//            info.setName(((String[])jsonQueryString.get("siteName"))[0].toString());
//        }
//        
//        if(!jsonQueryString.isNull("siteToken")){
//
//            boolean empty = StringUtils.isEmpty(((String[])jsonQueryString.get("siteToken"))[0].toString());
//            if(!((String[])jsonQueryString.get("siteToken"))[0].toString().equals("null") && !empty)
//                info.setToken(((String[])jsonQueryString.get("siteToken"))[0].toString());
//        }
//        
//        return info;
//    }
//    
//    @RequestMapping(value = "/plugin/register")
//    public ModelAndView register(@RequestParam(required=false) String callback,  HttpServletRequest request) {
//        
//        ModelAndView mav = null;
//        if(StringUtils.isEmpty(callback))
//            mav = new ModelAndView("mapper-result");
//        else {
//            mav = new ModelAndView("jsonp-mapper-result");
//            mav.addObject(
//                    "callback", 
//                    callback);
//        }
//        
//    	Map<String, Object> response = new HashMap<String, Object>();
//    	
//		Errors eajax = new AjaxErrors();
//    	
//    	
//    	//all we do here is gen the token and send it by email
//    	try {
//    	    
//    	    SiteInfoModel site = transformModel(new JSONObject(request.getParameterMap()));
//    	    
//    	    // validate input
//            if (StringUtils.isBlank(site.getHome())) {
//                //errors.add("Please provide the URL for your site");
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide the URL for your site."));
//            }
//            
//            if (StringUtils.isBlank(site.getName())) {
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide the name for your site."));
//            }
//            
//            if (StringUtils.isBlank(site.getEmail())) {
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.DATA_VALIDATION_MISSING, "Please provide a real email address."));
//            }
//
//            
//            if(StringUtils.isEmpty(site.getKey()) && StringUtils.isEmpty(site.getToken())){
//                String newkey = UUID.randomUUID().toString();
//                newkey = newkey.substring(newkey.lastIndexOf('-') + 1);
//                site.setKey(newkey);               
//                response.put("site", site);
//                response.put("subscriptionStatus", "KEY_ASSIGNED");               
//            } else if( eajax.getErrors().size()==0){
//                
//                Boolean verified = false;
//                if(site.getHome().equals(request.getHeader("referer"))){
//                    verified = true;
//                } 
//                
//                
//                
//                Publisher publisher = orderManager.processPublisherRegistration(
//                        site.getEmail(), 
//                        site.getKey(), 
//                        site.getToken(),
//                        site.getName(), 
//                        site.getHome(), 
//                        verified,
//                        adminEmailAccountFrom);
//                
//                site.setKey(publisher.getKey());
//                response.put("site", site);
//                if(publisher.getSubscriptionStatus().equals(Publisher.PublisherStatus.SUBSCRIBED)){
//                    response.put("key1", googleMapsApiKey);
//                }                
//                response.put("subscriptionStatus", publisher.getSubscriptionStatus());
//                
//            }
//
//			
//
//		} catch (JSONException e) {
//			logger.error("problem with request", e);
//			eajax.getErrors().add(
//					new AjaxError(AjaxError.REQ_PARSE_ERROR, "Problem parsing request."));
//
//		} catch (BLServiceException e) {
//		    logger.debug("problem with request"+e.getMessage()); 
//            for (BLMessage message : e.getMessages()) {
//                eajax.getErrors().add(
//                        new AjaxError(AjaxError.SUBSCRIPTION_INVALID, message.getMessageText()));
//                
//            }        
//        }
//		catch (Exception e) {
//			logger.error("problem with request", e);
//			eajax.getErrors().add(
//					new AjaxError(AjaxError.WL_SERVER_ERROR,
//							"Problem back at Welocally please report: "
//									+ e.getStackTrace()[0].toString().replace(
//											".", " ")));
//		}
//		 //if errors send them       
//        try {
//            if(eajax.getErrors().size()>0){
//                mav.addObject("mapperResult", jsonObjectSerializer.serialize(eajax));
//            } else {
//                mav.addObject("mapperResult", jsonObjectSerializer.serialize(response));
//            }
//            
//            
//        } catch (IOException e) {
//            mav.addObject("mapperResult", makeErrorsJson(e));   
//        }
//   
//        return mav;
//    }
    
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
