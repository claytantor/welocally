package com.sightlyinc.ratecred.model;

import java.util.Date;

import com.noi.utility.hibernate.ImageValue;

public class BusinessLocationImage {
	private Long id;
	private Integer version;
	private Date timeCreated;
	private String name;
	
	private BusinessLocation owner;
	private ImageValue image;
	private String description;
	
	private Long imageValueId;
	
	public Long getImageValueId() {
		return imageValueId;
	}
	public void setImageValueId(Long imageValueId) {
		this.imageValueId = imageValueId;
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
	public Date getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	

	public BusinessLocation getOwner() {
		return owner;
	}
	public void setOwner(BusinessLocation owner) {
		this.owner = owner;
	}
	public ImageValue getImage() {
		return image;
	}
	public void setImage(ImageValue image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		

}
