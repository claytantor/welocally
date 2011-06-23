package com.sightlyinc.ratecred.client.geo;

import com.sightlyinc.ratecred.model.Place;
import com.simplegeo.client.types.Feature;

public interface SimpleGeoPlaceManager {
	
	public Place trasformFeature(Feature f);

}
