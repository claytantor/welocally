package com.sightlyinc.ratecred.admin.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rating;

public class RaterAwards {
	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	private Rater rater;
	private List<Award> awards = new ArrayList<Award>();
	public RaterAwards(Rater rater) {
		super();
		this.rater = rater;
	}
	
	public void addAwardByRule(String ruleNameAssert) {
		if(ruleNameAssert.equals("HasFirstRating"))
		{
			Award hasFirstRating = new Award();
			AwardType at = new AwardType();
			at.setName("Has First Rating");
			hasFirstRating.setAwardType(at);	
			List<Award> oldState = new ArrayList<Award>(awards);
			awards.add(hasFirstRating);			
	        changes.firePropertyChange("awards", oldState, awards);
		}
	}
	
	public void addAward(Award a) {
		awards.add(a);
	}

	public List<Award> getAwards() {
		return awards;
	}

	public Rater getRater() {
		return rater;
	}
	
	public void addPropertyChangeListener( PropertyChangeListener l ) {
        changes.addPropertyChangeListener( l );
    }
    
    public void removePropertyChangeListener( PropertyChangeListener l ) {
        changes.removePropertyChangeListener( l ); 
    }
	

}
