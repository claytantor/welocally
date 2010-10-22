
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.noi.utility.date.DateUtils;
import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Rater;

public class HibernateComplimentDao 
	extends HibernateDaoSupport 
	implements ComplimentDao {

	static Logger logger = Logger.getLogger(HibernateComplimentDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }
    
    
    
	@Override
	public List<Compliment> findAll() {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Compliment.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}



	@Override
	public Compliment findByPrimaryKey(final Long id) {
		return (Compliment)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Compliment.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				Compliment t = (Compliment)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}

	@Override
	public List<Compliment> findByRaterBetweenTimes(final Rater towards,
			final Date startTime, final Date endTime) {
		
		final Long startTimeMills = startTime.getTime();
		final Long endTimeMills = endTime.getTime();
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Compliment.class.getName()+" as entityimpl " +
							"where entityimpl.towards.owner = :towards" +
							" and entityimpl.timeCreatedMills > :startTime" +
							" and entityimpl.timeCreatedMills < :endTime");
	
					query.setEntity("towards", towards);
					query.setLong("startTime", startTimeMills);
					query.setLong("endTime", endTimeMills);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}



	@Override
	public Long findCountByRaterBetweenTimes(final Rater towards,
			 Date startTime,  Date endTime) {
		
		if(startTime == null)
			startTime = 
				DateUtils.stringToDate("2010-01-01", DateUtils.DESC_SIMPLE_FORMAT);
		if(endTime == null)
			endTime = 
				Calendar.getInstance().getTime();
		
		final Long startTimeMills = startTime.getTime();
		final Long endTimeMills = endTime.getTime();
		
		logger.debug("start:"+startTimeMills.toString()+" end:"+endTimeMills);
		
		Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{

					Query query = session.createQuery(
							"select count(entityimpl.id) from "+Compliment.class.getName()+
							" as entityimpl where entityimpl.towards.owner = :towards" +
							" and entityimpl.timeCreatedMills > :startTime" +
							" and entityimpl.timeCreatedMills < :endTime");
	
					query.setEntity("towards", towards);
					query.setLong("startTime", startTimeMills);
					query.setLong("endTime", endTimeMills);
					
					Long count = 
						(Long)query.uniqueResult();

					return count;
			}
		});
		return result;
	}



	public void save(Compliment entity) {
		logger.debug("compliment time created:"+entity.getTimeCreated());
		logger.debug("compliment time created mills:"+entity.getTimeCreatedMills());
		
		try {
			getHibernateTemplateOverride().saveOrUpdate(entity);
		} catch (Exception e) {
			logger.debug("problem saving", e);
		}
		logger.debug("new id:"+entity.getId());
	}   
	

	
	
	public void delete(Compliment entity) {
        getHibernateTemplateOverride().delete(entity);
    }
	
	
	  
}
