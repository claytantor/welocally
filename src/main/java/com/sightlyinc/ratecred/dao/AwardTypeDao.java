package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.AwardType;

public interface AwardTypeDao extends BaseDao<AwardType> {
	public AwardType findByKeyname(String kn);	
	public List<AwardType> findByType(String type);	
}
