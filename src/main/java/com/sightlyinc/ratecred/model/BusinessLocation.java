package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BusinessLocation {
	
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

	
	private Business business;
	private Place place;
	
	private Set<BusinessLocationImage> images = new HashSet<BusinessLocationImage>();
	
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
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
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
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public Place getPlace() {
		return place;
	}
	
	public void setPlace(Place place) {
		this.place = place;
	}
	
	public void initializePlace(Place place) {
		this.place = place;
		this.name = place.getName();
		this.address = place.getAddress();
		this.city = place.getCity();
		this.state = place.getState();
		this.latitude = place.getLatitude();
		this.longitude = place.getLongitude();
		this.phone = place.getPhone();
		this.website = place.getWebsite();
	}
	
	public Set<BusinessLocationImage> getImages() {
		return images;
	}
	public void setImages(Set<BusinessLocationImage> images) {
		this.images = images;
	}
	@Override
	public String toString() {
		return "BusinessLocation [address=" + address + ", business="
				+ business + ", city=" + city + ", description=" + description
				+ ", flag=" + flag + ", id=" + id + ", images=" + images
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", name=" + name + ", phone=" + phone + ", place=" + place
				+ ", state=" + state + ", timeCreated=" + timeCreated
				+ ", type=" + type + ", version=" + version + ", website="
				+ website + ", zip=" + zip + "]";
	}
	
	
	
	

}
