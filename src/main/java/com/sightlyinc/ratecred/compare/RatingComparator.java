package com.sightlyinc.ratecred.compare;

import java.util.Comparator;

import com.sightlyinc.ratecred.model.Rating;

public class RatingComparator implements Comparator<Rating> {

	@Override
	public int compare(Rating t1, Rating t2) {	
		int val = 0;
		if(t2.getTimeCreatedMills() != null && t1.getTimeCreatedMills() != null)
			val= t2.getTimeCreatedMills().compareTo(t1.getTimeCreatedMills());
		else if(t2.getTimeCreated() != null && t1.getTimeCreated() != null)
			val = t2.getTimeCreated().compareTo(t1.getTimeCreated());
		else
			val= 0;
		return val;
		
	}

}
