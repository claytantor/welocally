package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.PlaceRating;

public interface PlaceRatingDao {
	
	public List<PlaceRating> findByCityStateTypePaged(
			String city,  String state, String type,
			int pageNum,  int pageSize,   String sortField,  boolean isAscending);
	
	public List<PlaceRating> findByCityStatePaged(
			String city,  String state, 
			int pageNum,  int pageSize, boolean isAscending);
	
	public List<PlaceRating> findByTypePaged(
			String type,
			int pageNum,  int pageSize,    boolean isAscending);
	
	public List<PlaceRating> findAllPaged(
			int pageNum,  int pageSize,    boolean isAscending);
	
	public Long findByCityStateCount(String city, String state);
	
	
}
