package com.sightlyinc.ratecred.model;

import java.util.Date;

import com.noi.utility.reflection.ReflectionUtils;

public class BusinessMetrics {
	
	private Long id;
	private Integer version = new Integer(0);
	private Integer yays;
	private Integer boos;
	private Integer ratings;
	private Float ratingAverage;
	
	private Date startTime;
	private Long startTimeMills;
	private Date endTime;
	private Long endTimeMills;

	private Long businessId;
	private Long businessLocationId;
	

	
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
	public Long getStartTimeMills() {
		return startTimeMills;
	}
	public void setStartTimeMills(Long startTimeMills) {
		this.startTimeMills = startTimeMills;
	}
	public Long getEndTimeMills() {
		return endTimeMills;
	}
	public void setEndTimeMills(Long endTimeMills) {
		this.endTimeMills = endTimeMills;
	}
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public Long getBusinessLocationId() {
		return businessLocationId;
	}
	public void setBusinessLocationId(Long businessLocationId) {
		this.businessLocationId = businessLocationId;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		BusinessMetrics clone = new BusinessMetrics();
		clone.setBoos(this.boos);
		clone.setBusinessId(this.businessId);
		clone.setBusinessLocationId(this.businessLocationId);
		clone.setEndTime(this.endTime);
		clone.setEndTimeMills(this.endTimeMills);
		clone.setId(this.id);
		clone.setRatingAverage(this.ratingAverage);
		clone.setStartTime(this.startTime);
		clone.setStartTimeMills(this.startTimeMills);
		clone.setRatings(this.ratings);
		clone.setVersion(this.version);
		clone.setYays(this.yays);
		return clone;
	}

	
	
	

}
