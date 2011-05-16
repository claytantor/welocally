package com.sightlyinc.ratecred.admin.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.compare.DescendingRateMapStringComparitor;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.Rating;

public class CityStateEvaluator {
	
	static Logger logger = Logger.getLogger(CityStateEvaluator.class);
	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	private PlaceCityState placeCityState;
	private Patron[] topRaters;
	
	
	//heavy lifting in constructor this may be served better by
	//sending all ratings for the city state
	public CityStateEvaluator(PlaceCityState placeCityState, List<Patron> topRatersList) {
		this.topRaters = (Patron[])topRatersList.toArray(new Patron[topRatersList.size()]); 
		this.placeCityState = placeCityState;
	}
	
	public boolean isValid()
	{
		if(!StringUtils.isEmpty(placeCityState.getCity()) && !StringUtils.isEmpty(placeCityState.getState()) )
				return true;
		else
			return false;
	}
	
	public boolean isLeadRater(Patron r)
	{
		
		/*logger.debug("rater:"+r.getUserName()+" isLeadRater:"+topRaters[0].getUserName()+
				" pcs:"+this.placeCityState.getCity()+","+placeCityState.getState());*/
		
		if(topRaters[0].getUserName().equals(r.getUserName())) {
			return true;
		} else {
			return false;
		}
		
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
