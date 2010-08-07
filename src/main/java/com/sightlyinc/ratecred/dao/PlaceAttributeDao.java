package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.PlaceAttribute;

public interface PlaceAttributeDao {

	public PlaceAttribute findByPrimaryKey(Long id);	
	public List<PlaceAttribute> findAll();
	public void save(PlaceAttribute entity);
	public void delete(PlaceAttribute entity);
}
