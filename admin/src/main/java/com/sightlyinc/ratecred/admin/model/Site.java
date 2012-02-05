package com.sightlyinc.ratecred.admin.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 referrerId	siteName	siteUrl	description	iconUrl
 * 
 * @author claygraham
 *
 */
@Deprecated
public class Site {
	
	@JsonProperty
	private String referrerId;
	
	@JsonProperty
	private String siteName;
	
	@JsonProperty
	private String siteUrl;
	
	@JsonProperty
	private String description;
	
	@JsonProperty
	private String iconUrl;
	
	@JsonProperty
	private String keywords;

	public String getReferrerId() {
		return referrerId;
	}

	public void setReferrerId(String referrerId) {
		this.referrerId = referrerId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
		

}
