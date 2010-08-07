package com.sightlyinc.ratecred.model;

import java.util.Date;

public class AwardOffer {

	private Long id;
	private Integer version = new Integer(0);
	private Date timeCreated;
	
	private String name;
	private String description;
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

	
	
}
