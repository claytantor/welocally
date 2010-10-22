
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
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sightlyinc.ratecred.model.Place;

public class HibernatePlaceDao 
	extends HibernateDaoSupport 
	implements PlaceDao {

	static Logger logger = Logger.getLogger(HibernatePlaceDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }
  
    
	@Override
	public Place findByTwitterId(final String twitterId) {
		return (Place)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Place.class.getName()+
					" as entityimpl where entityimpl.twitterId = :twitterId");
				
				query.setString("twitterId", twitterId);
				
				Place t = (Place)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}




	@Override
	public List<Place> findBySimillarNameCityState(String name, String city,
			String state) {
		return null;
	}




	@Override
	public List<Place> findByNamePrefix(final String namePrefix) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Place.class.getName()+" as entityimpl " +
							"where entityimpl.name like :namePrefix " +
							"and entityimpl.address is not null");
				query.setString("namePrefix", namePrefix+"%");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}




	@Override
	public List<Place> findByPrimaryKeys(List<Long> ids) {
		final Long[] idsArray = (Long[])ids.toArray(new Long[ids.size()]);

		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=
						session.createCriteria(Place.class)
							.add(Restrictions.in("id",idsArray));

					return criteria.list();				
				}
		});
		
		return result;
	}



	@Override
	public List<Place> findAll() {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Place.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}



	@Override
	public Place findByPrimaryKey(final Long id) {
		return (Place)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Place.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				Place t = (Place)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}



	public List<Place> findByCityState(final String city, final String state) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				Query query = session.createQuery(
						"select entityimpl from "+Place.class.getName()+
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
	public List<Place> findByNameCityState(
			final String name, 
			final String city,
			final String state) {
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				Query query = session.createQuery(
						"select entityimpl from "+Place.class.getName()+
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



	public List<Place> findByCityStateRatingTypePaged(
			final String city, final String state, final String ratingType, 
			int pageNum, final int pageSize,  final String sortField, final boolean isAscending) {
		
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					Criteria criteria=session.createCriteria(Place.class)					
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
					"select count(entityimpl.id) from "+Place.class.getName()+" as entityimpl " +
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
	public List<Place> findByGeoBounding(
			final Double minLat, final Double minLong,
			final Double maxLat, final Double maxLong) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
						"select entityimpl from "+Place.class.getName()+" as entityimpl " +
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



	public void save(Place entity) {
		getHibernateTemplateOverride().save(entity);
	}   

	public void delete(Place entity) {
        getHibernateTemplateOverride().delete(entity);
    }
	
	
	  
}
