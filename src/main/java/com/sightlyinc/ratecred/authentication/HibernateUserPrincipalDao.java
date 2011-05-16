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



public class HibernateUserPrincipalDao extends HibernateDaoSupport implements UserPrincipalDao {

	static Logger logger = Logger.getLogger(HibernateUserPrincipalDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        //template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }
    	  
    
    
    
	@Override
	public UserPrincipal findByEmail(final String email) {
		
		logger.debug("looking for email:"+email);
		
		UserPrincipal result = (UserPrincipal)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+UserPrincipal.class.getName()+
					" as entityimpl where entityimpl.email = :email");
				
				query.setString("email", email);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;	
	}




	public UserPrincipal create() {
		return new UserPrincipal();
	}

	public void save(UserPrincipal entity) {
		getHibernateTemplateOverride().saveOrUpdate(entity);
	}   
	
	public void detach() {
		super.getSession().disconnect();
	} 
	public void attach() {
		super.getSession().reconnect();
	} 

	public void delete(UserPrincipal entity) {
        getHibernateTemplateOverride().delete(entity);
    }

	public List<UserPrincipal> findAll() {
         return getHibernateTemplateOverride().loadAll(UserPrincipal.class);
    }
    
	public UserPrincipal findByPrimaryKey(final Long id) 
	{
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+UserPrincipal.class.getName()+" as entityimpl where entityimpl.id = :id");
				query.setLong("id", id.intValue());
				List list = query.list();
	
				return list;
	
			}
		});
		
		//we use an iterator so a null value can be returned if
		//it doesn't exist
		Iterator i = result.iterator();
		UserPrincipal bo = null;
		while(i.hasNext())
		{
			 bo = (UserPrincipal)i.next();			
		}
		
		return bo;			
			
    }

	/* (non-Javadoc)
	 * @see com.noi.utility.spring.UserPrincipalDao#findByUserName(java.lang.String)
	 */
	public List<UserPrincipal> findByUserName(final String username) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
			Query query = session.createQuery(
					"select entityimpl from "+UserPrincipal.class.getName()+
					" as entityimpl where entityimpl.username = :username");				
				query.setString("username", username);
				
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
		

	}

	@Override
	public UserPrincipal findByAuthId(final String authId) {
		UserPrincipal result = (UserPrincipal)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+UserPrincipal.class.getName()+
					" as entityimpl where entityimpl.authGuid = :authGuid");
				
				query.setString("authGuid", authId);
				Object oTat = query.uniqueResult();
	
				return oTat;
	
			}
		});
		return result;	
	}		
	
	
	
	

}
