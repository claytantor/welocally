
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.Offer;
import com.sightlyinc.ratecred.model.Order;

@Repository("offerDao")
public class OfferDaoDefaultImpl 
extends AbstractDao<Offer> 
	implements OfferDao {

	static Logger logger = Logger.getLogger(OfferDaoDefaultImpl.class);
	
	public OfferDaoDefaultImpl() {
		super(Offer.class);
	}

	@Override
	public List<Offer> findExpired() {
		final Long now = Calendar.getInstance().getTimeInMillis();
    	
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
						"select distinct entityimpl from "+Offer.class.getName()+
						" as entityimpl where entityimpl.expireDateMillis < :now");
				
				query.setLong("now", now);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}

	
	

	@Override
	public List<Offer> findByBusiness(final Business b) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Offer.class.getName()+" as entityimpl " +
							"where entityimpl.business.id = :businessId");
				
				query.setLong("businessId", b.getId());
				
				List list = query.list();
				
	
				return list;
	
			}
		});
		return result;
	}
	
	

	@Override
	public List<Offer> findByType(final String type) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Offer.class.getName()+" as entityimpl " +
							"where entityimpl.type = :type");
				
				query.setString("type", type);
				
				List list = query.list();
				
	
				return list;
	
			}
		});
		return result;
	}

	
	@Override
	public Offer findByKeyname(final String kn) {
		return (Offer)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Offer.class.getName()+
					" as entityimpl where entityimpl.keyname = :kn");
				
				query.setString("kn", kn);
				
				Offer t = (Offer)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}
}
