package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.service.EventService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/event")
public class EventController {
	
	
	static Logger logger = Logger.getLogger(EventController.class);


	@Autowired
	private EventService eventService;

	
	@RequestMapping(method=RequestMethod.GET)
	public String addEvent(Model model) {
		model.addAttribute("eventForm",new Event());
		return "event/edit";
	}

    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editEvent(@PathVariable Long id, Model model) {
        logger.debug("edit");
        model.addAttribute("eventForm", eventService.findByPrimaryKey(id));
        return "event/edit";
    }

	@RequestMapping(method=RequestMethod.POST)
	public String saveEvent(@Valid Event event) {
		logger.debug("got post action");

        Long id = eventService.save(event);
        return "redirect:/admin/event/"+id.toString();

	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String getEventById(@PathVariable Long id, Model model) {
		logger.debug("view");
		model.addAttribute("event", eventService.findByPrimaryKey(id));
		return "event/view";
	}

	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deleteEvent(@PathVariable Long id) {
		logger.debug("delete");
		Event event = eventService.findByPrimaryKey(id);
		eventService.delete(event);
		return "redirect:/admin/event/list";
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(Model model) {
		logger.debug("list");
		model.addAttribute("events", eventService.findAll());
		return "event/list";
	}

}
