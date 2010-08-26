package com.sightlyinc.ratecred.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.noi.utility.random.RandomMaker;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceRating;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.model.RatingAttribute;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.service.RatingHelper;


public class RaterDataLoadTest extends TestCase {
	
	private static String[] yays =
	{
		"Greeted with smiles", 
		"Host is courteous", 
		"Host is classy", 
		"Seated quickly", 
		"Drinks served quickly", 
		"Great drink mixology", 
		"Great wine suggestion", 
		"Appetizers served quickly",  		
		"Main course served quickly", 
		"Server checked in often",
		"Desert was served quickly",
		"Comp item",  
		"Server favorites will become mine",  
		"Server has great fashion sense",       
		"Server is knowledgeable",
		"Server is courteous"
	};
	
	private static String[] boos =
	{
		"No one greeted me", 
		"Host is arrogant punk",
		"Insane line or wait time", 
		"Drinks served after appetizer",
		"Watered down drink", 
		"Bartender made drink from recipe",
		"Bartender served hipsters first",
		"Bartender kept change as tip",
		"Sommelier drinks PBR", 
		"Sommelier calls you dude", 
		"Had to send back the wine",
		"Appetizers were cold", 
		"Appetizers served with meal",
		"Main course served long after appetizer",
		"Wrong main course served", 
		"Frozen desert was melted",
		"Overcharge",
		"Bill looks like written during seizure",
		"Server made snarky comment"
	};
	
	
	
	/**
	 * 
	 *     Greeted with smiles 
		Host is courteous 
		Host is classy 
		Seated quickly 
		Drinks served quickly 
		Great drink mixology 
		Great wine suggestion 
		Appetizers served quickly  		
		Main course served quickly 
		Server checked in often
		Desert was served quickly
		Comp item  
		Server favorites will become mine  
		Server has great fashion sense       
		Server is knowledgeable
		Server is courteous
		
		Boo
		No one greeted me 
	Host is arrogant punk
	Insane line or wait time 
	Drinks served after appetizer
	Watered down drink 
	Bartender made drink from recipe
	Bartender served hipsters first
	Bartender kept change as tip
	Sommelier drinks PBR 
	Sommelier calls you dude 
	Had to send back the wine
	Appetizers were cold 
	Appetizers served with meal
	Main course served long after appetizer
	Wrong main course served 
	Frozen desert was melted
	Overcharge
	Bill looks like written during seizure
	Server made snarky comment
	Server doesn't know menu 
	Server is a slob
	Had to wave down server 
	Had to go to server station
	Existential breakdown in a soulless prison
    
    
	 * 
	 */
	
	static Logger logger = 
		Logger.getLogger(RaterDataLoadTest.class);
	
	public void testLoadBusinessData()
	{
		
		try {
			SessionFactory sf = new Configuration()
			.configure("hibernate-mysql.cfg.xml")
			.buildSessionFactory();
			
			Session session = sf.getCurrentSession();
			session.beginTransaction();
			
			
			//find the business to rating on
			//Zachary's
			Business b = findBusiness(5l, session);
			logger.debug("got business:"+b.getName());
						
			//raters
			List<Rater> raters = new ArrayList<Rater>();
			for (int i = 0; i < 30; i++) {
				Rater t = getRandomRater(session);
				logger.debug("rater:"+t.getUserName()+" id:"+t.getId());
				for (BusinessLocation bl : b.getLocations()) {
					
					
					//place rating
					/**
					 * place ratings
					 */
					Place p = bl.getPlace();
					
					
					session.save(generateRating(t, bl.getPlace(), session));
				}
			}
	
	        session.getTransaction().commit();

			sf.close();
			
		} catch (HibernateException e) {
			logger.error("cannot init sf",e);
			fail();
		}

	}
	
	private Business findBusiness(Long id,Session session)
	{
		Query query = session.createQuery(
				"select entityimpl from "+Business.class.getName()+
				" as entityimpl where entityimpl.id = :id");
			
		query.setLong("id", id);
		Business oVal = (Business)query.uniqueResult();

		return oVal;
	}
	
/*	private BusinessLocation findBusinessLocation(Long id,Session session)
	{
		Query query = session.createQuery(
				"select entityimpl from "+BusinessLocation.class.getName()+
				" as entityimpl where entityimpl.id = :id");
			
		query.setLong("id", id);
		BusinessLocation oVal = (BusinessLocation)query.uniqueResult();

		return oVal;
	}*/
	
	private Rating generateRating(Rater rater, Place p, Session session)
	{
		//min time 1246646945000
		//max time now
		
		Long timeCreatedMills = RandomMaker.nextLong(
				1246646945000l, 
				Calendar.getInstance().getTimeInMillis());
		
		
		
		Rating t = new Rating();
		t.setOwner(rater);
		
		t.setPlace(p);
		t.setTimeCreatedMills(timeCreatedMills);
		t.setTimeCreated(new Date(timeCreatedMills));
		t.setNotes(RandomMaker.makeSentance(128));
		t.setType("service");
		t.setUserRating(RandomMaker.getFloat());
		
		DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
		double dd = RandomMaker.getFloat()*5.0f;
		Double dd2dec = new Double(df2.format(dd)).doubleValue();
		
		t.setRaterRating(dd2dec.floatValue());		
		t.setAttributes(generateAttributes());
		
		//change the rating
		Set<PlaceRating> ratings = p.getPlaceRatings();
		PlaceRating ratingForType = 
			RatingHelper.computeNewRatingAdd(
					new ArrayList<PlaceRating>(ratings), 
					new ArrayList<Rating>(p.getRatings()), 
					"service", 
					t.getRaterRating());
		
		//this needs to be tested
		ratings.add(ratingForType);
		p.setPlaceRatings(ratings);
		session.save(p);
		
		
		return t;
	}
	
	private Set<RatingAttribute> generateAttributes()
	{
		Set<RatingAttribute> attributes = new HashSet<RatingAttribute>();
		for (int i = 0; i < RandomMaker.nextInt(2, 10); i++) {
			RatingAttribute attribute = new RatingAttribute();
			if(RandomMaker.getBoolean())
			{
				attribute.setType("Yay");
				attribute.setName(yays[RandomMaker.nextInt(0, yays.length)]);
			}
			else
			{
				attribute.setType("Boo");
				attribute.setName(boos[RandomMaker.nextInt(0, boos.length)]);
			}
			attributes.add(attribute);
		}
		return attributes;
	}
	
	private Rater getRandomRater(Session session)
	{
	

		Long id = new Long(RandomMaker.nextInt(50, 550));
		//logger.debug("trying to get rater id:"+id);
		
		Query query = session.createQuery(
				"select entityimpl from "+Rater.class.getName()+
				" as entityimpl where entityimpl.id = :id");
			
		query.setLong("id", id);
		Rater oTat = (Rater)query.uniqueResult();

		return oTat;

	}
	
	

}
