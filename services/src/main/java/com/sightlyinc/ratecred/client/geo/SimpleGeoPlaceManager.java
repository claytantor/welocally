package com.sightlyinc.ratecred.client.geo;

import com.sightlyinc.ratecred.model.Place;
import com.simplegeo.client.types.Feature;

public interface SimpleGeoPlaceManager {
	
	public void transformFeature(Feature f, Place p);

}
