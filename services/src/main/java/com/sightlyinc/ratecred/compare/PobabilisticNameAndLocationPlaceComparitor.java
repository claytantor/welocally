package com.sightlyinc.ratecred.compare;

import java.util.Comparator;

import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.model.Place;

public class PobabilisticNameAndLocationPlaceComparitor implements Comparator<Place> {
	
	private Place p;

	public PobabilisticNameAndLocationPlaceComparitor(Place p) {
		super();
		this.p = p;
	}

	@Override
	public int compare(Place o1, Place o2) {
		

		Double abs1 = 0.0;
		
		Double abs2 = 0.0;
				
		abs1+= 1.0-StringUtils.compareStrings(p.getName(), o1.getName());
		
		abs2+= 1.0-StringUtils.compareStrings(p.getName(), o2.getName());	
		
		if(o1.getLatitude() != null && p.getLatitude() != null) 
			abs1 += Math.abs(o1.getLatitude()-p.getLatitude());			
		
		if(o2.getLatitude() != null && p.getLatitude() != null) 
			abs2 += Math.abs(o2.getLatitude()-p.getLatitude());

		if(o1.getLongitude() != null && p.getLongitude() != null) 
			abs1 += Math.abs(o1.getLongitude()-p.getLongitude());			
		
		if(o2.getLongitude() != null && p.getLongitude() != null) 
			abs2 += Math.abs(o2.getLongitude()-p.getLongitude());
		
		//backup plan
		if(p.getCity() != null 
				&& o1.getCity() != null 
				&& o2.getCity() != null 				
				&& p.getState() != null
				&& o1.getState() != null
				&& o2.getState() != null
				)
		{
			abs1+= 1.0-StringUtils.compareStrings(p.getCity().toLowerCase(), o1.getCity().toLowerCase());	
			abs2+= 1.0-StringUtils.compareStrings(p.getState().toLowerCase(), o2.getState().toLowerCase());	
		}
		
		//smallest wins
		return abs1.compareTo(abs2);
		
	}

}
