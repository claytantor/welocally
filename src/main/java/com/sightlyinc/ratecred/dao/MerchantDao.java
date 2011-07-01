package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Merchant;

public interface MerchantDao extends BaseDao<Merchant> {

    public Merchant findByUrl(String url);
}
