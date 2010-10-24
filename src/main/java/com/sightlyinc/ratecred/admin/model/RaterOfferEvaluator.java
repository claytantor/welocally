package com.sightlyinc.ratecred.admin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rating;

public class RaterOfferEvaluator {

	private Rater rater;
	private String mostRatedCityState;

	public RaterOfferEvaluator(Rater rater) {
		super();
		this.rater = rater;

		mostRatedCityState = getMostRatedCityState(new ArrayList<Rating>(rater
				.getRatings()));

	}

	private String getMostRatedCityState(List<Rating> ratings) {
		Map<String, Integer> tMap = new HashMap<String, Integer>();

		for (Rating rating : ratings) {
			String placeCityState = rating.getPlace().getCity().toLowerCase()
					+ rating.getPlace().getState().toLowerCase();
			Integer count = tMap.get(placeCityState);
			if (count == null)
				count = new Integer(1);
			else
				count++;

			tMap.put(placeCityState, count);
		}

		// now sort it
		Map<String, Integer> ratingCityLookup = sortByValue(tMap);
		if(ratingCityLookup.size()>0) {
			Entry<String,Integer> first = ratingCityLookup.entrySet().iterator().next();
			return first.getKey();
		} else
			return null;
	}

	private Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		// logger.info(list);
		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public boolean isInMostRatedCity(Offer offer) {
		String offerCityState = null;
		if( offer.getCity() != null &&  offer.getState() != null)
		{
			offerCityState = 
				offer.getCity().toLowerCase()
				+ offer.getState().toLowerCase();
			if(offerCityState.equals(mostRatedCityState))
				return true;			
		}
		
		return false;

	}

}
