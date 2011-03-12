package com.sightlyinc.ratecred.admin.compare;

import java.util.Comparator;

import com.sightlyinc.ratecred.client.offers.Offer;

public class OfferScoreComparitor implements Comparator<Offer> {

	@Override
	public int compare(Offer o1, Offer o2) {
		return o2.getScore().compareTo(o1.getScore());
	}

}
