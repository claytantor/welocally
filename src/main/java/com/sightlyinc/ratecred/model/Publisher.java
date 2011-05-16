package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonProperty;

public class Publisher {
	
	private Long id;
		
	
	private String token;
	private String url;
	private String siteName;
	private String description;
	private Integer monthlyPageviews;
	
	
	@ManyToOne
	@JoinColumn(name = "network_member_id")
	private NetworkMember networkMember;
	 
	@OneToMany(mappedBy = "affiliate")
	private Set<OfferEconomics> offerEconomics;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMonthlyPageviews() {
		return monthlyPageviews;
	}
	public void setMonthlyPageviews(Integer monthlyPageviews) {
		this.monthlyPageviews = monthlyPageviews;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public NetworkMember getNetworkMember() {
		return networkMember;
	}
	public void setNetworkMember(NetworkMember networkMember) {
		this.networkMember = networkMember;
	}
	public Set<OfferEconomics> getOfferEconomics() {
		return offerEconomics;
	}
	public void setOfferEconomics(Set<OfferEconomics> offerEconomics) {
		this.offerEconomics = offerEconomics;
	}
	
	

}
