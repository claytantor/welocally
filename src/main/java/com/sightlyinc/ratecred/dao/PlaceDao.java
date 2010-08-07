package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Place;

public interface PlaceDao {
	
	public List<Place> findByNamePrefix(String namePrefix);
	
	public List<Place> findByCityState(String city, String state);
	public List<Place> findByNameCityState(String name, String city, String state);
	
	public List<Place> findBySimillarNameCityState(String name, String city, String state);
	
	public List<Place> findByCityStateRatingTypePaged(
			 String city,  String state, String ratingType,
			int pageNum,  int pageSize,   String sortField,  boolean isAscending);
	
	public List<Place> findByGeoBounding(Double minLat, Double minLong,
			Double maxLat, Double maxLong);
	
	public Long findByCityStateCount(String city, String state);
	
	public Place findByPrimaryKey(Long id);	
	public List<Place> findByPrimaryKeys(List<Long> ids);	
	
	public List<Place> findAll();
	public void save(Place entity);
	public void delete(Place entity);
}
