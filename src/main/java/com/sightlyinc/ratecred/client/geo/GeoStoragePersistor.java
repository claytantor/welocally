package com.sightlyinc.ratecred.client.geo;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;

public interface GeoStoragePersistor {
	
	public void saveGeoEntityToStorage(GeoPersistable geoEntity) throws 
	    JsonGenerationException, 
	    JsonMappingException, 
	    IOException, 
	    JSONException, 
	    GeoPersistenceException ;
	
	 public void createLayersForKey(String key) throws IOException, JSONException;

}
