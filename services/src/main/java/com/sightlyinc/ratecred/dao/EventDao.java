package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Event;

public interface EventDao extends BaseDao<Event> {

    public Event findByUrl(String url);
}
