package com.sightlyinc.ratecred.model;

import java.util.Date;

public class AwardOffer {
	
	private Long id;
	private Integer version = new Integer(0);
	private Date timeCreated;
	
	private String externalId;
	private String externalSource;
	private String programId;
	private String programName;
	private String name;
	private String couponCode;
	private String description;
	private String url;
	private Long beginDateMillis;
	private Long expireDateMillis;
	
	
	private String status;
	private AwardType awardType;
	
	private Business business;
	
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
	public Date getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public AwardType getAwardType() {
		return awardType;
	}
	public void setAwardType(AwardType awardType) {
		this.awardType = awardType;
	}

	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getExternalSource() {
		return externalSource;
	}
	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}
	
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getBeginDateMillis() {
		return beginDateMillis;
	}
	public void setBeginDateMillis(Long beginDateMillis) {
		this.beginDateMillis = beginDateMillis;
	}
	public Long getExpireDateMillis() {
		return expireDateMillis;
	}
	public void setExpireDateMillis(Long expireDateMillis) {
		this.expireDateMillis = expireDateMillis;
	}


	
	
}
