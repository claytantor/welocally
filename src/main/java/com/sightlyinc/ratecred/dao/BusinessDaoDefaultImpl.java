
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Business;

@Repository("businessDao")
public class BusinessDaoDefaultImpl 
	extends AbstractDao<Business> 
	implements BusinessDao {

	static Logger logger = Logger.getLogger(BusinessDaoDefaultImpl.class);

    public BusinessDaoDefaultImpl() {
		super(Business.class);
	}

	@Override
	public Business findByAdvertiserIdAndSource(final String advertiserId,
			final String advertiserSource) {
    	return (Business)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Business.class.getName()+
					" as entityimpl where entityimpl.advertiserId = :advertiserId" +
					" and entityimpl.advertiserSource = :advertiserSource");
				
				query.setString("advertiserSource", advertiserSource);
				query.setString("advertiserId", advertiserId);
				
				Business t = (Business)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}



	@Override
	public Business findByUsername(final String userName) {
    	Business result = (Business)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Business.class.getName()+
					" as entityimpl where entityimpl.userName = :userName");
				
				query.setString("userName", userName);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;
	}



	@Override
	public Business findByAuthId(final String authId) {
		Business result = (Business)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Business.class.getName()+
					" as entityimpl where entityimpl.guid = :authGuid");
				
				query.setString("authGuid", authId);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;
	}   
    
	@Override
	public List<Business> findByPrimaryKeys(List<Long> ids) {
		final Long[] idsArray = (Long[])ids.toArray(new Long[ids.size()]);

		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=
						session.createCriteria(Business.class)
							.add(Restrictions.in("id",idsArray));

					return criteria.list();				
				}
		});
		
		return result;
	}



	public List<Business> findByCityState(final String city, final String state) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				Query query = session.createQuery(
						"select entityimpl from "+Business.class.getName()+
						" as entityimpl where entityimpl.city = :city " +
						"and entityimpl.state = :state");

				query.setString("city",city);
				query.setString("state",state);

			    return query.list();

	
			}
		});
		return result;
	}
	
	@Override
	public List<Business> findByNameCityState(
			final String name, 
			final String city,
			final String state) {
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				Query query = session.createQuery(
						"select entityimpl from "+Business.class.getName()+
						" as entityimpl" +
						" where entityimpl.name = :name" +
						" and entityimpl.city = :city" +
						" and entityimpl.state = :state");
				
				query.setString("name",name);
				query.setString("city",city);
				query.setString("state",state);

			    return query.list();

	
			}
		});
		return result;
	}



	public List<Business> findByCityStateRatingTypePaged(
			final String city, final String state, final String ratingType, 
			int pageNum, final int pageSize,  final String sortField, final boolean isAscending) {
		
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=session.createCriteria(Business.class)					
					.add( Restrictions.eq("city", city))
					.add( Restrictions.eq("state", state));

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
	public Long findByCityStateCount(final String city, final String state) {
		Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select count(entityimpl.id) from "+Business.class.getName()+" as entityimpl " +
							"where entityimpl.city = :city " +
							"and entityimpl.state = :state");
				query.setString("city", city);
				query.setString("state", state);
				
				Long count = (Long)query.uniqueResult();
				
				return count;
	
			}
		});
		return result;
	}



	@Override
	public List<Business> findByGeoBounding(
			final Double minLat, final Double minLong,
			final Double maxLat, final Double maxLong) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
						"select entityimpl from "+Business.class.getName()+" as entityimpl " +
								"where entityimpl.latitude >= :minLat and " +
								"entityimpl.longitude >= :minLong and " +
								"entityimpl.latitude <= :maxLat and " +
								"entityimpl.longitude <= :maxLong");
				
				query.setDouble("minLat", minLat);
				query.setDouble("minLong", minLong);
				query.setDouble("maxLat", maxLat);
				query.setDouble("maxLong", maxLong);
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	
	  
}
