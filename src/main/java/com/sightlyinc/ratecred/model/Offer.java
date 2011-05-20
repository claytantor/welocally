package com.sightlyinc.ratecred.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *   `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `merchant_id` BIGINT(20) NOT NULL ,
  `award_id` BIGINT(20) NOT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `status` VARCHAR(45) NULL DEFAULT NULL ,
  `code` VARCHAR(45) NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `discount_type` ENUM('DISCOUNT','COMP','PROMOCODE') NULL DEFAULT NULL ,
  `offer_type` ENUM('DEAL','VOUCHER','EVOUCHER','GIFTCARD','ADVERTISMENT') NULL DEFAULT NULL ,
  `quantity` INT(11) NULL DEFAULT NULL ,
  `price` FLOAT(11) NULL DEFAULT NULL ,
  `offer_value` FLOAT(11) NULL DEFAULT NULL ,
  `extra_details` TEXT NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `time_starts` BIGINT NULL DEFAULT NULL ,
  `time_ends` BIGINT NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */
@Entity
@Table(name="offer")
public class Offer extends BaseEntity {

	public enum DiscountType {DISCOUNT,COMP,PROMOCODE}
	public enum OfferType {DEAL,VOUCHER,EVOUCHER,GIFTCARD,ADVERTISMENT}
	
	private String name;
	private String code;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	private String url;
	
	@Column(name="time_starts")
	private Long timeStarts;
	
	@Column(name="time_ends")
	private Long timeEnds;
	
	private Float price;
	
	@Column(name="offer_value")
	private Float offerValue;
	
	@Column(name="extra_details", columnDefinition="TEXT")
	private String extraDetails;
	
	private Integer quantity;
	private String status;
	
	@Column(name="image_attachment_key")
	private String imageAttachmentKey;
	
	@Column(name="category_attachment_key")
	private String categoryAttachmentKey;
	

	//relationships
	@ManyToOne
	@JoinColumn(name = "award_id")
	private Award award;
	
	@ManyToOne
	@JoinColumn(name = "merchant_id")
	private Merchant merchant;
	
	//cant get this mapping working..help
	/*@OneToOne(cascade = CascadeType.ALL)
	private OfferEconomics offerEconomics;*/
			
	public Offer() {
		super();
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
	

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public Award getAward() {
		return award;
	}
	public void setAward(Award award) {
		this.award = award;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getExtraDetails() {
		return extraDetails;
	}
	public void setExtraDetails(String extraDetails) {
		this.extraDetails = extraDetails;
	}

	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/*public OfferEconomics getOfferEconomics() {
		return offerEconomics;
	}

	public void setOfferEconomics(OfferEconomics offerEconomics) {
		this.offerEconomics = offerEconomics;
	}*/

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getTimeStarts() {
		return timeStarts;
	}

	public void setTimeStarts(Long timeStarts) {
		this.timeStarts = timeStarts;
	}

	public Long getTimeEnds() {
		return timeEnds;
	}

	public void setTimeEnds(Long timeEnds) {
		this.timeEnds = timeEnds;
	}

	public Float getOfferValue() {
		return offerValue;
	}

	public void setOfferValue(Float offerValue) {
		this.offerValue = offerValue;
	}

	public String getImageAttachmentKey() {
		return imageAttachmentKey;
	}

	public void setImageAttachmentKey(String imageAttachmentKey) {
		this.imageAttachmentKey = imageAttachmentKey;
	}

	public String getCategoryAttachmentKey() {
		return categoryAttachmentKey;
	}

	public void setCategoryAttachmentKey(String categoryAttachmentKey) {
		this.categoryAttachmentKey = categoryAttachmentKey;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	
	
}
