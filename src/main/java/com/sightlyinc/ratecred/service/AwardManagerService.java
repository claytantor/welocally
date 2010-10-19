package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;

public interface AwardManagerService {
	
	public List<AwardType> findBusinessAwardTypes() throws BLServiceException;
	public List<AwardType> findAllAwardTypes() throws BLServiceException;
	public List<AwardOffer> findBusinessAwardOffers(Business b) throws BLServiceException;
	
	public List<Award> findBusinessAwards(Business b) throws BLServiceException;
	public Award findAwardByPrimaryKey(Long awardId) throws BLServiceException;
	public AwardOffer findAwardOfferByPrimaryKey(Long awardOfferId) throws BLServiceException;	
	public AwardType findAwardTypeByKey(String key) throws BLServiceException;
	
	
	public Long saveAward(Award award)  throws BLServiceException;
	public void saveAwardOffer(AwardOffer entity) throws BLServiceException;

}
