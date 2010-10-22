package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.client.link.Link;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;

public interface LinkManagerService {
	
	public List<Link> getLinks() throws BLServiceException;
	
	public List<Link> getLinksForPlace(Place p) throws BLServiceException;
	
	public List<Link> getLinksForCityState(PlaceCityState pcs) throws BLServiceException;
	

}
