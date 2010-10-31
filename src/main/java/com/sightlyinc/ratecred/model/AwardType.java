package com.sightlyinc.ratecred.model;

import java.util.Date;

public class AwardType {

	private Long id;
	private Integer version = new Integer(0);
	private Date timeCreated;
	private Integer value;
	private String name;
	private String description;
	private String type;
	private String keyname;
	private String previous;
	private String next;
	private String category;
	
	
	
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
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "AwardType [description=" + description + ", id=" + id
				+ ", keyname=" + keyname + ", name=" + name + ", next=" + next
				+ ", previous=" + previous + ", timeCreated=" + timeCreated
				+ ", type=" + type + ", value=" + value + ", version="
				+ version + "]";
	}
	
	
}
