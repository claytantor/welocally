
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sightlyinc.ratecred.model.AwardType;

public class HibernateAwardTypeDao 
	extends HibernateDaoSupport 
	implements AwardTypeDao {

	static Logger logger = Logger.getLogger(HibernateAwardTypeDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

	@Override
	public void delete(AwardType entity) {
		getHibernateTemplateOverride().delete(entity);
		
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
	public List<AwardType> findAll() {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+AwardType.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}

	@Override
	public AwardType findByPrimaryKey(final Long id) {
		return (AwardType)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+AwardType.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				AwardType t = (AwardType)query.uniqueResult();
				
			
				return t;
	
			}
		});		
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

	@Override
	public void save(AwardType entity) {
		getHibernateTemplateOverride().save(entity);		
	}
    

	
	
	
	  
}
