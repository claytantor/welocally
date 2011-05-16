package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.noi.utility.date.DateUtils;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.AwardForm;
import com.sightlyinc.ratecred.admin.model.CustomAwardForm;
import com.sightlyinc.ratecred.client.offers.OfferOld;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.Offer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.OfferPoolService;
import com.sightlyinc.ratecred.service.PatronAwardsService;
import com.sightlyinc.ratecred.service.RatingManagerService;

@Controller
@RequestMapping(value="/admin/award")
public class AwardController {
	
	
	static Logger logger = Logger.getLogger(AwardController.class);

	@Autowired
	@Qualifier("RatingManagerService")
	private RatingManagerService ratingManagerService;
	
	@Autowired
	@Qualifier("offerPoolService")
	private OfferPoolService offerPoolService;
	
	@Autowired
	private AwardManagerService awardManagerService;
	
	@Autowired
	PatronAwardsService raterAwardsService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("awardForm",new AwardForm());
		
		try {
			StringBuffer buf = new StringBuffer();
			List<Patron> users = ratingManagerService.findRatersByStatus("USER");
			for (Patron rater : users) {
				buf.append(rater.getUserName()+",");
			}
			model.addAttribute("users", buf.toString());
			
			List<AwardType> awardTypes = awardManagerService.findAllAwardTypes();
			StringBuffer buf2 = new StringBuffer();
			for (AwardType awardType : awardTypes) {
				buf2.append(awardType.getKeyname()+",");
			}
			model.addAttribute("awardTypes", buf2.toString());
			
			List<OfferOld> offersRaw = offerPoolService.getOfferPool();
			List<OfferOld> offersFiltered = new ArrayList<OfferOld>();
			for (OfferOld offer : offersRaw) {
				if(offer.getCouponCode() != null 
						&& !offer.getCouponCode().contains("No")
						&& !offer.getCouponCode().contains("no")
						&& !offer.getDescription().contains("Gay")
						&& !offer.getDescription().contains("Lesbian")
						&& !offer.getDescription().contains("shipping")
						&& !offer.getDescription().contains("Shipping")
						&& !offer.getName().contains("shipping")
						&& !offer.getName().contains("Shipping")
						&& !offer.getName().contains("SHIPPING")
						
				)
					offersFiltered.add(offer);
			}
			model.addAttribute("offers", offersFiltered);
			
			StringBuffer bufOfferIds = new StringBuffer();
			for (OfferOld offer : offersFiltered) {
				bufOfferIds.append(offer.getExternalId()+":"+offer.getExternalSource()+",");
			}
			model.addAttribute("offerIds", bufOfferIds.toString());			
			
			
		} catch (BLServiceException e) {
			logger.error("problem",e);
			return "error";
		}	
		
