package com.sightlyinc.ratecred.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@Entity
@Table(name = "award")
@JsonAutoDetect(fieldVisibility=Visibility.NONE, getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE)
public class Award extends BaseEntity {
		
	@JsonProperty
	@Column(name = "expires")
	private Long expires;	
	
	@JsonProperty
	@Column(name = "notes")
	private String notes;
	
	@JsonProperty
	@Column(name = "metadata")
	private String metadata;
	
	@JsonProperty
	@Column(name = "status")
	private String status;
	
	//not persistent
	@JsonProperty
	private Boolean giveOffer = true;
		
	@JsonIgnore
	@OneToMany(mappedBy = "award")
	private Set<Offer> offers;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "patron_id")
	private Patron owner;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "award_type_id")
	private AwardType awardType;
	

	public Award() {
		super();
		offers = new HashSet<Offer>();
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
	public String getNotes() {
		return notes;
	}
	
	@JsonProperty
	public void setNotes(String notes) {
		this.notes = notes;
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
	public Patron getOwner() {
		return owner;
	}
	
	@JsonIgnore
	public void setOwner(Patron owner) {
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
	public Set<Offer> getOffers() {
		return offers;
	}
	
	@JsonIgnore
	public void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}
	
	
}
