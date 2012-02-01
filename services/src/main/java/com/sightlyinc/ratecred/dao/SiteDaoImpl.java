
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Site;

@Repository
public class SiteDaoImpl 
	extends AbstractDao<Site> 
	implements SiteDao {

	static Logger logger = Logger.getLogger(SiteDaoImpl.class);
    
   
    
	public SiteDaoImpl() {
		super(Site.class);
	}


//	@Override
//	//the publisher key is the buyer name, shoudl be refactored
//	public List<Order> findByPublisherKey(final String publisherKey) {
//		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
//			public Object doInHibernate(Session session)
//				throws HibernateException, SQLException 
//				{
//	
//				Query query = session.createQuery(
//					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
//							"where entityimpl.buyerKey = :buyerKey");
//				
//				query.setString("buyerKey", publisherKey);
//								
//				List list = query.list();
//	
//				return list;
//	
//			}
//		});
//		return result;
//	}
//
//
//	@Override
//	public Order findByTxId(final String externalTxId) {
//		return (Order)getHibernateTemplateOverride().execute(new HibernateCallback() {
//			public Object doInHibernate(Session session)
//			throws HibernateException, SQLException 
//			{
//
//				Query query = session.createQuery(
//					"select distinct entityimpl from "+Order.class.getName()+
//					" as entityimpl where entityimpl.externalTxId = :externalTxId");
//				
//				query.setString("externalTxId", externalTxId);
//				
//				Order t = (Order)query.uniqueResult();
//				
//			
//				return t;
//	
//			}
//		});
//	}
//
//
//	@Override
//	public Order findByChannelAndExternalId(final String channel, final String externalId) {
//		return (Order)getHibernateTemplateOverride().execute(new HibernateCallback() {
//			public Object doInHibernate(Session session)
//			throws HibernateException, SQLException 
//			{
//
//				Query query = session.createQuery(
//					"select distinct entityimpl from "+Order.class.getName()+
//					" as entityimpl where entityimpl.channel = :channel and" +
//					" entityimpl.externalId = :externalId");
//				
//				query.setString("channel", channel);
//				query.setString("externalId", externalId);
//				
//				Order t = (Order)query.uniqueResult();
//				
//			
//				return t;
//	
//			}
//		});
//	}
//
//
//	@Override
//	public List<Order> findByOwner(final Patron owner) {
//		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
//			public Object doInHibernate(Session session)
//				throws HibernateException, SQLException 
//				{
//	
//				Query query = session.createQuery(
//					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
//							"where entityimpl.owner = :owner");
//				
//				query.setEntity("owner", owner);
//								
//				List list = query.list();
//	
//				return list;
//	
//			}
//		});
//		return result;
//	}


    	
	  
}
