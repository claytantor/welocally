package com.sightlyinc.ratecred.admin.model;


public class CustomAwardForm {

	private String username;
	private String type;
	private String note;
	
	private String giveAward;	
	private String externalId;
	private String externalSource;
	private String programId;
	private String programName;
	private String name;
	private String couponCode;
	private String description;
	private String url;
	private String beginDateString;
	private String expireDateString;
	
	/*
	 *			aoffer.setCouponCode(offer.getCouponCode());
				aoffer.setBeginDateMillis(offer.getBegin().getTime());
				aoffer.setDescription(offer.getDescription());
				aoffer.setExpireDateMillis(offer.getExpire().getTime());
				aoffer.setExternalId(offer.getExternalId().toString());
				aoffer.setExternalSource(offer.getExternalSource());
				aoffer.setName(offer.getName());
				aoffer.setProgramId(offer.getProgramId().toString());
				aoffer.setProgramName(offer.getProgramName());
				aoffer.setUrl(offer.getUrl()); 
	 * 
	 */


	public String getType() {
		return type;
	}

	public String getGiveAward() {
		return giveAward;
	}

	public void setGiveAward(String giveAward) {
		this.giveAward = giveAward;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
	

}
