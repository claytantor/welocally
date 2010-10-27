package com.sightlyinc.ratecred.admin.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.noi.utility.sort.MapSortUtils;
import com.sightlyinc.ratecred.admin.compare.DescendingComparitor;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rating;

public class CityStateEvaluator {
	
	static Logger logger = Logger.getLogger(CityStateEvaluator.class);
	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	private PlaceCityState placeCityState;
	private Map<Rater,Integer> raterMap = new HashMap<Rater,Integer>(); 
	private List<Rater> allRaters;

	//heavy lifting in constructor
	public CityStateEvaluator(PlaceCityState placeCityState, List<Rater> allRaters) {
		
		this.placeCityState = placeCityState;
		this.allRaters = allRaters;
		for (Rater rater : allRaters) {
			for (Rating rating : rater.getRatings())			
			{
				PlaceCityState cs = 
					new PlaceCityState(rating.getPlace().getCity(), rating.getPlace().getState(), null);
					
				if(cs.equals(this.placeCityState))
				{
					
					Integer count = raterMap.get(rater);
					if(count == null)
						raterMap.put(rater, new Integer(1));
					else
						raterMap.put(rater, count++);
				}
			}
		}
		
		//sort the map based on values
		MapSortUtils.sortByValue(raterMap, new DescendingComparitor());
		
	}
	
	public boolean isRated(Rater r)
	{
		if(raterMap.get(r) == null)
			return false;
		else
			return true;
	}
	
	public boolean isLeadRater(Rater r)
	{
		Rater[] raterlist = (Rater[])raterMap.keySet().toArray(new Rater[raterMap.keySet().size()]); 
		if(raterlist[0].equals(r))
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
