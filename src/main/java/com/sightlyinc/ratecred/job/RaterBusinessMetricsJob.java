package com.sightlyinc.ratecred.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.noi.utility.date.DateUtils;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.BusinessMetrics;
import com.sightlyinc.ratecred.service.BusinessManagerService;

public class RaterBusinessMetricsJob extends QuartzJobBean {
	
	static Logger logger = 
		Logger.getLogger(RaterBusinessMetricsJob.class);

	
	private BusinessManagerService businessManagerService;
	private SessionFactory sessionFactory;
	
	
		

	/**
	 * all businesses all locations
	 * 
	 * @throws JobExecutionException
	 */
	public void execute()
	throws JobExecutionException {
		
		
		logger.debug("[JOB] " + this.getClass().getName() +" running");
		
		
		
		
		
		long MILLS_DAY = 86400000l;
		

		
		Session session = null;

		try {
			
			//bind to thread			
			session = 
				SessionFactoryUtils.getSession(sessionFactory, true);
			
			TransactionSynchronizationManager.bindResource(
					sessionFactory,
					new SessionHolder(session));
			
			//find all businesses
			List<Business> allBusinesses = 
				businessManagerService.findAllBusinesss();
			
			for (Business business : allBusinesses) {
				
				for (BusinessLocation bl : business.getLocations()) {
					
					//see if the mined date is later than the persisted
									
					//find the last persited date for the business metric available
					Long lastDateStore = 
						businessManagerService.findLastCreatedBusinesssLocationMetricsTime(bl);
					
					Long lastDateMined = 
						businessManagerService.findLastMinedBusinesssLocationMetricsTime(bl);
					
					
					if(lastDateStore != null && 
							lastDateMined != null && 							
							lastDateMined>lastDateStore) {//metrics exist
						
						logger.debug("metrics exist");
						logger.debug("lastDateStore:"+lastDateStore);
						logger.debug("lastDateMined:"+lastDateMined);
						
						//get todays mined metrics we will replace the
						//one in the store with this one
						Date minedMidnight = 
							DateUtils.midnight(
									new Date(lastDateMined), 
									Calendar.getInstance().getTimeZone());
												
						List<BusinessMetrics> minedLocationMetrics =
							businessManagerService.findMinedBusinesssLocationMetricsSince(
									 bl, 
									 minedMidnight);
						
						for (BusinessMetrics businessMetrics : minedLocationMetrics) {
							logger.debug("update metric with start:"+minedMidnight.getTime()+
									" and blid:"+businessMetrics.getBusinessLocationId());
							
						}
						
					} else if(lastDateStore == null && 
							lastDateMined != null ) { //no values in store
						
/*						//now save those results
						saveAllMetrics(
								businessManagerService.findMinedBusinesssLocationMetricsSince(
										bl, 
										new Date(businessManagerService.findFirstMinedBusinesssLocationMetricsTime(bl)))) ;
*/						
					}

				}
												
			}
			
		} catch (Exception e) {
			logger.error("problem mining data", e);
			throw new JobExecutionException(e);
		} finally {
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			if(session != null)
				SessionFactoryUtils.releaseSession(
						session, 
						sessionFactory);
		}
		
		
		
	}
	
	private void saveAllMetrics(List<BusinessMetrics> all) 
		throws BLServiceException
	{
		for (BusinessMetrics businessMetrics : all) 
			businessManagerService.saveBusinessMetrics(businessMetrics);
	}
	
	@Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		logger.debug("running job");
		execute();
	}

	public void setBusinessManagerService(
			BusinessManagerService businessManagerService) {
		this.businessManagerService = businessManagerService;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}




	
	
	
	
	

}
