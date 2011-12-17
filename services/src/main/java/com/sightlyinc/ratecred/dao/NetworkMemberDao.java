package com.sightlyinc.ratecred.dao;


import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.model.NetworkMember;

public interface NetworkMemberDao extends BaseDao<NetworkMember> {
	
	public NetworkMember findByUserPrincipal(UserPrincipal up);

}
