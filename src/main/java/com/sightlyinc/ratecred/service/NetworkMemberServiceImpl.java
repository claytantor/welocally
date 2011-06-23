package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.NetworkMemberDao;
import com.sightlyinc.ratecred.dao.PublisherDao;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Publisher;

@Service
public class NetworkMemberServiceImpl extends AbstractTransactionalService<NetworkMember>
		implements NetworkMemberService {
	
	@Autowired
	private NetworkMemberDao networkMemberDao; 
	
	@Autowired 
	PublisherDao publisherDao;
	
	
	

	@Override
	public BaseDao<NetworkMember> getDao() {
		return networkMemberDao;
	}

	@Override
	public Long createPublisherMember(NetworkMember member, Publisher p) {
		Publisher pbind = publisherDao.findByPrimaryKey(p.getId());
		member.getPublishers().add(pbind);
		networkMemberDao.save(member);
		return member.getId();
	}

	@Override
	public NetworkMember findMemberByUserPrincipal(UserPrincipal up) {
		return networkMemberDao.findByUserPrincipal(up);
	}
	

}
