package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.Set;

public class Place {
	
	private Long id;
	private Integer version = new Integer(0);
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
	private String categoryType;
	private String addressFull;
	
	
	private Date timeCreated;

	// relations
	private Set<Rating> ratings;
	private Set<PlaceAttribute> attributes;
	private Set<PlaceRating> placeRatings;
	
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
	
	public Set<PlaceRating> getPlaceRatings() {
		return placeRatings;
	}
	public void setPlaceRatings(Set<PlaceRating> ratings) {
		this.placeRatings = ratings;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
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
	public Date getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
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
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	
	public String getAddressFull() {
		return addressFull;
	}
	public void setAddressFull(String addressFull) {
		this.addressFull = addressFull;
	}
	@Override
	public String toString() {
		return "Place [address=" + address + ", attributes=" + attributes
				+ ", businessLocation=" + businessLocation
				+ ", businessServices=" + businessServices + ", category="
				+ category + ", categoryType=" + categoryType + ", city="
				+ city + ", description=" + description + ", email=" + email
				+ ", flag=" + flag + ", id=" + id + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", name=" + name + ", phone="
				+ phone + ", placeRatings=" + placeRatings + ", ratings="
				+ ratings + ", simpleGeoId=" + simpleGeoId + ", state=" + state
				+ ", subcategory=" + subcategory + ", timeCreated="
				+ timeCreated + ", twitterId=" + twitterId + ", type=" + type
				+ ", version=" + version + ", website=" + website + ", zip="
				+ zip + "]";
	}

	
	
	

}
