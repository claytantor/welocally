package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
