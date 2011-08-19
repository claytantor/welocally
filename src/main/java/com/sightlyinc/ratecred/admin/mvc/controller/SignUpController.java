package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.admin.model.UserPrincipalForm;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

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

    public void setUserService(UserPrincipalService userService) {
        this.userService = userService;
    }
}
