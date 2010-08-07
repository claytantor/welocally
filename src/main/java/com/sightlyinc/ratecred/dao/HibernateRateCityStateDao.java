
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateRateCityStateDao 
	extends HibernateDaoSupport 
	implements RatingCityStateDao {

	static Logger logger = Logger.getLogger(HibernateRateCityStateDao.class);
    
    /*public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

	public List<PlaceCityState> findAll() {
		
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
				//native
				String sql = "select distinct uuid() as {tcs.id}, t.CITY as {tcs.city}," 
			           + " t.STATE as {tcs.state}" 
			           + " from rating t order by {tcs.state} asc, {tcs.city} asc"; 

				Query query =  
					session.createSQLQuery(sql)
					.addEntity("tcs", RateCityState.class); 
			    
			    return query.list();
	
			}
		});
		return result;
		
   }*/
	
	
	  
}
