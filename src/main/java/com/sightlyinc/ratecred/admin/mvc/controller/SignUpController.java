package com.sightlyinc.ratecred.admin.mvc.controller;

import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.model.UserPrincipalForm;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.dao.SimpleGeoJsonTokenDao;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.SimpleGeoJsonToken;
import com.sightlyinc.ratecred.service.NetworkMemberService;
import com.sightlyinc.ratecred.service.PublisherService;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private SimpleGeoJsonTokenDao simpleGeoJsonTokenDao;
    @Autowired
    private NetworkMemberService networkMemberService;

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
            if (StringUtils.isBlank(description)) {
                errors.add("Please provide the description of your site");
            }
            if (StringUtils.isBlank(email)) {
                errors.add("Please provide your email address");
            }

            if (errors.isEmpty()) {
                // TODO move all this logic into a service method
                UserPrincipal user = userService.findUserByEmail(email);
                if (user == null) {
                    user = new UserPrincipal();
                    user.setEmail(email);
                    user.setPassword(Long.toString(System.currentTimeMillis()));
                    user.setUsername(email);
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
                        publisher.setUrl(siteUrl);
                        publisher.setSiteName(siteName);
                        publisher.setDescription(description);
                        NetworkMember defaultNetworkMember = networkMemberService.getDefaultNetworkMember();
                        publisher.setNetworkMember(defaultNetworkMember);
                        // TODO put this UUID logic in its own method in a service class somewhere
                        String key = UUID.randomUUID().toString();
                        key = key.substring(key.lastIndexOf('-') + 1);
                        publisher.setKey(key);
                        publisher.setSubscriptionStatus("KEY_ASSIGNED");

                        //we usta set the token here and the service end date, moving that 
                        //to the notification controller 
                        
                        if (errors.isEmpty()) {
                            publisherService.save(publisher);
                        }
                    }
                    if (errors.isEmpty()) {
                        // publisher key, simplegeo token, trial end date
                        response.put("key", publisher.getKey());
                        response.put("token", publisher.getSimpleGeoJsonToken());
                        response.put("serviceEndDateMillis", publisher.getServiceEndDateMillis());
                    }
                }
            }
        }

        response.put("errors", errors);

        return response;
    }
    
    
    @RequestMapping(value = "/plugin/token")
    @ResponseBody
    public Map<String, Object> getPublisherToken(@RequestBody String requestJson) {
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
            if (StringUtils.isBlank(description)) {
                errors.add("Please provide the description of your site");
            }
            if (StringUtils.isBlank(email)) {
                errors.add("Please provide your email address");
            }

            if (errors.isEmpty()) {
                // TODO move all this logic into a service method
                UserPrincipal user = userService.findUserByEmail(email);
                if (user == null) {
                    user = new UserPrincipal();
                    user.setEmail(email);
                    user.setPassword(Long.toString(System.currentTimeMillis()));
                    user.setUsername(email);
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
                        publisher.setUrl(siteUrl);
                        publisher.setSiteName(siteName);
                        publisher.setDescription(description);
                        NetworkMember defaultNetworkMember = networkMemberService.getDefaultNetworkMember();
                        publisher.setNetworkMember(defaultNetworkMember);
                        // TODO put this UUID logic in its own method in a service class somewhere
                        String key = UUID.randomUUID().toString();
                        key = key.substring(key.lastIndexOf('-') + 1);
                        publisher.setKey(key);

                        //we usta set the token here and the service end date, moving that 
                        //to the notification controller 
                        
                        if (errors.isEmpty()) {
                            publisherService.save(publisher);
                        }
                    }
                    if (errors.isEmpty()) {
                        // publisher key, simplegeo token, trial end date
                        response.put("key", publisher.getKey());
                        response.put("token", publisher.getSimpleGeoJsonToken());
                        response.put("serviceEndDateMillis", publisher.getServiceEndDateMillis());
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
