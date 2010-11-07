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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

import com.noi.utility.random.RandomMaker;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.rosaloves.net.shorturl.bitly.Bitly;
import com.rosaloves.net.shorturl.bitly.BitlyFactory;
import com.rosaloves.net.shorturl.bitly.url.BitlyUrl;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.admin.mvc.controller.TestRulesController;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.dao.UserDao;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.User;

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
	
	@Autowired
	private UserDao userDao;
	
	@Value("${twitter.rateCredOAuth.appConsumerKey}")
	private String appConsumerKey;
	
	@Value("${twitter.rateCredOAuth.appSecretKey}")
	private String appSecretKey;
	
	@Value("${twitter.rateCredService.bitlyUserName}")
	private String bitlyUserName;
	
	@Value("${twitter.rateCredService.bitlyKey}")
	private String bitlyKey;
	
	@Value("${rateCredService.raterUrlPrefix}")
	private String raterProfilePrefix;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sightlyinc.ratecred.service.RaterAwardService#proccessAwardsForRater
	 * (com.sightlyinc.ratecred.admin.model.RaterAwards,
	 * com.sightlyinc.ratecred.model.Rater)
	 */
	public void proccessAwardsForRater(RaterAwards ra)
			throws BLServiceException {
		
		logger.debug(ra.getRater().getUserName() + " awards to add:"
				+ ra.getAwards().size() + " remove:"
				+ ra.getRemoveAwards().size());

		// awards to add
		for (Award award : ra.getAwards()) {
			logger.debug("adding type:"+award.getAwardType().getKeyname()+
					" "+award.getMetadata());
			AwardType awardType = awardManagerService.findAwardTypeByKey(award
					.getAwardType().getKeyname());
			
			award.setAwardType(awardType);

			PlaceCityState pcs = null;
			if (award.getMetadata() != null)
				pcs = AwardsRulesUtils.getPlaceCityStateFromMetaData(award
						.getMetadata());

			saveNewAward(award, awardType, ra.getRater(), pcs, ra
					.getGiveOffer());

		}

		// remove
		for (Award awardToRemove : ra.getRemoveAwards()) {
			logger.debug("removing type:"+awardToRemove.getAwardType().getKeyname()+
					" "+awardToRemove.getMetadata());
			
			List<Award> awardsFound = new ArrayList<Award>();
			AwardType awardTypeToRemove = awardManagerService.findAwardTypeByKey(
					awardToRemove.getAwardType()
					.getKeyname());
			
			if (awardToRemove.getAwardType().getKeyname().equals("citykey")) {
				
				PlaceCityState pcs = AwardsRulesUtils
						.getPlaceCityStateFromMetaData(awardToRemove
								.getMetadata());
				
				awardsFound = awardManagerService.findAwardByRaterTypeCity(
						ra.getRater(),
						awardTypeToRemove,
						pcs);
				
			} else {
				awardsFound = awardManagerService
						.findAwardByRaterAwardType(ra.getRater(),
								awardTypeToRemove);
			}
			
			for (Award awardR : awardsFound) {
				ra.getRater().getAwards().remove(awardR);
				awardManagerService.deleteAward(awardR);
			}
			
		}

		// now the rater
		ratingManagerService.saveRater(ra.getRater());

	}

	@Override
	public Long saveNewAward(Award award, AwardType awardType, Rater r,
			PlaceCityState pcs, Boolean giveOffer) throws BLServiceException {

		if (pcs != null) // this is a place award, there can be more than one
			award.setNotes("Ranked First " + pcs.getCity());
		else {
			award.setNotes(awardType.getDescription());
			award.setMetadata("imageUrl=/images/awards/award_"
					+ award.getAwardType().getKeyname() + ".png");
		}

		award.setAwardType(awardType);
		award.setOwner(r);
		award.setStatus("GIVEN");

		award.setExpiresMills(0l);

		// we should use rule based targeting for this
		if (giveOffer) {
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
		}
		
		awardManagerService.saveAward(award);
		
		//now tweet the award, if you can
		try {
			sendAwardStatusUpdate(award, userDao.findByUsername(r.getUserName()));
		} catch (Exception e) {
			logger.error("problem tweeting award", e);
			//throw new BLServiceException(e);
		}
		
		return award.getId();
		

	}
	
	private void sendAwardStatusUpdate(Award a, User u) throws TwitterException, IOException
	{
			AccessToken accessToken = 
				new AccessToken(
						u.getTwitterToken(), 
						u.getTwitterTokenSecret());
			
			Bitly bitly = 
				BitlyFactory.newInstance(bitlyUserName, bitlyKey);
			
			BitlyUrl url = 
				bitly.shorten(raterProfilePrefix + a.getOwner().getId().toString());
			
			Twitter twitter = 
				new TwitterFactory().getOAuthAuthorizedInstance(
						appConsumerKey, appSecretKey, accessToken);	 
			
			StringBuffer buf = new StringBuffer();
			buf.append("Awarded "+a.getAwardType().getName()+ " on RateCred! " +
					StringUtils.truncate(a.getAwardType().getDescription(), "...", 65) +" ");
			buf.append(url.getShortUrl()+" @ratecred");
			

			Status status = twitter.updateStatus(buf.toString());
		
	}

	private Offer targetOfferForRater(Rater r) throws BLServiceException {

		try {
			RuleBase ruleBase = RuleBaseLoader
					.loadFromInputStream(TestRulesController.class
							.getResourceAsStream("/rules/offer.java.drl"));

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();

			List<Offer> offersRaw = offerPoolService.getOfferPool();
			boolean dynamic = true;
			for (Offer offer : offersRaw) {
				workingMemory.assertObject(offer, dynamic);
			}
			workingMemory.fireAllRules();

			List<Offer> offersFiltered = new ArrayList<Offer>();
			for (Offer offer : offersRaw) {
				if (offer.isVisible())
					offersFiltered.add(offer);
			}
			int pos = RandomMaker.nextInt(1, offersFiltered.size() - 1);
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
