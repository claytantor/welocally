package com.sightlyinc.ratecred.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 *
 *`id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `offer_id` BIGINT(20) NULL DEFAULT NULL ,
  `version` INT(11) NULL DEFAULT NULL ,
  `title` VARCHAR(255) NULL DEFAULT NULL ,
  `description` LONGTEXT NULL DEFAULT NULL ,
  `quantity` INT(11) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */
@Entity
@Table(name="offer_item")
public class OfferItem extends BaseEntity {
	
	private String description;
	
	private String extraDetails;

	private String title;
		
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name = "offer_id")
	private Offer offer;
			
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

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
	

}
