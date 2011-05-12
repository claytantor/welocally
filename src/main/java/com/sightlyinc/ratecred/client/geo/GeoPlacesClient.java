package com.sightlyinc.ratecred.client.geo;

import java.util.List;

import com.sightlyinc.ratecred.model.Place;

public interface GeoPlacesClient {
	public List<Place> findPlaces(double lat, double lon, double radiusInKMeters);
    public Place findById(String id);
}
