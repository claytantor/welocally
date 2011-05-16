package com.sightlyinc.ratecred.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Rating implements Serializable {
	
	@JsonProperty
	private Long id;
	
	@JsonProperty
	private Integer version = new Integer(0);
	
	@JsonProperty
	private String type;
	
	@JsonProperty
	private String notes;
	
	@JsonProperty
	private Date timeCreated;
	
	@JsonProperty
	private Long timeCreatedMills;
	
	@JsonProperty
	private Long twitterStatusId;
	
	@JsonProperty
	private String timeCreatedGmt;
	
	@JsonProperty
	private Float raterRating;
	
	@JsonProperty
	private Float userRating;
	
	@JsonProperty
	private String referalUrl;
	
	@JsonProperty
	private String referalToken;
	
	@JsonProperty
	private String checkedinFoursquare;
	
	@JsonProperty
	private String txIdFoursquare;
	
	@JsonProperty
	private String checkedinGowalla;
	
	@JsonProperty
	private String txIdGowalla;

	@JsonProperty
	private transient Set<RatingAttribute> attributes = new HashSet<RatingAttribute>();

	
	@JsonProperty
	private transient Set<Compliment> compliments;
	
	@JsonProperty
	private transient Patron owner;
	
	@JsonProperty
	private transient Place place;
	

	@JsonProperty
	public Long getTimeCreatedMills() {
		return timeCreatedMills;
	}

	@JsonProperty
	public void setTimeCreatedMills(Long timeCreatedMills) {
		this.timeCreatedMills = timeCreatedMills;
	}

	@JsonProperty
	public String getTimeCreatedGmt() {
		return timeCreatedGmt;
	}

	@JsonProperty
	public void setTimeCreatedGmt(String timeCreatedGmt) {
		this.timeCreatedGmt = timeCreatedGmt;
	}

	@JsonProperty
	public Float getRaterRating() {
		return raterRating;
	}

	@JsonProperty
	public void setRaterRating(Float raterRating) {
		this.raterRating = raterRating;
	}

	@JsonProperty
	public Float getUserRating() {
		return userRating;
	}

	@JsonProperty
	public void setUserRating(Float userRating) {
		this.userRating = userRating;
	}

	@JsonProperty
	public String getNotes() {
		return notes;
	}

	@JsonProperty
	public void setNotes(String notes) {
		this.notes = notes;
	}

	@JsonProperty
	public Patron getOwner() {
		return owner;
	}

	@JsonProperty
	public void setOwner(Patron owner) {
		this.owner = owner;
	}

	@JsonProperty
	public Integer getVersion() {
		return version;
	}

	@JsonProperty
	public void setVersion(Integer version) {
		this.version = version;
	}

	@JsonProperty
	public Date getTimeCreated() {
		return timeCreated;
	}

	@JsonProperty
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	@JsonProperty
	public Place getPlace() {
		return place;
	}

	@JsonProperty
	public void setPlace(Place place) {
		this.place = place;
	}

	@JsonProperty
	public Long getId() {
		return id;
	}

	@JsonProperty
	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty
	public String getType() {
		return type;
	}

	@JsonProperty
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty
	public Set<RatingAttribute> getAttributes() {
		return attributes;
	}

	@JsonProperty
	public void setAttributes(Set<RatingAttribute> attributes) {
		this.attributes = attributes;
	}
		

	@JsonIgnore
	public Set<Compliment> getCompliments() {
		return compliments;
	}

	@JsonIgnore
	public void setCompliments(Set<Compliment> compliments) {
		this.compliments = compliments;
	}

	@JsonProperty
	public Long getTwitterStatusId() {
		return twitterStatusId;
	}

	@JsonProperty
	public void setTwitterStatusId(Long twitterStatusId) {
		this.twitterStatusId = twitterStatusId;
	}

	@JsonProperty
	public String getReferalUrl() {
		return referalUrl;
	}

	@JsonProperty
	public void setReferalUrl(String referalUrl) {
		this.referalUrl = referalUrl;
	}

	@JsonProperty
	public String getReferalToken() {
		return referalToken;
	}

	@JsonProperty
	public void setReferalToken(String referalToken) {
		this.referalToken = referalToken;
	}

	@JsonProperty
	public String getCheckedinFoursquare() {
		return checkedinFoursquare;
	}

	@JsonProperty
	public void setCheckedinFoursquare(String checkedinFoursquare) {
		this.checkedinFoursquare = checkedinFoursquare;
	}

	@JsonProperty
	public String getCheckedinGowalla() {
		return checkedinGowalla;
	}

	@JsonProperty
	public void setCheckedinGowalla(String checkedinGowalla) {
		this.checkedinGowalla = checkedinGowalla;
	}

	@JsonProperty
	public String getTxIdFoursquare() {
		return txIdFoursquare;
	}

	@JsonProperty
	public void setTxIdFoursquare(String txIdFoursquare) {
		this.txIdFoursquare = txIdFoursquare;
	}

	@JsonProperty
	public String getTxIdGowalla() {
		return txIdGowalla;
	}

	@JsonProperty
	public void setTxIdGowalla(String txIdGowalla) {
		this.txIdGowalla = txIdGowalla;
	}

	/*@Override
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
	}*/


	



}
