package com.sightlyinc.ratecred.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * 
 * 
 * 
 * 
 * @author claygraham
 * 
 */
@Entity
@Table(name = "affiliate")
public class Affiliate extends BaseEntity {

	@ManyToMany
	@JoinTable(name = "affiliate_has_business", joinColumns = @JoinColumn(name = "affiliate_id"), inverseJoinColumns = @JoinColumn(name = "business_id"))
	private Set<Business> businesses;

	@ManyToOne
	@JoinColumn(name = "network_member_id")
	private NetworkMember networkMember;

	@Column(name = "icon_url")
	private String iconUrl;

	@Column(name = "map_icon_url")
	private String mapIconUrl;

	@OneToMany
	@JoinColumn(name = "affiliate_id")
	private Set<OfferEconomics> offerEconomics;

	public Set<Business> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(Set<Business> businesses) {
		this.businesses = businesses;
	}

	public NetworkMember getNetworkMember() {
		return networkMember;
	}

	public void setNetworkMember(NetworkMember networkMember) {
		this.networkMember = networkMember;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getMapIconUrl() {
		return mapIconUrl;
	}

	public void setMapIconUrl(String mapIconUrl) {
		this.mapIconUrl = mapIconUrl;
	}

	public Set<OfferEconomics> getOfferEconomics() {
		return offerEconomics;
	}

	public void setOfferEconomics(Set<OfferEconomics> offerEconomics) {
		this.offerEconomics = offerEconomics;
	}
	
	
}
