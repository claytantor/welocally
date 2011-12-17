package com.sightlyinc.ratecred.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * 
 *   `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `address1` VARCHAR(255) NULL DEFAULT NULL ,
  `city` VARCHAR(255) NULL DEFAULT NULL ,
  `state` VARCHAR(255) NULL DEFAULT NULL ,
  `zip` VARCHAR(45) NULL DEFAULT NULL ,
  `twitter_id` VARCHAR(255) NULL DEFAULT NULL ,
  `sg_id` VARCHAR(255) NULL DEFAULT NULL ,
  `phone` VARCHAR(45) NULL DEFAULT NULL ,
  `latitude` DOUBLE NULL DEFAULT NULL ,
  `longitude` DOUBLE NULL DEFAULT NULL ,
  `descr` TEXT NULL DEFAULT NULL ,
  `type` VARCHAR(255) NULL DEFAULT NULL ,
  `flag` VARCHAR(16) NULL DEFAULT 'ACTIVE' ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `email` VARCHAR(255) NULL DEFAULT NULL ,
  `business_services` VARCHAR(10) NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */
@Entity
@Table(name="place")
public class Place extends BaseEntity {
	
	private String type;
	
	@Column(name="address1")
	private String address;
	private String city;
	private String state;
	
	@Column(name="zip")
	private String postalCode;
	
	@Column(name="twitter_id")
	private String twitterId;
	@Column(name="simple_geo_id")
	private String simpleGeoId;
	private String email;
	//@Column(name="business_services")
	//private String businessServices;	
	private String phone;
	private String name;
	
	private Double latitude;
	
	private Double longitude;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	private String website;
	
	private String flag;

	@JsonProperty
	@Column(name="image_attachment_key")
	protected String imageAttachmentKey;
	
	@JsonProperty
	@Column(name="category_attachment_key")
	protected String categoryAttachmentKey;
	
	@Transient
	private String addressFull;
	
	@Column(name="website_type")
	private String websiteType;
	
	// relations
	@OneToMany
	@JoinColumn(name="place_id")
	private Set<Rating> ratings;

	@OneToMany
	@JoinColumn(name="place_id")
	private Set<PlaceAttribute> attributes;
	
	@ManyToOne
	@JoinColumn(name = "business_location_id")
	private BusinessLocation businessLocation;
	
	@Transient
	private List<String> categories = new ArrayList<String>();
			
	
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public String getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	
	
	public String getSimpleGeoId() {
		return simpleGeoId;
	}
	public void setSimpleGeoId(String simpleGeoId) {
		this.simpleGeoId = simpleGeoId;
	}
	
	/*public Set<PlaceRating> getPlaceRatings() {
		return placeRatings;
	}
	public void setPlaceRatings(Set<PlaceRating> ratings) {
		this.placeRatings = ratings;
	}*/

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String zip) {
		this.postalCode = zip;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}

    @JsonIgnore
	public Set<Rating> getRatings() {
		return ratings;
	}
    @JsonIgnore
	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}

    // serialization is happening outside of a transaction so there is no session to lazily load these
    // if we went them in the JSON we'll have to move the serialization into a service method - sam
    @JsonIgnore
	public Set<PlaceAttribute> getAttributes() {
		return attributes;
	}
	
		
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String url) {
		this.website = url;
	}
	public BusinessLocation getBusinessLocation() {
		return businessLocation;
	}
	public void setBusinessLocation(BusinessLocation businessLocation) {
		this.businessLocation = businessLocation;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getAddressFull() {
		return addressFull;
	}
	public void setAddressFull(String addressFull) {
		this.addressFull = addressFull;
	}
	public String getCategoryAttachmentKey() {
		return categoryAttachmentKey;
	}
	public void setCategoryAttachmentKey(String categoryAttachmentKey) {
		this.categoryAttachmentKey = categoryAttachmentKey;
	}
	public String getImageAttachmentKey() {
		return imageAttachmentKey;
	}
	public void setImageAttachmentKey(String imageAttachmentKey) {
		this.imageAttachmentKey = imageAttachmentKey;
	}
	

	
	
	

}
