package com.sightlyinc.ratecred.service;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Rating;

public interface CheckinService {
	public void checkinRating(String provider, Rating r) throws BLServiceException;
}
