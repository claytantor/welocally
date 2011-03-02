package com.sightlyinc.ratecred.service;

import java.util.List;

import com.sightlyinc.ratecred.client.offers.Offer;

public interface OfferPoolService {

	public abstract void refresh();

	public abstract List<Offer> getOfferPool();
	
	public abstract Offer getOfferByExternalIdSource(String externalId, String sourceName);
	
	public void addOffersToPool(List<Offer> offer);

}