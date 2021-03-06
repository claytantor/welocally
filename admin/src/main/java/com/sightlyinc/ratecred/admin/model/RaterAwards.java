package com.sightlyinc.ratecred.admin.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.service.AwardsUtils;

public class RaterAwards {
	
	static Logger logger = Logger.getLogger(RaterAwards.class);
	
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );
	
	private Patron rater;
	private List<Award> awards = new ArrayList<Award>();
	private List<Award> removeAwards = new ArrayList<Award>();
	private Set<String> keys = new HashSet<String>();
	private Set<String> citykeys = new HashSet<String>();
	
	public RaterAwards(Patron rater) {
		super();
		this.rater = rater;
		for (Award award : rater.getAwards())
		{
			keys.add(award.getAwardType().getKeyname());
			
			PlaceCityState pcs = null;
			if(award.getMetadata() != null)
				pcs = AwardsUtils.getPlaceCityStateFromMetaData(award.getMetadata());
			if(pcs != null)
			{
				StringBuffer s2 = new StringBuffer();
				if(pcs.getCity() != null)
					s2.append(pcs.getCity().toLowerCase());
				if(pcs.getState() != null)
					s2.append(pcs.getState().toLowerCase());
				citykeys.add(s2.toString());
			}
			
			
		}
		
		
	}
	
	
	public boolean hasAwardKey(String key)
	{
		boolean contains = keys.contains(key);
		logger.debug("rater:"+rater.getUserName()+" has key:"+key+" ="+contains);
		return contains;
	}
	
	public boolean hasAward(String key)
	{
		boolean contains = keys.contains(key);
		return contains;
	}
	
	//NEED TO RECHECK THIS LOGIC
	public boolean hasPlaceAward(int id)
	{
		throw new RuntimeException("NEED TO REVIEW");
		/*Long placeId = new Long(id);
		for (Award award : rater.getAwards()) {
			Long awardPlaceId = AwardsUtils.getPlaceIdMetaData(award.getMetadata());
			if(awardPlaceId != null 
					&& awardPlaceId.equals(placeId))
				return true;
		}
		return false;*/
	}
	
	public boolean hasAwardCityStar(String city, String state)
	{
		StringBuffer s2 = new StringBuffer();
		if(city != null)
			s2.append(city.toLowerCase());
		if(state != null)
			s2.append(state.toLowerCase());
		
		boolean hasAwardCityStar = citykeys.contains(s2.toString());
		
		if(this.rater.getUserName().equals("sampento"))
			logger.debug(hasAwardCityStar);
		
		return hasAwardCityStar;
		
	}
	
	public boolean hasNotAwardCityStar(String city, String state)
	{
		StringBuffer s2 = new StringBuffer();
		if(city != null)
			s2.append(city.toLowerCase());
		if(state != null)
			s2.append(state.toLowerCase());
		
		boolean hasNotAwardCityStar = !citykeys.contains(s2.toString());
		
		if(this.rater.getUserName().equals("sampento"))
			logger.debug(hasNotAwardCityStar);
		
		return hasNotAwardCityStar;
		
	}
	
	
	
	public void removeAward(Award a) {
		removeAwards.add(a);
	}
	
	public void addAward(Award a) {
		awards.add(a);
	}
	

/*	public Boolean getGiveOffer() {
		return giveOffer;
	}


	public void setGiveOffer(boolean giveOffer) {
		this.giveOffer = giveOffer;
	}*/


	public List<Award> getAwards() {
		return awards;
	}
	
	public List<Award> getRemoveAwards() {
		return removeAwards;
	}


	public Patron getRater() {
		return rater;
	}
	
	public void addPropertyChangeListener( PropertyChangeListener l ) {
        changes.addPropertyChangeListener( l );
    }
    
    public void removePropertyChangeListener( PropertyChangeListener l ) {
        changes.removePropertyChangeListener( l ); 
    }
	

}
