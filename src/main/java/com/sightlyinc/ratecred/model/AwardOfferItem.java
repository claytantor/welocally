package com.sightlyinc.ratecred.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class AwardOfferItem {
	
	protected Long id;
	protected Integer version = new Integer(0);
	
	private Long timeCreated;
	private Long timeUpdated;	
	
	private String description;
	private String extraDetails;

	private String title;
		
	private Integer quantity;
	
	private AwardOffer offer;
		

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

	public Long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Long getTimeUpdated() {
		return timeUpdated;
	}

	public void setTimeUpdated(Long timeUpdated) {
		this.timeUpdated = timeUpdated;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

	public String getExtraDetails() {
		return extraDetails;
	}

	public void setExtraDetails(String extraDetails) {
		this.extraDetails = extraDetails;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public AwardOffer getOffer() {
		return offer;
	}

	public void setOffer(AwardOffer offer) {
		this.offer = offer;
	}
	
	

}
