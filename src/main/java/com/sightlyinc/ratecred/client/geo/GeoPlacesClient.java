package com.sightlyinc.ratecred.client.geo;

import java.util.List;
import java.util.Map;

import com.sightlyinc.ratecred.model.Place;
import com.simplegeo.client.types.Feature;

public interface GeoPlacesClient {
	public List<Place> findPlaces(double lat, double lon, double radiusInKMeters);
    public Place findById(String id);
    public Map<String, Object> addPlace(Place place);
}
