package com.sightlyinc.ratecred.admin.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sightlyinc.ratecred.client.offers.Location;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.service.AwardsUtils;

public class AwardOfferEvaluator {

	private Award award;
	private AwardType awardType;
	private Rater rater;
	private PlaceCityState pcs;
	private List<AwardOffer> awardOffers = new ArrayList<AwardOffer>();
	private Set<PlaceCityState> allCities = new HashSet<PlaceCityState>();
	
	private Offer offer;


	public AwardOfferEvaluator(
			Award award, 
			AwardType awardType, 
			Rater r,
			PlaceCityState pcs,
			Set<PlaceCityState> allcites) {
		super();
		this.award = award;
		this.awardType = awardType;
		this.rater = r;
		Collection<Award> awards = rater.getAwards();
		for (Award awardItem : awards) {
			if(awardItem.getOffers() != null)
			{
				for (AwardOffer offer : awardItem.getOffers()) {
					awardOffers.add(offer);
				}
			}
				
		}
		
		this.allCities = allcites;
		
		
		this.pcs = pcs;

	}

	public boolean isRatedInCityOffer(Offer offer) {
		
		for (PlaceCityState pcs : allCities) {
			if(isLocalOffer(offer))
				return true;
		}
		return false;
		
		
	}
	
	public boolean isLocalOffer(Offer offer) {
		
		try {			
			if(pcs != null && offer.getCity().equalsIgnoreCase(pcs.getCity()) && 
					offer.getState().equalsIgnoreCase(pcs.getState()) ) {
				return true;
			} else if(hasCityStateLocation(offer,pcs)) {
				return true;				
			}
			else
				return false;
			
		} catch (Exception e) {			
			return false;
		}
	}
	
	private boolean hasCityStateLocation(Offer offer, PlaceCityState pcs) {
		List<Location> locations = offer.getAdvertiser().getLocations();
		for (Location location : locations) {
			if(pcs != null && location.getCity().equalsIgnoreCase(pcs.getCity()) && 
					location.getState().equalsIgnoreCase(pcs.getState()) ) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public boolean hasOffer()
	{
		return(offer != null);
	}
	
	public boolean isOfferNotGiven(Offer offer) {
		for (AwardOffer awardOffer : awardOffers) {
			if(awardOffer != null && awardOffer.getExternalId().equals(offer.getExternalId()))
				return false;
		}
		return true;
		
	}
	
	public boolean isPlaceOffer(Offer offer) {
		//parse the metadata of the award, if the offer program id is the 
		//same and the source is RATECRED it is a place offer
		Long awardPlaceId = AwardsUtils.getPlaceIdMetaData(award.getMetadata());
		if(awardPlaceId != null 
				&& offer.getExternalSource().equals("RATECRED")
				&& offer.getProgramId().equals(awardPlaceId.toString()))
			return true;
		else
			return false;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public Offer getOffer() {
		return offer;
	}
	

}
