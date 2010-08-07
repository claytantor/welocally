package com.sightlyinc.ratecred.compare;

import java.util.Comparator;

import com.sightlyinc.ratecred.model.Rating;

public class RatingComparator implements Comparator<Rating> {

	@Override
	public int compare(Rating t1, Rating t2) {		
		return t2.getTimeCreated().compareTo(t1.getTimeCreated());
	}

}
