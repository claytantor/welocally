package com.sightlyinc.ratecred.client.offers;

import java.util.Date;

import com.noi.utility.date.DateUtils;

public class Offer {
	private Long id;
	private Long externalId;
	private String externalSource;
	private Long programId;
	private String programName;
	private String name;
	private String couponCode;
	private String description;
	private String url;
	private String beginDateString;
	private String expireDateString;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public Long getExternalId() {
		return externalId;
	}
	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}
	public String getExternalSource() {
		return externalSource;
	}
	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}
	public Long getProgramId() {
		return programId;
	}
	public void setProgramId(Long programId) {
		this.programId = programId;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBeginDateString() {
		return beginDateString;
	}
	public void setBeginDateString(String beginDateString) {
		this.beginDateString = beginDateString;
	}
	public String getExpireDateString() {
		return expireDateString;
	}
	public void setExpireDateString(String expireDateString) {
		this.expireDateString = expireDateString;
	}
	
	public Date getExpire()
	{
		return DateUtils.stringToDate(expireDateString, DateUtils.DESC_SIMPLE_FORMAT);
	}
	
	public Date getBegin()
	{
		return DateUtils.stringToDate(beginDateString, DateUtils.DESC_SIMPLE_FORMAT);
	}
	
	
	@Override
	public String toString() {
		return "Offer [beginDateString=" + beginDateString + ", couponCode="
				+ couponCode + ", description=" + description
				+ ", expireDateString=" + expireDateString + ", id=" + id
				+ ", name=" + name + ", programId=" + programId
				+ ", programName=" + programName + ", url=" + url + "]";
	}
	
	

}
