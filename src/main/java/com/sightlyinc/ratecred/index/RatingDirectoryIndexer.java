package com.sightlyinc.ratecred.index;

import com.sightlyinc.ratecred.model.Rating;

public interface RatingDirectoryIndexer {

	public abstract void indexRate(Rating rating);
	public abstract void removeRate(Rating rating);
	

}