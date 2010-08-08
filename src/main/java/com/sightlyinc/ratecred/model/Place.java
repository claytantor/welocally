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
	private String phone;
	private String name;
	private Double latitude;
	private Double longitude;
	private String description;
	private String website;
	private String flag;
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
	
	
	/**
	 * 
	 * 	
	private Long id;
	private Integer version = new Integer(0);
	private String type;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String name;
	private Double latitude;
	private Double longitude;
	private String description;
	private String website;
	private String flag;
	private Date timeCreated;
	 * 
	 */
	
	@Override
	public String toString() {
		return super.toString();
		/*StringBuffer buf = new StringBuffer();
		
		buf.append(id.toString())
		.append(type)
		.append(address)
		.append(city)
		.append(state)
		.append(zip)
		.append(phone)
		.append(name)
		.append(latitude.toString())
		.append(longitude.toString())
		.append(description)
		.append(website)
		.append(flag)
		;
		
		return buf.toString();*/
	}
	
	

}
