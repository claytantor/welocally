package com.sightlyinc.ratecred.model;

import java.util.Date;

public class Award {
	
	private Long id;
	private Integer version = new Integer(0);
	
	private Date timeCreated;
	private Long timeCreatedMills;
	private String timeCreatedGmt;
	
	private Date expires;
	private Long expiresMills;
	private String expiresGmt;
		
	private String notes;
	private String metadata;
	private String status;
	
	private AwardOffer offer;
	
	private Rater owner;
	
	private AwardType awardType;
	

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getExpiresMills() {
		return expiresMills;
	}
	public void setExpiresMills(Long expiresMills) {
		this.expiresMills = expiresMills;
	}
	public String getExpiresGmt() {
		return expiresGmt;
	}
	public void setExpiresGmt(String expiresGmt) {
		this.expiresGmt = expiresGmt;
	}
	public String getTimeCreatedGmt() {
		return timeCreatedGmt;
	}
	public void setTimeCreatedGmt(String timeCreatedGmt) {
		this.timeCreatedGmt = timeCreatedGmt;
	}
	public Long getTimeCreatedMills() {
		return timeCreatedMills;
	}
	public void setTimeCreatedMills(Long timeCreatedMills) {
		this.timeCreatedMills = timeCreatedMills;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
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
	public Date getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
	public Rater getOwner() {
		return owner;
	}
	public void setOwner(Rater owner) {
		this.owner = owner;
	}
	public AwardType getAwardType() {
		return awardType;
	}
	public void setAwardType(AwardType awardType) {
		this.awardType = awardType;
	}
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	public AwardOffer getOffer() {
		return offer;
	}
	public void setOffer(AwardOffer offer) {
		this.offer = offer;
	}
	@Override
	public String toString() {
		return "Award [awardType=" + awardType + ", expires=" + expires
				+ ", expiresGmt=" + expiresGmt + ", expiresMills="
				+ expiresMills + ", id=" + id + ", metadata=" + metadata
				+ ", notes=" + notes + ", offer=" + offer + ", owner=" + owner
				+ ", status=" + status + ", timeCreated=" + timeCreated
				+ ", timeCreatedGmt=" + timeCreatedGmt + ", timeCreatedMills="
				+ timeCreatedMills + ", version=" + version + "]";
	}

	

}
