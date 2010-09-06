package com.sightlyinc.ratecred.model;

import java.util.Date;


/**
 * 
 * 
 * @author claygraham
 *
 */
public class Compliment {
	private Long id;
	private Integer version;
	private Date timeCreated;
	private String note;
	private Long timeCreatedMills;
	private String timeCreatedGmt;
	private Rater owner;
	private Rating towards;
	
	public Long getTimeCreatedMills() {
		return timeCreatedMills;
	}
	
	public void setTimeCreatedMills(Long timeCreatedMills) {
		this.timeCreatedMills = timeCreatedMills;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTimeCreatedGmt() {
		return timeCreatedGmt;
	}
	public void setTimeCreatedGmt(String timeCreatedGmt) {
		this.timeCreatedGmt = timeCreatedGmt;
	}
	
	
	public Date getTimeCreated() {
		return timeCreated;
	}
	
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
		/*if(timeCreated != null)
			this.timeCreatedMills = timeCreated.getTime();*/
	}
	
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Rater getOwner() {
		return owner;
	}
	public void setOwner(Rater owner) {
		this.owner = owner;
	}
	public Rating getTowards() {
		return towards;
	}
	public void setTowards(Rating towards) {
		this.towards = towards;
	}
	
	
	

}
