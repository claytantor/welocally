package com.sightlyinc.ratecred.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.ContactDao;
import com.sightlyinc.ratecred.dao.PublisherDao;
import com.sightlyinc.ratecred.model.Contact;
import com.sightlyinc.ratecred.model.Publisher;

@Service
public class PublisherServiceImpl extends AbstractTransactionalService<Publisher>
		implements PublisherService {
	
	@Autowired
	private PublisherDao publisherDao; 
	
	@Autowired
	private ContactDao contactDao; 

	@Override
	public Long save(Publisher entity) {
		if(entity.getContacts()!=null){
			for (Contact c : entity.getContacts()) {
				contactDao.save(c);
			}
		}
		
		return super.save(entity);
	}

	@Override
	public BaseDao<Publisher> getDao() {
		return publisherDao;
	}

    @Override
    public Publisher findByName(String name) {
    	throw new RuntimeException("NO IMPL");
    }

    
    
	@Override
	public Publisher findByNetworkKeyAndPublisherKey(String networkMemberKey,
			String publisherKey) {
		return publisherDao.findByNetworkMemberAndKey(networkMemberKey, publisherKey);
	}


//	@Override
//	public List<Publisher> findByUserPrincipal(UserPrincipal up) {
//		return publisherDao.findByUserPrincipal(up);
//	}

    @Override
    public Publisher findBySiteUrl(String siteUrl) {
        return publisherDao.findBySiteUrl(siteUrl);
    }

    @Override
    public List<Publisher> findExpiringPublishers() {
        long maxServiceEndDate = new Date().getTime();
        maxServiceEndDate += 30l * 24l * 60l * 60l * 1000l;
        return publisherDao.findByMaxServiceEndDateWithNullSimpleGeoToken(maxServiceEndDate);
    }

	@Override
	public Publisher findByPublisherKey(String publisherKey) {
		return publisherDao.findByPublisherKey(publisherKey);
	}

	@Override
	public void delete(Publisher entity) {
		/*Set<Site> sites = entity.getSites();
		for (Site site : sites) {
			siteDao.delete(site);
		}
		entity*/
		
		super.delete(entity);
	}


}
