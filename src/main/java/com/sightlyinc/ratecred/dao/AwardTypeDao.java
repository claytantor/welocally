package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.AwardType;

public interface AwardTypeDao {
	
	public AwardType findByPrimaryKey(Long id);	
	public AwardType findByKeyname(String kn);	
	public List<AwardType> findByType(String type);	
	public void delete(AwardType entity);	
	public void save(AwardType entity);
	public List<AwardType> findAll();		
	

}
