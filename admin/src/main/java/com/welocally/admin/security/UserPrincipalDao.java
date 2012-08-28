package com.welocally.admin.security;

import java.util.List;

import com.sightlyinc.ratecred.dao.BaseDao;

public interface UserPrincipalDao extends BaseDao<UserPrincipal> {

	public UserPrincipal findByUserName(String username);
	public List<UserPrincipal> findByUserNameLike(String username); 
	public UserPrincipal findByAuthId(String authId);
	public UserPrincipal findByEmail(String email);
	public Long saveWithCommit(UserPrincipal entity);
   
}
