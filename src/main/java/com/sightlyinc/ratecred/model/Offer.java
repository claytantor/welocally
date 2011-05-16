package com.sightlyinc.ratecred.model;

import java.util.HashSet;
import java.util.Set;

public class Offer extends BaseEntity {
		
	private String externalId;
	private String externalSource;
	private String programId;
	private String programName;
	private String name;
	private String couponCode;
	private String description;
	private String finePrint;
	
	private String url;
	private Long beginTime;
	private Long endTime;
	
	private String discountType;
	private String type;
	private Float price;
	private Float value;
	private String extraDetails;
	private String illustrationUrl;
	private Integer quantity;
	private String status;

	//relationships
	private Award award;
	
	private AwardType awardType;
	
	private Business business;
	
	private Set<OfferItem> items;
	
	private OfferEconomics offerEconomics;
	
	
	
		
	public Offer() {
		super();
		items = new HashSet<OfferItem>();
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
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public AwardType getAwardType() {
		return awardType;
	}
	public void setAwardType(AwardType awardType) {
		this.awardType = awardType;
	}

	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getExternalSource() {
		return externalSource;
	}
	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}
	
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
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
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	public String getExtraDetails() {
		return extraDetails;
	}
	public void setExtraDetails(String extraDetails) {
		this.extraDetails = extraDetails;
	}
	public String getIllustrationUrl() {
		return illustrationUrl;
	}
	public void setIllustrationUrl(String illustrationUrl) {
		this.illustrationUrl = illustrationUrl;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
		
	public Set<OfferItem> getItems() {
		return items;
	}
	public void setItems(Set<OfferItem> items) {
		this.items = items;
	}
		
	public String getFinePrint() {
		return finePrint;
	}

	public void setFinePrint(String finePrint) {
		this.finePrint = finePrint;
	}



	public Long getBeginTime() {
		return beginTime;
	}



	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}



	public Long getEndTime() {
		return endTime;
	}



	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}



	public OfferEconomics getOfferEconomics() {
		return offerEconomics;
	}



	public void setOfferEconomics(OfferEconomics offerEconomics) {
		this.offerEconomics = offerEconomics;
	}
		


	
	
}
