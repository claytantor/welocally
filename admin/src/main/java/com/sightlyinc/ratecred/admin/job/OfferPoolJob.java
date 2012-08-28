package com.sightlyinc.ratecred.admin.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sightlyinc.ratecred.service.OfferPoolService;

public class OfferPoolJob  {
	
	static Logger logger = 
		Logger.getLogger(OfferPoolJob.class);

	@Autowired
	private OfferPoolService offerPoolService;
	

	public void execute() {				
		logger.debug("[JOB] " + this.getClass().getName() +" running");
		offerPoolService.refresh();		
	}

}
