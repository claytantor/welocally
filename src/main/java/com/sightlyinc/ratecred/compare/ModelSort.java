package com.sightlyinc.ratecred.compare;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.sightlyinc.ratecred.model.Rating;

public class ModelSort {
	
	private static ModelSort _instance;
	
	private ModelSort() {
		
	}
	
	public List<Rating> sortRating(Collection<Rating> unsorted)
	{
		List<Rating> sorted = new ArrayList<Rating>(unsorted);
		Collections.sort(sorted, new RatingComparator());
		return sorted;
	}
	
	public static ModelSort getInstance()
	{
		if(_instance == null)
			_instance = new ModelSort();
		
		return _instance;
	}

}
