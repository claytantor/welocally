package com.sightlyinc.ratecred.admin.mvc.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sightlyinc.ratecred.authentication.UserNotFoundException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalService;
import com.sightlyinc.ratecred.authentication.UserPrincipalServiceException;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.EventService;
import com.sightlyinc.ratecred.service.PublisherService;

@Controller
@RequestMapping("publisher/event")
public class EventController {	
	
	static Logger logger = Logger.getLogger(EventController.class);


	@Autowired
	private EventService eventService;
	
	@Autowired
	private PublisherService publisherService;
		
	
	@RequestMapping(method=RequestMethod.GET)
	public String addEvent(@RequestParam Long publisherId, Model model) {
    	Publisher publisher = publisherService.findByPrimaryKey(publisherId);
		model.addAttribute("publisher", publisher);
		Event e = new Event();
		e.setPublisher(publisher);
		model.addAttribute("eventForm",e);
		return "event/edit";
	}

    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editEvent(@PathVariable Long id, Model model) {
        logger.debug("edit");
        Event e = eventService.findByPrimaryKey(id);
        model.addAttribute("publisher", e.getPublisher());
		model.addAttribute("eventForm", e);	
        return "event/edit";
    }

	@RequestMapping(method=RequestMethod.POST)
	public String saveEvent(@Valid Event event) {
		logger.debug("got post action");
        Long id = eventService.save(event);
        return "redirect:/publisher/event/"+id.toString();

	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String getEventById(@PathVariable Long id, Model model) {
		logger.debug("view");
        Event e = eventService.findByPrimaryKey(id);
        model.addAttribute("publisher", e.getPublisher());
		model.addAttribute("event", e);	
		return "event/view";
	}

	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deleteEvent(@PathVariable Long id) {
		logger.debug("delete");
		Event event = eventService.findByPrimaryKey(id);
		eventService.delete(event);
		return "redirect:/publisher/event/list";
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(@RequestParam Long publisherId, Model model) {
		logger.debug("list");		
		Publisher publisher = publisherService.findByPrimaryKey(publisherId);
		model.addAttribute("publisher", publisher);
		model.addAttribute("events", publisher.getEvents());
		return "event/list";
	}

}
