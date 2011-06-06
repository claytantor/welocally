package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;

public interface NetworkMemberService extends BaseService<NetworkMember> {
	
	public Long createPublisherMember(NetworkMember member, Publisher p);
	
	public NetworkMember findMemberByUserPrincipal(UserPrincipal up);
	

}
