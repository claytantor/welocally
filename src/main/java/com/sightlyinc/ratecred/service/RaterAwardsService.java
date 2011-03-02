package com.sightlyinc.ratecred.service;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;

public interface RaterAwardsService {

	public abstract void proccessAwardsForRater(RaterAwards ra)
			throws BLServiceException;
	
	public Long saveNewAward(Award award, AwardType awardType, Rater r) 
		throws BLServiceException; 
	
	public Long saveUpdateAwardOffer(Award award, AwardType awardType, Rater r) 
		throws BLServiceException;

}