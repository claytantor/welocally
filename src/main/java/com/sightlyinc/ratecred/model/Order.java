package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 *  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `offer_id` BIGINT(20) NOT NULL ,
  `voucher_id` BIGINT(20) NOT NULL ,
  `patron_id` BIGINT(20) NOT NULL ,
  `external_id` VARCHAR(255) NULL DEFAULT NULL ,
  `external_txid` VARCHAR(255) NULL DEFAULT NULL ,
  `channel` VARCHAR(45) NULL DEFAULT NULL ,
  `buyer_name` VARCHAR(128) NULL DEFAULT NULL ,
  `buyer_email` VARCHAR(255) NULL DEFAULT NULL ,
  `shipping_name` VARCHAR(128) NULL DEFAULT NULL ,
  `address_one` VARCHAR(128) NULL DEFAULT NULL ,
  `address_two` VARCHAR(128) NULL DEFAULT NULL ,
  `city` VARCHAR(128) NULL DEFAULT NULL ,
  `state` VARCHAR(45) NULL DEFAULT NULL ,
  `postal_code` VARCHAR(45) NULL DEFAULT NULL ,
  `country_code` VARCHAR(8) NULL DEFAULT NULL ,
  `external_orderitem` VARCHAR(255) NULL DEFAULT NULL ,
  `sku` VARCHAR(255) NULL DEFAULT NULL ,
  `title` VARCHAR(255) NULL DEFAULT NULL ,
  `description` LONGTEXT NULL DEFAULT NULL ,
  `price` FLOAT(11) NULL DEFAULT NULL ,
  `quantity` INT(11) NULL DEFAULT NULL ,
  `status` VARCHAR(45) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */

@Entity
@Table(name="cust_order")
public class Order extends BaseEntity {

	//order id
	private String externalId;
	
	//this is the id that is used to get the voucher
	private String externalTxId;
	
	private String channel;
	private String buyerName;
	private String buyerEmail;	
	private String shippingName;

	@Column(name="address_one")
	private String addressFieldOne;
	
	@Column(name="address_one")
	private String addressFieldTwo;
	
	private String city;
	private String state;
	private String postalCode;
	private String countryCode;
	
	//amazon  order item id
	@Column(name="external_id")
	private String externalOrderItemCode;
	
	private String sku;
	private String title;
	private String description;
	private Float price;
	private String status;
	
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name = "patron_id")
	private Patron owner;
	
	@ManyToOne
	@JoinColumn(name = "offer_id")
	private Offer offer;
	
	@ManyToOne
	@JoinColumn(name = "voucher_id")
	private Voucher voucher;


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	
	public String getExternalTxId() {
		return externalTxId;
	}


	public void setExternalTxId(String externalTxId) {
		this.externalTxId = externalTxId;
	}


	public String getBuyerName() {
		return buyerName;
	}


	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}


	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getAddressFieldOne() {
		return addressFieldOne;
	}
	public void setAddressFieldOne(String addressFieldOne) {
		this.addressFieldOne = addressFieldOne;
	}
	public String getAddressFieldTwo() {
		return addressFieldTwo;
	}
	public void setAddressFieldTwo(String addressFieldTwo) {
		this.addressFieldTwo = addressFieldTwo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getExternalOrderItemCode() {
		return externalOrderItemCode;
	}
	public void setExternalOrderItemCode(String externalOrderItemCode) {
		this.externalOrderItemCode = externalOrderItemCode;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Patron getOwner() {
		return owner;
	}
	public void setOwner(Patron owner) {
		this.owner = owner;
	}
	public Offer getOffer() {
		return offer;
	}
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	public Voucher getVoucher() {
		return voucher;
	}
	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}


	public String getBuyerEmail() {
		return buyerEmail;
	}


	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}
		

}
