package com.sightlyinc.ratecred.admin.model.wordpress;

import java.util.List;

import org.json.JSONObject;

import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;
import com.simplegeo.client.types.Feature;

public interface JsonModelProcessor {

	public abstract List<Event> saveJsonEventsAsPostsForPublisher(
			JSONObject obj, Publisher p);

    public void saveEventAndPlaceFromPostJson(JSONObject jsonObject, Publisher publisher, String status);
    public void saveArticleAndPlaceFromPostJson(JSONObject jsonObject, Publisher publisher, String status);
    public Feature saveNewPlaceAsFeatureFromPostJson(JSONObject jsonObject);
    public Place saveNewPlaceAsFromPostJson(JSONObject requestJSONObject);
    
    public boolean isEventPost(JSONObject jsonPost);
    public boolean isArticlePost(JSONObject jsonPost);
    
}