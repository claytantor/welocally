
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.model.Publisher;

import java.sql.SQLException;
import java.util.List;

/**
 * how the publisher gets data another shot
 * 
 * @author claygraham
 *
 */
@Repository("publisherDao")
public class PublisherDaoDefaultImpl 
extends AbstractDao<Publisher>  
	implements PublisherDao {

	static Logger logger = Logger.getLogger(PublisherDaoDefaultImpl.class);
    
    public PublisherDaoDefaultImpl() {
		super(Publisher.class);
	}

    @Override
	public Publisher findByPublisherKey(final String key) {
    	return (Publisher)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Publisher.class.getName()+
					" as entityimpl where entityimpl.key = :key");
				
				query.setString("key", key);
				
				
				Publisher t = (Publisher)query.uniqueResult();
							
				return t;
	
			}
		});	
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
    public Publisher findBySiteName(final String name) {

    	return (Publisher)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Publisher.class.getName()+
					" as entityimpl where entityimpl.name = :name");
				
				query.setString("name", name);
				
				Publisher t = (Publisher)query.uniqueResult();
							
				return t;
	
			}
		});		
    }

    @Override
    public List<Publisher> findBySiteNameLike(final String name) {
//        return (List<Publisher>) getCurrentSession()
//            .createCriteria(this.getPersistentClass())
//            .add(Restrictions.ilike("name", '%' + name + '%'))
//            .list();
    	return (List<Publisher>)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Publisher.class.getName()+
					" as entityimpl where entityimpl.name like :name");
				
				query.setString("name", "%"+name+"%");
				
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

    @Override
    public Publisher findBySiteUrl(final String siteUrl) {
        return (Publisher)getHibernateTemplateOverride().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
            throws HibernateException, SQLException
            {

                Query query = session.createQuery(
                    "select distinct pub from "+Publisher.class.getName()+
                    " as pub inner join pub.sites site where site.url = :siteUrl");

                query.setString("siteUrl", siteUrl);

                Publisher t = (Publisher)query.uniqueResult();

                return t;

            }
        });
    }

    @Override
    public List<Publisher> findByMaxServiceEndDateWithNullSimpleGeoToken(long maxServiceEndDate) {
        Criteria criteria = getCurrentSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.isNotNull("simpleGeoJsonToken"));
        criteria.add(Restrictions.le("serviceEndDateMillis", maxServiceEndDate));
        criteria.addOrder(Order.asc("serviceEndDateMillis"));
        criteria.addOrder(Order.asc("name"));
        return (List<Publisher>) criteria.list();
    }
}
