package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.SiteDao;
import com.sightlyinc.ratecred.model.Site;


@Service
public class SiteServiceImpl extends AbstractTransactionalService<Site>
		implements SiteService {
	
	@Autowired
	private SiteDao siteDao; 
	

	@Override
	public BaseDao<Site> getDao() {
		return siteDao;
	}

    @Override
	public Site findBySiteUrl(String siteUrl) {
    	throw new RuntimeException("NO IMPL");
	}

	@Override
    public Site findByName(String name) {
    	throw new RuntimeException("NO IMPL");
    }
  

	@Override
	public void delete(Site entity) {		
		super.delete(entity);
	}


}
