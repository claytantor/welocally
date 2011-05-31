package com.sightlyinc.ratecred.admin.mvc.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sightlyinc.ratecred.admin.model.NetworkMemberForm;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.service.NetworkMemberService;

@Controller
@RequestMapping(value="/admin/member")
public class NetworkMemberController {
	
	
	static Logger logger = Logger.getLogger(NetworkMemberController.class);


	@Autowired
	private NetworkMemberService networkMemberService;

	
	@RequestMapping(method=RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("networkMemberForm",new NetworkMemberForm());
		return "member/edit";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String create(@Valid NetworkMemberForm form, BindingResult result, Model model) {
		logger.debug("got post action");
		NetworkMember member = new NetworkMember();
		
		if(form.getId() != null)
			member = networkMemberService.findByPrimaryKey(form.getId());
		
		if(member!= null)
		{
			
			member.setId(form.getId());
			member.setVersion(form.getVersion());
			
			member.setDescription(form.getDescription());
			member.setIconUrl(form.getIconUrl());
			member.setMapIconUrl(form.getMapIconUrl());
			member.setMemberKey(form.getMemberKey());
			member.setName(form.getName());
			member.setPaypalEmail(form.getPaypalEmail());
			member.setPrimaryEmail(form.getPrimaryEmail());
			
			/*member.setType(form.getType());
		
			if(form.getPublisher() != null)
				member.setPublisherId(form.getPublisher().getId());
			if(form.getAffiliate() != null)
				member.setAffiliateId(form.getAffiliate().getId());
			if(form.getMerchant() != null)
				member.setMerchantId(form.getMerchant().getId());*/
			
	
			Long id = networkMemberService.save(member);
			
			
			return "redirect:/admin/member/"+id.toString();
		} else {
			model.addAttribute("networkMember", member);
			return "member/edit";
		}

	}
	
	@RequestMapping(value="{id}", method=RequestMethod.GET)
	public String getNetworkMemberById(@PathVariable Long id, Model model) {
		logger.debug("view");
		model.addAttribute("member", networkMemberService.findByPrimaryKey(id));
		return "member/view";
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String editNetworkMember(@PathVariable Long id, Model model) {
		logger.debug("edit");
		model.addAttribute(
				"networkMemberForm",
				new NetworkMemberForm(networkMemberService.findByPrimaryKey(id)));
		return "member/edit";
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deleteNetworkMember(@PathVariable Long id, Model model) {
		logger.debug("delete");
		NetworkMember p = networkMemberService.findByPrimaryKey(id);
		networkMemberService.delete(p);
		return "redirect:/admin/member/list";
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String getList(Model model) {
		logger.debug("list");
		model.addAttribute("members", networkMemberService.findAll());
		return "member/list";
	}

}
