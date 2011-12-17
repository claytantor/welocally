package com.sightlyinc.ratecred.service;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.admin.model.TargetModel;
import com.sightlyinc.ratecred.client.offers.OfferOld;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.Offer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Patron;

public interface PatronAwardsService {
	
	public OfferOld targetOfferByTargetingModel(TargetModel targetModel) throws BLServiceException;
	
	public void targetAwardById(Long awardId) throws BLServiceException;
	
	public void removeAllOffers() throws BLServiceException;
	
	public void reassignAllOffers()
		throws BLServiceException;
	
	public void deleteRaterAwardOffers(Long raterId)
		throws BLServiceException;

	public abstract void proccessAwardsForRater(RaterAwards ra)
			throws BLServiceException;
	
	public Long saveNewAward(Award award, AwardType awardType, Patron r, Offer ao) 
		throws BLServiceException; 
	
	public Long saveUpdateAwardOffer(Award award, AwardType awardType, Patron r) 
		throws BLServiceException;

}