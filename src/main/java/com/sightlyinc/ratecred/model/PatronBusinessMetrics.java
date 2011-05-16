package com.sightlyinc.ratecred.model;

import java.util.Date;

/**
 * NOT PERSISTED, will need to determine if this is obsolete 
 * @author claygraham
 *
 */
public class PatronBusinessMetrics {
	
	private Long id;
	private Long raterId;
	private String raterUserName;
	private Long raterImageValueId;
	private String raterImageFilename;	
	private Integer yays;
	private Integer boos;
	private Integer ratings;
	private Integer score;
	private Float ratingAverage;
	private Integer awardsActive;
	private Integer awardsUsed;
	private Integer awardsExpired;
	
	
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
	
	public String getRaterImageFilename() {
		return raterImageFilename;
	}
	public void setRaterImageFilename(String raterImageFilename) {
		this.raterImageFilename = raterImageFilename;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getRaterUserName() {
		return raterUserName;
	}
	public void setRaterUserName(String raterUserName) {
		this.raterUserName = raterUserName;
	}
	public Long getRaterId() {
		return raterId;
	}
	public void setRaterId(Long raterId) {
		this.raterId = raterId;
	}
	public Long getRaterImageValueId() {
		return raterImageValueId;
	}
	public void setRaterImageValueId(Long raterImageValueId) {
		this.raterImageValueId = raterImageValueId;
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
	
	public Integer getAwardsActive() {
		return awardsActive;
	}
	public void setAwardsActive(Integer awardsActive) {
		this.awardsActive = awardsActive;
	}
	public Integer getAwardsUsed() {
		return awardsUsed;
	}
	public void setAwardsUsed(Integer awardsUsed) {
		this.awardsUsed = awardsUsed;
	}
	public Integer getAwardsExpired() {
		return awardsExpired;
	}
	public void setAwardsExpired(Integer awardsExpired) {
		this.awardsExpired = awardsExpired;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
		/*RaterBusinessMetrics clone = new RaterBusinessMetrics();
		clone.setBoos(this.boos);
		clone.setBusinessId(this.businessId);
		clone.setBusinessLocationId(this.businessLocationId);
		clone.setEndTime(this.endTime);
		clone.setEndTimeMills(this.endTimeMills);
		clone.setId(this.id);
		clone.setRatingAverage(this.ratingAverage);
		clone.setStartTime(this.startTime);
		clone.setStartTimeMills(this.startTimeMills);
		clone.setRates(this.ratings);
		clone.setYays(this.yays);
		return clone;*/
	}

	
	
	

}
