package com.sightlyinc.ratecred.authentication;

import java.util.List;

public interface RoleDao {
	public void save(Role role);
	public void delete(Role role);
	public Role findByPrimaryKey(Long id);
	public List<Role> findAll();
	
}
