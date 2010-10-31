package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Rating;

public interface RatingDao {
	
	public Rating findByPrimaryKey(Long id);
	public List<Rating> findByPrimaryKeys(List<Long> ids);
			
			
	public Rating create();
		
	public void delete(Rating entity);
	
	public void save(Rating entity);
	
	public List<Rating> findAll();
	public Long findAllCount();
	public List<Rating> findAllPaged(int pageNum, int pageSize, String sortField, boolean isAscending);
	
	public Rating findByTime(Long time);
	public List<Rating> findSince(Long time);
	
	public List<Rating> findByCityState(String city, String state);
	public Long findByCityStateCount(String city, String state);
	public List<Rating> findByCityStatePaged(String city, String state, int pageNum, final int pageSize, String sortField, boolean isAscending);
	public List<Rating> findByOwner(Long ownerId, int pageNum, final int pageSize, String sortField, boolean isAscending);
	public List<Rating> findByOwners(Long[] ownerIds, int pageNum, final int pageSize, String sortField, boolean isAscending);
	public Long findByOwnerCount(Long ownerId);
	
	public List<Rating> findByCityStatePlaceInfo(String city, String state, String placeInfo);
	public Long findByCityStatePlaceInfoCount(String city, String state, String placeInfo);
	public List<Rating> findByCityStatePlaceInfoPaged(String city, String state, String placeInfo, int pageNum, final int pageSize, String sortField, boolean isAscending);
	
		
	

}
