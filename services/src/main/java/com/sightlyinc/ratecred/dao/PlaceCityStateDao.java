package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PlaceCityState;

public interface PlaceCityStateDao {
	public List<PlaceCityState> findAll();	
	public List<PlaceCityState> findMostRatedOrdered(int pageNum, int pageSize);	
	public List<PlaceCityState> findByRater(Patron t);		
}
