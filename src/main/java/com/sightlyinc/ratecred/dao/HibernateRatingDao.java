
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Rating;

public class HibernateRatingDao 
	extends HibernateDaoSupport 
	implements RatingDao {

	static Logger logger = Logger.getLogger(HibernateRatingDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

    
    public Date findPlaceFirstRateDate(Place p)
    {
    	return null;
    }
    
    @Override
	public List<Rating> findSince(final Long time) {

		
		return (List<Rating>)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{
				Long since = Calendar.getInstance().getTimeInMillis()-time;
				Query query = session.createQuery(
						"select entityimpl from "+Rating.class.getName()+
						" as entityimpl where entityimpl.timeCreatedMills > :since");
				
					query.setLong("since", since);
					
					List<Rating> t = (List<Rating>)query.list();
			
				return t;
	
			}
		});		
		
	}    
    
	@Override
	public Rating findByTime(final Long time) {

		
		return (Rating)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
						"select entityimpl from "+Rating.class.getName()+
						" as entityimpl where entityimpl.timeCreatedMills = :time");
					query.setLong("time", time);
					
				Rating t = (Rating)query.uniqueResult();
			
				return t;
	
			}
		});		
		
	}


	@Override
	public Long findAllCount() {
		Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select count(entityimpl.id) from "+Rating.class.getName()+" as entityimpl");
				Long count = (Long)query.uniqueResult();
				
				return count;
	
			}
		});
		return result;
	}
	
	
	

	@Override
	public Long findByCityStateCount(final String city, final String state) {
		Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select count(entityimpl.id) from "+Rating.class.getName()+" as entityimpl " +
							"where entityimpl.place.city = :city " +
							"and entityimpl.place.state = :state");
				query.setString("city", city);
				query.setString("state", state);
				
				Long count = (Long)query.uniqueResult();
				
				return count;
	
			}
		});
		return result;
	}

	@Override
	public Long findByCityStatePlaceInfoCount(final String city, final String state, final String placeInfo) {
		
		Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select count(entityimpl.id) from "+Rating.class.getName()+" as entityimpl " +
							"where entityimpl.place.city = :city " +
							"and entityimpl.place.state = :state " +
							"and entityimpl.place.name = :placeInfo");
				query.setString("city", city);
				query.setString("state", state);
				query.setString("placeInfo", placeInfo);
				
				Long count = (Long)query.uniqueResult();
				
				return count;
	
			}
		});
		return result;
	}

	@Override
	public List<Rating> findAllPaged(final int pageNum, final int pageSize, final String sortField, final boolean isAscending) {
		
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=session.createCriteria(Rating.class);

					if(isAscending)
						criteria.addOrder(Order.asc(sortField));
					else
						criteria.addOrder(Order.desc(sortField));
					
					criteria.setFirstResult(index);
					criteria.setMaxResults(pageSize);
					return criteria.list();				
				}
		});
		
		return result;
	}

	
	
	
	@Override
	public List<Rating> findByPrimaryKeys(final List<Long> ids) {

		final Long[] idsArray = (Long[])ids.toArray(new Long[ids.size()]);

		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=
						session.createCriteria(Rating.class)
							.add(Restrictions.in("id",idsArray));

					return criteria.list();				
				}
		});
		
		return result;
	}

	@Override
	public List<Rating> findByCityStatePaged(final String city,
			final String state, 
			int pageNum, final int pageSize,  final String sortField, final boolean isAscending) {
		
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Rating.class.getName()+" as entityimpl " +
					" where entityimpl.place.city = :city"+
					" and entityimpl.place.state = :state"+
					" order by entityimpl.timeCreated desc"
					);
				query.setString("city", city);
				query.setString("state", state);
				query.setFirstResult(index);
				query.setMaxResults(pageSize);
				List list = query.list();
				
	
				return list;
	
			}
		});
		
		return result;
	}
	
	

	@Override
	public List<Rating> findByCityStatePlaceInfoPaged(
			final String city,
			final String state, 
			final String placeInfo, 
			int pageNum,
			final int pageSize, 
			final String sortField, 
			final boolean isAscending) 
		{
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		

		
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Rating.class.getName()+" as entityimpl " +
					" where entityimpl.place.city = :city"+
					" and entityimpl.place.state = :state"+
					" and entityimpl.place.name = :placeInfo" +
					" order by entityimpl.timeCreated desc"
					);
				query.setString("city", city);
				query.setString("state", state);
				query.setString("placeInfo", placeInfo);
				query.setFirstResult(index);
				query.setMaxResults(pageSize);
				List list = query.list();
	
				return list;
	
			}
		});
		
		return result;
	}
	
	
	

	@Override
	public List<Rating> findByOwner(
			final Long ownerId, final int pageNum, final int pageSize,
			final String sortField, final boolean isAscending) {
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=session.createCriteria(Rating.class)					
					.add( Restrictions.eq("owner.id", ownerId) );

					if(isAscending)
						criteria.addOrder(Order.asc(sortField));
					else
						criteria.addOrder(Order.desc(sortField));
					
					criteria.setFirstResult(index);
					criteria.setMaxResults(pageSize);
					return criteria.list();				
				}
		});
		
		return result;
	}
	
	@Override
	public List<Rating> findByOwners(
			final Long[] ownerIds, final int pageNum, final int pageSize,
			final String sortField, final boolean isAscending) {
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=session.createCriteria(Rating.class)					
					.add( Restrictions.in("owner.id", ownerIds) );

					if(isAscending)
						criteria.addOrder(Order.asc(sortField));
					else
						criteria.addOrder(Order.desc(sortField));
					
					criteria.setFirstResult(index);
					criteria.setMaxResults(pageSize);
					return criteria.list();				
				}
		});
		
		return result;
	}
	
	

	@Override
	public Long findByOwnerCount(final Long ownerId) {
		Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select count(entityimpl.id) from "+Rating.class.getName()+" as entityimpl " +
							"where entityimpl.owner.id = :ownerId");
				query.setLong("ownerId", ownerId);
				
				Long count = (Long)query.uniqueResult();
				
				return count;
	
			}
		});
		return result;
	}

	public Rating create() {
		return new Rating();
	}

	public void save(Rating entity) {
		getHibernateTemplateOverride().save(entity);
	}   

	public void delete(Rating entity) {
        getHibernateTemplateOverride().delete(entity);
    }

	public List<Rating> findAll() {

		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Rating.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
		
   }
	

	/**
	 * this should be load.
	 */
	public Rating findByPrimaryKey(final Long id) 
	{ 
		
		return (Rating)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Rating.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				Rating t = (Rating)query.uniqueResult();
				
			
				return t;
	
			}
		});		
							
    }

	@Override
	public List<Rating> findByCityStatePlaceInfo(
			final String city, final String state, final String placeInfo) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Rating.class.getName()+" as entityimpl " +
					" where entityimpl.place.city = :city"+
					" and entityimpl.place.state = :state"+
					" and entityimpl.place.name = :placeInfo" +
					" order by entityimpl.timeCreated desc"
					);
				query.setString("city", city);
				query.setString("state", state);
				query.setString("placeInfo", placeInfo);
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}

	@Override
	public List<Rating> findByCityState(final String city, final String state) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Rating.class.getName()+" as entityimpl " +
					" where entityimpl.place.city = :city"+
					" and entityimpl.place.state = :state"+
					" order by entityimpl.timeCreated desc"
					);
				query.setString("city", city);
				query.setString("state", state);
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}
	
	
	
	  
}
