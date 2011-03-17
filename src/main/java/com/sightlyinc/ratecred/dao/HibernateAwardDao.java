
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;


public class HibernateAwardDao 
	extends HibernateDaoSupport 
	implements AwardDao {

	static Logger logger = Logger.getLogger(HibernateAwardDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

 
    
    
    /*@Override
	public List<Award> findByOfferExpired() {
    	
    	final Long now = Calendar.getInstance().getTimeInMillis();
    	
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
						"select distinct entityimpl from "+Award.class.getName()+
						" as entityimpl where entityimpl.offer.expireDateMillis < :now");
				
				query.setLong("now", now);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
		
	}*/




	@Override
	public List<Award> findByOffer(final AwardOffer offer) {
    	return (List)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+AwardOffer.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", offer.getId());
				
				List list = query.list();
				
				return list;
	
			}
		});		
	}




	@Override
	public List<Award> findByOfferRater(final AwardOffer offer, final Rater r) {
		return (List)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+AwardOffer.class.getName()+
					" as entityimpl where entityimpl.id = :id and" +
					" entityimpl.award.owner = :owner");
				
				query.setLong("id", offer.getId());
				query.setEntity("owner", r);
				
				List list = query.list();
				
				return list;
	
			}
		});		
	}




	@Override
	public List<Award> findByOwnerTypePlaceCityState(
			final Rater towards,
			final AwardType at,
			final PlaceCityState pcs) {
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Award.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards and " +
							"entityimpl.awardType = :awardType and " +
							"entityimpl.metadata like %:city% and " +
							"entityimpl.metadata like %:state%");
	
					query.setEntity("towards", towards);
					query.setEntity("awardType", at);
					query.setEntity("city", pcs.getCity());
					query.setEntity("state", pcs.getState());
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	@Override
	public List<Award> findByOwnerAwardType(final Rater towards, final AwardType at) {
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Award.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards and " +
							"entityimpl.awardType = :awardType");
	
					query.setEntity("towards", towards);
					query.setEntity("awardType", at);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	@Override
	public Long findCountByOwnerBetweenTimes(final Rater towards,
			final Date startTime, final Date endTime) {
		
		final Long startTimeMills = startTime.getTime();
		final Long endTimeMills = endTime.getTime();
		
		logger.debug("start:"+startTimeMills.toString()+" end:"+endTimeMills);
		
		Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{

					Query query = session.createQuery(
							"select count(entityimpl.id) from "+Award.class.getName()+
							" as entityimpl where entityimpl.owner = :towards" +
							" and entityimpl.timeCreatedMills > :startTime" +
							" and entityimpl.timeCreatedMills < :endTime");
	
					query.setEntity("towards", towards);
					query.setLong("startTime", startTimeMills);
					query.setLong("endTime", endTimeMills);
					
					Long count = 
						(Long)query.uniqueResult();

					return count;
			}
		});
		return result;
	}
    
    
	@Override
	public List<Award> findByOwnerBetweenTimes(
			final Rater towards, 
			final Date startTime,
			final Date endTime) {
		final Long startTimeMills = startTime.getTime();
		final Long endTimeMills = endTime.getTime();
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Award.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards" +
							" and entityimpl.timeCreatedMills > :startTime" +
							" and entityimpl.timeCreatedMills < :endTime");
	
					query.setEntity("towards", towards);
					query.setLong("startTime", startTimeMills);
					query.setLong("endTime", endTimeMills);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}




	@Override
	public List<Award> findByOwner(final Rater towards) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Award.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards");
	
					query.setEntity("towards", towards);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	@Override
	public List<Award> findByBusiness(final Business b) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Award.class.getName()+" as entityimpl " +
							"where entityimpl.offer.business.id = :businessId");
				query.setLong("businessId", b.getId());
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}




	@Override
	public void delete(Award entity) {
		getHibernateTemplateOverride().delete(entity);
		
	}
		


	@Override
	public List<Award> findAll() {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Award.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}

	@Override
	public Award findByPrimaryKey(final Long id) {
		return (Award)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Award.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				Award t = (Award)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}

	@Override
	public Award findByKeyname(final String kn) {
		return (Award)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Award.class.getName()+
					" as entityimpl where entityimpl.keyname = :kn");
				
				query.setString("kn", kn);
				
				Award t = (Award)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}

	@Override
	public void save(Award entity) {
		/*//should not have to do this
		try {
			//bind to thread			
	        //Session session = SessionFactoryUtils.getSession(super.getSessionFactory(), true); 
			Session session = SessionFactoryUtils.getNewSession(super.getSessionFactory());
			org.hibernate.Transaction t = session.beginTransaction();
			TransactionSynchronizationManager.setCurrentTransactionReadOnly(false);
	        TransactionSynchronizationManager.bindResource(super.getSessionFactory(), new SessionHolder(session)); 
			session.setFlushMode(FlushMode.AUTO);
									
			session.save(entity);
			
			t.commit();
		
			
		} catch (DataAccessException e) {
			logger.error("cant save", e);
		} catch (Exception e) {
			logger.error("cant save", e);
		}*/
		
		
		getHibernateTemplateOverride().save(entity);		
	}
    

	
	
	
	  
}
