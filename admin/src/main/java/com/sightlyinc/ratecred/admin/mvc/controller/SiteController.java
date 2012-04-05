package com.sightlyinc.ratecred.admin.mvc.controller;


import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.model.Site;
import com.sightlyinc.ratecred.service.PublisherService;
import com.sightlyinc.ratecred.service.SiteService;

@Controller
@RequestMapping(value="/site")
public class SiteController {
		
	static Logger logger = Logger.getLogger(SiteController.class);

	@Autowired
	private PublisherService publisherService;
	
	@Autowired
	private SiteService siteService;
	
    	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(@RequestParam Long publisherId, Model model) { 
		Site newSite= new Site();
		Publisher publisheToCreateSiteFor = publisherService.findByPrimaryKey(publisherId);
		newSite.setPublisher(publisheToCreateSiteFor);
		model.addAttribute("siteForm", newSite);
		return "site/edit";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(@Valid Site form, BindingResult result, Model model) {
		Site site = new Site();
		try {	
			
			if(form.getId() != null)
				site = siteService.findByPrimaryKey(form.getId());
			
			if(site!=null){
				site.setPublisher(form.getPublisher());
				site.setName(form.getName());
				site.setActive(form.getActive());
				site.setVerified(form.getVerified());
				site.setDescription(form.getDescription());
				site.setNotes(form.getNotes());
				site.setUrl(form.getUrl());
				
				
				Long id = siteService.saveSiteWithChecks(site);
				return "redirect:/site/"+id.toString();
			}
		
		} 
		catch (BLServiceException e) {
            logger.debug("problem with request"+e.getMessage()); 
            model.addAttribute("error", e);
            model.addAttribute("siteForm",form);
            return "site/edit";
        }
		catch(Exception e){
			logger.error("", e);
			return "error";
		}
		return "home";
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getById(@PathVariable Long id, Model model) {
		logger.debug("view");
		model.addAttribute("site", siteService.findByPrimaryKey(id));
		return "site/view";
	}

	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String edit(@PathVariable Long id, Model model) {
		logger.debug("edit");
		model.addAttribute(
				"siteForm",
				siteService.findByPrimaryKey(id));
		return "site/edit";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String delete(@PathVariable Long id, Model model) {
		logger.debug("delete");
		Site p = siteService.findByPrimaryKey(id);
		Long publisherId = p.getPublisher().getId();
		
		siteService.delete(p);
		return "redirect:/publisher/"+publisherId;
	}
	
	
}
