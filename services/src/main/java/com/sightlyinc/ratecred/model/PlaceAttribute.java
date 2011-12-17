package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 
 *   `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT,
  `name` VARCHAR(255) NOT NULL ,
  `attribute_value` LONGTEXT NULL DEFAULT NULL ,
  `place_id` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL , 
  
 * @author claygraham
 *
 */
@Entity
@Table(name="place_attribute")
public class PlaceAttribute  extends BaseEntity {
	
		
	
	private String name;
	
	@Column(name="attribute_value", columnDefinition="TEXT")
	private String value;

	@ManyToOne
	@JoinColumn(name = "place_id")
	private Place place;
	
	public PlaceAttribute() {
		super();
	}

	public PlaceAttribute(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
	

}
