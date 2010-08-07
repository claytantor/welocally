package com.sightlyinc.ratecred.index;

import com.sightlyinc.ratecred.model.Place;

public interface PlaceDirectoryIndexer {

	public abstract void indexPlace(Place place);
	public abstract void removePlace(Place place);

}