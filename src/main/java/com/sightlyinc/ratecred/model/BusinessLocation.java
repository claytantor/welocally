package com.sightlyinc.ratecred.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 *     `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `place_id` BIGINT(20) NOT NULL ,
  `business_id` BIGINT(20) NULL DEFAULT NULL ,  
  `phone` VARCHAR(16) NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `flag` VARCHAR(16) NULL DEFAULT 'ACTIVE' ,
  `descr` TEXT NULL DEFAULT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,  

 * @author claygraham
 *
 */
@Entity
@Table(name="business_location")
public class BusinessLocation extends BaseEntity {
	
	private String phone;
	private String name;
	private String description;
	private String url;
	private String flag;

	@ManyToOne
	@JoinColumn(name = "business_id")
	private Business business;
	
	@ManyToOne
	@JoinColumn(name = "place_id")
	private Place place;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public Place getPlace() {
		return place;
	}
	
	public void setPlace(Place place) {
		this.place = place;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void initializePlace(Place place) {
		this.place = place;
		this.name = place.getName();
		this.phone = place.getPhone();
		
	}
	
	
	
	
	

}
