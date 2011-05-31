
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.NetworkMember;

@Repository("networkMemberDao")
public class NetworkMemberDaoDefaultImpl 
extends AbstractDao<NetworkMember>  
	implements NetworkMemberDao {

	static Logger logger = Logger.getLogger(NetworkMemberDaoDefaultImpl.class);
    
    public NetworkMemberDaoDefaultImpl() {
		super(NetworkMember.class);
	}	
	  
}
