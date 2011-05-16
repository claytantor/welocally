package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Offer;
import com.sightlyinc.ratecred.model.Business;

public interface OfferDao extends BaseDao<Offer> {
	
	public Offer findByKeyname(String kn);	
	public List<Offer> findByType(String type);
	public List<Offer> findByBusiness(Business b);
	public List<Offer> findExpired();

}
