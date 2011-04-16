package com.sightlyinc.ratecred.admin.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @RequestParam(value="address1",required=true) String address1,
			@RequestParam(value="url",required=true) String url,
			@RequestParam(value="teaser",required=true) String teaser,
			@RequestParam(value="title",required=true) String title,
			@RequestParam(value="keywords",required=false) String keywords, 
			@RequestParam(value="view",required=false) String view, 
			@RequestParam(value="css",required=false) String css,
			@RequestParam(value="reffererId",required=false) String reffererId,
 * 
 * @author claygraham
 *
 */
public class Article {
	
	@JsonProperty
	private String address1;
	
	@JsonProperty
	private String url;
	
	@JsonProperty
	private String teaser;
	
	@JsonProperty
	private String title;
	
	@JsonProperty
	private String keywords;
	
	@JsonProperty
	private String referrerId;
	
	@JsonProperty
	private Site site;
		
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTeaser() {
		return teaser;
	}
	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getReferrerId() {
		return referrerId;
	}
	public void setReferrerId(String referrerId) {
		this.referrerId = referrerId;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	
	

}
