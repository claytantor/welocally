package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


@JsonAutoDetect(fieldVisibility=Visibility.NONE, getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE)
public class Award {
	
	@JsonProperty
	private Long id;
	
	@JsonProperty
	private Integer version = new Integer(0);
	
	@JsonProperty
	private Date timeCreated;
	
	@JsonProperty
	private Long timeCreatedMills;
	
	@JsonProperty
	private String timeCreatedGmt;
	
	@JsonProperty
	private Date expires;
	
	@JsonProperty
	private Long expiresMills;
	
	@JsonProperty
	private String expiresGmt;
	
	@JsonProperty
	private String notes;
	
	@JsonProperty
	private String metadata;
	
	@JsonProperty
	private String status;
	
	@JsonProperty
	private Boolean giveOffer = true;
	
	@JsonIgnore
	private Set<AwardOffer> offers;
	
	@JsonIgnore
	private Rater owner;
	
	@JsonIgnore
	private AwardType awardType;
	

	public Award() {
		super();
		offers = new HashSet<AwardOffer>();
	}
	
	@JsonProperty
	public Boolean getGiveOffer() {
		return giveOffer;
	}
	
	@JsonProperty
	public void setGiveOffer(boolean giveOffer) {
		this.giveOffer = giveOffer;
	}
	
	@JsonProperty
	public String getStatus() {
		return status;
	}
	
	@JsonProperty
	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonProperty
	public Long getExpiresMills() {
		return expiresMills;
	}
	
	@JsonProperty
	public void setExpiresMills(Long expiresMills) {
		this.expiresMills = expiresMills;
	}
	
	@JsonProperty
	public String getExpiresGmt() {
		return expiresGmt;
	}
	
	@JsonProperty
	public void setExpiresGmt(String expiresGmt) {
		this.expiresGmt = expiresGmt;
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
	public Long getTimeCreatedMills() {
		return timeCreatedMills;
	}
	
	@JsonProperty
	public void setTimeCreatedMills(Long timeCreatedMills) {
		this.timeCreatedMills = timeCreatedMills;
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
	public Date getExpires() {
		return expires;
	}
	
	@JsonProperty
	public void setExpires(Date expires) {
		this.expires = expires;
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
	public String getMetadata() {
		return metadata;
	}
	
	@JsonProperty
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	
	@JsonProperty
	public void setGiveOffer(Boolean giveOffer) {
		this.giveOffer = giveOffer;
	}
	
	//relationships
	
	@JsonIgnore
	public Rater getOwner() {
		return owner;
	}
	
	@JsonIgnore
	public void setOwner(Rater owner) {
		this.owner = owner;
	}
	
	@JsonIgnore
	public AwardType getAwardType() {
		return awardType;
	}
	
	@JsonIgnore
	public void setAwardType(AwardType awardType) {
		this.awardType = awardType;
	}
	
	@JsonIgnore
	public Set<AwardOffer> getOffers() {
		return offers;
	}
	
	@JsonIgnore
	public void setOffers(Set<AwardOffer> offers) {
		this.offers = offers;
	}
	
	
}
