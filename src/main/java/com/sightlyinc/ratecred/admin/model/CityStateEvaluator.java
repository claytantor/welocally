package com.sightlyinc.ratecred.admin.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sightlyinc.ratecred.admin.compare.DescendingRateMapStringComparitor;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rating;

public class CityStateEvaluator {
	
	static Logger logger = Logger.getLogger(CityStateEvaluator.class);
	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	private PlaceCityState placeCityState;
	private Map<Long,Integer> raterMap = new HashMap<Long,Integer>(); 
	
	private List<String> raterMapNew = new ArrayList<String>();

	//heavy lifting in constructor this may be served better by
	//sending all ratings for the city state
	public CityStateEvaluator(PlaceCityState placeCityState, List<Rater> allRaters) {
		
		this.placeCityState = placeCityState;
		//this.allRaters = allRaters;
		for (Rater rater : allRaters) {
			for (Rating rating : rater.getRatings())			
			{
				PlaceCityState cs = 
					new PlaceCityState(rating.getPlace().getCity(), rating.getPlace().getState(), null);
					
				if(cs.equals(this.placeCityState))
				{					
					Integer count = raterMap.get(rater.getId());
					if(count == null)
						raterMap.put(rater.getId(), new Integer(1));
					else
					{
						count = count+1;
						raterMap.put(rater.getId(), count);
					}
				}
			}
		}
		
		for (Map.Entry<Long,Integer> raterCount : raterMap.entrySet()) {
			raterMapNew.add(raterCount.getValue()+","+raterCount.getKey());
		}
		
		Collections.sort(raterMapNew,new DescendingRateMapStringComparitor());
		
	}
	
	public boolean isRated(Rater r)
	{
		if(raterMap.get(r.getId()) == null)
			return false;
		else
			return true;
	}
	
	public boolean isLeadRater(Rater r)
	{
		
		String[] raterlist = (String[])raterMapNew.toArray(new String[raterMapNew.size()]); 
		String[] leader = raterlist[0].split(",");
		
		if(r.getUserName().equals("sampento"))
			logger.debug("sams lead in:"+this.getPlaceCityState().toString());
				
		if(leader[1].equals(r.getId().toString()))
			return true;
		else
			return false;
	}
	
	
	
	public PlaceCityState getPlaceCityState() {
		return placeCityState;
	}

	public void addPropertyChangeListener( PropertyChangeListener l ) {
        changes.addPropertyChangeListener( l );
    }
    
    public void removePropertyChangeListener( PropertyChangeListener l ) {
        changes.removePropertyChangeListener( l ); 
    }	

}
