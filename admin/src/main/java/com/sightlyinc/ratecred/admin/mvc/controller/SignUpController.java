package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.model.UserPrincipalForm;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.Site;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.OrderService;
import com.sightlyinc.ratecred.service.PublisherService;

/**
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/signup")
public class SignUpController {

    private static final Logger LOGGER = Logger.getLogger(SignUpController.class);

    @Autowired
    private UserPrincipalService userService;
    

    @Autowired
    private PublisherService publisherService;
    

    @Autowired
    private NetworkMemberService networkMemberService;
    
	@Autowired
	private OrderService orderManagerService;
	
    
    @Value("${signUpController.paypal.trailButtonKey:VC6W4WB2VLHAE}")
    private String trialButtonKey;
    
    @Value("${signUpController.paypal.nonTrailButtonKey:CYUU2TQ7EJYFY}")
    private String nonTrialButtonKey;
    
    @Value("${signUpController.paypal.yearPayKey:BRT4H8JZQ56RN}")
    private String yearPayKey;
    
    @Value("${paypal.callback.endpoint:https://www.sandbox.paypal.com/cgi-bin/webscr}")
    private String paypalFormEndpoint;
    

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
                LOGGER.error(e);
                e.printStackTrace();
                errors.reject("error", "Something went wrong saving your account information, please try again");
            }
        }

        return viewName;
    }
    
    @RequestMapping(value="/plugin/login", method=RequestMethod.GET)
	public String homePublisher(@RequestParam String siteKey, @RequestParam String siteToken, HttpServletRequest request, Model model) {
    	LOGGER.debug("home");
		
			
		// Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(siteKey, siteToken);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authentication = ((AuthenticationProvider)userService).authenticate(token);
        LOGGER.debug("Logging in with [{}]"+ authentication.getPrincipal());
        SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/home";

	}

    @RequestMapping(value = "/plugin/key")
    @ResponseBody
    public Map<String, Object> signUpFromPluginForKey(@RequestBody String requestJson) {
        Map<String, Object> response = new HashMap<String, Object>();
        List<String> errors = new ArrayList<String>();

        String email = null;
        String siteUrl = null;
        String siteName = null;
        String description = null;
        String iconUrl = null;

        try {
            JSONObject jsonObject = new JSONObject(requestJson);

            email = jsonObject.getString("siteEmail");
            siteUrl = jsonObject.getString("siteHome");
            siteName = jsonObject.getString("siteName");
            description = jsonObject.getString("siteDescription");
            iconUrl = jsonObject.getString("iconUrl");
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add("Unable to parse request, please check the format of your data");
        }

        if (errors.isEmpty()) {
            // validate input
            if (StringUtils.isBlank(siteUrl)) {
                errors.add("Please provide the URL for your site");
            }
            if (StringUtils.isBlank(siteName)) {
                errors.add("Please provide the name for your site");
            }
            if (StringUtils.isBlank(email)) {
                errors.add("Please provide a real email address");
            }

            String key = UUID.randomUUID().toString();
            key = key.substring(key.lastIndexOf('-') + 1);
            
            if (errors.isEmpty()) {
                // TODO move all this logic into a service method
                UserPrincipal user = userService.findUserByEmail(email);
                if (user == null) {
                    user = new UserPrincipal();
                    user.setEmail(email);
                    user.setPassword(Long.toString(System.currentTimeMillis()));
                    user.setUsername(key);
                    user.setEnabled(false);
                    try {
                        userService.saveUserPrincipal(user);
                        // not going to worry about roles for now since user isn't going to be logging in
                    } catch (UserPrincipalServiceException e) {
                        e.printStackTrace();
                        errors.add("Unable to create user, please try again");
                    }
                }
                
                if (errors.isEmpty()) {
                    Publisher publisher = publisherService.findBySiteUrl(siteUrl);
                    if (publisher == null) {
                        publisher = new Publisher();
                        publisher.setUserPrincipal(user);
                        
                        publisher.setIconUrl(iconUrl);
                        
                        Site s = new Site();
                        s.setName(siteName);
                        s.setDescription(description);
                        s.setUrl(siteUrl);
                        s.setActive(true);
                        s.setVerified(false);
                        
                        
                        Set<Site> sites = new HashSet<Site>();
                        sites.add(s);
                        publisher.setSites(sites);
                        
                        publisher.setName(siteName);
                        
                        //publisher.setUrl(siteUrl);
                        //publisher.setSiteName(siteName);
                        publisher.setDescription(description);
                        NetworkMember defaultNetworkMember = networkMemberService.getDefaultNetworkMember();
                        publisher.setNetworkMember(defaultNetworkMember);


                        publisher.setKey(key);
                        publisher.setSubscriptionStatus(Publisher.PublisherStatus.KEY_ASSIGNED);

                        //we usta set the token here and the service end date, moving that 
                        //to the notification controller 
                        
                        if (errors.isEmpty()) {
                            publisherService.save(publisher);
                        }
                    }
                    
                    if (errors.isEmpty()) {
                    	
                        // publisher key, simplegeo token, trial end date
                        response.put("key", publisher.getKey());
                        response.put("token", publisher.getJsonToken());
                        response.put("subscriptionStatus", publisher.getSubscriptionStatus());
                        response.put("serviceEndDateMillis", publisher.getServiceEndDateMillis());
                        response.put("paypalFormEndpoint", paypalFormEndpoint);
                        
                        //check for existing orders, dont let people repeat trails
                        List<Order> publisherOrders = 
                        	orderManagerService.findOrderByPublisherKey(publisher.getKey());
                        
                        if(publisher.getSubscriptionStatus().equals("SUBSCRIPTION FAILURE")
                    			|| publisher.getSubscriptionStatus().equals("SUSPENDED")){
                    		response.put("buttonToken", yearPayKey);
                    	} else if(publisher.getSubscriptionStatus().equals("CANCELLED")){
                        		response.put("buttonToken", nonTrialButtonKey);
                     	
                        } else {
                        	response.put("buttonToken", trialButtonKey);
                        }  
                        
                    }
                }
            }
        }

        response.put("errors", errors);

        return response;
    }
    


    public void setUserService(UserPrincipalService userService) {
        this.userService = userService;
    }
}
