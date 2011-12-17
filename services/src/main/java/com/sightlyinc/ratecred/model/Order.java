package com.sightlyinc.ratecred.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
  
  
  [item_number=]
[shipping_method=Default]
[residence_country=US]
[shipping_discount=0.00]
[verify_sign=A6wG0UoVz6n9iNjyem4bin4H2gBeAmBMmTLkfjMgyLYGWgS36OLo8uI2]
[insurance_amount=0.00]
[address_country=United States]
[address_city=San Jose]
[address_status=confirmed]
[business=clay_1314558577_biz@ratecred.com]
[payment_status=Completed]
[transaction_subject=welocallywpdev]
[protection_eligibility=Eligible]
[shipping=0.00]
[payer_id=D6V5WV763Y5YJ]
[first_name=Test]
[payer_email=clay_1314675261_per@ratecred.com]
[btn_id=2352782]
[txn_id=1WR48273AG091110C]
[quantity=1]
[receiver_email=clay_1314558577_biz@ratecred.com]
[notify_version=3.4]
[txn_type=web_accept]
[payer_status=verified]
[mc_gross=59.99]
[test_ipn=1]
[mc_currency=USD]
[custom=welocallywpdev]
[payment_date=16:25:36 Dec 01, 2011 PST]
[charset=windows-1252]
[address_country_code=US]
[payment_gross=59.99]
[address_zip=95131]
[ipn_track_id=vwu4FpXRF2LPSm-SpVJAPw]
[address_state=CA]
[discount=0.00]
[tax=0.00]
[handling_amount=0.00]
[item_name=Welocally Places 1YR Prepay]
[address_name=Test User]
[last_name=User]
[payment_type=instant]
[receiver_id=KEPU9HHFUN97N]
[address_street=1 Main St]

  
  
 * 
 * @author claygraham
 *
 */

@Entity
@Table(name="cust_order")
public class Order extends BaseEntity {

	
	//this is the id that is used to get the voucher
	@Column(name="external_txid")
	private String externalTxId;
	
	private String channel;
	
	@Column(name="buyer_key")
	private String buyerKey;
	
	@Column(name="buyer_email")
	private String buyerEmail;
	
	@Column(name="shipping_name")
	private String shippingName;

	@Column(name="address_one")
	private String addressFieldOne;
	
	@Column(name="address_two")
	private String addressFieldTwo;
	
	@Column(name="external_payer_id")
	private String externalPayerId;
	
	private String city;
	
	private String state;
	
	@Column(name="postal_code")
	private String postalCode;
	
	@Column(name="country_code")
	private String countryCode;
	
	//amazon  order item id
	@Column(name="external_id")
	private String externalOrderItemCode;
		
	private String sku;
	private String title;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	private Float price;
	private String status;
	
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher owner;
	
	@ManyToOne
	@JoinColumn(name = "offer_id")
	private Offer offer;
	
	@OneToMany
	@JoinColumn(name = "cust_order_id")
	private Set<Voucher> voucher;


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
	
	public String getExternalTxId() {
		return externalTxId;
	}


	public void setExternalTxId(String externalTxId) {
		this.externalTxId = externalTxId;
	}


	
	public String getBuyerKey() {
		return buyerKey;
	}
	public void setBuyerKey(String buyerKey) {
		this.buyerKey = buyerKey;
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

	public Publisher getOwner() {
		return owner;
	}
	public void setOwner(Publisher owner) {
		this.owner = owner;
	}
	
	public Offer getOffer() {
		return offer;
	}
	public void setOffer(Offer offer) {
		this.offer = offer;
	}


	public String getExternalPayerId() {
		return externalPayerId;
	}
	public void setExternalPayerId(String externalPayerId) {
		this.externalPayerId = externalPayerId;
	}
	public Set<Voucher> getVoucher() {
		return voucher;
	}
	public void setVoucher(Set<Voucher> voucher) {
		this.voucher = voucher;
	}
	public String getBuyerEmail() {
		return buyerEmail;
	}


	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}
		

}
