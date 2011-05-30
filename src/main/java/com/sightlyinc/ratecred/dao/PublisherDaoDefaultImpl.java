
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Publisher;

@Repository("publisherDao")
public class PublisherDaoDefaultImpl 
extends AbstractDao<Publisher>  
	implements PublisherDao {

	static Logger logger = Logger.getLogger(PublisherDaoDefaultImpl.class);
    
    public PublisherDaoDefaultImpl() {
		super(Publisher.class);
	}	
	  
}
