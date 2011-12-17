package com.sightlyinc.ratecred.authentication;

import com.sightlyinc.ratecred.dao.BaseDao;

public interface RoleDao extends BaseDao<Role> {
	public Role findByName(String roleName);
	
}
