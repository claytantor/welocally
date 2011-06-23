package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.Offer;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PlaceCityState;

public interface AwardManagerService {
	
	public Offer findAwardOfferByPrimaryKeywordsAndLocation(
			List<String> keywords, Double lat, Double lon) throws BLServiceException;
	
	public List<AwardType> findBusinessAwardTypes() throws BLServiceException;
	public List<AwardType> findAllAwardTypes() throws BLServiceException;		
	public List<Offer> findExpiredAwardOffers() throws BLServiceException;
	public List<Award> findBusinessAwards(Business b) throws BLServiceException;	
	
	public Award findAwardByPrimaryKey(Long awardId) throws BLServiceException;
	
	public List<Award> findAwardByOffer(Offer offer) throws BLServiceException;
	public List<Award> findAwardByOfferRater(Offer offer,Patron r) throws BLServiceException;
	
	public List<Award> findAwardByRaterAwardType(Patron r, AwardType at) throws BLServiceException;
	public List<Award> findAwardByRaterTypeCity(Patron r, AwardType at, PlaceCityState pcs) throws BLServiceException;
	
	public Offer findAwardOfferByPrimaryKey(Long awardOfferId) throws BLServiceException;	
	public List<Offer> findBusinessAwardOffers(Business b) throws BLServiceException;
	
	public AwardType findAwardTypeByKey(String key) throws BLServiceException;
	public AwardType findAwardTypeByPrimaryKey(Long id) throws BLServiceException;
		
	public Long saveAward(Award award)  throws BLServiceException;
	public void deleteAward(Award award)  throws BLServiceException;
	
	public void deleteAwardOffer(Offer awardOffer)  throws BLServiceException;
	
	public Long saveAwardOffer(Offer entity) throws BLServiceException;

}
