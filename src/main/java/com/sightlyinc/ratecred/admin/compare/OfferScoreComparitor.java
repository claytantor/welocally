package com.sightlyinc.ratecred.admin.compare;

import java.util.Comparator;

import com.sightlyinc.ratecred.client.offers.OfferOld;

public class OfferScoreComparitor implements Comparator<OfferOld> {

	@Override
	public int compare(OfferOld o1, OfferOld o2) {
		return o2.getScore().compareTo(o1.getScore());
	}

}
