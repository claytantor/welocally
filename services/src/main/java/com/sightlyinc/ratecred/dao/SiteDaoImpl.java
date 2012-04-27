
package com.sightlyinc.ratecred.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Site;

@Repository
public class SiteDaoImpl 
	extends AbstractDao<Site> 
	implements SiteDao {

	static Logger logger = Logger.getLogger(SiteDaoImpl.class);
    
   
    
	@Override
    public List<Site> findByUrl(String url) {
        Query q = getSession().createQuery("select e from "+Site.class.getName()+" as e where e.url = :url");
        q.setString("url", url);
        return (List<Site>)q.list();
    }



    public SiteDaoImpl() {
		super(Site.class);
	}


    	
	  
}
