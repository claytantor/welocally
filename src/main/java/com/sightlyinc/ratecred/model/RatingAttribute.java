package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *   `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT,
  `name` VARCHAR(255) NOT NULL ,
  `attribute_value` TEXT NULL DEFAULT NULL ,
  `rating_id` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  
 * @author claygraham
 *
 */
@Entity
@Table(name="rating_attribute")
public class RatingAttribute extends BaseEntity {
	
	private String name;
	
	@Column(name="attribute_value", columnDefinition="TEXT")
	private String value;

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

	

}
