package com.sightlyinc.ratecred.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 *   `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `status` VARCHAR(15) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
  
 * @author claygraham
 *
 */
@Entity
@Table(name="business")
public class Business extends BaseEntity {
	
	@Column(name="name")
	private String name;
	
	@Column(name="description", columnDefinition="TEXT")
	private String description;
	
	@Column(name="status")
	private String status;

	@Column(name="url")
	private String url;
	
	@Column(name="image_attachment_key")
	private String imageAttachmentKey;
	
	@Column(name="category_attachment_key")
	private String categoryAttachmentKey;
	
	@OneToMany
	@JoinColumn(name="business_id")
	private Set<BusinessLocation> locations;
	
	@OneToMany
	@JoinColumn(name="business_id")
	private Set<BusinessAttribute> attributes;

	public Business() {
		super();
		locations = new HashSet<BusinessLocation>();
		attributes = new HashSet<BusinessAttribute>(); 
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