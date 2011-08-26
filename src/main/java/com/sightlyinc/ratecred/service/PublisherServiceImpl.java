package com.sightlyinc.ratecred.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.PublisherDao;
import com.sightlyinc.ratecred.model.Publisher;

@Service
public class PublisherServiceImpl extends AbstractTransactionalService<Publisher>
		implements PublisherService {
	
	@Autowired
	private PublisherDao publisherDao; 

	@Override
	public BaseDao<Publisher> getDao() {
		return publisherDao;
	}

    @Override
    public Publisher findBySiteName(String siteName) {
        return publisherDao.findBySiteName(siteName);
    }

    
    
	@Override
	public Publisher findByNetworkKeyAndPublisherKey(String networkMemberKey,
			String publisherKey) {
		return publisherDao.findByNetworkMemberAndKey(networkMemberKey, publisherKey);
	}

	@Override
	public List<Publisher> findBySiteNameLike(String siteName) {
		return publisherDao.findBySiteNameLike(siteName);
	}

	@Override
	public List<Publisher> findByUserPrincipal(UserPrincipal up) {
		return publisherDao.findByUserPrincipal(up);
	}

    @Override
    public Publisher findBySiteUrl(String siteUrl) {
        return publisherDao.findBySiteUrl(siteUrl);
    }


}
