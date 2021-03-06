package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.BusinessLocation;

public interface BusinessLocationDao extends BaseDao<BusinessLocation> {
	public List<BusinessLocation> findByCityState(String city, String state);
	public List<BusinessLocation> findByNameCityState(String name, String city, String state);
	public List<BusinessLocation> findByCityStateRatingTypePaged(
			 String city,  String state, String ratingType,
			int pageNum,  int pageSize,   String sortField,  boolean isAscending);
	
	public List<BusinessLocation> findByGeoBounding(Double minLat, Double minLong,
			Double maxLat, Double maxLong);
	
	public Long findByCityStateCount(String city, String state);
		
	public BusinessLocation findByAuthId(String guid);	
	
	public BusinessLocation findByUsername(String username);	
	
	public List<BusinessLocation> findByPrimaryKeys(List<Long> ids);
	
	public List<BusinessLocation> findByExample(BusinessLocation example);
	

}
