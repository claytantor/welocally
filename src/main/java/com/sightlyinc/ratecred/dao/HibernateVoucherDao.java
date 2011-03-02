
package com.sightlyinc.ratecred.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sightlyinc.ratecred.model.Voucher;
import com.sightlyinc.ratecred.model.Rater;


public class HibernateVoucherDao 
	extends HibernateDaoSupport 
	implements VoucherDao {

	static Logger logger = Logger.getLogger(HibernateVoucherDao.class);
    
    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

 
	@Override
	public void delete(Voucher entity) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public List<Voucher> findAll() {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public Voucher findByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void save(Voucher entity) {
		getHibernateTemplateOverride().save(entity);		
	}
    

	   
    
  /*  @Override
	public List<Voucher> findByOfferExpired() {
    	
    	final Long now = Calendar.getInstance().getTimeInMillis();
    	
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
						"select distinct entityimpl from "+Voucher.class.getName()+
						" as entityimpl where entityimpl.offer.expireDateMillis < :now");
				
				query.setLong("now", now);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
		
	}




	@Override
	public Voucher findByOffer(final VoucherOffer offer) {
    	return (Voucher)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Voucher.class.getName()+
					" as entityimpl where entityimpl.offer = :offer");
				
				query.setEntity("offer", offer);
				
				Voucher t = (Voucher)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}




	@Override
	public List<Voucher> findByOwnerTypePlaceCityState(
			final Rater towards,
			final VoucherType at,
			final PlaceCityState pcs) {
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Voucher.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards and " +
							"entityimpl.awardType = :awardType and " +
							"entityimpl.metadata like %:city% and " +
							"entityimpl.metadata like %:state%");
	
					query.setEntity("towards", towards);
					query.setEntity("awardType", at);
					query.setEntity("city", pcs.getCity());
					query.setEntity("state", pcs.getState());
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	@Override
	public List<Voucher> findByOwnerVoucherType(final Rater towards, final VoucherType at) {
    	List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Voucher.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards and " +
							"entityimpl.awardType = :awardType");
	
					query.setEntity("towards", towards);
					query.setEntity("awardType", at);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	@Override
	public Long findCountByOwnerBetweenTimes(final Rater towards,
			final Date startTime, final Date endTime) {
		
		final Long startTimeMills = startTime.getTime();
		final Long endTimeMills = endTime.getTime();
		
		logger.debug("start:"+startTimeMills.toString()+" end:"+endTimeMills);
		
		Long result = (Long)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{

					Query query = session.createQuery(
							"select count(entityimpl.id) from "+Voucher.class.getName()+
							" as entityimpl where entityimpl.owner = :towards" +
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
    
    
	@Override
	public List<Voucher> findByOwnerBetweenTimes(
			final Rater towards, 
			final Date startTime,
			final Date endTime) {
		final Long startTimeMills = startTime.getTime();
		final Long endTimeMills = endTime.getTime();
		
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Voucher.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards" +
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
	public List<Voucher> findByOwner(final Rater towards) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Voucher.class.getName()+" as entityimpl " +
							"where entityimpl.owner = :towards");
	
					query.setEntity("towards", towards);
				
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}


	@Override
	public List<Voucher> findByBusiness(final Business b) {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Voucher.class.getName()+" as entityimpl " +
							"where entityimpl.offer.business.id = :businessId");
				query.setLong("businessId", b.getId());
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}




	@Override
	public void delete(Voucher entity) {
		getHibernateTemplateOverride().delete(entity);
		
	}
		


	@Override
	public List<Voucher> findAll() {
		List result = getHibernateTemplateOverride().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
				throws HibernateException, SQLException 
				{
	
				Query query = session.createQuery(
					"select entityimpl from "+Voucher.class.getName()+" as entityimpl");
				List list = query.list();
	
				return list;
	
			}
		});
		return result;
	}

	@Override
	public Voucher findByPrimaryKey(final Long id) {
		return (Voucher)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Voucher.class.getName()+
					" as entityimpl where entityimpl.id = :id");
				
				query.setLong("id", id);
				
				Voucher t = (Voucher)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}

	@Override
	public Voucher findByKeyname(final String kn) {
		return (Voucher)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Voucher.class.getName()+
					" as entityimpl where entityimpl.keyname = :kn");
				
				query.setString("kn", kn);
				
				Voucher t = (Voucher)query.uniqueResult();
				
			
				return t;
	
			}
		});		
	}*/


	
	
	  
}
