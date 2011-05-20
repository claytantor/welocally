package com.sightlyinc.ratecred.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="publisher")
public class Publisher extends BaseEntity {
	
	private String url;
	
	@Column(name="site_name")
	private String siteName;
	
	@Column(name="monthly_pageviews")
	private Integer monthlyPageviews;
	
	
	@ManyToOne	
	@JoinColumn(name = "network_member_id")
	private NetworkMember networkMember;
	 
	@OneToMany
	@JoinColumn(name = "publisher_id")
	private Set<OfferEconomics> offerEconomics;


	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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
