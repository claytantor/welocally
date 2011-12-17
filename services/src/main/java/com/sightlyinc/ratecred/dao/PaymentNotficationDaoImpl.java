
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.PaymentNotification;

@Repository("paymentNotificationDao")
public class PaymentNotficationDaoImpl 
extends AbstractDao<PaymentNotification> 
	implements PaymentNotificationDao {


	public PaymentNotficationDaoImpl() {
		super(PaymentNotification.class);
	}

	static Logger logger = Logger.getLogger(PaymentNotficationDaoImpl.class);

	@Override
	public List<PaymentNotification> findByPublisherKey(final String publisherKey) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+PaymentNotification.class.getName()+" as entityimpl " +
							"where entityimpl.publisherKey = :publisherKey");
				
				query.setString("publisherKey", publisherKey);
								
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}

	@Override
	public PaymentNotification findByTxId(final String txId) {
		return (PaymentNotification)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Award.class.getName()+
					" as entityimpl where entityimpl.externalKey = :externalKey");
				
				query.setString("externalKey", txId);
				
				PaymentNotification t = (PaymentNotification)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}


    	
	  
}
