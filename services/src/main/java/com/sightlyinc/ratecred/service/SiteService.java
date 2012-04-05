package com.sightlyinc.ratecred.service;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Site;

public interface SiteService extends BaseService<Site> {
    public Site findByName(String name);
    public Site findBySiteUrl(String siteUrl);
    public Long saveSiteWithChecks(Site entity) throws BLServiceException;

}
