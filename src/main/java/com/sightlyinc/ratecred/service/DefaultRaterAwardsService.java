package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.FactException;
import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.noi.utility.random.RandomMaker;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.admin.mvc.controller.TestRulesController;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Rater;

@Service("raterAwardsService")
public class DefaultRaterAwardsService implements RaterAwardsService {
	
	static Logger logger = Logger.getLogger(DefaultRaterAwardsService.class);
	
	@Autowired
	@Qualifier("RatingManagerService")
	private RatingManagerService ratingManagerService;
	
	@Autowired
	@Qualifier("offerPoolService")
	private OfferPoolService offerPoolService;
	
	@Autowired
	private AwardManagerService awardManagerService;
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.service.RaterAwardService#proccessAwardsForRater(com.sightlyinc.ratecred.admin.model.RaterAwards, com.sightlyinc.ratecred.model.Rater)
	 */
	public void proccessAwardsForRater(RaterAwards ra, Rater r) throws BLServiceException
	{
		for (Award award : ra.getAwards()) {
			//get the real one from the database
			AwardType awardType = awardManagerService.findAwardTypeByKey(award.getAwardType().getKeyname());
			
			//check to see if they have been awarded this already
			List<Award> hasAwards = awardManagerService.findAwardByRaterAwardType(r, awardType);
			if(hasAwards == null || hasAwards.size()==0)
				saveNewAward(award, awardType, r);
			else
				award.setAwardType(awardType);
			
		}
		
	}
	
	private void saveNewAward(Award award, AwardType awardType, Rater r)
	throws BLServiceException
	{
		award.setAwardType(awardType);
		award.setOwner(r);			
		award.setNotes(awardType.getDescription());
		award.setStatus("GIVEN");
		award.setMetadata("imageUrl=/images/awards/award_"+award.getAwardType().getKeyname()+".png");
		award.setExpiresMills(0l);
		
		//we should use rule based targeting for this
		Offer offer = targetOfferForRater(r);
		
		AwardOffer aoffer = new AwardOffer();
		aoffer.setAwardType(awardType);
		aoffer.setCouponCode(offer.getCouponCode());
		aoffer.setBeginDateMillis(offer.getBegin().getTime());
		aoffer.setDescription(offer.getDescription());
		aoffer.setExpireDateMillis(offer.getExpire().getTime());
		aoffer.setExternalId(offer.getExternalId().toString());
		aoffer.setExternalSource(offer.getExternalSource());
		aoffer.setName(offer.getName());
		aoffer.setProgramId(offer.getProgramId().toString());
		aoffer.setProgramName(offer.getProgramName());
		aoffer.setStatus("GIVEN");
		aoffer.setTimeCreated(Calendar.getInstance().getTime());
		aoffer.setUrl(offer.getUrl());
		awardManagerService.saveAwardOffer(aoffer);
		
		award.setOffer(aoffer);
		Long id = awardManagerService.saveAward(award);
		
		//remove previous award if it exists
		if(awardType.getPrevious()!=null)
		{
			AwardType previousType = awardManagerService.findAwardTypeByKey(awardType.getPrevious());
			List<Award> removeAwards = awardManagerService.findAwardByRaterAwardType(r, previousType);
			for (Award awardToRemove : removeAwards) {
				r.getAwards().remove(awardToRemove);
				awardManagerService.deleteAward(awardToRemove);
			}
			//now the rater
			ratingManagerService.saveRater(r);
			
		}		
	}
	
	private Offer targetOfferForRater(Rater r) throws BLServiceException {
		
		try {
			RuleBase ruleBase = 
				RuleBaseLoader.loadFromInputStream(
						TestRulesController.class.getResourceAsStream("/rules/offer.java.drl"));
			
			WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
			
			List<Offer> offersRaw = offerPoolService.getOfferPool();
			boolean dynamic = true;
			for (Offer offer : offersRaw) {
				workingMemory.assertObject( offer, dynamic );
			}			
			workingMemory.fireAllRules( );
			
			List<Offer> offersFiltered = new ArrayList<Offer>();
			for (Offer offer : offersRaw) {
				if(offer.isVisible())
					offersFiltered.add(offer);
			}
			int pos = RandomMaker.nextInt(1, offersFiltered.size()-1);
			return offersFiltered.get(pos);
			
			
		} catch (IntegrationException e) {
			logger.error("IntegrationException", e);
			throw new BLServiceException(e);
		} catch (FactException e) {
			logger.error("FactException", e);
			throw new BLServiceException(e);
		} catch (SAXException e) {
			logger.error("SAXException", e);
			throw new BLServiceException(e);
		} catch (IOException e) {
			logger.error("IOException", e);
			throw new BLServiceException(e);
		} 	


	}

}
