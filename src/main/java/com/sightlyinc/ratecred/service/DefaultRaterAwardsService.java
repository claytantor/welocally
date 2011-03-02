package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

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
import com.adility.resources.model.Illustration;
import com.adility.resources.model.OffersResponse;
import com.adility.resources.model.RequestModel;
import com.noi.utility.date.DateUtils;
import com.noi.utility.mail.MailerQueueService;
import com.noi.utility.random.RandomMaker;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.rosaloves.net.shorturl.bitly.Bitly;
import com.rosaloves.net.shorturl.bitly.BitlyFactory;
import com.rosaloves.net.shorturl.bitly.url.BitlyUrl;
import com.sightlyinc.ratecred.admin.jms.SaveNewAwardMessageProducer;
import com.sightlyinc.ratecred.admin.model.AwardOfferEvaluator;
import com.sightlyinc.ratecred.admin.model.RaterAwards;
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
	
	@Value("${raterAwardsService.offerRulesUrl}")
	private String offerRulesUrl;
	
	@Value("${raterAwardsService.useAwardOfferUrl}")
	private String useAwardOfferUrl;
	
	@Value("${MailerQueueService.smtpUsername}")
	private String fromAddress;
	

	

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
	public Long saveNewAward(Award award, AwardType awardType, Rater r) 
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
		
		giveAwardOffer( award,  awardType,  r,  p,  pcs) ;
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
			
			try {
				AwardOffer aoffer = transformOffer(offer);
				aoffer.setAwardType(awardType);
				awardManagerService.saveAwardOffer(aoffer);
				award.getOffers().add(aoffer);
				
			} catch (Exception e) {
				logger.error("fail", e);
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
		awardOffer.setDescription(offer.getDescription());
		awardOffer.setExpireDateMillis(offer.getExpire().getTime());
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
					Advertiser a = offer.getAdvertiser();
					b.setAdvertiserId(a.getExternalId());
					b.setAdvertiserSource(offer.getExternalSource());
					b.setDescription(a.getDescription());
					b.setName(a.getTitle());
					b.setWebsite(a.getSiteUrl());
					
					//do locations
					if(a.getLocations().size()>0) {
						for (Location location : a.getLocations()) {
							
							//try to find the business location, if not there
							//create both the bl and the place
							List<BusinessLocation> locations = 
								businessManagerService.findBusinessLocationByInfo(a.getTitle(),
									location.getAddressOne(), 
									location.getCity(), 
									location.getState(), 
									location.getPostalCode());
							
							if(locations == null || locations.size()==0)
							{
								BusinessLocation bloc = new BusinessLocation();
								bloc.setName(a.getTitle());
								bloc.setAddress(location.getAddressOne());
								bloc.setCity(location.getCity());
								bloc.setState(location.getState());
								bloc.setZip(location.getPostalCode());
								bloc.setDescription(location.getComments());
								bloc.setLatitude(location.getLat());
								bloc.setLongitude(location.getLat());
								
								//make the place
								Place p = new Place();
								p.setName(a.getTitle());
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
	 * needs to support adility targeting rules
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
			RuleBase ruleBase = RuleBaseLoader.loadFromUrl(new URL(offerRulesUrl));

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			
			offerPoolService.addOffersToPool(
					getLocalOffersForAward( award, 
						 awardType, 
						 r,
						 p,
						 pcs));

			List<Offer> offersRaw = offerPoolService.getOfferPool();			
			
			boolean dynamic = true;
			for (Offer offer : offersRaw) {
				logger.debug("asserting offer:"+offer.getName());
				/*if(pcs == null && 
						!StringUtils.isEmpty(offer.getCity())
						&& !StringUtils.isEmpty(offer.getState()))
				{
					pcs = new PlaceCityState(offer.getCity(), offer.getState(), 1);
				} */
				
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
			
			if(awardOfferEval.hasOffer()) {
				logger.debug("best offer found:"+awardOfferEval.getOffer().toString());
				return awardOfferEval.getOffer();
			}
			else //backup plan
			{
				List<Offer> offersFiltered = new ArrayList<Offer>();
				for (Offer offer : offersRaw) {
					if (offer.isVisible())
						offersFiltered.add(offer);
				}
				int pos = RandomMaker.nextInt(1, offersFiltered.size() - 1);
				return offersFiltered.get(pos);
			}


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
			params.put("distance", "10");
		} else if(pcs != null) { //else use the city state
			params.put("city", pcs.getCity());
			params.put("state", pcs.getState());
			params.put("distance", "15");
		} else if(r.getRatings().size()>0) {
			Rating rating = r.getRatings().iterator().next();
			params.put("lat", rating.getPlace().getLatitude().toString());
			params.put("lng", rating.getPlace().getLongitude().toString());
			params.put("distance", "10");			
		}		
		
		//we have enough info to make a query
		if(params.size()>0)
		{
			RequestModel model = new RequestModel(params,"json");
			try {
				OffersResponse response = resourcesClient.getOffers(model);
				//Offer [beginDateString=2009-12-15, couponCode=null, description=null, expireDateString=2012-10-13, id=null, name=$34.99 For Four Visits, programId=null, programName=null, url=null]
				for (com.adility.resources.model.Offer aoffer : response.getOffers()) { 
					logger.debug(aoffer.toString());
										
					offersForAward.add(transformAdilityOffer(aoffer) );					
				}
				
			} catch (AudilityClientException e) {
				logger.error("cannot get response from client", e);
			}
		}
		
		return offersForAward;

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
		if(aoffer.getAdvertiser().getLocations().size()>0)
		{
			offer.setCity(aoffer.getAdvertiser().getLocations().get(0).getCity());
			offer.setState(aoffer.getAdvertiser().getLocations().get(0).getState());
		}
		if(aoffer.getDescription() == null)
			offer.setDescription(aoffer.getFineprint());
		else	
			offer.setDescription(aoffer.getDescription());
		
		offer.setDiscountType(aoffer.getDiscountType());
		offer.setType(aoffer.getType());
		
		try {
			offer.setDiscountValue(Float.parseFloat(aoffer.getDiscountValue()));
		} catch (RuntimeException e) { //number format, null
			offer.setDiscountValue(0.0f);
		}
		
		try {
			offer.setValue(Float.parseFloat(aoffer.getValue()));
		} catch (RuntimeException e) { //number format, null
			offer.setValue(0.0f);
		}
		
		try {
			offer.setPrice(Float.parseFloat(aoffer.getPrice()));
		} catch (RuntimeException e) { //number format, null
			offer.setPrice(0.0f);
		}
		
		if(aoffer.getIllustrations().size()>0)  {
			Illustration i = aoffer.getIllustrations().get(0);
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
		offer.setProgramName(aoffer.getAdvertiser().getTitle());
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
			dest.setAdvertiserLogoUrl(advertiser.getLogo().getUrl());
			dest.setCategoryId(advertiser.getCategoryId());
			dest.setContactPhone(advertiser.getContact_phone());
			dest.setSiteUrl(advertiser.getSiteUrl());
			dest.setTitle(advertiser.getTitle());			
			dest.setExternalId(advertiser.getId());
			
			for (com.adility.resources.model.Location location : advertiser.getLocations()) {
							
				
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
