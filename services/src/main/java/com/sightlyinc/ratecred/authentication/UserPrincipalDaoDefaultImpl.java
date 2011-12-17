package com.sightlyinc.ratecred.authentication;

import java.sql.SQLException;
import java.util.List;

import com.sightlyinc.ratecred.dao.AbstractDao;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

@Repository("userPrincipalDao")
public class UserPrincipalDaoDefaultImpl extends AbstractDao<UserPrincipal> implements UserPrincipalDao {

    public UserPrincipalDaoDefaultImpl() {
        super(UserPrincipal.class);
    }

	@Override
	public List<UserPrincipal> findByUserNameLike(final String username) {
		
		//this is returning multiple items
//		List users = (List<UserPrincipal>) getCurrentSession()
//        .createCriteria(this.getPersistentClass())
//        .add(Restrictions.ilike("username", '%' + username + '%'))
//        .list();
//		return users;
		
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
}
