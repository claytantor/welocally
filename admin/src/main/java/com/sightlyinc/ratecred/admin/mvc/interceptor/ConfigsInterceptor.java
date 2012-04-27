package com.sightlyinc.ratecred.admin.mvc.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sightlyinc.ratecred.admin.model.Configuration;

public class ConfigsInterceptor extends HandlerInterceptorAdapter{
 
    private static final Logger logger = Logger.getLogger(ConfigsInterceptor.class);
    
    @Autowired Configuration configuration;
 
    //before the actual handler will be executed
    public boolean preHandle(HttpServletRequest request, 
        HttpServletResponse response, Object handler)
        throws Exception {
        
        request.setAttribute("config", configuration);
 
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        String redirect = "https://www.facebook.com/dialog/oauth/?" +
                "client_id=fb_app_id&" +
                "redirect_uri=YOUR_REDIRECT_URI&" +
                "state=SOME_ARBITRARY_BUT_UNIQUE_STRING";
                
        redirect = redirect.replaceAll("fb_app_id","212416905466586");       
        redirect = redirect.replaceAll("YOUR_REDIRECT_URI","http://gaudi.welocally.com/admin/facebook/1_0/auth");
        redirect = redirect.replaceAll("SOME_ARBITRARY_BUT_UNIQUE_STRING",key.substring(0, 8));
        
        request.setAttribute("facebookRedirect", redirect);
        
        request.setAttribute("iframeTrackerCode", configuration.getIframeTrackerCode());
        
        
        
        return true;
    }
 
}
