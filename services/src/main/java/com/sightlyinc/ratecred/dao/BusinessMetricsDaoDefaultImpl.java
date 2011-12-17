
package com.sightlyinc.ratecred.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.BusinessMetrics;

@Repository("businessMetricsDao")
public class BusinessMetricsDaoDefaultImpl 
extends AbstractDao<BusinessMetrics> 
	implements BusinessMetricsDao {

	static Logger logger = Logger.getLogger(BusinessMetricsDaoDefaultImpl.class);
    
    public BusinessMetricsDaoDefaultImpl() {
		super(BusinessMetrics.class);
	}


	/**
     * this should be a unique result because only one metric will exist for 
     * this date
     */
 	@Override
	public BusinessMetrics findBusinessMetricsByLocationAndStartTime(
			final BusinessLocation bl, final Long startTime) {
 
 		return (BusinessMetrics)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+BusinessMetrics.class.getName()+
						" as entityimpl where entityimpl.startTimeMills = :startTimeMills " +
						"and entityimpl.businessLocationId = :businessLocationId");
				
				query.setLong("startTimeMills", startTime);
				query.setLong("businessLocationId", bl.getId());
				
				BusinessMetrics t = null;
				
				try {
					t = (BusinessMetrics)query.uniqueResult();
				} catch (Exception e) {
					logger.debug("no existing metrics found");
				}
							
				return t;
	
			}
		});		
	}




	@Override
	public Long save(BusinessMetrics bm) {
    	getHibernateTemplateOverride().save(bm);
		return bm.getId();
	}


	@Override
	public Long mineLastBusinessMetricsTime(final BusinessLocation bl) {
		Long result = (Long)getHibernateTemplateOverride().execute(
    			new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
		{

				
				//native				
				String sqlFirstDate = 
					"SELECT max(tl.time_mills) as created "+
						"FROM business LEFT JOIN business_location bl "+ 
						"on (bl.business_id = business.id) "+ 
						"LEFT JOIN place pl on (pl.business_location_id = bl.id) "+ 
						"LEFT JOIN rating tl on (tl.place_id = pl.id) "+
						"where bl.id = :businessLocationId";
				
				Query queryFirstDate =  
					session.createSQLQuery(sqlFirstDate); 
				queryFirstDate.setLong("businessLocationId", bl.getId());
				
				
				List oList = queryFirstDate.list();
				
				if(oList != null && oList.size()>0)
				{
					BigInteger oScalar = (BigInteger)oList.get(0);
					return oScalar.longValue();
				}				
				
				return null;
	
			}
		});
		return result;   	
	}



	public Long findLastBusinessLocationMetricsTime(final BusinessLocation bl)
    {
    	Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
								
				
				//native, earlier				
				String sql = 
					"SELECT "+
							"max(business_metrics.start_time_mills) "+ 
						"FROM business_metrics "+
						"where business_metrics.business_location_id = :businessLocationId";
				
				Query query =  
					session.createSQLQuery(sql); 
				query.setLong("businessLocationId", bl.getId());
				List oList = query.list();
				
				try {
					if(oList != null && oList.size()>0)
					{
						BigInteger oScalar = (BigInteger)oList.get(0);
						return oScalar.longValue();
					}	
				} catch (NullPointerException e) {
					logger.error("no date found");
				} catch (Exception e) {
					logger.error("problem getting date",e);
				}
				return null;
	
			}
		});
		return result;
    }
    
    /**
     * find out when the first rating happened at a business location
     * 
     * @param bl
     * @return
     */
    public Long mineFirstBusinessMetricsTime(final BusinessLocation bl)
    {
    	Long result = (Long)getHibernateTemplateOverride().execute(
    			new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
		{

				
				//native				
				String sqlFirstDate = 
					"SELECT min(tl.time_mills) as created "+
						"FROM business LEFT JOIN business_location bl "+ 
						"on (bl.business_id = business.id) "+ 
						"LEFT JOIN place pl on (pl.business_location_id = bl.id) "+ 
						"LEFT JOIN rating tl on (tl.place_id = pl.id) "+
						"where bl.id = :businessLocationId";
				
				Query queryFirstDate =  
					session.createSQLQuery(sqlFirstDate); 
				queryFirstDate.setLong("businessLocationId", bl.getId());
				
				
				List oList = queryFirstDate.list();
				
				if(oList != null && oList.size()>0)
				{
					BigInteger oScalar = (BigInteger)oList.get(0);
					return oScalar.longValue();
				}				
				
				return null;
	
			}
		});
		return result;   	

    }

    /**
     * 
     * REIMPLEMENT
     * 
     * will do the heavy lifting (expensive) of mining the domain 
     * model for business metrics over a date range.
     * will primarily be used by jobs to extract 
     * metrics because heavy lifting will be 
     * required
     * 
     * @return
     */
    public BusinessMetrics mineMetricsForDateRange(final BusinessLocation bl, final Date startDate, final Date endDate)
    {
    	BusinessMetrics result = (BusinessMetrics)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				BusinessMetrics bm = new BusinessMetrics();
				bm.setStartTime(startDate.getTime());
				
				bm.setEndTime(endDate.getTime());
				
				//native				
				String sqlYayCount = 
					"SELECT "+
							"count(distinct(ta.id)) as yay_count "+ 
						"FROM business "+
						"LEFT JOIN business_location bl on (bl.business_id = business.id) "+
						"LEFT JOIN place pl on (pl.business_location_id = bl.id) "+
						"LEFT JOIN rating tl on (tl.place_id = pl.id) "+
						"LEFT JOIN rating_attribute ta on (ta.rating_id = tl.id) "+
						"where ta.type= 'Yay' "+
						"and tl.time_mills > :timeStartMills "+
						"and tl.time_mills < :timeEndMills "+
						"and bl.id = :businessLocationId";
				
				Query queryYay =  
					session.createSQLQuery(sqlYayCount); 
				queryYay.setLong("businessLocationId", bl.getId());
				queryYay.setLong("timeStartMills", startDate.getTime());
				queryYay.setLong("timeEndMills", endDate.getTime());
				
				List oListYay = queryYay.list();
				
				if(oListYay != null && oListYay.size()>0)
				{
					BigInteger oScalar = (BigInteger)oListYay.get(0);
					bm.setYays(oScalar.intValue());
				}
				
				
				String sqlBooCount = 
					"SELECT "+
							"count(distinct(ta.id)) as yay_count "+ 
						"FROM business "+
						"LEFT JOIN business_location bl on (bl.business_id = business.id) "+
						"LEFT JOIN place pl on (pl.business_location_id = bl.id) "+
						"LEFT JOIN rating tl on (tl.place_id = pl.id) "+
						"LEFT JOIN rating_attribute ta on (ta.rating_id = tl.id) "+
						"where ta.type= 'Boo' "+
						"and tl.time_mills > :timeStartMills "+
						"and tl.time_mills < :timeEndMills "+
						"and bl.id = :businessLocationId";
				
				Query queryBoo =  
					session.createSQLQuery(sqlBooCount); 
				queryBoo.setLong("businessLocationId", bl.getId());
				queryBoo.setLong("timeStartMills", startDate.getTime());
				queryBoo.setLong("timeEndMills", endDate.getTime());
				List oListBoo = queryBoo.list();
				if(oListBoo != null && oListBoo.size()>0)
				{
					BigInteger oScalar = (BigInteger)oListBoo.get(0);
					bm.setBoos(oScalar.intValue());
				}
				
				
				//now the number of ratings
				String sqlRateCount = 
					"SELECT "+
							"count(distinct(tl.id)) as rating_count "+ 
						"FROM business "+
						"LEFT JOIN business_location bl on (bl.business_id = business.id) "+
						"LEFT JOIN place pl on (pl.business_location_id = bl.id) "+
						"LEFT JOIN rating tl on (tl.place_id = pl.id) "+
						"where tl.time_mills > :timeStartMills "+
						"and tl.time_mills < :timeEndMills "+
						"and bl.id = :businessLocationId";

				Query queryRates =  
					session.createSQLQuery(sqlRateCount); 
				queryRates.setLong("businessLocationId", bl.getId());
				queryRates.setLong("timeStartMills", startDate.getTime());
				queryRates.setLong("timeEndMills", endDate.getTime());
				List oListRates = queryRates.list();
				if(oListRates != null && oListRates.size()>0)
				{
					BigInteger oScalar = (BigInteger)oListRates.get(0);
					bm.setRatings(oScalar.intValue());
				}
				
				bm.setBusinessLocation(bl);
				
				return bm;
	
			}
		});
		return result;
    }
    

	public List<BusinessMetrics> findDailyByBusiness(
			final Business b, final Date startDate, final Date endDate) {
		
		return null;
	}
	
	public List<BusinessMetrics> findDailyByBusinessLocationDateRange(
			final BusinessLocation bl, final Date startDate, final Date endDate) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+BusinessMetrics.class.getName()+" as entityimpl " +
							"where entityimpl.startTimeMills > :startTimeMills " +
							"and entityimpl.endTimeMills < :endTimeMills " +
							"and entityimpl.businessLocationId = :businessLocationId");
				
				query.setLong("startTimeMills", startDate.getTime());
				query.setLong("endTimeMills", endDate.getTime());
				query.setLong("businessLocationId", bl.getId());
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}
	
	
	public List<BusinessMetrics> findDailyByBusinessLocation(
			final BusinessLocation bl, final Date startDate, final Date endDate) {
		
		return null;
	}
	
	
	  
}
