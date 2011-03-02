package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Business;

public interface BusinessDao {
	public List<Business> findByCityState(String city, String state);
	public List<Business> findByNameCityState(String name, String city, String state);
	public List<Business> findByCityStateRatingTypePaged(
			 String city,  String state, String ratingType,
			int pageNum,  int pageSize,   String sortField,  boolean isAscending);
	
	public List<Business> findByGeoBounding(Double minLat, Double minLong,
			Double maxLat, Double maxLong);
	
	public Long findByCityStateCount(String city, String state);
	
	public Business findByPrimaryKey(Long id);	
	
	public Business findByAdvertiserIdAndSource(String advertiserId, String source);	
	
	public Business findByAuthId(String guid);	
	
	public Business findByUsername(String username);	
	
	public List<Business> findByPrimaryKeys(List<Long> ids);	
	
	public List<Business> findAll();
	public void save(Business entity);
	public void delete(Business entity);
}
