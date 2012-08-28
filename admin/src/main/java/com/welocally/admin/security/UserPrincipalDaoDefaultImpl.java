package com.welocally.admin.security;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.dao.AbstractDao;

@Repository("userPrincipalDao")
public class UserPrincipalDaoDefaultImpl extends AbstractDao<UserPrincipal> implements UserPrincipalDao {

    public UserPrincipalDaoDefaultImpl() {
        super(UserPrincipal.class);
    }

	@Override
	public List<UserPrincipal> findByUserNameLike(final String username) {
				
		return (List<UserPrincipal>)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+UserPrincipal.class.getName()+
					" as entityimpl where entityimpl.username like :username");
				
				query.setString("username", "%"+username+"%");
				
				List<UserPrincipal> t = (List<UserPrincipal>)query.list();
							
				return t;
	
			}
		});		
			
	}

	@Override
	public UserPrincipal findByEmail(final String email) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(UserPrincipal.class);
        criteria.add(Restrictions.eq("email", email));
        return ((UserPrincipal) criteria.uniqueResult());
	}

	/* (non-Javadoc)
	 * @see com.noi.utility.spring.UserPrincipalDao#findByUserName(java.lang.String)
	 */
	public UserPrincipal findByUserName(final String username) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(UserPrincipal.class);
        criteria.add(Restrictions.eq("username", username));
        return ((UserPrincipal) criteria.uniqueResult());
	}

	@Override
	public UserPrincipal findByAuthId(final String authId) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(UserPrincipal.class);
        criteria.add(Restrictions.eq("authGuid", authId));
        return ((UserPrincipal) criteria.uniqueResult());
	}

    public Long saveWithCommit(final UserPrincipal entity) {
        return (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
            throws HibernateException, SQLException 
            {
                session.getTransaction().begin();
                session.save(entity);
                session.getTransaction().commit();
                session.flush();
                                            
                return entity.getId();
                
            }
        }); 
    }	
	
	
	
	
	
	
}
