package com.welocally.admin.mvc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.welocally.admin.domain.Index;
import com.welocally.admin.mvc.model.GeodbIndexModel;
import com.welocally.admin.mvc.model.WorksheetPublishForm;
import com.welocally.admin.security.UserPrincipal;
import com.welocally.admin.security.UserPrincipalServiceException;
import com.welocally.admin.service.IndexService;
import com.welocally.admin.spreadsheet.client.GoogleOAuthSpreadsheetClient;
import com.welocally.admin.spreadsheet.template.SpreadsheetTransformationUtils;
import com.welocally.admin.spreadsheet.template.WorksheetGrid;


@Controller
@RequestMapping(value="/index")
@Scope("request")
public class IndexController extends AbstractAuthicatedController {
       
    private static final Logger logger = Logger.getLogger(IndexController.class);
    
    @Autowired IndexService indexService;
    @Autowired SpreadsheetTransformationUtils spreadsheetTransformationUtils;
    @Autowired GoogleOAuthSpreadsheetClient authService;
     
    
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String home(Model m) {
        try {
            UserPrincipal up = super.getUserPrincipal();           
            List<Index> indexes = indexService.findAllByOwner(up);
            m.addAttribute("indexes", indexes);
            return "index/list";
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
    }
    
    @RequestMapping(value="/detail", method=RequestMethod.GET)
    public @ResponseBody GeodbIndexModel detail(@RequestParam String id, Model m){
        
        try {
            UserPrincipal up = super.getUserPrincipal();
            
            if(up != null)
                authService.setUserAuthInfo(up);
            
            //try to find the index
            Index index = indexService.findByWorksheetFeed(id);
            
            //get the feed
            WorksheetGrid grid = spreadsheetTransformationUtils.makeWorksheetGridFromWorksheetCellFeed(
                    authService.retrieveWorksheetCellFeed(index.getWorksheetFeed()));
                     
            return spreadsheetTransformationUtils.makeIndexModel(grid, index);
            
            
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
        
    
    }
    
    @RequestMapping(value="/publish", method=RequestMethod.POST)
    public String publish(@ModelAttribute("worksheetForm")
            WorksheetPublishForm form, BindingResult result, Model m){
        try {
            UserPrincipal up = super.getUserPrincipal();
            
            if(up != null)
                authService.setUserAuthInfo(up);
            
            //get the feed
            WorksheetGrid grid = spreadsheetTransformationUtils.makeWorksheetGridFromWorksheetCellFeed(
                    authService.retrieveWorksheetCellFeed(form.getFeedUrl()));
            
                       
            //try to find the index
            Index index = indexService.findByWorksheetFeed(form.getFeedUrl());
            if(index == null){
                index = new Index(); 
                index.setWorksheetFeed(form.getFeedUrl());
                index.setName(form.getName());
                index.setOwner(up);     
                
            }
            index.setPrimaryKey(form.getPrimaryKey());
            index.setSearchFields(form.getSearchFields());
            index.setLat(form.getLat());
            index.setLng(form.getLng());
            indexService.save(index);
            
            
            
            List<Index> indexes = indexService.findAllByOwner(up);
            m.addAttribute("indexes", indexes);
            return "index/list";
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
    }
    
    
    /*
     * try {
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
     */
   

}
