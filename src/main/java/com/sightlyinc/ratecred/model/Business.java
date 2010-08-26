package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Business {
	
	private Long id;
	private Integer version;
	private Date timeCreated;
	

	private String name;
	private String userName;
	private String guid;
	

	private String description;
	
	private String status;
	
	private String website;
	
	/**
	 * 
	 * @element-type BusinessImage
	 */
	private Set<BusinessImage> images = new HashSet<BusinessImage>();
	
	private Set<BusinessLocation> locations = new HashSet<BusinessLocation>();
	
	
	/**
	 * 
	 * @element-type Business Attribute
	 */
	private Set<BusinessAttribute> attributes = new HashSet<BusinessAttribute>();

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
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

	public Long getId() {
		return id;
	}

	public Set<BusinessImage> getImages() {
		return images;
	}

	public void setImages(Set<BusinessImage> images) {
		this.images = images;
	}

	public Set<BusinessLocation> getLocations() {
		return locations;
	}

	public void setLocations(Set<BusinessLocation> locations) {
		this.locations = locations;
	}

	public Set<BusinessAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<BusinessAttribute> attributes) {
		this.attributes = attributes;
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

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}	
	
	
	
	
}