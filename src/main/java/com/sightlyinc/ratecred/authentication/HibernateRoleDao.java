package com.sightlyinc.ratecred.authentication;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;



public class HibernateRoleDao extends HibernateDaoSupport implements RoleDao {

	static Logger logger = Logger.getLogger(HibernateRoleDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        //template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }
    	    
	public Role create() {
		return new Role();
	}

	public void save(Role entity) {
		getHibernateTemplateOverride().saveOrUpdate(entity);
	}   
	
	public void detach() {
		super.getSession().disconnect();
	} 
	public void attach() {
		super.getSession().reconnect();
	} 

	public void delete(Role entity) {
        getHibernateTemplateOverride().delete(entity);
    }

	public List<Role> findAll() {
         return getHibernateTemplateOverride().loadAll(Role.class);
    }
    
	public Role findByPrimaryKey(final Long id) 
	{
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Role.class.getName()+" as entityimpl where entityimpl.id = :id");
				query.setLong("id", id.intValue());
				List list = query.list();
	
				return list;
	
			}
		});
		
		//we use an iterator so a null value can be returned if
		//it doesn't exist
		Iterator i = result.iterator();
		Role bo = null;
		while(i.hasNext())
		{
			 bo = (Role)i.next();			
		}
		
		return bo;			
			
    }

	
	
	
	
	

}
