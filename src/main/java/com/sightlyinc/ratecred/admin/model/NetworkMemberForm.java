package com.sightlyinc.ratecred.admin.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.sightlyinc.ratecred.model.NetworkMember;


public class NetworkMemberForm {
	
	static Logger logger = Logger.getLogger(NetworkMemberForm.class);
	
	private Long id;
	
	private Integer version;
	
	private String name;
	
	private String memberKey;
	
	private String description;
	
	private String iconUrl;
	
	private String mapIconUrl;
	
	private String primaryEmail;
	
	private String paypalEmail;
	
	private String type;
	
	private Long publisherId;
	
	private Long affiliateId;
	
	private Long merchantId;

	public NetworkMemberForm() {
		super();
	}
	
	public NetworkMemberForm(NetworkMember member) {

		this.setId(member.getId());
		this.setVersion(member.getVersion());
		
		this.setDescription(member.getDescription());
		this.setIconUrl(member.getIconUrl());
		this.setMapIconUrl(member.getMapIconUrl());
		this.setMemberKey(member.getMemberKey());
		this.setName(member.getName());
		this.setPaypalEmail(member.getPaypalEmail());
		this.setPrimaryEmail(member.getPrimaryEmail());		
		this.setType(member.getType().name());
	
		if(member.getPublisher() != null)
			this.setPublisherId(member.getPublisher().getId());
		if(member.getAffiliate() != null)
			this.setAffiliateId(member.getAffiliate().getId());
		if(member.getMerchant() != null)
			this.setMerchantId(member.getMerchant().getId());
		
		
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemberKey() {
		return memberKey;
	}

	public void setMemberKey(String memberKey) {
		this.memberKey = memberKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getMapIconUrl() {
		return mapIconUrl;
	}

	public void setMapIconUrl(String mapIconUrl) {
		this.mapIconUrl = mapIconUrl;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getPaypalEmail() {
		return paypalEmail;
	}

	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public Long getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(Long affiliateId) {
		this.affiliateId = affiliateId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	

	
	
}
