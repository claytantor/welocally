package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.model.Publisher;

import java.util.List;

public interface PublisherService extends BaseService<Publisher> {

    List<Publisher> findBySiteName(String siteName);
}
