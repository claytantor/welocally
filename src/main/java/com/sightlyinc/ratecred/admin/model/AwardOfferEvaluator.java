package com.sightlyinc.ratecred.admin.model;

import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.service.AwardsUtils;

public class AwardOfferEvaluator {

	private Award award;
	private AwardType awardType;
	private Rater rater;
	private PlaceCityState pcs;
	
	private Offer offer;


	public AwardOfferEvaluator(
			Award award, 
			AwardType awardType, 
			Rater r,
			PlaceCityState pcs) {
		super();
		this.award = award;
		this.awardType = awardType;
		this.rater = r;
		this.pcs = pcs;

	}
	
	public boolean isLocalOffer(Offer offer) {
		if(offer.getDescription().contains(pcs.getCity()) ||
				offer.getDescription().contains(pcs.getState()))
			return true;
		else
			return false;
	}
	
	public boolean hasOffer()
	{
		return(offer != null);
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
