package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Site;

public interface SiteDao extends BaseDao<Site> {
    
    public List<Site> findByUrl(String url);

}
