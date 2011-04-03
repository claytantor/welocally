package com.sightlyinc.ratecred.admin.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import org.mcavallo.opencloud.filters.DictionaryFilter;
import org.mcavallo.opencloud.filters.Filter;
import org.mcavallo.opencloud.filters.TagFilter;

import com.sightlyinc.ratecred.admin.compare.OfferScoreComparitor;
import com.sightlyinc.ratecred.admin.compare.TagScoreComparitor;
import com.sightlyinc.ratecred.client.offers.Location;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.service.AwardsUtils;

public class OfferTargetEvaluator {
	
	static Logger logger = Logger.getLogger(OfferTargetEvaluator.class);


	private PlaceCityState pcs;
	private Map<String,Offer> offerCache = new HashMap<String,Offer>();
	private List<Offer> targetedOffers = new ArrayList<Offer>();
	private Cloud ratingCloud = new Cloud();
	
	private static String TERMS = "a,about,all,and,are,as,at,back,be,because,been," +
	"but,can,can't,come,could,did,didn't,do,don't,for,from,get,go,going," +
	"good,got,had,have,he,her,here,he's,hey,him,his,how,I,if,I'll,I'm," +
	"in,is,it,it's,just,know,like,look,me,mean,my,no,not,now,of,oh,OK," +
	"okay,on,one,or,out,really,right,say,see,she,so,some,something,tell," +
	"that,that's,the,then,there,they,think,this,time,to,up,want,was,we,well," +
	"were,what,when,who,why,will,with,would,yeah,yes,you,your,you're";
	
	private static String[] TERMS_LIST = TERMS.split(",");


	public OfferTargetEvaluator(
			List<String> terms,
			PlaceCityState pcs) {
		super();
		
		//build the cloud
		StringBuffer termsBuffer = new StringBuffer(); 
		for (String term : terms) {
			logger.debug("adding search term:"+term);
			termsBuffer.append(term+" ");
		}
				
		DictionaryFilter fTerms = 
			new DictionaryFilter( TERMS_LIST );
		
		ratingCloud.addOutputFilter(fTerms); 
		ratingCloud.addText(termsBuffer.toString());
			
		
		this.pcs = pcs;

	}
	
	public void addOfferToCache(Offer o) {
		String key = o.getExternalSource()+o.getExternalId();
		Offer o2 = offerCache.get(key);
		if(o2 == null) {
			
			//make a cloud for the offer and then score this 
			//offer against it
			DictionaryFilter fTerms = 
				new DictionaryFilter( TERMS_LIST );
			
			Cloud offerCloud = new Cloud();
			
			offerCloud.addOutputFilter(fTerms); 
			
			String offerText = o.getProgramName() + " " +
				o.getName()+ " " + 
				o.getDescription();
			
			offerCloud.addText(offerText);
			Double newScore = findCompositScore(ratingCloud, offerCloud);				
			logger.debug("putting offer in cache with score:"+newScore+" name:"+o.getName());
			o.setScore(newScore.intValue());
			offerCache.put(key, o);
		}
		
	}
	
	private Double findCompositScore(Cloud raterCloud, Cloud offerCloud) {
		Double tagScore = new Double(0.0d);
		for (Tag rtag : raterCloud.allTags(new TagScoreComparitor())) {
			for (Tag otag : offerCloud.allTags(new TagScoreComparitor())) {
				if(otag.getName().equalsIgnoreCase(rtag.getName())) {
					
					tagScore = tagScore + (otag.getScore()*(rtag.getScore()*10));
					logger.debug("match on tag:"+otag.getName()+" tag score:"+tagScore);
				}
			}
		}
		return tagScore;
	}
	
	public boolean hasOffers() {
		return offerCache.size()>0;
	}
	
	public boolean isExternalSource(String name, Offer offer) {
		return offer.getExternalSource().equalsIgnoreCase(name);
	}
	
	public boolean isRatedInCityOffer(Offer offer) {
		if(isLocalOffer(offer))
			return true;
		else
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
		
	public void chooseBestOffers() {
		List<Offer> sortedOffers = new ArrayList<Offer>(this.offerCache.values()); 
		Collections.sort(
				sortedOffers, 
				new OfferScoreComparitor());
		
		for (Offer offer : sortedOffers) {
			logger.debug("offer score:"+offer.getScore()+" name:"+offer.getName());
		}
		
		//only choose best offer for now
		if(sortedOffers.size()>0) {
			Offer bestOffer = sortedOffers.get(0);
			logger.debug("best offer:"+bestOffer.toString());
			targetedOffers.add(bestOffer);
		}
		
	}

	public List<Offer> getTargetedOffers() {
		return targetedOffers;
	}
	
	
	

}
