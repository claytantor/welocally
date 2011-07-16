package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.model.Merchant;
import com.sightlyinc.ratecred.model.NetworkMember;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface MerchantService extends BaseService<Merchant> {

	public Merchant findByUrl(String url);
	public Merchant findByNameAndMember(String name, NetworkMember member);
    
}
