
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

import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.Place;

@Repository("orderDao")
public class OrderDaoImpl 
	extends AbstractDao<Order> 
	implements OrderDao {

	static Logger logger = Logger.getLogger(OrderDaoImpl.class);
    
   
    
	public OrderDaoImpl() {
		super(Order.class);
	}


	@Override
    public List<Order> findByDaysTrailing(final int days) {
	    List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session)
                throws HibernateException, SQLException 
                {
    
                Query query = session.createQuery(
                    "select entityimpl from "+Order.class.getName()+" as entityimpl " +
                            "where entityimpl.timeCreated > :timeInMillis order by entityimpl.timeCreated desc");
                Long sTime = Calendar.getInstance().getTimeInMillis()-(days*86400000);
                query.setLong("timeInMillis", sTime);
                                
                List list = query.list();
    
                return list;
    
            }
        });
        return result;
    }


    @Override
	//the publisher key is the buyer name, shoudl be refactored
	public List<Order> findByPublisherKey(final String publisherKey) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
							"where entityimpl.buyerKey = :buyerKey");
				
				query.setString("buyerKey", publisherKey);
								
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	@Override
	public Order findByTxId(final String externalTxId) {
		return (Order)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Order.class.getName()+
					" as entityimpl where entityimpl.externalTxId = :externalTxId");
				
				query.setString("externalTxId", externalTxId);
				
				Order t = (Order)query.uniqueResult();
				
			
				return t;
	
			}
		});
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
	public List<Order> findByOwner(final Patron owner) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Order.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :owner");
				
				query.setEntity("owner", owner);
								
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


    	
	  
}
