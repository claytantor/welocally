package com.sightlyinc.ratecred.client.geo;

import com.sightlyinc.ratecred.model.Place;

public interface GeoPersistable {
	
	public String getGeoRecordId() throws GeoPersistenceException;
	
	public Place getGeoPlace() throws GeoPersistenceException;
	
	public String getMemberKey() throws GeoPersistenceException;
	
	public Long getExpiration() throws GeoPersistenceException; 
	
}
