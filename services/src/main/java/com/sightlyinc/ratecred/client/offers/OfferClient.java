package com.sightlyinc.ratecred.client.offers;

import java.util.List;

public interface OfferClient {
	
	public List<OfferOld> getOffers() throws OfferFeedException;

}
