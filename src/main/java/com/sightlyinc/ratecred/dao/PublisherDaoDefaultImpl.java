
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.model.Publisher;

import java.sql.SQLException;
import java.util.List;

@Repository("publisherDao")
public class PublisherDaoDefaultImpl 
extends AbstractDao<Publisher>  
	implements PublisherDao {

	static Logger logger = Logger.getLogger(PublisherDaoDefaultImpl.class);
    
    public PublisherDaoDefaultImpl() {
		super(Publisher.class);
	}

    @Override
	public Publisher findByNetworkMemberAndKey(final String networkMemberKey,
			final String key) {
    	return (Publisher)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Publisher.class.getName()+
					" as entityimpl where entityimpl.key = :key" +
					" and entityimpl.networkMember.memberKey = :networkMemberKey");
				
				query.setString("key", key);
				query.setString("networkMemberKey", networkMemberKey);
				
				
				Publisher t = (Publisher)query.uniqueResult();
							
				return t;
	
			}
		});		
	}

	@Override
    public Publisher findBySiteName(final String siteName) {

    	return (Publisher)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Publisher.class.getName()+
					" as entityimpl where entityimpl.siteName = :siteName");
				
				query.setString("siteName", siteName);
				
				Publisher t = (Publisher)query.uniqueResult();
							
				return t;
	
			}
		});		
    }

    @Override
    public List<Publisher> findBySiteNameLike(final String siteName) {
//        return (List<Publisher>) getCurrentSession()
//            .createCriteria(this.getPersistentClass())
//            .add(Restrictions.ilike("siteName", '%' + siteName + '%'))
//            .list();
    	return (List<Publisher>)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Publisher.class.getName()+
					" as entityimpl where entityimpl.siteName like :siteName");
				
				query.setString("siteName", "%"+siteName+"%");
				
				List<Publisher> t = (List<Publisher>)query.list();
							
				return t;
	
			}
		});		    	
    	
    }
    
	@Override
	public List<Publisher> findByUserPrincipal(UserPrincipal up) {
		// TODO Auto-generated method stub
		return null;
	}
}
