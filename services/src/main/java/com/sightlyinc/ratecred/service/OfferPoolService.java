package com.sightlyinc.ratecred.service;

import java.util.List;

import com.sightlyinc.ratecred.client.offers.OfferOld;

public interface OfferPoolService {

	public abstract void refresh();

	public abstract List<OfferOld> getOfferPool();
	
	public abstract OfferOld getOfferByExternalIdSource(String externalId, String sourceName);
	
	public void addOffersToPool(List<OfferOld> offer);

}