
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Publisher;

import java.util.List;

@Repository("publisherDao")
public class PublisherDaoDefaultImpl 
extends AbstractDao<Publisher>  
	implements PublisherDao {

	static Logger logger = Logger.getLogger(PublisherDaoDefaultImpl.class);
    
    public PublisherDaoDefaultImpl() {
		super(Publisher.class);
	}

    @Override
    public List<Publisher> findBySiteName(String siteName) {
        return (List<Publisher>) getCurrentSession()
            .createCriteria(this.getPersistentClass())
            .add(Restrictions.ilike("siteName", '%' + siteName + '%'))
            .list();
    }
}
