package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;

public interface AwardManagerService {
	
	public List<AwardType> findBusinessAwardTypes() throws BLServiceException;
	public List<AwardType> findAllAwardTypes() throws BLServiceException;		
	public List<AwardOffer> findExpiredAwardOffers() throws BLServiceException;
	public List<Award> findBusinessAwards(Business b) throws BLServiceException;	
	
	public Award findAwardByPrimaryKey(Long awardId) throws BLServiceException;
	
	public Award findAwardByOffer(AwardOffer offer) throws BLServiceException;
	
	public List<Award> findAwardByRaterAwardType(Rater r, AwardType at) throws BLServiceException;
	public List<Award> findAwardByRaterTypeCity(Rater r, AwardType at, PlaceCityState pcs) throws BLServiceException;
	
	public AwardOffer findAwardOfferByPrimaryKey(Long awardOfferId) throws BLServiceException;	
	public List<AwardOffer> findBusinessAwardOffers(Business b) throws BLServiceException;
	
	public AwardType findAwardTypeByKey(String key) throws BLServiceException;
	public AwardType findAwardTypeByPrimaryKey(Long id) throws BLServiceException;
		
	public Long saveAward(Award award)  throws BLServiceException;
	public void deleteAward(Award award)  throws BLServiceException;
	
	public void deleteAwardOffer(AwardOffer awardOffer)  throws BLServiceException;
	
	public Long saveAwardOffer(AwardOffer entity) throws BLServiceException;

}
