package com.sightlyinc.ratecred.service;

import java.util.List;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.model.Publisher;

public interface PublisherService extends BaseService<Publisher> {

    public Publisher findBySiteName(String siteName);
    public List<Publisher>  findBySiteNameLike(String siteName);
    public List<Publisher> findByUserPrincipal(UserPrincipal principal);
}
