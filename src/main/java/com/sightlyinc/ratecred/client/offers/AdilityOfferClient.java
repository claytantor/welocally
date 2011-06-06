package com.sightlyinc.ratecred.client.offers;



public class AdilityOfferClient  {
	
//	static Logger logger = Logger.getLogger(AdilityOfferClient.class);
//	
//
//	private List<RequestModel> requestModelList = new ArrayList<RequestModel>();
//
//	/*@Autowired
//    @Qualifier("resourcesClient")*/
//    private ResourcesClient resourcesClient;
//	
//	/*@Autowired
//	@Qualifier("sanFranciscoRequestModel")*/
//	private RequestModel sanFranciscoRequestModel;
//	
//	
//	/**
//	 * I hate that I have to do this but the <util:list> beans are being
//	 * found by the component scanner
//	 */
//	@PostConstruct
//	private void setupRequestModel() {
//		if(requestModelList.size() == 0) {
//			requestModelList.add(sanFranciscoRequestModel);
//		}
//		
//	}
//	
//	@Override
//	public List<OfferOld> getOffers() throws OfferFeedException {
//		List<OfferOld> allOffers = new ArrayList<OfferOld>();
//		for (RequestModel model : requestModelList) {
//			allOffers.addAll(findOffersForArea(model, 20, 10));
//		}
//		return allOffers;
//	}
//	
//	public List<OfferOld> findOffersForArea(RequestModel model, Integer max, Integer minOffers) {
//		List<OfferOld> offersFound = new ArrayList<OfferOld>();
//		try {
//			
//			Integer modelDistance = 
//				Integer.parseInt(model.getParams().get("distance"));
//			
//			if(modelDistance<max) {
//				modelDistance++;
//				
//				model.getParams().put("distance", modelDistance.toString());
//				Thread.sleep(3000);
//				OffersResponse response = resourcesClient.getOffers(model);
//				logger.debug("trying distance:"+modelDistance+" offers:"+response.getOffers().size());
//				if(response.getOffers().size()<minOffers && modelDistance <= max)
//					return findOffersForArea(model, max, minOffers);
//				else
//				{
//					//Offer [beginDateString=2009-12-15, couponCode=null, description=null, expireDateString=2012-10-13, id=null, name=$34.99 For Four Visits, programId=null, programName=null, url=null]
//					for (com.adility.resources.model.Offer aoffer : response.getOffers()) { 
//						logger.debug(aoffer.toString());
//											
//						offersFound.add(transformAdilityOffer(aoffer) );					
//					}
//				}
//			}
//			
//				
//			
//			
//		} catch (AudilityClientException e) {
//			logger.error("cannot get response from client", e);
//		} catch (InterruptedException e) {
//			logger.error("cannot get response from client", e);
//		} catch (Exception e) {
//			logger.error("cannot get response from client", e);
//		}
//		return offersFound;
//	}
//	
//	
//	
//	/**
//	 * turn an adility offer into a ratecred one that can be used by the
//	 * targeting engine
//	 * 
//	 * @param aoffer
//	 * @return
//	 */
//	private OfferOld transformAdilityOffer(com.adility.resources.model.Offer aoffer) {
//		OfferOld offer = new OfferOld();
//		offer.setExternalId(aoffer.getId());
//		offer.setBeginDateString(aoffer.getStartDate());
//		
//		if(!StringUtils.isEmpty(aoffer.getOfferClosedDate()))
//			offer.setEndDateString(aoffer.getOfferClosedDate());
//		else			
//			offer.setEndDateString(aoffer.getEndDate());
//		
//		offer.setExpireDateString(aoffer.getEndDate());
//		
//		if(aoffer.getAdvertiser().getRedemptionLocations().size()>0)
//		{
//			offer.setCity(aoffer.getAdvertiser().getRedemptionLocations().get(0).getCity());
//			offer.setState(aoffer.getAdvertiser().getRedemptionLocations().get(0).getState());
//		}
//		
//		offer.setDescription(aoffer.getDescription());
//		offer.setExtraDetails(aoffer.getFineprint());
//		
//		
//		offer.setDiscountType(aoffer.getDiscountType());
//		offer.setType(aoffer.getType());
//		
//		try {
//			offer.setDiscountValue(Float.parseFloat(aoffer.getDiscountValue()));
//		} catch (RuntimeException e) { //number format, null
//			offer.setDiscountValue(0.0f);
//		}
//		
//		try {
//			Money valueMoney = aoffer.getValue();
//			if(valueMoney.getCurrencyCode().equals("USD"))
//				offer.setValue(valueMoney.getAmount().floatValue()/100.00f);
//		} catch (RuntimeException e) { //number format, null
//			offer.setValue(0.0f);
//		}
//		
//		try {
//			Money priceMoney = aoffer.getPrice();
//			if(priceMoney.getCurrencyCode().equals("USD"))
//				offer.setPrice(priceMoney.getAmount().floatValue()/100.00f);
//			
//		} catch (RuntimeException e) { //number format, null
//			offer.setPrice(0.0f);
//		}
//		
//		if(aoffer.getCreative() != null && aoffer.getCreative().getIllustrations().size()>0 )  {
//			Illustration i = aoffer.getCreative().getIllustrations().get(0);
//			offer.setIllustrationUrl(i.getUrl());			
//		}
//		
//		//items
//		for (com.adility.resources.model.Item aitem : aoffer.getItems()) {
//			Item item = new Item();
//			item.setDescription(aitem.getDescription());
//			item.setTitle(aitem.getTitle());
//			item.setQuantity(aitem.getQuantity());
//			offer.getItems().add(item);
//		}
//		
//		//if you cant find it expire in a week from now
//		if(StringUtils.isEmpty(aoffer.getEndDate())) {
//			Date sevenDays = 
//				DateUtils.addDays(
//						Calendar.getInstance().getTime(), 
//						14,
//						TimeZone.getDefault());
//			String expires = DateUtils.dateToString(sevenDays, 
//					DateUtils.DESC_SIMPLE_FORMAT);
//			offer.setExpireDateString(expires);
//		} else {
//			offer.setExpireDateString(aoffer.getEndDate());
//		}
//		
//		offer.setExternalSource("ADILITY");
//		offer.setName(aoffer.getTitle());
//		offer.setUrl(aoffer.getAdvertiser().getSiteUrl());
//		offer.setProgramName(aoffer.getAdvertiser().getName());
//		offer.setProgramId(aoffer.getAdvertiser().getId());
//		
//		//do the advertiser and locations
//		if(aoffer.getAdvertiser() != null) 
//			offer.setAdvertiser(
//				transformAdilityAdvertiser(aoffer.getAdvertiser()));
//	
//		
//		return offer;
//	}
//	
//	private Advertiser transformAdilityAdvertiser(com.adility.resources.model.Advertiser advertiser) {
//			Advertiser dest = new Advertiser();
//			
//			//not locations or id
//			dest.setDescription(advertiser.getDescription());
//			//dest.setAdvertiserLogoUrl(advertiser.getLogo().getUrl());
//			
//			if(advertiser.getCategories() != null && advertiser.getCategories().size()>0)
//			{
//				Category cat = advertiser.getCategories().get(0);
//				dest.setCategoryId(cat.getId());				
//			}
//			
//			
//			
//			dest.setContactPhone(advertiser.getPublicPhone());
//			dest.setSiteUrl(advertiser.getSiteUrl());
//			dest.setName(advertiser.getName());			
//			dest.setExternalId(advertiser.getId());
//			
//			for (com.adility.resources.model.Location location : advertiser.getRedemptionLocations()) {
//											
//				Location newLoc = new Location();
//				newLoc.setAddressOne(location.getAddressOne());
//				newLoc.setAddressTwo(location.getAddressTwo());
//				newLoc.setCity(location.getCity());
//				newLoc.setComments(location.getComments());
//				newLoc.setLat(location.getLat());
//				newLoc.setLng(location.getLng());
//				newLoc.setPostalCode(location.getPostalCode());
//				newLoc.setState(location.getState());
//				
//				dest.getLocations().add(newLoc);
//				
//			}
//			
//			return dest;
//		
//	}	

}
