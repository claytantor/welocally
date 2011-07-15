package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.model.Event;

public interface EventService extends BaseService<Event> {
	
//	public List<Event> findByUserPrincipal(UserPrincipal up) ;

    public Event findByUrl(String url);
}
