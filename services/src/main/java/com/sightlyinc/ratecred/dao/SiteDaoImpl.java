
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Site;

@Repository
public class SiteDaoImpl 
	extends AbstractDao<Site> 
	implements SiteDao {

	static Logger logger = Logger.getLogger(SiteDaoImpl.class);
    
   
    
	public SiteDaoImpl() {
		super(Site.class);
	}


    	
	  
}
