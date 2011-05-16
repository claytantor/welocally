package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


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
	private String address;
	private String city;
	private String state;
	private String zip;
	private String twitterId;
	private String simpleGeoId;
	private String email;
	private String businessServices;	
	private String phone;
	private String name;
	private Double latitude;
	private Double longitude;
	private String description;
	private String website;
	private String flag;
	private String category;
	private String subcategory;
	private String categoryAttachmentKey;
	private String imageAttachmentKey;
	
	private String addressFull;
	
	// relations
	@OneToMany(mappedBy = "place")
	private Set<Rating> ratings;
	
	@OneToMany(mappedBy = "place")
	private Set<PlaceAttribute> attributes;
	
	//@OneToMany(mappedBy = "place")
	//private Set<PlaceRating> placeRatings;
	
	@ManyToOne
	@JoinColumn(name = "business_location_id")
	private BusinessLocation businessLocation;
		
	
	
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
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
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

	public Set<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	public Set<PlaceAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(Set<PlaceAttribute> attributes) {
		this.attributes = attributes;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
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
	public String getBusinessServices() {
		return businessServices;
	}
	public void setBusinessServices(String businessServices) {
		this.businessServices = businessServices;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
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
