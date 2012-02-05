package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 	`name` varchar(255) unsigned DEFAULT NULL,
	`url` varchar(255) unsigned DEFAULT NULL,
	`description` TEXT NOT NULL,	
	`notes` TEXT DEFAULT NULL,
 * @author claygraham
 *
 */

@Entity
@Table(name="site")
public class Site extends BaseEntity {
	
	@Column(name="name")
	private String name;
	
	@Column(name="description",columnDefinition="TEXT")
	private String description;
	
	@Column(name="url")
	private String url;
	
	@Column(name="verified")
	private Boolean verified;
	
	@Column(name="is_active")
	private Boolean active;
	
	@Column(name="notes",columnDefinition="TEXT")
	private String notes;
	
	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;
		
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	

}
