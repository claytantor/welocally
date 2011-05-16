package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 *   `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `yays` INT(11) NOT NULL ,
  `boos` INT(11) NOT NULL ,
  `ratings` INT(11) NOT NULL ,
  `rating_avg` FLOAT(11) NULL DEFAULT NULL ,
  `start_time` BIGINT(20) NULL DEFAULT NULL ,
  `end_time` BIGINT(20) NULL DEFAULT NULL ,
  `business_location_id` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */
@Entity
@Table(name="business_metrics")
public class BusinessMetrics extends BaseEntity {
	
	private Integer yays;
	private Integer boos;
	private Integer ratings;
	
	@Column(name="rating_avg")
	private Float ratingAverage;
	
	private Long startTime;
	private Long endTime;
	
	@ManyToOne
	@JoinColumn(name = "business_location_id")
	private BusinessLocation businessLocation;
	
	public Integer getYays() {
		return yays;
	}
	public void setYays(Integer yays) {
		this.yays = yays;
	}
	public Integer getBoos() {
		return boos;
	}
	public void setBoos(Integer boos) {
		this.boos = boos;
	}
	public Integer getRatings() {
		return ratings;
	}
	public void setRatings(Integer ratings) {
		this.ratings = ratings;
	}
	
	public Float getRatingAverage() {
		return ratingAverage;
	}
	public void setRatingAverage(Float ratingAverage) {
		this.ratingAverage = ratingAverage;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		BusinessMetrics clone = new BusinessMetrics();
		return clone;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public BusinessLocation getBusinessLocation() {
		return businessLocation;
	}
	public void setBusinessLocation(BusinessLocation businessLocation) {
		this.businessLocation = businessLocation;
	}

	
	
	

}
