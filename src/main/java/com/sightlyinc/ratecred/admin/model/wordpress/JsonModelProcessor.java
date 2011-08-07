package com.sightlyinc.ratecred.admin.model.wordpress;

import java.util.List;

import org.json.JSONObject;

import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Publisher;

public interface JsonModelProcessor {

	public abstract List<Event> saveJsonEventsAsPostsForPublisher(
			JSONObject obj, Publisher p);

}