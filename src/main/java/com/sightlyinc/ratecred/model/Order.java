package com.sightlyinc.ratecred.model;

import java.util.Date;



public class Order {
	

	protected Long id;
	protected Integer version = new Integer(0);
	
	private Long timeCreated;
	private Long timeUpdated;	
	
	//order id
	private String externalId;
	
	//this is the id that is used to get the voucher
	private String externalTxId;
	
	private String channel;
	private String buyerName;
	private String buyerEmail;	
	private String shippingName;
	private String addressFieldOne;
	private String addressFieldTwo;
	private String city;
	private String state;
	private String postalCode;
	private String countryCode;
	
	//amazon  order item id
	private String externalOrderItemCode;
	private String sku;
	private String title;
	private String description;
	private Float price;
	private String status;
	
	private Integer quantity;
	
	private Rater owner;
	private AwardOffer offer;
	private Voucher voucher;
	

	
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
	
	public Date getTimeCreatedDate() {
		return new Date(timeCreated);
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
	public Rater getOwner() {
		return owner;
	}
	public void setOwner(Rater owner) {
		this.owner = owner;
	}
	public AwardOffer getOffer() {
		return offer;
	}
	public void setOffer(AwardOffer offer) {
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
