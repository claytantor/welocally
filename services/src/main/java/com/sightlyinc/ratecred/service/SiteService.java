package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.model.Site;

public interface SiteService extends BaseService<Site> {
    public Site findByName(String name);
    public Site findBySiteUrl(String siteUrl);

}
