package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Affiliate;

public interface AffiliateDao extends BaseDao<Affiliate> {

    public Affiliate findByUrl(String url);
}
