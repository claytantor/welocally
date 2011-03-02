
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Rater;


public class HibernateOrderDao 
	extends HibernateDaoSupport 
	implements OrderDao {

	static Logger logger = Logger.getLogger(HibernateOrderDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

    
	@Override
	public Order findByChannelAndExternalId(final String channel, final String externalId) {
		return (Order)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Order.class.getName()+
					" as entityimpl where entityimpl.channel = :channel and" +
					" entityimpl.externalId = :externalId");
				
				query.setString("channel", channel);
				query.setString("externalId", externalId);
				
				Order t = (Order)query.uniqueResult();
				
			
				return t;
	
			}
		});
	}


	@Override
	public List<Order> findByOwner(final Rater owner) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :owner");
				
				query.setEntity("owner", owner);
				
				/*Query query = session.createQuery(
						"select entityimpl from "+Order.class.getName()+" as entityimpl");*/
				
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}

	@Override
	public List<Order> findAll() {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}



	@Override
	public Order findByPrimaryKey(final Long id) {
		return (Order)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Order.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				Place t = (Place)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}




	@Override
	public void save(Order entity) {
		getHibernateTemplateOverride().save(entity);		
	}
    
	@Override
	public void delete(Order entity) {
		getHibernateTemplateOverride().delete(entity);
		
	}
	   
    
  /*  @Override
	public List<Order> findByOfferExpired() {
    	
    	final Long now = Calendar.getInstance().getTimeInMillis();
    	
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
						"select distinct entityimpl from "+Order.class.getName()+
						" as entityimpl where entityimpl.offer.expireDateMillis < :now");
				
				query.setLong("now", now);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
		
	}




	@Override
	public Order findByOffer(final OrderOffer offer) {
    	return (Order)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Order.class.getName()+
					" as entityimpl where entityimpl.offer = :offer");
				
				query.setEntity("offer", offer);
				
				Order t = (Order)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}




	@Override
	public List<Order> findByOwnerTypePlaceCityState(
			final Rater towards,
			final OrderType at,
			final PlaceCityState pcs) {
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
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
	public List<Order> findByOwnerOrderType(final Rater towards, final OrderType at) {
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
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
							"select count(entityimpl.id) from "+Order.class.getName()+
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
	public List<Order> findByOwnerBetweenTimes(
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
					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
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
	public List<Order> findByOwner(final Rater towards) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards");
	
					query.setEntity("towards", towards);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	@Override
	public List<Order> findByBusiness(final Business b) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
							"where entityimpl.offer.business.id = :businessId");
				query.setLong("businessId", b.getId());
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}




	@Override
	public void delete(Order entity) {
		getHibernateTemplateOverride().delete(entity);
		
	}
		


	@Override
	public List<Order> findAll() {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}

	@Override
	public Order findByPrimaryKey(final Long id) {
		return (Order)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Order.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				Order t = (Order)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}

	@Override
	public Order findByKeyname(final String kn) {
		return (Order)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Order.class.getName()+
					" as entityimpl where entityimpl.keyname = :kn");
				
				query.setString("kn", kn);
				
				Order t = (Order)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}*/


	
	
	  
}
