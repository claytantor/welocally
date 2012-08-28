package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.AwardType;

@Repository("awardTypeDao")
public class AwardTypeDaoDefaultImpl 
extends AbstractDao<AwardType>
	implements AwardTypeDao {

	static Logger logger = Logger.getLogger(AwardTypeDaoDefaultImpl.class);
    
	public AwardTypeDaoDefaultImpl() {
		super(AwardType.class);
	}

	
	@Override
	public List<AwardType> findByType(final String type) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+AwardType.class.getName()+" as entityimpl " +
							"where entityimpl.type = :type");
				
				query.setString("type", type);
				
				List list = query.list();
				
	
				return list;
	
			}
		});
		return result;
	}

	
	
	@Override
	public AwardType findByKeyname(final String kn) {
		return (AwardType)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+AwardType.class.getName()+
					" as entityimpl where entityimpl.keyname = :kn");
				
				query.setString("kn", kn);
				
				AwardType t = (AwardType)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}

	
	  
}
