package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.AffiliateLink;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;

public interface LinkManagerService {
	
	public List<AffiliateLink> getLinks() throws BLServiceException;
	
	public List<AffiliateLink> getLinksForPlace(Place p) throws BLServiceException;
	
	public List<AffiliateLink> getLinksForCityState(PlaceCityState pcs) throws BLServiceException;
	

}
