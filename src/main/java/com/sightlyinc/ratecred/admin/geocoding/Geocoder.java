package com.sightlyinc.ratecred.admin.geocoding;

import com.sightlyinc.ratecred.model.Location;


public interface Geocoder {
	
	public Location geocode(String fullAddress) throws GeocoderException;

}
