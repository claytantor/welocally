package com.sightlyinc.ratecred.client.places;

import java.util.List;

import com.sightlyinc.ratecred.model.Place;

public interface LocationPlacesClient {
	public List<Place> findPlaces(double lat, double lon, double radiusInKMeters);

    public Place findById(String id);
}
