package com.sightlyinc.ratecred.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.noi.utility.string.StringUtils;
import com.noi.utility.web.UrlUtils;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.Rating;

public class AwardsUtils {
	
	public static Long getPlaceIdMetaData(String metadata)
	{
		List<NameValuePair> nvs = null;
		try {
			if(!StringUtils.isEmpty(metadata))
				nvs = UrlUtils.parse(new URI("?"+metadata), "UTF-8");
			Map<String, String> model = UrlUtils.convert(nvs);
			//return new PlaceCityState(
			//		model.get("city"), model.get("state"), null);
			if(model.get("placeId") != null)
				return Long.parseLong(model.get("placeId"));
			else 
				return null;
			
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static PlaceCityState getPlaceCityStateFromMetaData(String metadata)
	{
		List<NameValuePair> nvs = null;
		try {
			if(!StringUtils.isEmpty(metadata))
				nvs = UrlUtils.parse(new URI("?"+metadata), "UTF-8");
			Map<String, String> model = UrlUtils.convert(nvs);
			if(model.get("city") != null &&  model.get("state") != null)
				return new PlaceCityState(
						model.get("city"), model.get("state"), null);
			else
				return null;
			
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static List<PlaceCityState> getCitiesRated(Patron r)
	{
		List<PlaceCityState> allcs = new ArrayList<PlaceCityState>();
		for (Rating rating : r.getRatings()) {
			allcs.add(
					new PlaceCityState(
							rating.getPlace().getCity(), 
							rating.getPlace().getState(),
							null));
		}
			
		return allcs;
	}
	
	public static void addCitiesToMap(List<PlaceCityState> cities, Map<String, PlaceCityState> maplookup)
	{
		for (PlaceCityState placeCityState : cities) {
			if(placeCityState.getCity() != null && placeCityState.getState() != null)
				maplookup.put(
						placeCityState.getCity().toLowerCase()+placeCityState.getState().toLowerCase(), 
						placeCityState);
		}
	}

}
