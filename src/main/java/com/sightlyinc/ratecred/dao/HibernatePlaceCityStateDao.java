
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PlaceCityState;

/**
 * special implementation to find city states out of ratings, there isnt 
 * a place city state table, and it could be argued that this is required but
 * this will require a refactor
 * 
 * @author claygraham
 *
 */
@Repository("placeCityStateDao")
public class HibernatePlaceCityStateDao 
	extends HibernateDaoSupport 
	implements PlaceCityStateDao {
	
	static Logger logger = Logger.getLogger(HibernatePlaceCityStateDao.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@PostConstruct
	private void initSessionFactory(){
		super.setSessionFactory(sessionFactory);
	}
	
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

	@Override
	public List<PlaceCityState> findByRater(final Patron t) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				
					Query query = 
						session.createSQLQuery("SELECT round(rand()*10000) as id, " +
								"place.city, place.state FROM rating,place,rater " +
								"where rating.place_id = place.id " +
								"and rating.rater_id = rater.id and rater.id = :id " +
								"group by place.city, place.state").addEntity(PlaceCityState.class);

					query.setLong("id", t.getId());
					List list = query.list();
						
					return list;
	
				}
		});
		
		return result;
	}

	public List<PlaceCityState> findAll() {
		
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				//native
				String sql = "select t.ID as {tcs.id}, t.CITY as {tcs.city}," 
			           + " t.STATE as {tcs.state}" 
			           + " from place t group by {tcs.city}, {tcs.state} order by {tcs.state} asc, {tcs.city} asc"; 

				Query query =  
					session.createSQLQuery(sql)
					.addEntity("tcs", PlaceCityState.class); 
			    
			    return query.list();
	
			}
		});
		return result;
		
   }

	@Override
	/**
	 * SELECT count(rating.id) as total_ratings, place.city, place.state FROM place,rating where place.id = rating.place_id 
group by place.city, place.state order by total_ratings desc

	 */
	public List<PlaceCityState> findMostRatedOrdered(final int pageNum, final int pageSize) {
		
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				//native
				String sql = "select pE.ID as {tcs.id}, pE.CITY as {tcs.city}," +
			            " pE.STATE as {tcs.state}, count(tE.id) as tcount" + 
			            " from place pE, rating tE where pE.id = tE.place_id group " +
			           		"by {tcs.city}, {tcs.state} order by tcount desc"; 

				Query query =  
					session.createSQLQuery(sql)
					.addEntity("tcs", PlaceCityState.class); 
				
				query.setFirstResult(pageNum);
				query.setMaxResults(pageSize);
				return query.list();
			    
	
			}
		});
		return result;
	}
	
	
	
	
	
	
	
	
	  
}
