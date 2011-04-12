package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.drools.FactException;
import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

import com.adility.resources.client.AudilityClientException;
import com.adility.resources.client.ResourcesClient;
import com.adility.resources.model.Category;
import com.adility.resources.model.Illustration;
import com.adility.resources.model.Money;
import com.adility.resources.model.OffersResponse;
import com.adility.resources.model.RequestModel;
import com.noi.utility.date.DateUtils;
import com.noi.utility.mail.MailerQueueService;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.rosaloves.net.shorturl.bitly.Bitly;
import com.rosaloves.net.shorturl.bitly.BitlyFactory;
import com.rosaloves.net.shorturl.bitly.url.BitlyUrl;
import com.sightlyinc.ratecred.admin.jms.SaveNewAwardMessageProducer;
import com.sightlyinc.ratecred.admin.jms.UpdateAwardOfferMessageProducer;
import com.sightlyinc.ratecred.admin.model.AwardOfferEvaluator;
import com.sightlyinc.ratecred.admin.model.OfferTargetEvaluator;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
import com.sightlyinc.ratecred.admin.model.TargetModel;
import com.sightlyinc.ratecred.admin.velocity.BusinessServicesPlaceAwardGenerator;
import com.sightlyinc.ratecred.client.offers.Advertiser;
import com.sightlyinc.ratecred.client.offers.Item;
import com.sightlyinc.ratecred.client.offers.Location;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.dao.UserDao;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardOfferItem;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.model.User;

@Service("raterAwardsService")
@Transactional(readOnly = true)
public class DefaultRaterAwardsService implements RaterAwardsService {

	static Logger logger = Logger.getLogger(DefaultRaterAwardsService.class);

	@Autowired
	@Qualifier("RatingManagerService")
	private RatingManagerService ratingManagerService;
	
	@Autowired
	@Qualifier("PlaceManagerService")
	private PlaceManagerService placeManagerService;
	
	@Autowired
	@Qualifier("BusinessManagerService")
	private BusinessManagerService businessManagerService;
	
	@Autowired
	@Qualifier("offerPoolService")
	private OfferPoolService offerPoolService;

	@Autowired
	private AwardManagerService awardManagerService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	@Qualifier("mailerQueueService")
	private MailerQueueService mailerQueueService;
	
	@Autowired
    @Qualifier("saveNewAwardMessageProducer")
    private SaveNewAwardMessageProducer saveNewAwardMessageProducer;
	
	@Autowired
    @Qualifier("resourcesClient")
    private ResourcesClient resourcesClient;
	
	@Autowired
    @Qualifier("updateAwardOfferMessageProducer")
	private UpdateAwardOfferMessageProducer updateAwardOfferMessageProducer;
	
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
	
	@Value("${RatingController.supressTwitterPublish}")
	private Boolean supressTwitterPublish;
	
	@Value("${raterAwardsService.awardOfferRulesUrl}")
	private String awardOfferRulesUrl;
	
	@Value("${raterAwardsService.offerRulesUrl}")
	private String offerRulesUrl;
	
	/*@Value("${raterAwardsService.useAwardOfferUrl}")
	private String useAwardOfferUrl;
	*/
	
