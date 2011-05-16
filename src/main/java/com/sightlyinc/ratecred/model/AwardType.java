package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="award_type")
public class AwardType extends BaseEntity{

	@Column(name = "points_value")
	private Integer value;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "class_type")
	private String type;
	
	@Column(name = "keyname")
	private String keyname;
	
	@Column(name = "previous")
	private String previous;
	
	@Column(name = "next")
	private String next;
	
	@Column(name = "category_attachment_key")
	private String categoryAttachmentKey;
	

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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKeyname() {
		return keyname;
	}
	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getPrevious() {
		return previous;
	}
	public void setPrevious(String previous) {
		this.previous = previous;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getCategoryAttachmentKey() {
		return categoryAttachmentKey;
	}
	public void setCategoryAttachmentKey(String categoryAttachmentKey) {
		this.categoryAttachmentKey = categoryAttachmentKey;
	}
	
	
	
}
