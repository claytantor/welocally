
package com.sightlyinc.ratecred.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.noi.utility.hibernate.ImageValue;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.BusinessMetrics;
import com.sightlyinc.ratecred.model.Patron;

@Repository("patronDao")
public class PatronDaoDefaultImpl 
extends AbstractDao<Patron>  
	implements PatronDao {

	static Logger logger = Logger.getLogger(PatronDaoDefaultImpl.class);
    
	public PatronDaoDefaultImpl() {
		super(Patron.class);
	}

	@Override
	public List<Patron> findByBusinessDateRange(
			final Business b, 
			final Date startDate,
			final Date endDate) {
		List result = (List)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{
			
			BusinessMetrics bm = new BusinessMetrics();
			bm.setStartTime(startDate.getTime());

			bm.setEndTime(endDate.getTime());
			
			//native
			String sqlRater = 
				"SELECT "+
					"tr.id, "+ 
					"tr.version, "+ 
					"tr.username, "+ 
					"tr.secretkey, "+ 
					"tr.time_created, "+ 
					"tr.score, "+ 
					"tr.imagevalue_id, "+ 
					"tr.guid, "+ 
					"tr.status "+ 
					"FROM business "+
					"LEFT JOIN business_location bl on (bl.business_id = business.id) "+
					"LEFT JOIN place pl on (pl.business_location_id = bl.id) "+
					"LEFT JOIN rating tl on (tl.place_id = pl.id) "+
					"LEFT JOIN rater tr on (tl.rater_id = tr.id) "+
					"where tl.time_mills > :timeStartMills "+
					"and tl.time_mills < :timeEndMills "+
					"and business.id = :businessId";

			Query query =  
				session.createSQLQuery(sqlRater); 
			query.setLong("timeStartMills", startDate.getTime());
			query.setLong("timeEndMills", endDate.getTime());
			query.setLong("businessId", b.getId());

			List<Object[]> oList = query.list();
			Set<Patron> result = new HashSet<Patron>();
			
			for (Object[] oScalar : oList) 
				result.add(makeRaterFromRow(oScalar,session));
			
			return new ArrayList<Patron>(result);

		}
	});
	return result;
	}

	private Patron makeRaterFromRow(Object[] oScalar, Session session)
	{
		Patron t = new Patron();
		t.setId(((BigInteger)oScalar[0]).longValue());
		t.setVersion((Integer)oScalar[1]);
		t.setUserName((String)oScalar[2]);
		t.setSecretKey((String)oScalar[3]);
		
		//pass a fake date for now [4]
		//Timestamp 2009-08-19 01:31:35.0
		//t.setTimeCreated(Calendar.getInstance().getTime());
		
		t.setScore(((BigInteger)oScalar[5]).longValue());
		
		if(oScalar[6] != null)
		{
			//t.setImageValueId(((BigInteger)oScalar[6]).longValue());	
			//get the image value
			Query queryImage = session.createQuery(
					"select entityimpl from "+ImageValue.class.getName()+
					" as entityimpl where entityimpl.id = :ivid");
				
			queryImage.setLong("ivid", ((BigInteger)oScalar[6]).longValue());
			ImageValue oRaterImage = (ImageValue)queryImage.uniqueResult();
			//t.setRaterImage(oRaterImage);
			throw new RuntimeException("NEED NEW IMAGE IMPL");
		}
	
		
		
				
		t.setAuthGuid((String)oScalar[7]);
		
		if(oScalar[8] != null)
			t.setStatus((String)oScalar[8]);
		
		return t;
	}


	@Override
	public List<Patron> findByBusinessLocationDateRange(final BusinessLocation bl,
			final Date startDate, final Date endDate) {
		List result = (List)getHibernateTemplateOverride().execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				
				BusinessMetrics bm = new BusinessMetrics();

				bm.setStartTime(startDate.getTime());				
				bm.setEndTime(endDate.getTime());
				
				//native
				String sqlRater = 
					"SELECT "+
						"tr.id, "+ 
						"tr.version, "+ 
						"tr.username, "+ 
						"tr.secretkey, "+ 
						"tr.time_created, "+ 
						"tr.score, "+ 
						"tr.imagevalue_id, "+ 
						"tr.guid, "+ 
						"tr.status "+ 
						"FROM business "+
						"LEFT JOIN business_location bl on (bl.business_id = business.id) "+
						"LEFT JOIN place pl on (pl.business_location_id = bl.id) "+
						"LEFT JOIN rating tl on (tl.place_id = pl.id) "+
						"LEFT JOIN rater tr on (tl.rater_id = tr.id) "+
						"where tl.time_mills > :timeStartMills "+
						"and tl.time_mills < :timeEndMills "+
						"and bl.id = :businessLocationId";

				Query query =  
					session.createSQLQuery(sqlRater); 
				
				query.setLong("businessLocationId", bl.getId());
				query.setLong("timeStartMills", startDate.getTime());
				query.setLong("timeEndMills", endDate.getTime());
				
				List<Object[]> oList = query.list();
				Set<Patron> result = new HashSet<Patron>();
				
				for (Object[] oScalar : oList) 
					result.add(makeRaterFromRow(oScalar,session));

				
				return new ArrayList<Patron>(result);
	
			}
		});
		return result;
	}

    @Override
	public List<Patron> findByUserNames(final String[] userNames) {
    	List executeFind = getHibernateTemplateOverride().executeFind(
    	new HibernateCallback<List<Patron>>() {
			@SuppressWarnings("unchecked")
			public List<Patron> doInHibernate(final Session session)
				throws HibernateException, SQLException 
				{
					final Query query = session.createQuery(
						"select entityimpl from "+Patron.class.getName()+
						" as entityimpl where entityimpl.userName in (:userNames)");
					
					query.setParameterList("userNames", userNames);
					return query.list();
				}
    	});
		List<Patron> result = executeFind;
		
		return result;
	}


    public List<Patron> findByStatus(final String status)
    {
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					final Query query = session.createQuery(
						"select entityimpl from "+Patron.class.getName()+
						" as entityimpl where status = :status" );
					
					query.setString("status", status);
					return query.list();					
				}
		});
		
		return result;
    }

	public List<Patron> findByPrimaryKeys(final Long[] ids)
    {
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					List<Patron> raters = new ArrayList<Patron>();
					for (int i = 0; i < ids.length; i++) {
						raters.add((Patron)session.load(Patron.class, ids[i]));
					}
					return raters;						
				}
		});
		
		return result;
    }
	
	
	
	

	@Override
	public Patron findByAuthId(final String authId) {
		Patron result = (Patron)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Patron.class.getName()+
					" as entityimpl where entityimpl.authGuid = :authGuid");
				
				query.setString("authGuid", authId);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;
	}

	@Override
	public Patron findByUserName(final String userName) {
		Patron result = (Patron)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Patron.class.getName()+
					" as entityimpl where entityimpl.userName = :userName");
				
				query.setString("userName", userName);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;	
	}
	
	@Override
	public List<Patron> findByScorePaged(
			final int pageNum, 
			final int pageSize,
			final boolean isAscending) {
		
		final int index = (pageNum-1)*pageSize;
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					String order = "desc";
					if(isAscending)
						order="asc";
					Query query = session.createQuery(
						"select entityimpl from "+Patron.class.getName()+" as entityimpl " +
						" order by entityimpl.score "+order
						);
					query.setFirstResult(index);
					query.setMaxResults(pageSize);
					List list = query.list();
						
					return list;
	
				}
		});
		
		return result;
	}	
	
	
	

	@Override
	public List<Patron> findByCityStateScorePaged(final String city, final String state,
			final int pageNum, final int pageSize, final boolean isAscending) 
	{
		final int index = (pageNum-1)*pageSize;
		//logger.debug("pagenum:"+pageNum+" index:"+index);
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
					String order = "desc";
					if(isAscending)
						order="asc";
				
					Query query = 
						session.createSQLQuery("SELECT rater.id, rater.username, rater.imagevalue_id, rater.secretkey, " +
								"rater.time_created, rater.version, rater.guid, rater.status,rater.auth_foursquare,rater.auth_gowalla, (count(rating.id)*10) as score " +
								"FROM rating,place,rater where rating.place_id = place.id " +
								"and rating.rater_id = rater.id and place.city = :city and place.state =:state " +
								"group by rater.id order by score "+order).addEntity(Patron.class);

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
	public Patron findBySecretKey(final String secretKey) {
		Patron result = (Patron)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Patron.class.getName()+
					" as entityimpl where entityimpl.secretKey = :secretKey");
				
				query.setString("secretKey", secretKey);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;		
	}
	
	
	

	
	



	

	  
}
