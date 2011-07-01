package com.sightlyinc.ratecred.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sightlyinc.ratecred.client.geo.GeoPersistable;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.interceptor.PersistenceObservable;

@PersistenceObservable
@Entity
@Table(name="merchant")
public class Merchant extends BaseEntity implements GeoPersistable {
	
	@Transient
	private String voucherVerificationPhone;

	@ManyToOne
	@JoinColumn(name = "network_member_id")
	private NetworkMember networkMember;

	@OneToMany(mappedBy = "merchant")
	private Set<OfferEconomics> offerEconomics;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description", columnDefinition="TEXT")
	private String description;
	
	@Column(name="status")
	private String status;

	@Column(name="url")
	private String url;
	
	@Column(name="facebook_url")
	private String facebookUrl;
	
	@Column(name="image_attachment_key")
	private String imageAttachmentKey;
	
	@Column(name="category_attachment_key")
	private String categoryAttachmentKey;

	@ManyToOne
	@JoinColumn(name = "place_id")
	private Place place;

	public Merchant() {
		super();
		offerEconomics = new HashSet<OfferEconomics>();
	}

	@Override
	public String getGeoRecordId() throws GeoPersistenceException {
    	if(getId() != null)
    		return getId().toString();
    	else 
    		throw new GeoPersistenceException("geo record id cannot be null");
	}

	@Override
	public String getMemberKey() throws GeoPersistenceException  {
		if(networkMember != null)
			return this.networkMember.getMemberKey();
		else
			throw new GeoPersistenceException("geo member key cannot be null");
	}
	
	@Override
	public Place getGeoPlace() throws GeoPersistenceException {
		if(place != null)
			return place;
		else
			throw new GeoPersistenceException("geo place cannot be null");
	}	
	
	
	public String getVoucherVerificationPhone() {
		return voucherVerificationPhone;
	}

	public void setVoucherVerificationPhone(String voucherVerificationPhone) {
		this.voucherVerificationPhone = voucherVerificationPhone;
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

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	public String getImageAttachmentKey() {
		return imageAttachmentKey;
	}

	public void setImageAttachmentKey(String imageAttachmentKey) {
		this.imageAttachmentKey = imageAttachmentKey;
	}

	public String getCategoryAttachmentKey() {
		return categoryAttachmentKey;
	}

	public void setCategoryAttachmentKey(String categoryAttachmentKey) {
		this.categoryAttachmentKey = categoryAttachmentKey;
	}


}
