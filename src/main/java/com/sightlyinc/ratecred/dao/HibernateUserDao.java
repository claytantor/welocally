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

import com.sightlyinc.ratecred.model.User;


public class HibernateUserDao extends HibernateDaoSupport implements UserDao {
	
	static Logger logger = Logger.getLogger(HibernateUserDao.class);
	
	public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

	
	
	@Override
	public List<User> findByTwitterIds(final Long[] twitterIds) {
		return (List<User>)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+User.class.getName()+
					" as entityimpl where entityimpl.twitterId in (:twitterIds)");
				
				query.setParameterList("twitterIds", twitterIds);
			
				return query.list();
	
			}
		});		
	}



	@Override
	public User findByTwitterScreenName(final String twitterScreenName) {
		return (User)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+User.class.getName()+
					" as entityimpl where entityimpl.twitterScreenName = :twitterScreenName");
				
				query.setString("twitterScreenName", twitterScreenName);
				
				User t = (User)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}



	@Override
	public User findById(final Long id) {
		return (User)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+User.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				User t = (User)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}

	@Override
	public User findByUsername(final String username) {
		return (User)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+User.class.getName()+
					" as entityimpl where entityimpl.userName = :username");
				
				query.setString("username", username);
				
				User t = (User)query.uniqueResult();
				
			
				return t;
	
			}
		});	
	}
	
	

	@Override
	public Long save(User user) {
		
		if(user.getId() != null)
			logger.debug("update user with id:"+user.getId());
		else
			logger.debug("create user");
		
		getHibernateTemplateOverride().save(user);
		return user.getId();
	}



	@Override
	public void delete(User u) {
		getHibernateTemplateOverride().delete(u);
		
	}
	
	
	
}
