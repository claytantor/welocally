
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.BusinessLocation;

@Repository("businessLocationDao")
public class BusinessLocationDaoDefaultImpl 
extends AbstractDao<BusinessLocation> 
	implements BusinessLocationDao {

	static Logger logger = Logger.getLogger(BusinessLocationDaoDefaultImpl.class);
    
    public BusinessLocationDaoDefaultImpl() {
		super(BusinessLocation.class);
	}

	@Override
	public List<BusinessLocation> findByExample(BusinessLocation exampleLocation) {
    	
    	final Example example = Example.create(exampleLocation)
	        .excludeZeroes()           //exclude zero valued properties
	        .ignoreCase();             //use like for string comparisons
    			
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					return  session.createCriteria(BusinessLocation.class)
				        .add(example)
				        .list();
		
				}
		});
		
		return result;
	}    

    @Override
	public BusinessLocation findByUsername(final String userName) {
    	BusinessLocation result = (BusinessLocation)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+BusinessLocation.class.getName()+
					" as entityimpl where entityimpl.userName = :userName");
				
				query.setString("userName", userName);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;
	}

	@Override
	public BusinessLocation findByAuthId(final String authId) {
		BusinessLocation result = (BusinessLocation)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+BusinessLocation.class.getName()+
					" as entityimpl where entityimpl.guid = :authGuid");
				
				query.setString("authGuid", authId);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;
	}   
    
	@Override
	public List<BusinessLocation> findByPrimaryKeys(List<Long> ids) {
		final Long[] idsArray = (Long[])ids.toArray(new Long[ids.size()]);

		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=
						session.createCriteria(BusinessLocation.class)
							.add(Restrictions.in("id",idsArray));

					return criteria.list();				
				}
		});
		
		return result;
	}

	public List<BusinessLocation> findByCityState(final String city, final String state) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				Query query = session.createQuery(
						"select entityimpl from "+BusinessLocation.class.getName()+
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
	public List<BusinessLocation> findByNameCityState(
			final String name, 
			final String city,
			final String state) {
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				Query query = session.createQuery(
						"select entityimpl from "+BusinessLocation.class.getName()+
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

	public List<BusinessLocation> findByCityStateRatingTypePaged(
			final String city, final String state, final String ratingType, 
			int pageNum, final int pageSize,  final String sortField, final boolean isAscending) {
		
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=session.createCriteria(BusinessLocation.class)					
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
					"select count(entityimpl.id) from "+BusinessLocation.class.getName()+" as entityimpl " +
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
	public List<BusinessLocation> findByGeoBounding(
			final Double minLat, final Double minLong,
			final Double maxLat, final Double maxLong) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
						"select entityimpl from "+BusinessLocation.class.getName()+" as entityimpl " +
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
