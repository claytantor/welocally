package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.Offer;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PlaceCityState;

public interface AwardDao extends BaseDao<Award> {
	
	public Award findByKeyname(String kn);
	
	public List<Award> findByOwnerBetweenTimes(Patron towards, Date startTime, Date endTime);
	public List<Award> findByOwner(Patron towards);
	
	public List<Award> findByOffer(Offer offer);
	public List<Award> findByOfferRater(Offer offer, Patron r);
	
	public List<Award> findByOwnerAwardType(Patron towards, AwardType at);
	public List<Award> findByOwnerTypePlaceCityState(Patron towards, AwardType at, PlaceCityState pcs);
	
	public Long findCountByOwnerBetweenTimes(final Patron towards,
			final Date startTime, final Date endTime);
	
	public List<Award> findByBusiness(Business b);
	
	

}
