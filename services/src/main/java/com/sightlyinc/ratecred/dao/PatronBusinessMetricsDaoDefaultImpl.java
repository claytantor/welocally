
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.pojo.PatronBusinessMetrics;

@Repository
public class PatronBusinessMetricsDaoDefaultImpl
	extends AbstractDao<PatronBusinessMetrics>
	implements PatronBusinessMetricsDao {

	static Logger logger = Logger.getLogger(PatronBusinessMetricsDaoDefaultImpl.class);

    public PatronBusinessMetricsDaoDefaultImpl() {
        super(PatronBusinessMetrics.class);
    }

    public HibernateTemplate getHibernateTemplateOverride() {
        HibernateTemplate template = getHibernateTemplate();
        template.setFlushMode(HibernateTemplate.FLUSH_AUTO);
        return template;
    }

    
    
    
    @SuppressWarnings("unchecked")
	@Override
	public List<PatronBusinessMetrics> mineBusinessMetricsForRaters(
			final Business b, final Long[] raterIds, final Date startDate, final Date endDate) {
		List<PatronBusinessMetrics> execute = 
			(List<PatronBusinessMetrics>)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(final Session session)
				throws HibernateException, SQLException 
				{
					final List<PatronBusinessMetrics> result = new ArrayList<PatronBusinessMetrics>();
					
					for(Long raterId: raterIds)
					{
						//get the metrics for each rater
						String q2 = 
							"SELECT "+ 
								"rater.id, "+ 
							    "rater.username, "+  
							  	"rater.score, "+ 
							  	"rater_image.rater_avatar_filename, "+
							  	"rater.status, "+ 
							  	"rating_avg_rating.avg_rating, "+ 
								"yay_count.yays, "+  
								"boo_count.boos, "+ 
								"rating_count.ratings, "+
							    "awards_active.active, "+ 
							    "awards_used.used, "+ 
							    "awards_expired.aexpired "+	
							"from rater, "+ 
							"( "+ 
							"SELECT avg(rating.rater_rating) as avg_rating "+ 
							"FROM rater,rating, place, business_location "+  
								"where business_location.business_id = :businessId "+ 
								"and rater.id=:raterId "+ 
								"and rating.rater_id = rater.id "+ 
							    "and place.id = business_location.place_id "+ 
								"and place.id = rating.place_id "+ 
								"and rating.time_mills>:timeStartMills "+ 
								"and rating.time_mills<:timeEndMills "+ 
							") as rating_avg_rating, "+	
							"( "+ 
								"SELECT count(rating_attribute.id) as yays "+ 
								"FROM rater,rating, rating_attribute, place, business_location "+  
									"where business_location.business_id = :businessId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
								    "and rating.id = rating_attribute.rating_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
									"and rating_attribute.type = 'Yay' "+ 
							") as yay_count, "+ 
							"( "+ 
								"SELECT count(rating_attribute.id) as boos "+ 
								"FROM rater,rating, rating_attribute, place, business_location "+  
									"where business_location.business_id = :businessId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
								    "and rating.id = rating_attribute.rating_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
									"and rating_attribute.type = 'Boo' "+ 
							") as boo_count, "+								
							"( "+ 
								"SELECT count(rating.id) as ratings "+ 
								"FROM rater,rating, place, business_location "+  
									"where business_location.business_id = :businessId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
							") as rating_count, "+ 
							"( "+ 
								"SELECT count(award.id) as active "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_id = :businessId "+ 
									"and rater.id=:raterId "+ 
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.expires_mills>:timeEndMills "+ 
								    "and award.status='ACTIVE' "+ 
							") as awards_active, "+ 
							"( "+ 
								"SELECT count(award.id) as used "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_id = :businessId "+ 
									"and rater.id=:raterId "+  
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.rater_id=rater.id "+ 
									"and award.status='USED' "+ 
							") as awards_used, "+ 
							"( "+ 
								"SELECT count(award.id) as aexpired "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_id = :businessId "+ 
									"and rater.id=:raterId "+  
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.expires_mills<:timeEndMills "+ 
									"and award.status='ACTIVE' "+ 
							") as awards_expired, "+ 							
							"( "+ 
								"SELECT ca.filename as rater_avatar_filename "+
								"FROM rater "+	
								"LEFT JOIN image_value ca on (rater.imagevalue_id = ca.id) "+
								"where rater.id=:raterId "+ 
							") as rater_image "+ 
							"where rater.id = :raterId" ;
						
						Query queryRater =  
							session.createSQLQuery(q2); 
						queryRater.setLong("businessId", b.getId());
						queryRater.setLong("timeStartMills", startDate.getTime());
						queryRater.setLong("timeEndMills", endDate.getTime());
						queryRater.setLong("raterId", raterId);
						
						String out = "";
						
						//should only be one row each
						List rRater = queryRater.list();
						result.add(
								makeFromResult(
										rRater, 
										raterId, 
										startDate, 
										endDate));
					}
					
					return result;				
				}
		});
		List<PatronBusinessMetrics> result = 
			execute;
		return result;
	}




	@SuppressWarnings("unchecked")
	@Override
	public List<PatronBusinessMetrics> mineBusinessMetricsForDateRange(
			final Business b, final Date startDate, final Date endDate) {
		
		List<PatronBusinessMetrics> execute = (List<PatronBusinessMetrics>)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(final Session session)
				throws HibernateException, SQLException 
				{
					final List<PatronBusinessMetrics> result = new ArrayList<PatronBusinessMetrics>();
					//now the number of ratings						
					String q1 = 
						"SELECT "+
							"rater.id, " +
							"rating.time_created, "+
							"business_location.id, " +
							"business_location.business_id "+					
						"FROM rater, rating, place, business_location " +
						"where rating.rater_id = rater.id " +
						"and place.id = business_location.place_id " +
						"and place.id = rating.place_id "+
						"and rating.time_mills > :timeStartMills "+
						"and rating.time_mills < :timeEndMills "+
						"and business_location.business_id = :businessId " +
						"group by rater_id"; 
											
					Query queryRates =  
						session.createSQLQuery(q1); 
					queryRates.setLong("businessId", b.getId());
					queryRates.setLong("timeStartMills", startDate.getTime());
					queryRates.setLong("timeEndMills", endDate.getTime());
					
					List<Object[]> rows = (List<Object[]>)queryRates.list();
					logger.debug("rows:"+rows.size());
					for(Object[] cols: rows)
					{
						//get the metrics for each rater
						String q2 = 
							"SELECT "+ 
								"rater.id, "+ 
							    "rater.username, "+  
							  	"rater.score, "+ 
							  	"rater_image.rater_avatar_filename, "+
							  	"rater.status, "+ 
							  	"rating_avg_rating.avg_rating, "+ 
								"yay_count.yays, "+  
								"boo_count.boos, "+ 
								"rating_count.ratings, "+
							    "awards_active.active, "+ 
							    "awards_used.used, "+ 
							    "awards_expired.aexpired "+	
							"from rater, "+ 
							"( "+ 
							"SELECT avg(rating.rater_rating) as avg_rating "+ 
							"FROM rater,rating, place, business_location "+  
								"where business_location.business_id = :businessId "+ 
								"and rater.id=:raterId "+ 
								"and rating.rater_id = rater.id "+ 
							    "and place.id = business_location.place_id "+ 
								"and place.id = rating.place_id "+ 
								"and rating.time_mills>:timeStartMills "+ 
								"and rating.time_mills<:timeEndMills "+ 
							") as rating_avg_rating, "+	
							"( "+ 
								"SELECT count(rating_attribute.id) as yays "+ 
								"FROM rater,rating, rating_attribute, place, business_location "+  
									"where business_location.business_id = :businessId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
								    "and rating.id = rating_attribute.rating_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
									"and rating_attribute.type = 'Yay' "+ 
							") as yay_count, "+ 
							"( "+ 
								"SELECT count(rating_attribute.id) as boos "+ 
								"FROM rater,rating, rating_attribute, place, business_location "+  
									"where business_location.business_id = :businessId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
								    "and rating.id = rating_attribute.rating_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
									"and rating_attribute.type = 'Boo' "+ 
							") as boo_count, "+								
							"( "+ 
								"SELECT count(rating.id) as ratings "+ 
								"FROM rater,rating, place, business_location "+  
									"where business_location.business_id = :businessId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
							") as rating_count, "+ 
							"( "+ 
								"SELECT count(award.id) as active "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_id = :businessId "+ 
									"and rater.id=:raterId "+ 
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.expires_mills>:timeEndMills "+ 
								    "and award.status='ACTIVE' "+ 
							") as awards_active, "+ 
							"( "+ 
								"SELECT count(award.id) as used "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_id = :businessId "+ 
									"and rater.id=:raterId "+  
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.rater_id=rater.id "+ 
									"and award.status='USED' "+ 
							") as awards_used, "+ 
							"( "+ 
								"SELECT count(award.id) as aexpired "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_id = :businessId "+ 
									"and rater.id=:raterId "+  
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.expires_mills<:timeEndMills "+ 
									"and award.status='ACTIVE' "+ 
							") as awards_expired, "+ 							
							"( "+ 
								"SELECT ca.filename as rater_avatar_filename "+
								"FROM rater "+	
								"LEFT JOIN image_value ca on (rater.imagevalue_id = ca.id) "+
								"where rater.id=:raterId "+ 
							") as rater_image "+ 
							"where rater.id = :raterId" ;
						
						Query queryRater =  
							session.createSQLQuery(q2); 
						queryRater.setLong("businessId", b.getId());
						queryRater.setLong("timeStartMills", startDate.getTime());
						queryRater.setLong("timeEndMills", endDate.getTime());
						queryRater.setLong("raterId", Long.parseLong(cols[0].toString()));
						
						String out = "";
						
						//should only be one row each
						List rRater = queryRater.list();
						result.add(
								makeFromResult(
										rRater, 
										Long.parseLong(cols[0].toString()), 
										startDate, 
										endDate));
					}
					
					return result;				
				}
		});
		List<PatronBusinessMetrics> result = 
			execute;
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PatronBusinessMetrics> mineBusinessLocationMetricsForRaters(
			final BusinessLocation bl, final Long[] raterIds, final Date startDate, final Date endDate) {
		List<PatronBusinessMetrics> execute = (List<PatronBusinessMetrics>)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(final Session session)
				throws HibernateException, SQLException 
				{
					final List<PatronBusinessMetrics> result = new ArrayList<PatronBusinessMetrics>();

					for(Long raterId: raterIds)
					{
						
						//ok lets get hardcore							
												
						//get the metrics for each rater
						String q2 = 
							"SELECT "+ 
								"rater.id, "+ 
							    "rater.username, "+  
							  	"rater.score, "+ 
							  	"rater_image.rater_avatar_filename, "+
							  	"rater.status, "+ 
							  	"rating_avg_rating.avg_rating, "+ 
								"yay_count.yays, "+  
								"boo_count.boos, "+ 
								"rating_count.ratings, "+  
							    "awards_active.active, "+ 
							    "awards_used.used, "+ 
							    "awards_expired.aexpired "+
							"from rater, "+ 
							"( "+ 
							"SELECT avg(rating.rater_rating) as avg_rating "+ 
							"FROM rater,rating, place, business_location "+  
								"where business_location.id= :businessLocationId "+ 
								"and rater.id=:raterId "+ 
								"and rating.rater_id = rater.id "+ 
							    "and place.id = business_location.place_id "+ 
								"and place.id = rating.place_id "+ 
								"and rating.time_mills>:timeStartMills "+ 
								"and rating.time_mills<:timeEndMills "+ 
							") as rating_avg_rating, "+	
							"( "+ 
								"SELECT count(rating_attribute.id) as yays "+ 
								"FROM rater,rating, rating_attribute, place, business_location "+  
									"where business_location.id= :businessLocationId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
								    "and rating.id = rating_attribute.rating_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
									"and rating_attribute.type = 'Yay' "+ 
							") as yay_count, "+ 
							"( "+ 
								"SELECT count(rating_attribute.id) as boos "+ 
								"FROM rater,rating, rating_attribute, place, business_location "+  
									"where business_location.id= :businessLocationId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
								    "and rating.id = rating_attribute.rating_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
									"and rating_attribute.type = 'Boo' "+ 
							") as boo_count, "+								
							"( "+ 
								"SELECT count(rating.id) as ratings "+ 
								"FROM rater,rating, place, business_location "+  
									"where business_location.id= :businessLocationId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
							") as rating_count, "+ 
							"( "+ 
								"SELECT count(award.id) as active "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_location_id= :businessLocationId "+
									"and rater.id=:raterId "+ 
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.expires_mills>:timeEndMills "+ 
								    "and award.status='ACTIVE' "+ 
							") as awards_active, "+ 
							"( "+ 
								"SELECT count(award.id) as used "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_location_id= :businessLocationId "+
									"and rater.id=:raterId "+  
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.rater_id=rater.id "+ 
									"and award.status='USED' "+ 
							") as awards_used, "+ 
							"( "+ 
								"SELECT count(award.id) as aexpired "+  
								"FROM rater, award, award_offer "+   
								"where award_offer.business_location_id= :businessLocationId "+
									"and rater.id=:raterId "+  
									"and award.rater_id=rater.id "+
								    "and award.award_offer_id=award_offer.id "+  
									"and award.expires_mills<:timeEndMills "+ 
									"and award.status='ACTIVE' "+ 
							") as awards_expired, "+ 															
							"( "+ 
								"SELECT ca.filename as rater_avatar_filename "+
								"FROM rater "+	
								"LEFT JOIN image_value ca on (rater.imagevalue_id = ca.id) "+
								"where rater.id=:raterId "+ 
							") as rater_image "+ 
							"where rater.id = :raterId" ;
						
						Query queryRater =  
							session.createSQLQuery(q2); 
						queryRater.setLong("businessLocationId", bl.getId());
						queryRater.setLong("timeStartMills", startDate.getTime());
						queryRater.setLong("timeEndMills", endDate.getTime());
						queryRater.setLong("raterId", raterId);
						
						String out = "";
						
						//should only be one row each
						List rRater = queryRater.list();

						result.add(
								makeFromResult(
										rRater, 
										raterId, 
										startDate, 
										endDate));
					}
					
					return result;				
				}
	});
	List<PatronBusinessMetrics> result = 
		execute;
	return result;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<PatronBusinessMetrics> mineBusinessLocationMetricsForDateRange(
			final BusinessLocation bl, final Date startDate, final Date endDate) {
		List<PatronBusinessMetrics> execute = (List<PatronBusinessMetrics>)getHibernateTemplateOverride().execute(new HibernateCallback() {
				public Object doInHibernate(final Session session)
					throws HibernateException, SQLException 
					{
						final List<PatronBusinessMetrics> result = new ArrayList<PatronBusinessMetrics>();
						//now the number of ratings						
						String q1 = 
							"SELECT "+
								"rater.id, " +
								"rating.time_created, "+
								"business_location.id, " +
								"business_location.business_id "+
							"FROM rater, rating, place, business_location " +
							"where rating.rater_id = rater.id " +
							"and place.id = business_location.place_id " +
							"and place.id = rating.place_id "+
							"and rating.time_mills > :timeStartMills "+
							"and rating.time_mills < :timeEndMills "+
							"and business_location.id = :businessLocationId " +
							"group by rater_id"; 
												
						Query queryRates =  
							session.createSQLQuery(q1); 
						queryRates.setLong("businessLocationId", bl.getId());
						queryRates.setLong("timeStartMills", startDate.getTime());
						queryRates.setLong("timeEndMills", endDate.getTime());
						
						List<Object[]> rows = (List<Object[]>)queryRates.list();
						logger.debug("rows:"+rows.size());
						for(Object[] cols: rows)
						{
							
							//ok lets get hardcore							
													
							//get the metrics for each rater
							String q2 = 
								"SELECT "+ 
									"rater.id, "+ 
								    "rater.username, "+  
								  	"rater.score, "+ 
								  	"rater_image.rater_avatar_filename, "+
								  	"rater.status, "+ 
								  	"rating_avg_rating.avg_rating, "+ 
									"yay_count.yays, "+  
									"boo_count.boos, "+ 
									"rating_count.ratings, "+  
								    "awards_active.active, "+ 
								    "awards_used.used, "+ 
								    "awards_expired.aexpired "+
								"from rater, "+ 
								"( "+ 
								"SELECT avg(rating.rater_rating) as avg_rating "+ 
								"FROM rater,rating, place, business_location "+  
									"where business_location.id= :businessLocationId "+ 
									"and rater.id=:raterId "+ 
									"and rating.rater_id = rater.id "+ 
								    "and place.id = business_location.place_id "+ 
									"and place.id = rating.place_id "+ 
									"and rating.time_mills>:timeStartMills "+ 
									"and rating.time_mills<:timeEndMills "+ 
								") as rating_avg_rating, "+	
								"( "+ 
									"SELECT count(rating_attribute.id) as yays "+ 
									"FROM rater,rating, rating_attribute, place, business_location "+  
										"where business_location.id= :businessLocationId "+ 
										"and rater.id=:raterId "+ 
										"and rating.rater_id = rater.id "+ 
									    "and place.id = business_location.place_id "+ 
										"and place.id = rating.place_id "+ 
									    "and rating.id = rating_attribute.rating_id "+ 
										"and rating.time_mills>:timeStartMills "+ 
										"and rating.time_mills<:timeEndMills "+ 
										"and rating_attribute.type = 'Yay' "+ 
								") as yay_count, "+ 
								"( "+ 
									"SELECT count(rating_attribute.id) as boos "+ 
									"FROM rater,rating, rating_attribute, place, business_location "+  
										"where business_location.id= :businessLocationId "+ 
										"and rater.id=:raterId "+ 
										"and rating.rater_id = rater.id "+ 
									    "and place.id = business_location.place_id "+ 
										"and place.id = rating.place_id "+ 
									    "and rating.id = rating_attribute.rating_id "+ 
										"and rating.time_mills>:timeStartMills "+ 
										"and rating.time_mills<:timeEndMills "+ 
										"and rating_attribute.type = 'Boo' "+ 
								") as boo_count, "+								
								"( "+ 
									"SELECT count(rating.id) as ratings "+ 
									"FROM rater,rating, place, business_location "+  
										"where business_location.id= :businessLocationId "+ 
										"and rater.id=:raterId "+ 
										"and rating.rater_id = rater.id "+ 
									    "and place.id = business_location.place_id "+ 
										"and place.id = rating.place_id "+ 
										"and rating.time_mills>:timeStartMills "+ 
										"and rating.time_mills<:timeEndMills "+ 
								") as rating_count, "+ 
								"( "+ 
									"SELECT count(award.id) as active "+  
									"FROM rater, award, award_offer "+   
									"where award_offer.business_location_id= :businessLocationId "+
										"and rater.id=:raterId "+ 
										"and award.rater_id=rater.id "+
									    "and award.award_offer_id=award_offer.id "+  
										"and award.expires_mills>:timeEndMills "+ 
									    "and award.status='ACTIVE' "+ 
								") as awards_active, "+ 
								"( "+ 
									"SELECT count(award.id) as used "+  
									"FROM rater, award, award_offer "+   
									"where award_offer.business_location_id= :businessLocationId "+
										"and rater.id=:raterId "+  
										"and award.rater_id=rater.id "+
									    "and award.award_offer_id=award_offer.id "+  
										"and award.rater_id=rater.id "+ 
										"and award.status='USED' "+ 
								") as awards_used, "+ 
								"( "+ 
									"SELECT count(award.id) as aexpired "+  
									"FROM rater, award, award_offer "+   
									"where award_offer.business_location_id= :businessLocationId "+
										"and rater.id=:raterId "+  
										"and award.rater_id=rater.id "+
									    "and award.award_offer_id=award_offer.id "+  
										"and award.expires_mills<:timeEndMills "+ 
										"and award.status='ACTIVE' "+ 
								") as awards_expired, "+ 															
								"( "+ 
									"SELECT ca.filename as rater_avatar_filename "+
									"FROM rater "+	
									"LEFT JOIN image_value ca on (rater.imagevalue_id = ca.id) "+
									"where rater.id=:raterId "+ 
								") as rater_image "+ 
								"where rater.id = :raterId" ;
							
							Query queryRater =  
								session.createSQLQuery(q2); 
							queryRater.setLong("businessLocationId", bl.getId());
							queryRater.setLong("timeStartMills", startDate.getTime());
							queryRater.setLong("timeEndMills", endDate.getTime());
							queryRater.setLong("raterId", Long.parseLong(cols[0].toString()));
							
							String out = "";
							
							//should only be one row each
							List rRater = queryRater.list();

							result.add(
									makeFromResult(
											rRater, 
											Long.parseLong(cols[0].toString()), 
											startDate, 
											endDate));
						}
						
						return result;				
					}
		});
		List<PatronBusinessMetrics> result = 
			execute;
		return result;
	}
	
	
	private PatronBusinessMetrics makeFromResult(List rRater, Long id, Date startDate, Date endDate)
	{
		PatronBusinessMetrics tbm = new PatronBusinessMetrics();
		tbm.setRaterId(id);
		tbm.setStartTime(startDate);
		tbm.setStartTimeMills(startDate.getTime());
		
		tbm.setEndTime(endDate);
		tbm.setEndTimeMills(endDate.getTime());
		
		
		if(rRater.size()>0) {
			Object[] rowRater = (Object[])rRater.get(0);
			tbm.setRaterUserName(rowRater[1].toString());
			tbm.setScore(Integer.parseInt(rowRater[2].toString()));
			
			if(rowRater[3] != null)
				tbm.setRaterImageFilename(rowRater[3].toString());
			
			tbm.setRatingAverage(Float.parseFloat(rowRater[5].toString()));
			tbm.setYays(Integer.parseInt(rowRater[6].toString()));
			tbm.setBoos(Integer.parseInt(rowRater[7].toString()));
			tbm.setRatings(Integer.parseInt(rowRater[8].toString()));
			
			tbm.setAwardsActive(Integer.parseInt(rowRater[9].toString()));
			tbm.setAwardsUsed(Integer.parseInt(rowRater[10].toString()));
			tbm.setAwardsExpired(Integer.parseInt(rowRater[11].toString()));
			
			
		}
		

		
		return tbm;
	}
    
    
    
    
	
	  
}
