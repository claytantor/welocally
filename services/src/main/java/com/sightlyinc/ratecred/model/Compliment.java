package com.sightlyinc.ratecred.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 *   `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `patron_id` BIGINT(20) NOT NULL ,
  `rating_id` BIGINT(20) NOT NULL ,
  `note` TEXT NULL DEFAULT NULL ,
  `time_created` BIGINT NULL DEFAULT NULL ,
  `time_updated` BIGINT NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */
@Entity
@Table(name="compliment")
public class Compliment extends BaseEntity{
	
	@Column(columnDefinition="TEXT")
	private String note;
	
	@ManyToOne
	@JoinColumn(name = "patron_id")
	private Patron owner;
	
	@ManyToOne
	@JoinColumn(name = "rating_id")
	private Rating towards;
		
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Patron getOwner() {
		return owner;
	}
	public void setOwner(Patron owner) {
		this.owner = owner;
	}
	public Rating getTowards() {
		return towards;
	}
	public void setTowards(Rating towards) {
		this.towards = towards;
	}
	
	
	

}
