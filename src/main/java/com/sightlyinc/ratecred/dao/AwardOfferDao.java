package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.Business;

public interface AwardOfferDao {
	
	public AwardOffer findByPrimaryKey(Long id);	
	public AwardOffer findByKeyname(String kn);	
	public List<AwardOffer> findByType(String type);
	public List<AwardOffer> findByBusiness(Business b);
	public void delete(AwardOffer entity);	
	public void save(AwardOffer entity);
	public List<AwardOffer> findAll();		
	

}
