package com.welocally.admin.dao;

import java.util.List;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.welocally.admin.domain.Index;
import com.welocally.admin.security.UserPrincipal;

public interface IndexDao extends BaseDao<Index> {
    public List<Index> findByKey(String key);
    public List<Index> findByOwner(UserPrincipal up);
    public Index findByFeedUrl(String url);
}
