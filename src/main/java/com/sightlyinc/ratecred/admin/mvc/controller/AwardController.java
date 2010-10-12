package com.sightlyinc.ratecred.admin.mvc.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.model.AwardForm;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.RatingManagerService;

@Controller
@RequestMapping(value="/admin/award")
public class AwardController {
	
	
	static Logger logger = Logger.getLogger(AwardController.class);

	@Autowired
	@Qualifier("RatingManagerService")
	private RatingManagerService ratingManagerService;
	
	@Autowired
	private AwardManagerService awardManagerService;
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("awardForm",new AwardForm());
		
		try {
			StringBuffer buf = new StringBuffer();
			List<Rater> users = ratingManagerService.findRatersByStatus("USER");
			for (Rater rater : users) {
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
		
		return "award_form";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid AwardForm awardForm, BindingResult result) {
		logger.debug("got post action");
		Award award = new Award();
		try {
			AwardType awardType = awardManagerService.findAwardTypeByKey(awardForm.getType());
			award.setAwardType(awardType);
			Rater r = ratingManagerService.findRaterByUsername(awardForm.getUsername());
			award.setOwner(r);			
			award.setNotes(awardForm.getNote());
			award.setStatus("GIVEN");
			award.setMetadata("imageUrl=/images/awards/award_"+awardForm.getType()+".png");
			award.setExpiresMills(0l);
			Long id = awardManagerService.saveAward(award);
			
			if(award.getId() != null)		
				return "redirect:/do/admin/award/" + id;
			else
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
