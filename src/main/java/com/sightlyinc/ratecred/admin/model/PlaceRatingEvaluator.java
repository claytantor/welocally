package com.sightlyinc.ratecred.admin.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.sightlyinc.ratecred.admin.compare.DescendingAwardDateComparitor;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.service.AwardsUtils;

public class PlaceRatingEvaluator {
	
	static Logger logger = Logger.getLogger(PlaceRatingEvaluator.class);
	
	private Place place;
	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	//heavy lifting in constructor this may be served better by
	//sending all ratings for the city state
	public PlaceRatingEvaluator(Place p) {
		this.place = p;
	}
	
	
	public boolean isPlace(int placeId)
	{
		return this.place.getId().equals(new Long(placeId));
	}
	
	/**
	 * this will look at all the awards a rater has gotten, it will filter 
	 * out just the awards related to the place, then it will filter
	 * the ratings after the last award that expired for this place to 
	 * determine a count
	 * 
	 * @param ra
	 * @return
	 */
	public int getRatingCount(RaterAwards ra)
	{
		logger.debug("getRatingCount");
		List<Award> placeAwards = new ArrayList<Award>();
		for (Award a : ra.getRater().getAwards()) {
			Long awardPlaceId = AwardsUtils.getPlaceIdMetaData(a.getMetadata());
			if(awardPlaceId != null && awardPlaceId.equals(this.place.getId()))
				placeAwards.add(a);
		}
		//if no awards you can count all 
		//otherwise you have to find the last expired
		//award from this place and only count after that
		//date
		Integer count = 0;
		if(placeAwards.size()==0)
		{
			count = countRatingsByPlaceId(
					new ArrayList<Rating>(ra.getRater().getRatings()), 
					this.place.getId());
			
			
		} else {
			Long since = getLastAwardDate(placeAwards);
			Date sd = new Date(since);
			logger.debug("get since:"+sd.toString());
			count =  countRatingsByPlaceId(
					filterRatingsSince(
							new ArrayList<Rating>(ra.getRater().getRatings()), since), 
					this.place.getId());
		}
		
		logger.debug("count since last expired award:"+count);
		return count;

	}
	
	//THIS CANT BE GOOD
	private Long getLastAwardDate(List<Award> awards)
	{
		Long largest = -1l;
		for (Award award : awards) {
			for (AwardOffer offer : award.getOffers()) {
				if(offer.getExpireDateMillis()>largest)
					largest = offer.getExpireDateMillis();
			}			
		}		
		return largest;
	}
	
	private List<Rating> filterRatingsSince(List<Rating> ratings,Long since)
	{
		ArrayList<Rating> filtered = new ArrayList<Rating>();
		for (Rating rating : ratings) {
			if(rating.getTimeCreatedMills()>since)
				filtered.add(rating);
		}
		return filtered;
	}
	
	private int countRatingsByPlaceId(List<Rating> ratings, Long placeId)
	{
		int count = 0;
		for (Rating rating : ratings) {
			if(rating.getPlace().getId().equals(placeId))
				count++;
		}
		return count;
	}
	

	public void addPropertyChangeListener( PropertyChangeListener l ) {
        changes.addPropertyChangeListener( l );
    }
    
    public void removePropertyChangeListener( PropertyChangeListener l ) {
        changes.removePropertyChangeListener( l ); 
    }	

}
