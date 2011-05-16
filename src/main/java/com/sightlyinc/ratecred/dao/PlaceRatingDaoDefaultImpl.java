
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceRating;

@Repository("placeRatingDao")
public class PlaceRatingDaoDefaultImpl 
extends AbstractDao<PlaceRating>  
	implements PlaceRatingDaoOLD {

	static Logger logger = Logger.getLogger(PlaceRatingDaoDefaultImpl.class);
	
	public PlaceRatingDaoDefaultImpl() {
		super(PlaceRating.class);
	}

	@Override
	public List<PlaceRating> findAllPaged(final int pageNum, final int pageSize,
			final boolean isAscending) {
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);

		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				String order = "desc";
				if(isAscending)
					order="asc";
				
				Query query = session.createQuery(
					"select entityimpl from "+PlaceRating.class.getName()+" as entityimpl " +
					" order by entityimpl.rating "+order
					);

				//query.setString("ratingType", type);
				
				query.setMaxResults(pageSize);
				query.setFirstResult(index);

				
				List list = query.list();
	
				return list;
	
			}
		});
		   
		return result;
	}


	@Override
	public List<PlaceRating> findByCityStatePaged(final String city, final String state,
			final int pageNum, final int pageSize, final boolean isAscending) {
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+PlaceRating.class.getName()+" as entityimpl " +
					" where entityimpl.place.city = :city"+
					" and entityimpl.place.state = :state"+
					" order by entityimpl.rating desc"
					);
				query.setString("city", city);
				query.setString("state", state);
				
				query.setMaxResults(pageSize);
				query.setFirstResult(index);

				
				List list = query.list();
	
				return list;
	
			}
		});
		   
		return result;
	}


	@Override
	public List<PlaceRating> findByTypePaged(
			final String type, final int pageNum,
			final int pageSize, final boolean isAscending) {
		
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);

		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				String order = "desc";
				if(isAscending)
					order="asc";
				
				Query query = session.createQuery(
					"select entityimpl from "+PlaceRating.class.getName()+" as entityimpl " +
					" where entityimpl.type = :ratingType" +
					" order by entityimpl.rating "+order
					);

				query.setString("ratingType", type);
				
				query.setMaxResults(pageSize);
				query.setFirstResult(index);

				
				List list = query.list();
	
				return list;
	
			}
		});
		   
		return result;
	}




	public List<PlaceRating> findByCityStateTypePaged(
			final String city, final String state, final String ratingType,
			final int pageNum, final int pageSize,  final String sortField, final boolean isAscending) {
		
		final int index = (pageNum-1)*pageSize;
		logger.debug("pagenum:"+pageNum+" index:"+index);
		
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+PlaceRating.class.getName()+" as entityimpl " +
					" where entityimpl.place.city = :city"+
					" and entityimpl.place.state = :state"+
					" and entityimpl.type = :ratingType" +
					" order by entityimpl.rating desc"
					);
				query.setString("city", city);
				query.setString("state", state);
				query.setString("ratingType", ratingType);
				
				query.setMaxResults(pageSize);
				query.setFirstResult(index);

				
				List list = query.list();
	
				return list;
	
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



	public void save(Place entity) {
		getHibernateTemplateOverride().save(entity);
	}   

	public void delete(Place entity) {
        getHibernateTemplateOverride().delete(entity);
    }
	
	
	  
}
