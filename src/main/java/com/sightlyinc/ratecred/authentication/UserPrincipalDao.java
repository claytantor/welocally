package com.sightlyinc.ratecred.authentication;

import java.util.List;

public interface UserPrincipalDao {
	public void save(UserPrincipal user);
	public void delete(UserPrincipal user);
	public UserPrincipal findByPrimaryKey(Long id);
	public List<UserPrincipal> findByUserName(String username);
	public UserPrincipal findByAuthId(String authId);
	public UserPrincipal findByEmail(String email);
	public List<UserPrincipal> findAll();
	
}
