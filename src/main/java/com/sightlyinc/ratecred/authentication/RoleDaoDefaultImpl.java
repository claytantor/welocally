package com.sightlyinc.ratecred.authentication;

import com.sightlyinc.ratecred.dao.AbstractDao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository("roleDao")
public class RoleDaoDefaultImpl extends AbstractDao<Role> implements RoleDao {
    public RoleDaoDefaultImpl() {
        super(Role.class);
    }

	@Override
	public Role findByName(String role) {
		return (Role) getCurrentSession()
        .createCriteria(this.getPersistentClass())
        .add(Restrictions.eq("role",role))
        .uniqueResult();
	}
    
    
    
}
