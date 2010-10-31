package com.sightlyinc.ratecred.service;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.model.Rater;

public interface RaterAwardsService {

	public abstract void proccessAwardsForRater(RaterAwards ra)
			throws BLServiceException;

}