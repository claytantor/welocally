package com.sightlyinc.ratecred.service;

import java.util.ArrayList;
import java.util.List;

import com.sightlyinc.ratecred.model.PlaceRating;
import com.sightlyinc.ratecred.model.Rating;

public class RatingHelper {
	
	public static PlaceRating computeNewRatingAdd(
			List<PlaceRating> placeRatings, 
			List<Rating> ratings, 
			String ratingType, 
			Float ratingRatingToAdd)
	{
		PlaceRating ratingForType = new PlaceRating();
		/*//find by type
		for (PlaceRating placeRating : placeRatings) {
			if(placeRating.getType().equals(ratingType))
				ratingForType = placeRating;
		}
		
		if(ratingForType == null)
		{
			ratingForType = new PlaceRating();
			ratingForType.setType(ratingType);
			ratingForType.setRating(ratingRatingToAdd);
			//ratings.add(ratingForType);
		} else {*/
			//recompute based on all ratings for type, plus the new one
			List<Rating> allRates = 
				new ArrayList<Rating>(ratings);
			Float sum = 0.0f;
			/*for (Rating rating : allRates) {
				if(rating.getType().equals(ratingType) && rating.getRaterRating()!=null)
					sum+=rating.getRaterRating();
			}*/
			for (Rating rating : allRates) {
					sum+=rating.getRaterRating();
			}
			sum+=ratingRatingToAdd;
			Float avg = sum/(allRates.size()+1);
			ratingForType.setRating(avg);				
		//}
		return ratingForType;
		//p.setRatings(ratings);
	}
	
	public static PlaceRating computeNewRatingRemove(
			List<PlaceRating> placeRatings, 
			List<Rating> ratings, 
			String ratingType, 
			Float ratingRatingToRemove)
	{
		PlaceRating ratingForType = null;
		//find by type
		for (PlaceRating placeRating : placeRatings) {
			if(placeRating.getType().equals(ratingType))
				ratingForType = placeRating;
		}
		
		if(ratingForType == null)
		{
			ratingForType = new PlaceRating();
			ratingForType.setType(ratingType);
			ratingForType.setRating(ratingRatingToRemove);
			//ratings.add(ratingForType);
		} else {
			//recompute based on all ratings for type, plus the new one
			List<Rating> allRates = 
				new ArrayList<Rating>(ratings);
			Float sum = 0.0f;
			for (Rating rating : allRates) {
				if(rating.getType().equals(ratingType))
					sum+=rating.getRaterRating();
			}
			sum -=ratingRatingToRemove;
			Float avg = sum/(allRates.size()-1);
			
			//what if you delete the only rating?
			if(!avg.equals(Float.NaN))
				ratingForType.setRating(avg);	
			else
				ratingForType.setRating(2.5f);
		}
		return ratingForType;
		//p.setRatings(ratings);
	}

}
