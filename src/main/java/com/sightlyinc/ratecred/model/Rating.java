package com.sightlyinc.ratecred.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Rating implements Serializable {
	private Long id;
	private Integer version = new Integer(0);
	private String type;
	private String notes;
	private Date timeCreated;
	private Long timeCreatedMills;
	private Long twitterStatusId;
	private String timeCreatedGmt;	
	private Float raterRating;
	private Float userRating;
	private String referalUrl;
	private String referalToken;
	
	private String checkedinFoursquare;
	private String txIdFoursquare;
	private String checkedinGowalla;
	private String txIdGowalla;

	private transient Set<RatingAttribute> attributes = new HashSet<RatingAttribute>();
	
	private transient Set<Compliment> compliments;
	
	private transient Rater owner;
	
	private transient Place place;
	

	public Long getTimeCreatedMills() {
		return timeCreatedMills;
	}

	public void setTimeCreatedMills(Long timeCreatedMills) {
		this.timeCreatedMills = timeCreatedMills;
	}

	public String getTimeCreatedGmt() {
		return timeCreatedGmt;
	}

	public void setTimeCreatedGmt(String timeCreatedGmt) {
		this.timeCreatedGmt = timeCreatedGmt;
	}

	public Float getRaterRating() {
		return raterRating;
	}

	public void setRaterRating(Float raterRating) {
		this.raterRating = raterRating;
	}

	public Float getUserRating() {
		return userRating;
	}

	public void setUserRating(Float userRating) {
		this.userRating = userRating;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Rater getOwner() {
		return owner;
	}

	public void setOwner(Rater owner) {
		this.owner = owner;
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

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public Set<RatingAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<RatingAttribute> attributes) {
		this.attributes = attributes;
	}
		

	public Set<Compliment> getCompliments() {
		return compliments;
	}

	public void setCompliments(Set<Compliment> compliments) {
		this.compliments = compliments;
	}

	public Long getTwitterStatusId() {
		return twitterStatusId;
	}

	public void setTwitterStatusId(Long twitterStatusId) {
		this.twitterStatusId = twitterStatusId;
	}

	public String getReferalUrl() {
		return referalUrl;
	}

	public void setReferalUrl(String referalUrl) {
		this.referalUrl = referalUrl;
	}

	public String getReferalToken() {
		return referalToken;
	}

	public void setReferalToken(String referalToken) {
		this.referalToken = referalToken;
	}

	public String getCheckedinFoursquare() {
		return checkedinFoursquare;
	}

	public void setCheckedinFoursquare(String checkedinFoursquare) {
		this.checkedinFoursquare = checkedinFoursquare;
	}

	public String getCheckedinGowalla() {
		return checkedinGowalla;
	}

	public void setCheckedinGowalla(String checkedinGowalla) {
		this.checkedinGowalla = checkedinGowalla;
	}

	public String getTxIdFoursquare() {
		return txIdFoursquare;
	}

	public void setTxIdFoursquare(String txIdFoursquare) {
		this.txIdFoursquare = txIdFoursquare;
	}

	public String getTxIdGowalla() {
		return txIdGowalla;
	}

	public void setTxIdGowalla(String txIdGowalla) {
		this.txIdGowalla = txIdGowalla;
	}

	@Override
	public String toString() {
		return "Rating [attributes=" + attributes + ", checkedinFoursquare="
				+ checkedinFoursquare + ", checkedinGowalla="
				+ checkedinGowalla + ", compliments=" + compliments + ", id="
				+ id + ", notes=" + notes + ", owner=" + owner + ", place="
				+ place + ", raterRating=" + raterRating + ", referalToken="
				+ referalToken + ", referalUrl=" + referalUrl
				+ ", timeCreated=" + timeCreated + ", timeCreatedGmt="
				+ timeCreatedGmt + ", timeCreatedMills=" + timeCreatedMills
				+ ", twitterStatusId=" + twitterStatusId + ", type=" + type
				+ ", userRating=" + userRating + ", version=" + version + "]";
	}


	



}
