package com.sightlyinc.ratecred.client.geo;

import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Events;

public interface GeoEventClient {
	public Event findById(String id);
	public Events findEvents(Double lat, Double lng, Float radiusInKm, String sourceId);
	public void publishEvents(Events e, String sourceId);
}
