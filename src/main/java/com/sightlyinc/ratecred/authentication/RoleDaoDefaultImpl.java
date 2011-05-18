package com.sightlyinc.ratecred.authentication;

import com.sightlyinc.ratecred.dao.AbstractDao;
import org.springframework.stereotype.Repository;


@Repository("roleDao")
public class RoleDaoDefaultImpl extends AbstractDao<Role> implements RoleDao {
    public RoleDaoDefaultImpl() {
        super(Role.class);
    }
}