	@Value("${MailerQueueService.smtpUsername}")
	private String fromAddress;
	
	
	private RuleBase ruleBase ;
		
	
	@PostConstruct
    public void setUpOffersTargetingWorkingMemory() {
		
			logger.debug("getting offer targeting rule base:"+offerRulesUrl);
			
			try {
				ruleBase = RuleBaseLoader.loadFromUrl(new URL(offerRulesUrl));
			} catch (IntegrationException e) {
				logger.error("IntegrationException", e);
			} catch (MalformedURLException e) {
				logger.error("IntegrationException", e);
			} catch (SAXException e) {
				logger.error("IntegrationException", e);
			} catch (IOException e) {
				logger.error("IntegrationException", e);
			}
        
    }

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void reassignAllOffers() throws BLServiceException {
		List<Rater> allRaters = ratingManagerService.findRatersByStatus("USER");
		for (Rater rater : allRaters) {
			deleteRaterAwardOffers(rater.getId());
			for (Award award : rater.getAwards()) {
				targetAwardById(award.getId());
			}
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeAllOffers() throws BLServiceException {
		List<Rater> allRaters = ratingManagerService.findRatersByStatus("USER");
		for (Rater rater : allRaters) {
			deleteRaterAwardOffers(rater.getId());
		}
		
	}

	@Override
	public void targetAwardById(Long awardId) throws BLServiceException {
		Award a = awardManagerService.findAwardByPrimaryKey(awardId);
		//dont throw if one has a problem
		try {
			updateAwardOfferMessageProducer
			.generateMessage(
					a, 
					a.getAwardType(), 
					a.getOwner());
		} catch (JsonGenerationException e) {
			logger.error("problem udating expired offer", e);
		} catch (JsonMappingException e) {
			logger.error("problem udating expired offer", e);
		} catch (JMSException e) {
			logger.error("problem udating expired offer", e);
		} catch (IOException e) {
			logger.error("problem udating expired offer", e);
		}
		
	}
	
	@Override
	public Offer targetOfferByTargetingModel(TargetModel targetModel) throws BLServiceException {
		
		try {			

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			
			List<Offer> offersRaw = offerPoolService.getOfferPool();
			
			
			boolean dynamic = true;
			
			//only do this if there are offers to work with
			if(offersRaw.size()>0)
			{
				for (Offer offer : offersRaw) {
					logger.debug("asserting offer:"+offer.getName());
					
				
					//use a cloned offer
					workingMemory.assertObject((Offer)offer.clone(), dynamic);
				}
				
				
				//now add the offer evaluator
				OfferTargetEvaluator awardOfferEval = new OfferTargetEvaluator(
						targetModel.getKeywords(),
						new PlaceCityState(targetModel.getCity(), targetModel.getState(), null));
				
				workingMemory.assertObject(awardOfferEval, dynamic);
				
				workingMemory.fireAllRules();
				
				awardOfferEval.chooseBestOffers();
				
				//this really should not just return the 
				//offer but set it to the award based on the award type
				if(awardOfferEval.getTargetedOffers().size()>0) {					
					Offer bestOffer = awardOfferEval.getTargetedOffers().get(0);
					logger.debug("best offer found:"+bestOffer.toString());
					return bestOffer;
				}
				else //backup plan
				{
					List<Offer> offersFiltered = new ArrayList<Offer>();
					for (Offer offer : offersRaw) {
						if (offer.isVisible())
							offersFiltered.add(offer);
					}
					return getRandomOffer(offersFiltered);
				}
				
				
				
				
			} else 
				return null;
			


		} catch (FactException e) {
			logger.error("FactException", e);
			throw new BLServiceException(e);
		} catch (CloneNotSupportedException e) {
			logger.error("CloneNotSupportedException", e);
			throw new BLServiceException(e);
		} 		
		
	}
	
	
	public Offer getRandomOffer(List<Offer> offers) {
		int size = offers.size();
		int item = new Random().nextInt(size); 
		int i = 0;
		for(Offer obj : offers) {
		    if (i == item)
		        return obj;
		    i = i + 1;
		}
		return offers.get(0);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRaterAwardOffers(Long raterId) {
		try {
			Rater r = ratingManagerService.findRaterByPrimaryKey(raterId);
			Set<AwardOffer> offers = new HashSet<AwardOffer>();
			
			for (Award award : r.getAwards()) 
				offers.addAll(award.getOffers());
			
			for (AwardOffer awardOffer : offers) {
				Award a = awardOffer.getAward();
				a.getOffers().remove(awardOffer);
				awardManagerService.saveAward(a);
				
				awardManagerService.deleteAwardOffer(awardOffer);
				
			}
			
		} catch (BLServiceException e) {
			logger.error("cannot delete offers", e);
		}
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sightlyinc.ratecred.service.RaterAwardService#proccessAwardsForRater
	 * (com.sightlyinc.ratecred.admin.model.RaterAwards,
	 * com.sightlyinc.ratecred.model.Rater)
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
			
			//saveNewAward(award, awardType, ra.getRater());			
			//this is our first producer integration
			//throw new RuntimeException("SEND TO QUEUE");
			//skip problems do not except 
			try {
				saveNewAwardMessageProducer.generateMessage(award, awardType, ra.getRater());
			} catch (JsonGenerationException e) {
				logger.error("problem sending message for award", e);
			} catch (JsonMappingException e) {
				logger.error("problem sending message for award", e);
			} catch (JMSException e) {
				logger.error("problem sending message for award", e);
			} catch (IOException e) {
				logger.error("problem sending message for award", e);
			}

		}
		
		// remove (if there are any)
		if(ra.getRater().getAwards().size()>0)
		{
			for (Award awardToRemove : ra.getRemoveAwards()) {
				logger.debug("removing type:"+awardToRemove.getAwardType().getKeyname()+
						" "+awardToRemove.getMetadata());
				
				List<Award> awardsFound = new ArrayList<Award>();
				AwardType awardTypeToRemove = awardManagerService.findAwardTypeByKey(
						awardToRemove.getAwardType()
						.getKeyname());
				
				if (awardToRemove.getAwardType().getKeyname().equals("citykey")) {
					
					PlaceCityState pcs = AwardsUtils
							.getPlaceCityStateFromMetaData(awardToRemove
									.getMetadata());
					
					awardsFound = awardManagerService.findAwardByRaterTypeCity(
							ra.getRater(),
							awardTypeToRemove,
							pcs);
					
				} else {
					if(ra.hasAward(awardTypeToRemove.getKeyname()))
					{
 						awardsFound = awardManagerService
								.findAwardByRaterAwardType(ra.getRater(),
										awardTypeToRemove);
					}
				}
				
				for (Award awardR : awardsFound) {
					awardR.setOwner(null);
					ra.getRater().getAwards().remove(awardR);
					awardManagerService.deleteAward(awardR);
				}
				
			}
		}


	}
	
	/**
	 * This message should only be consumed by a queue, that's because it makes calls
	 * to rate limited activities that should be in a single worker queue that can be
	 * controlled
	 * send this to queue immediately
	 * 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Long saveNewAward(Award award, AwardType awardType, Rater r, AwardOffer aoffer) 
		throws BLServiceException {
		
		PlaceCityState pcs = null;
		Long placeId = null;
		Place p = null;
		
		//save the relationship
		award.setAwardType(awardType);
		
		if(award.getMetadata() != null && award.getMetadata().contains("city") && award.getMetadata().contains("state"))
		{
			pcs = AwardsUtils.getPlaceCityStateFromMetaData(award.getMetadata());
		} else if(award.getMetadata() != null && award.getMetadata().contains("placeId")) {
			placeId = AwardsUtils.getPlaceIdMetaData(award.getMetadata());
		} 
		
		if(placeId != null)
			p = placeManagerService.findPlaceByPrimaryKey(placeId);
			
		if (pcs != null) 
			award.setNotes("Ranked First " + pcs.getCity());
		else if(p != null) {
			logger.debug("award for place:"+p.getName());
			award.setNotes(awardType.getName()+" at " + p.getName()+". ");			
		}
		else {
			award.setNotes(awardType.getDescription());
			award.setMetadata("imageUrl=/images/awards/award_"
					+ award.getAwardType().getKeyname() + ".png");
		}

		award.setAwardType(awardType);
		award.setOwner(r);
		award.setStatus("GIVEN");
		award.setExpiresMills(0l);
		
		if(aoffer == null)
			giveAwardOffer( award,  awardType,  r,  p,  pcs) ;
		else {
			aoffer.setAwardType(awardType);
			awardManagerService.saveAwardOffer(aoffer);
			award.getOffers().add(aoffer);
		}
		
		awardManagerService.saveAward(award);
		
		//now tweet the award, if you can
		try {
			if(!supressTwitterPublish)
				sendAwardStatusUpdate(award, userDao.findByUsername(r.getUserName()));
		} catch (Exception e) {
			logger.error("problem tweeting award", e);
			//throw new BLServiceException(e);
		}
		
		return award.getId();
		

	}
	
	/**
	 * This message should only be consumed by a queue, that's because it makes calls
	 * to rate limited activities that should be in a single worker queue that can be
	 * controlled
	 * 
	 * Secondly we should re evaluate how we run the targeting for offers, right now
	 * we are targeting per award which works, but a better way would be to look at raters who 
	 * have expired offers and then put all their awards and potential offers into a single
	 * working memory, fire rules against the entire model. then then expert systems could 
	 * be applied better  
	 * 
	 * send this to queue daily by a quartz job
	 * 
	 */
	public Long saveUpdateAwardOffer(Award award, AwardType awardType, Rater r) 
	throws BLServiceException {
		
		logger.debug("saveUpdateAwardOffer");
		
		PlaceCityState pcs = null;
		Long placeId = null;
		Place p = null;
		
		if(award.getMetadata() != null 
				&& award.getMetadata().contains("city") && award.getMetadata().contains("state"))
		{
			pcs = AwardsUtils.getPlaceCityStateFromMetaData(award.getMetadata());
		} else if(award.getMetadata() != null && award.getMetadata().contains("placeId")) {
			placeId = AwardsUtils.getPlaceIdMetaData(award.getMetadata());
		} 
		
		if(placeId != null)
			p = placeManagerService.findPlaceByPrimaryKey(placeId);
		

		//local awards should have pcs, place awards should have place
		giveAwardOffer( award,  awardType,  r,  p,  pcs) ;
		
		awardManagerService.saveAward(award);

		return award.getId();
	}
	
	private void giveAwardOffer(Award award, AwardType awardType, Rater r, Place p, PlaceCityState pcs) 
		throws BLServiceException {
		if (award.getGiveOffer()) {
			Offer offer = targetOfferForRater(award, 
					awardType, 
					r,
					p,
					pcs);
			
			if(offer != null) {
				try {
					AwardOffer aoffer = transformOffer(offer);
					aoffer.setAwardType(awardType);
					awardManagerService.saveAwardOffer(aoffer);
					award.getOffers().add(aoffer);
					
				} catch (Exception e) {
					logger.error("fail", e);
				}
			}
			
			
			
			//should we send an email here?
			/*		//this is a place award override the offer
			if(p!=null && p.getBusinessServices().equals("true")) {
				aoffer.setUrl(this.useAwardOfferUrl+"/"+aoffer.getId());
				sendOfferAwardedEmail(p.getEmail(), aoffer, r);
			}*/
						
			
		}
	}
	
	private AwardOffer transformOffer(Offer offer) {
		
		AwardOffer awardOffer = new AwardOffer();
		
		awardOffer.setCouponCode(offer.getCouponCode());
		awardOffer.setBeginDateMillis(offer.getBegin().getTime());
		awardOffer.setExpireDateMillis(offer.getExpire().getTime());
		awardOffer.setEndDateMillis(offer.getEnds().getTime());
		awardOffer.setDescription(offer.getDescription());
		awardOffer.setExtraDetails(offer.getExtraDetails());
		awardOffer.setExternalId(offer.getExternalId().toString());
		awardOffer.setExternalSource(offer.getExternalSource());
		awardOffer.setName(offer.getName());
		awardOffer.setProgramId(offer.getProgramId().toString());
		awardOffer.setProgramName(offer.getProgramName());
		awardOffer.setStatus("GIVEN");
		awardOffer.setType(offer.getType());
		awardOffer.setTimeCreated(Calendar.getInstance().getTime());
		awardOffer.setUrl(offer.getUrl());
		awardOffer.setIllustrationUrl(offer.getIllustrationUrl());
		awardOffer.setPrice(offer.getPrice());
		awardOffer.setValue(offer.getValue());
		awardOffer.setQuantity(offer.getQuantity());
		
		
		//offer items
		for (Item item : offer.getItems()) {
			AwardOfferItem offerItem = new AwardOfferItem();
			offerItem.setDescription(item.getDescription());
			offerItem.setTitle(item.getTitle());
			offerItem.setQuantity(item.getQuantity());	
			offerItem.setTimeCreated(Calendar.getInstance().getTimeInMillis());
			offerItem.setTimeUpdated(Calendar.getInstance().getTimeInMillis());
			awardOffer.getItems().add(offerItem);
		}
		
		//now do the business
		if(offer.getAdvertiser() != null) {			
			//try to find the business by the external id and source
			Business b = null;
			try {
				b = 
					businessManagerService.findBusinessByAdvertiserIdAndSource(
							offer.getAdvertiser().getExternalId(), offer.getExternalSource());
						
				if(b == null) {
					b = new Business();
					Advertiser advertiser = offer.getAdvertiser();
					b.setAdvertiserId(advertiser.getExternalId());
					b.setAdvertiserSource(offer.getExternalSource());
					b.setDescription(advertiser.getDescription());
					b.setName(advertiser.getName());
					b.setWebsite(advertiser.getSiteUrl());
					
					//do locations
					if(advertiser.getLocations().size()>0) {
						for (Location location : advertiser.getLocations()) {
							
							//try to find the business location, if not there
							//create both the bl and the place
							List<BusinessLocation> locations = 
								businessManagerService.findBusinessLocationByInfo(advertiser.getName(),
									location.getAddressOne(), 
									location.getCity(), 
									location.getState(), 
									location.getPostalCode());
							
							if(locations == null || locations.size()==0)
							{
								BusinessLocation bloc = new BusinessLocation();
								bloc.setName(advertiser.getName());
								bloc.setAddress(location.getAddressOne());
								bloc.setCity(location.getCity());
								bloc.setState(location.getState());
								bloc.setZip(location.getPostalCode());
								bloc.setDescription(location.getComments());
								bloc.setLatitude(location.getLat());
								bloc.setLongitude(location.getLng());
								
								//make the place
								Place p = new Place();
								p.setName(advertiser.getName());
								p.setAddress(location.getAddressOne());
								p.setCity(location.getCity());
								p.setState(location.getState());
								p.setZip(location.getPostalCode());
								p.setLatitude(location.getLat());
								p.setLongitude(location.getLng());
								
								placeManagerService.savePlace(p);
								
								bloc.setPlace(p);								
								
								b.getLocations().add(bloc);
								
								
							}
							
							
							
						}						
					}
					
					businessManagerService.saveBusiness(b);
					
				}
				
				awardOffer.setBusiness(b);
				
			} catch (BLServiceException e) {
				logger.error("problem when trying to find business", e);
			}
			
		}
				
		return awardOffer;
	}


	
	private void sendOfferAwardedEmail(String emailTo, AwardOffer ao, Rater rater) 
	{
				
		Map model = new HashMap();

		model.put("awardOffer", ao);
		model.put("rater", rater);
		
		
		BusinessServicesPlaceAwardGenerator generator = 
			new BusinessServicesPlaceAwardGenerator(model);

		/*
		 * sendMessage(String fromAddressName,String fromName, String toAddressName,
					String toName, String subject, String body, String mimetype) 
		 */
		mailerQueueService.sendMessage(
				fromAddress, 
				"RateCred.com Mailer",
				emailTo, 
				ao.getProgramName(), 
				"[RateCred] An Award Has Been Given To A Customer!", 
				generator.makeDisplayString(),
				"text/plain");		
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

	/**
	 * needs to support adility targeting rules, also this should not
	 * only return one offer what it should really do is set all the
	 * possible offers for a type.
	 * 
	 * @param award
	 * @param awardType
	 * @param r
	 * @param pcs
	 * @return
	 * @throws BLServiceException
	 */
	private Offer targetOfferForRater(Award award, 
			AwardType awardType, 
			Rater r,
			Place p,
			PlaceCityState pcs) throws BLServiceException {

		try {
			
			//offerRulesUrl
			RuleBase ruleBase = RuleBaseLoader.loadFromUrl(new URL(awardOfferRulesUrl));

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			
			//ok the reason we dont want to do this is because 
			//we dont want to add these too the pool yet
			//List<Offer> offersRaw = offerPoolService.getOfferPool();
			
			List<Offer> offersRaw = new ArrayList<Offer>();
						
			offersRaw.addAll(getLocalOffersForAward( award, 
					 awardType, 
					 r,
					 p,
					 pcs));
			
			boolean dynamic = true;
			
			//only do this if there are offers to work with
			if(offersRaw.size()>0)
			{
				for (Offer offer : offersRaw) {
					logger.debug("asserting offer:"+offer.getName());
					workingMemory.assertObject(offer, dynamic);
				}
				
				
				//make sure there is a location for the award to go to
				Set<PlaceCityState> allcites = new HashSet<PlaceCityState>();
				for(Rating rating: r.getRatings()) {
					PlaceCityState pcsRating = new PlaceCityState();
					pcsRating.setCity(rating.getPlace().getCity());
					pcsRating.setState(rating.getPlace().getState());				
				}
				
				//now add the offer evaluator
				AwardOfferEvaluator awardOfferEval = new AwardOfferEvaluator(
						award, 
						awardType, 
						r,
						pcs,
						allcites);
				
				workingMemory.assertObject(awardOfferEval, dynamic);
				
				workingMemory.fireAllRules();
				
				awardOfferEval.chooseBestOffers();
				
				//this really should not just return the 
				//offer but set it to the award based on the award type
				if(awardOfferEval.getTargetedOffers().size()>0) {
					Offer bestOffer = awardOfferEval.getTargetedOffers().get(0);
					logger.debug("best offer found:"+bestOffer.toString());
					return bestOffer;
				}
				else //backup plan
				{
					List<Offer> offersFiltered = new ArrayList<Offer>();
					for (Offer offer : offersRaw) {
						if (offer.isVisible())
							offersFiltered.add(offer);
					}
					return offersFiltered.get(0);
				}
				
			} else 
				return null;
			


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
	
	private List<Offer> getLocalOffersForAward(Award award, 
			AwardType awardType, 
			Rater r,
			Place p,
			PlaceCityState pcs) {
		
		logger.debug("getting adility offers");
		
		List<Offer> offersForAward = new ArrayList<Offer>();
		Map<String, String> params = new HashMap<String,String>();
		
		//try to target available offers in pool
		//if this is a place award
		if(p != null) {
			params.put("lat", p.getLatitude().toString());
			params.put("lng", p.getLongitude().toString());
			params.put("distance", "5");
			
		} else if(pcs != null) { //else use the city state
			params.put("city", pcs.getCity());
			params.put("state", pcs.getState());
			params.put("distance", "5");
			
		} else if(r.getRatings().size()>0) {
			Rating rating = r.getRatings().iterator().next();
			params.put("lat", rating.getPlace().getLatitude().toString());
			params.put("lng", rating.getPlace().getLongitude().toString());
			params.put("distance", "10");			
		}		
		
		//we have enough info to make a query
		try {
			if(params.size()>0)
				offersForAward = findOffersForArea(new RequestModel(params,"json"), 20, 5);
		} catch (Exception e) {
			logger.debug("cant find offers for area", e);
		}
		
		logger.debug("returning");
		return offersForAward;

	}
	
	private List<Offer> findOffersForArea(RequestModel model, Integer max, Integer minOffers) {
		List<Offer> offersFound = new ArrayList<Offer>();
		try {
			
			Integer modelDistance = 
				Integer.parseInt(model.getParams().get("distance"));
			
			if(modelDistance<max) {
				modelDistance++;
				
				model.getParams().put("distance", modelDistance.toString());
				Thread.sleep(3000);
				OffersResponse response = resourcesClient.getOffers(model);
				logger.debug("trying distance:"+modelDistance+" offers:"+response.getOffers().size());
				if(response.getOffers().size()<minOffers && modelDistance <= max)
					return findOffersForArea(model, max, minOffers);
				else
				{
					//Offer [beginDateString=2009-12-15, couponCode=null, description=null, expireDateString=2012-10-13, id=null, name=$34.99 For Four Visits, programId=null, programName=null, url=null]
					for (com.adility.resources.model.Offer aoffer : response.getOffers()) { 
						logger.debug(aoffer.toString());
											
						offersFound.add(transformAdilityOffer(aoffer) );					
					}
				}
			}
			
				
			
			
		} catch (AudilityClientException e) {
			logger.error("cannot get response from client", e);
		} catch (InterruptedException e) {
			logger.error("cannot get response from client", e);
		} catch (Exception e) {
			logger.error("cannot get response from client", e);
		}
		return offersFound;
	}
	
	/**
	 * turn an adility offer into a ratecred one that can be used by the
	 * targeting engine
	 * 
	 * @param aoffer
	 * @return
	 */
	private Offer transformAdilityOffer(com.adility.resources.model.Offer aoffer) {
		Offer offer = new Offer();
		offer.setExternalId(aoffer.getId());
		offer.setBeginDateString(aoffer.getStartDate());
		
		if(!StringUtils.isEmpty(aoffer.getOfferClosedDate()))
			offer.setEndDateString(aoffer.getOfferClosedDate());
		else			
			offer.setEndDateString(aoffer.getEndDate());
		
		offer.setExpireDateString(aoffer.getEndDate());
		
		if(aoffer.getAdvertiser().getRedemptionLocations().size()>0)
		{
			offer.setCity(aoffer.getAdvertiser().getRedemptionLocations().get(0).getCity());
			offer.setState(aoffer.getAdvertiser().getRedemptionLocations().get(0).getState());
		}
		
		offer.setDescription(aoffer.getDescription());
		offer.setExtraDetails(aoffer.getFineprint());
		
		
		offer.setDiscountType(aoffer.getDiscountType());
		offer.setType(aoffer.getType());
		
		try {
			offer.setDiscountValue(Float.parseFloat(aoffer.getDiscountValue()));
		} catch (RuntimeException e) { //number format, null
			offer.setDiscountValue(0.0f);
		}
		
		try {
			Money valueMoney = aoffer.getValue();
			if(valueMoney.getCurrencyCode().equals("USD"))
				offer.setValue(valueMoney.getAmount().floatValue()/100.00f);
		} catch (RuntimeException e) { //number format, null
			offer.setValue(0.0f);
		}
		
		try {
			Money priceMoney = aoffer.getPrice();
			if(priceMoney.getCurrencyCode().equals("USD"))
				offer.setPrice(priceMoney.getAmount().floatValue()/100.00f);
			
		} catch (RuntimeException e) { //number format, null
			offer.setPrice(0.0f);
		}
		
		if(aoffer.getCreative() != null && aoffer.getCreative().getIllustrations().size()>0 )  {
			Illustration i = aoffer.getCreative().getIllustrations().get(0);
			offer.setIllustrationUrl(i.getUrl());			
		}
		
		//items
		for (com.adility.resources.model.Item aitem : aoffer.getItems()) {
			Item item = new Item();
			item.setDescription(aitem.getDescription());
			item.setTitle(aitem.getTitle());
			item.setQuantity(aitem.getQuantity());
			offer.getItems().add(item);
		}
		
		//if you cant find it expire in a week from now
		if(StringUtils.isEmpty(aoffer.getEndDate())) {
			Date sevenDays = 
				DateUtils.addDays(
						Calendar.getInstance().getTime(), 
						14,
						TimeZone.getDefault());
			String expires = DateUtils.dateToString(sevenDays, 
					DateUtils.DESC_SIMPLE_FORMAT);
			offer.setExpireDateString(expires);
		} else {
			offer.setExpireDateString(aoffer.getEndDate());
		}
		
		offer.setExternalSource("ADILITY");
		offer.setName(aoffer.getTitle());
		offer.setUrl(aoffer.getAdvertiser().getSiteUrl());
		offer.setProgramName(aoffer.getAdvertiser().getName());
		offer.setProgramId(aoffer.getAdvertiser().getId());
		
		//do the advertiser and locations
		if(aoffer.getAdvertiser() != null) 
			offer.setAdvertiser(
				transformAdilityAdvertiser(aoffer.getAdvertiser()));
	
		
		return offer;
	}
	
	private Advertiser transformAdilityAdvertiser(com.adility.resources.model.Advertiser advertiser) {
			Advertiser dest = new Advertiser();
			
			//not locations or id
			dest.setDescription(advertiser.getDescription());
			//dest.setAdvertiserLogoUrl(advertiser.getLogo().getUrl());
			
			if(advertiser.getCategories() != null && advertiser.getCategories().size()>0)
			{
				Category cat = advertiser.getCategories().get(0);
				dest.setCategoryId(cat.getId());				
			}
			
			
			
			dest.setContactPhone(advertiser.getPublicPhone());
			dest.setSiteUrl(advertiser.getSiteUrl());
			dest.setName(advertiser.getName());			
			dest.setExternalId(advertiser.getId());
			
			for (com.adility.resources.model.Location location : advertiser.getRedemptionLocations()) {
											
				Location newLoc = new Location();
				newLoc.setAddressOne(location.getAddressOne());
				newLoc.setAddressTwo(location.getAddressTwo());
				newLoc.setCity(location.getCity());
				newLoc.setComments(location.getComments());
				newLoc.setLat(location.getLat());
				newLoc.setLng(location.getLng());
				newLoc.setPostalCode(location.getPostalCode());
				newLoc.setState(location.getState());
				
				dest.getLocations().add(newLoc);
				
			}
			
			return dest;
		
	}
	

}
