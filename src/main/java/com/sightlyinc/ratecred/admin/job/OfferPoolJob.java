package com.sightlyinc.ratecred.admin.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.sightlyinc.ratecred.service.OfferPoolService;

public class OfferPoolJob extends QuartzJobBean {
	
	static Logger logger = 
		Logger.getLogger(OfferPoolJob.class);

	private OfferPoolService offerPoolService;
	
	/**
	 * all businesses all locations
	 * 
	 * @throws JobExecutionException
	 */
	public void execute()
	throws JobExecutionException {				
		logger.debug("[JOB] " + this.getClass().getName() +" running");
		offerPoolService.refresh();		
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		logger.debug("running job");
		execute();
	}

	public void setOfferPoolService(OfferPoolService offerPoolService) {
		this.offerPoolService = offerPoolService;
	}
	

}
