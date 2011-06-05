package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Publisher;

import java.util.List;

public interface PublisherDao extends BaseDao<Publisher> {

    List<Publisher> findBySiteName(String name);
}
