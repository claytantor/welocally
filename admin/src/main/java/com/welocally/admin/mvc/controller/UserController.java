package com.welocally.admin.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.welocally.admin.domain.Index;
import com.welocally.admin.mvc.model.WorksheetPublishForm;
import com.welocally.admin.security.UserPrincipal;
import com.welocally.admin.security.UserPrincipalService;
import com.welocally.admin.security.UserPrincipalServiceException;
import com.welocally.admin.service.IndexService;
import com.welocally.admin.spreadsheet.client.GoogleOAuthSpreadsheetClient;
import com.welocally.admin.spreadsheet.template.SpreadsheetTransformationUtils;


@Controller
@RequestMapping(value="/user")
@Scope("request")
public class UserController extends AbstractAuthicatedController {
       
    private static final Logger logger = Logger.getLogger(UserController.class);
    
    @Value("${google.oauth2.callbackPrefix}")
    private String callbackPrefix;
    
    @Autowired GoogleOAuthSpreadsheetClient authService;
    
    @Autowired SpreadsheetTransformationUtils spreadsheetTransformationUtils;
     
    @Autowired IndexService indexService;
     
    
    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String home(Model model) {
        
        logger.debug("home");
        
        //see if the user is a member
        UserDetails user = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        model.addAttribute("user", user);    
        
        return "home";
    }
    
    @RequestMapping(value="/spreadsheet", method=RequestMethod.GET)
    public String spreadsheet(
            @RequestParam String key, 
            @RequestParam(value = "oauth_verifier", required = false) String oauth_verifier,
            Model model, 
            HttpServletRequest request, 
            HttpServletResponse resp) {
        
        logger.debug("spreadsheet auth:"+authService.toString());
             
        try {
            //see if the user is a member
            UserDetails user = 
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

             
            UserPrincipal up = userPrincipalService.findByUserName(user.getUsername());
            if(up != null)
            {
                authService.setUserAuthInfo(up);
                saveAuthInfo(up);
            }
                       
            Token accessToken = authService.getAccessToken();
            if (accessToken == null) {                      
                //there is a better way
                String callbackURL = callbackPrefix+"/user/spreadsheet?key="+key;
                
                if (oauth_verifier == null) {
                    String authUrl = authService.getOAuthorizationURL(callbackURL);                
                    return "redirect:" + authUrl;
                } else {
                    authService.authorizeWith(oauth_verifier);
                    return "redirect:" + callbackURL;
                }
            }
                      
            model.addAttribute("spreadsheet", authService.retrieveSpreadsheetFeed(key));

            
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("could not find user", e);
            throw new RuntimeException(e);
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
      
        return "spreadsheet";
              
    }
    
    @RequestMapping(value="/worksheet", method=RequestMethod.GET)
    public String worksheet(
            @RequestParam String url,
            @RequestParam(value = "oauth_verifier", required = false) String oauth_verifier,
            Model model, 
            HttpServletRequest request, 
            HttpServletResponse resp) {
        
        logger.debug("worksheet auth:"+authService.toString());
             
        try {
            //see if the user is a member
            UserDetails user = 
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

             
            UserPrincipal up = userPrincipalService.findByUserName(user.getUsername());
            if(up != null)
            {
                authService.setUserAuthInfo(up);
                saveAuthInfo(up);
            }
                       
            Token accessToken = authService.getAccessToken();
            if (accessToken == null) {                      
                //there is a better way
                String callbackURL = callbackPrefix+"/user/worksheet?url="+url;
                
                if (oauth_verifier == null) {
                    String authUrl = authService.getOAuthorizationURL(callbackURL);                
                    return "redirect:" + authUrl;
                } else {
                    authService.authorizeWith(oauth_verifier);
                    return "redirect:" + callbackURL;
                }
            }
                                
            model.addAttribute("worksheet", 
                    spreadsheetTransformationUtils.makeWorksheetGridFromWorksheetCellFeed(
                            authService.retrieveWorksheetCellFeed(url)));

            
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("could not find user", e);
            throw new RuntimeException(e);
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
      
        return "worksheet";
              
    }
    

    
    private void saveAuthInfo(UserPrincipal up) throws ServletException, IOException, UserPrincipalServiceException{
        
        String authT = null;
        String authS = null;
        String accessT = null;
        String accessS = null;
        
        if(authService.getAuthToken() != null)
        {
            authT = authService.getAuthToken().getToken();
            authS = authService.getAuthToken().getSecret();
        }
        
        if(authService.getAccessToken() != null)
        {
            accessT = authService.getAccessToken().getToken();
            accessS = authService.getAccessToken().getSecret();
        }
                       
        userPrincipalService.saveAuthInfo(
                up, 
                authService.getKey(), 
                authService.getAuthUrl(),
                authT, 
                authS,
                accessT,
                accessS);
    }
    
    
    



   

}
