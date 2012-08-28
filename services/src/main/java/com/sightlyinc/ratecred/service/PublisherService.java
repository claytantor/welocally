package com.sightlyinc.ratecred.service;

import java.util.List;


import com.sightlyinc.ratecred.model.Publisher;

public interface PublisherService extends BaseService<Publisher> {

    public Publisher findByName(String name);
    public Publisher findByNetworkKeyAndPublisherKey(String networkMemberKey, String publisherKey);
    public Publisher findByPublisherKey(String publisherKey); 

    Publisher findBySiteUrl(String siteUrl);

    List<Publisher> findExpiringPublishers();
}
