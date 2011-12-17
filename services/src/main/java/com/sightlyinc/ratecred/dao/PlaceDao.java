package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Place;

public interface PlaceDao extends BaseDao<Place>{
	public Place findByTwitterId(String id);	

    public Place findBySimpleGeoId(String simpleGeoId);

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
	
	public List<Place> findByPrimaryKeys(List<Long> ids);	


}
