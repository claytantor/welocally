package com.sightlyinc.ratecred.pojo;

import java.util.List;

import com.sightlyinc.ratecred.model.Event;

/*non transient type*/
public class Events {
	
	private List<Event> events;

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
}
