package com.sightlyinc.ratecred.authentication;

import java.util.List;

import com.sightlyinc.ratecred.dao.AbstractDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("userPrincipalDao")
public class UserPrincipalDaoDefaultImpl extends AbstractDao<UserPrincipal> implements UserPrincipalDao {

    public UserPrincipalDaoDefaultImpl() {
        super(UserPrincipal.class);
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
