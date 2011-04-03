package com.sightlyinc.ratecred.service;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.admin.model.TargetModel;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Rater;

public interface RaterAwardsService {
	
	public Offer targetOfferByTargetingModel(TargetModel targetModel) throws BLServiceException;
	
	public void targetAwardById(Long awardId) throws BLServiceException;
	
	public void removeAllOffers() throws BLServiceException;
	
	public void reassignAllOffers()
		throws BLServiceException;
	
	public void deleteRaterAwardOffers(Long raterId)
		throws BLServiceException;

	public abstract void proccessAwardsForRater(RaterAwards ra)
			throws BLServiceException;
	
	public Long saveNewAward(Award award, AwardType awardType, Rater r, AwardOffer ao) 
		throws BLServiceException; 
	
	public Long saveUpdateAwardOffer(Award award, AwardType awardType, Rater r) 
		throws BLServiceException;

}