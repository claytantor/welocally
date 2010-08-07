
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

import com.sightlyinc.ratecred.model.PlaceAttribute;

public class HibernatePlaceAttributeDao 
	extends HibernateDaoSupport 
	implements PlaceAttributeDao {

	static Logger logger = Logger.getLogger(HibernatePlaceAttributeDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }
    
    
    
	@Override
	public List<PlaceAttribute> findAll() {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+PlaceAttribute.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}



	@Override
	public PlaceAttribute findByPrimaryKey(final Long id) {
		return (PlaceAttribute)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+PlaceAttribute.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				PlaceAttribute t = (PlaceAttribute)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}


	public void save(PlaceAttribute entity) {
		getHibernateTemplateOverride().saveOrUpdate(entity);
	}   
	

	
	
	public void delete(PlaceAttribute entity) {
        getHibernateTemplateOverride().delete(entity);
    }
	
	
	  
}