		return "award_form";
	}
	
	@RequestMapping(value="/custom",method=RequestMethod.GET)
	public String getCustomCreateForm(Model model) {
		model.addAttribute("customAwardForm",new CustomAwardForm());
		
		try {
			StringBuffer buf = new StringBuffer();
			List<Patron> users = ratingManagerService.findRatersByStatus("USER");
			for (Patron rater : users) {
				buf.append(rater.getUserName()+",");
			}
			model.addAttribute("users", buf.toString());
			
			List<AwardType> awardTypes = awardManagerService.findAllAwardTypes();
			StringBuffer buf2 = new StringBuffer();
			for (AwardType awardType : awardTypes) {
				buf2.append(awardType.getKeyname()+",");
			}
			model.addAttribute("awardTypes", buf2.toString());
			
			
			
		} catch (BLServiceException e) {
			logger.error("problem",e);
			return "error";
		}	
		
		return "custom_award_form";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid AwardForm awardForm, BindingResult result) {
		logger.debug("got post action");
		Award award = new Award();
		try {
			AwardType awardType = awardManagerService.findAwardTypeByKey(awardForm.getType());
			award.setAwardType(awardType);
			Patron r = ratingManagerService.findRaterByUsername(awardForm.getUsername());
			award.setOwner(r);			
			award.setNotes(awardForm.getNote());
			award.setStatus("GIVEN");
			award.setMetadata("imageUrl=/images/awards/award_"+awardForm.getType()+".png");
			award.setExpires(0l);
			
			String[] offerInfo = awardForm.getOfferId().split(":");
			
			//offer stuff
			OfferOld offer = offerPoolService.getOfferByExternalIdSource(offerInfo[0], offerInfo[1]);
			
			if(offer != null)
			{
				Offer aoffer = new Offer();
				award.setGiveOffer(true);
				aoffer.setAwardType(awardType);
				
				aoffer.setCode(offer.getCouponCode());
				aoffer.setTimeStarts(offer.getBegin().getTime());
				aoffer.setTimeEnds(offer.getExpire().getTime());
				aoffer.setDescription(offer.getDescription());
				aoffer.setExtraDetails(offer.getExtraDetails());
				aoffer.setName(offer.getName());
				aoffer.setStatus("GIVEN");
				aoffer.setUrl(offer.getUrl());
				aoffer.setIllustrationUrl(offer.getIllustrationUrl());
				aoffer.setPrice(offer.getPrice());
				aoffer.setOfferValue(offer.getValue());
				aoffer.setQuantity(offer.getQuantity());
	
				awardManagerService.saveAwardOffer(aoffer);
				
				//award.setOffer(aoffer);
				award.getOffers().add(aoffer);
				
				//Long id = raterAwardsService.saveNewAward(award, awardType, r);
				//return "redirect:/do/admin/award/" + id;
				throw new RuntimeException("SEND TO QUEUE");
				
			}
			

			return "error";
			
		} catch (BLServiceException e) {
			logger.error("problem saving award", e);
			return "error";
		}

	}
	
	@RequestMapping(value="/custom",method=RequestMethod.POST)
	public String createCustom(@Valid CustomAwardForm customAwardForm, BindingResult result) {
		logger.debug("got post action");
		Award award = new Award();
		try {
			AwardType awardType = awardManagerService.findAwardTypeByKey(customAwardForm.getType());
			award.setAwardType(awardType);
			Patron r = ratingManagerService.findRaterByUsername(customAwardForm.getUsername());
			award.setOwner(r);			
			award.setNotes(customAwardForm.getNote());
			award.setStatus("GIVEN");
			award.setMetadata("imageUrl=/images/awards/award_"+customAwardForm.getType()+".png");
			award.setExpires(0l);
			if(Boolean.parseBoolean(customAwardForm.getGiveAward()))
			{
				Offer aoffer = new Offer();
				
				//dont auto assign
				award.setGiveOffer(false);
				
				aoffer.setAwardType(awardType);
				
				aoffer.setCode(customAwardForm.getCouponCode());
				aoffer.setTimeStarts(
						DateUtils.stringToDate(
								customAwardForm.getBeginDateString(), 
								DateUtils.DESC_SIMPLE_FORMAT).getTime());
				aoffer.setTimeEnds(
						DateUtils.stringToDate(customAwardForm.getExpireDateString(), 
						DateUtils.DESC_SIMPLE_FORMAT).getTime());
				
				aoffer.setDescription(customAwardForm.getDescription());
				
				
				aoffer.setName(customAwardForm.getName());

				aoffer.setStatus("GIVEN");
				aoffer.setUrl(customAwardForm.getUrl());
				awardManagerService.saveAwardOffer(aoffer);
				
				//award.setOffer(aoffer);
				award.getOffers().add(aoffer);
				
				//Long id = raterAwardsService.saveNewAward(award, awardType, r);
				//return "redirect:/do/admin/award/" + id;
				throw new RuntimeException("SEND TO QUEUE");
			}
						

			return "error";
			
		} catch (BLServiceException e) {
			logger.error("problem saving award", e);
			return "error";
		}

	}	
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getAward(@PathVariable Long id, Model model) {
		logger.debug("getAward");
		try {
			Award a = awardManagerService.findAwardByPrimaryKey(id);
			if (a == null) {
				return "error";
			}
			model.addAttribute("award",a);
		} catch (BLServiceException e) {
			logger.error("problem",e);
			return "error";
		}
		return "award";
	}

	public void setOfferPoolService(OfferPoolService offerPoolService) {
		this.offerPoolService = offerPoolService;
	}
	
	
	
/*private Map<Long, Account> accounts = new ConcurrentHashMap<Long, Account>();
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute(new Account());
		return "account/createForm";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid Account account, BindingResult result) {
		if (result.hasErrors()) {
			return "account/createForm";
		}
		this.accounts.put(account.assignId(), account);
		return "redirect:/account/" + account.getId();
	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getView(@PathVariable Long id, Model model) {
		Account account = this.accounts.get(id);
		if (account == null) {
			throw new ResourceNotFoundException(id);
		}
		model.addAttribute(account);
		return "account/view";
	}*/

}
