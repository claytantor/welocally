package com.sightlyinc.ratecred.admin.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.service.OfferPoolService;

@Component("offerPoolJob")
public class OfferPoolJob  {
	
	static Logger logger = 
		Logger.getLogger(OfferPoolJob.class);

	@Autowired
	private OfferPoolService offerPoolService;
	
	/**
	 * every 2 mins
	 * 
	 * @throws JobExecutionException
	 */
	@Scheduled(fixedRate = 120000)
	public void execute() {				
		logger.debug("[JOB] " + this.getClass().getName() +" running");
		offerPoolService.refresh();		
	}

	

}
