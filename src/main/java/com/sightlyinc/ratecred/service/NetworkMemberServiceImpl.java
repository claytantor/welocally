package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.NetworkMemberDao;
import com.sightlyinc.ratecred.model.NetworkMember;

@Service
public class NetworkMemberServiceImpl extends AbstractTransactionalService<NetworkMember>
		implements NetworkMemberService {
	
	@Autowired
	private NetworkMemberDao networkMemberDao; 

	@Override
	public BaseDao<NetworkMember> getDao() {
		return networkMemberDao;
	}

}
