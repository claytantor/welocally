package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AwardOffer {
	
	private Long id;
	private Integer version = new Integer(0);
	private Date timeCreated;
	
	private String externalId;
	private String externalSource;
	private String programId;
	private String programName;
	private String name;
	private String couponCode;
	private String description;
	private String url;
	private Long beginDateMillis;
	private Long expireDateMillis;
		
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
	private Set<AwardOfferItem> items;
	
	
	
		
	public AwardOffer() {
		super();
		items = new HashSet<AwardOfferItem>();
	}
	
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
	public Date getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
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
	public Long getBeginDateMillis() {
		return beginDateMillis;
	}
	public void setBeginDateMillis(Long beginDateMillis) {
		this.beginDateMillis = beginDateMillis;
	}
	public Long getExpireDateMillis() {
		return expireDateMillis;
	}
	public void setExpireDateMillis(Long expireDateMillis) {
		this.expireDateMillis = expireDateMillis;
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
		
	public Set<AwardOfferItem> getItems() {
		return items;
	}
	public void setItems(Set<AwardOfferItem> items) {
		this.items = items;
	}
	
	@Override
	public String toString() {
		return "AwardOffer [awardType=" + awardType + ", beginDateMillis="
				+ beginDateMillis + ", business=" + business + ", couponCode="
				+ couponCode + ", description=" + description
				+ ", expireDateMillis=" + expireDateMillis + ", externalId="
				+ externalId + ", externalSource=" + externalSource + ", id="
				+ id + ", name=" + name + ", programId=" + programId
				+ ", programName=" + programName + ", status=" + status
				+ ", timeCreated=" + timeCreated + ", url=" + url
				+ ", version=" + version + "]";
	}



	
	
}
