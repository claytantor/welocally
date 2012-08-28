package com.welocally.admin.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.welocally.admin.security.UserPrincipal;
import com.welocally.admin.security.UserPrincipalService;
import com.welocally.admin.security.UserPrincipalServiceException;

public abstract class AbstractAuthicatedController {
    @Autowired UserPrincipalService userPrincipalService;
    
    /** Returns the user ID for the given HTTP servlet request. */
    protected  String getUsername() throws ServletException, IOException{
        UserDetails user = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }
    
    /** Returns the user ID for the given HTTP servlet request. 
     * @throws UserPrincipalServiceException */
    protected  UserPrincipal getUserPrincipal() throws ServletException, IOException, UserPrincipalServiceException{
        UserDetails user = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipalService.findByUserName(user.getUsername());
    }

}
