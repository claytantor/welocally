package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.model.Publisher;

import java.util.List;

public interface PublisherDao extends BaseDao<Publisher> {

    public Publisher findBySiteName(String name);
    public Publisher findByNetworkMemberAndKey(String networkMemberKey, String key);
    public List<Publisher> findBySiteNameLike(String siteName);
    public List<Publisher> findByUserPrincipal(UserPrincipal up);
    public Publisher findByPublisherKey(String key);
    
    Publisher findBySiteUrl(String siteUrl);

    List<Publisher> findByMaxServiceEndDateWithNullSimpleGeoToken(long maxServiceEndDate);
}
