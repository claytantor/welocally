package com.sightlyinc.ratecred.client.geo;

import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.pojo.Events;
import com.simplegeo.client.types.Feature;

public interface GeoEventClient {
	public Event findById(String id);
	public Events findEvents(Double lat, Double lng, Float radiusInKm, String sourceId);
	public void publishEvents(Events e, String sourceId);
	public Event makeEventFromFeature(Feature f,
			String name,
			String status,
			String url,
			String phone,
			String placeName,	
			String description,
			String address,	
			String city,
			String state,	
			Long timeStarts,	
			Long timeEnds,
			Publisher pub) ;
}
