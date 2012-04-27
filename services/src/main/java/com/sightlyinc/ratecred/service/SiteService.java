package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Site;

public interface SiteService extends BaseService<Site> {
    public List<Site>  findByName(String name);
    public List<Site>  findBySiteUrl(String siteUrl);
    public Long saveSiteWithChecks(Site entity) throws BLServiceException;

}
