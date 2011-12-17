package com.sightlyinc.ratecred.admin.geocoding;

import com.sightlyinc.ratecred.pojo.Location;


public interface Geocoder {
	
	public Location geocode(String fullAddress) throws GeocoderException;

}
