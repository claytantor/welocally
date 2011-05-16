package com.sightlyinc.ratecred.model;

import java.util.Set;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class Merchant extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "network_member_id")
	private NetworkMember networkMember;

	@OneToMany(mappedBy = "merchant")
	private Set<OfferEconomics> offerEconomics;

	@ManyToOne
	@JoinColumn(name = "business_id")
	private Business business;

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

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

}
