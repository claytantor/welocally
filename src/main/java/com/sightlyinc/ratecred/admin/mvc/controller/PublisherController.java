package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sightlyinc.ratecred.admin.model.PublisherForm;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.PublisherService;

@Controller
@RequestMapping(value="/admin/publisher")
public class PublisherController {
	
	
	static Logger logger = Logger.getLogger(PublisherController.class);


	@Autowired
	private PublisherService publisherService;

	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("publisherForm",new PublisherForm());
		return "publisher/edit";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid PublisherForm form, BindingResult result, Model model) {
		logger.debug("got post action");
		Publisher p = new Publisher();
		
		if(form.getId() != null)
			p = publisherService.findByPrimaryKey(form.getId());
		
		if(p!= null)
		{
			p.setMonthlyPageviews(form.getMonthlyPageviews());
			p.setSiteName(form.getSiteName());
			p.setUrl(form.getUrl());
			Long id = publisherService.save(p);
			return "redirect:/admin/publisher/"+id.toString();
		} else {
			model.addAttribute("publisher", p);
			return "publisher/edit";
		}

	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getPublisherById(@PathVariable Long id, Model model) {
		logger.debug("view");
		model.addAttribute("publisher", publisherService.findByPrimaryKey(id));
		return "publisher/view";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String editPublisher(@PathVariable Long id, Model model) {
		logger.debug("edit");
		model.addAttribute("publisher", publisherService.findByPrimaryKey(id));
		model.addAttribute("publisherForm",new PublisherForm());
		return "publisher/edit";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deletePublisher(@PathVariable Long id, Model model) {
		logger.debug("delete");
		Publisher p = publisherService.findByPrimaryKey(id);
		publisherService.delete(p);
		return "publisher/list";
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String getList(Model model) {
		logger.debug("list");
		model.addAttribute("publishers", publisherService.findAll());
		return "publisher/list";
	}

}
