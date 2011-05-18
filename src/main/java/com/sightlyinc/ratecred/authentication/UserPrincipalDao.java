package com.sightlyinc.ratecred.authentication;

import com.sightlyinc.ratecred.dao.BaseDao;

import java.util.List;

public interface UserPrincipalDao extends BaseDao<UserPrincipal> {

	public List<UserPrincipal> findByUserName(String username);
	public UserPrincipal findByAuthId(String authId);
	public UserPrincipal findByEmail(String email);

    
}
