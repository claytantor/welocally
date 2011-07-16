package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Merchant;
import com.sightlyinc.ratecred.model.NetworkMember;

public interface MerchantDao extends BaseDao<Merchant> {

	public Merchant findByUrl(String url);
	
	public Merchant findByNameAndMember(String name, NetworkMember member);
        
}
