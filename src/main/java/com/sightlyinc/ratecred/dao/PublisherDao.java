package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.model.Publisher;

import java.util.List;

public interface PublisherDao extends BaseDao<Publisher> {

    public Publisher findBySiteName(String name);
    public List<Publisher> findBySiteNameLike(String siteName);
    public List<Publisher> findByUserPrincipal(UserPrincipal up);
}
