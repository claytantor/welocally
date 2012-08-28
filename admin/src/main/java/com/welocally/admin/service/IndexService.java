package com.welocally.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welocally.admin.dao.IndexDao;
import com.welocally.admin.domain.Index;
import com.welocally.admin.security.UserPrincipal;

@Service
@Transactional
public class IndexService {
    
    @Autowired IndexDao indexDao;
    
    public List<Index> findAllByOwner(UserPrincipal up){
        return indexDao.findByOwner(up);
    }
    
    public Index findByWorksheetFeed(String feedUrl){
        return indexDao.findByFeedUrl(feedUrl);
    }
    
    public Long save(Index e){
        indexDao.save(e);
        return e.getId();
    }

}
