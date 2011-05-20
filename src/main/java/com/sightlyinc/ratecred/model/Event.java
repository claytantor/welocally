package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * <event>
   <name>Sun Prairie</name>
   <url>http://www.eastbayexpress.com/ebx/sun-prairie/Event?oid=2627434</url>
   <categories>Rock &amp; Pop</categories>
   <when>Thu., May 12, 9 p.m.</when>
   <phone>510-444-6174</phone>
   <locationName>Stork Club</locationName>
   <description>with Woods and One $5</description>
   <address>2330 Telegraph Ave., Oakland CA</address>
</event>



  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NULL ,
  `publisher_id` BIGINT(20) NOT NULL ,
  `place_id` BIGINT(20) NOT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,
  `cost` FLOAT(11) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `category_attachment_key` VARCHAR(45) NULL DEFAULT NULL ,
  `image_attachment_key` VARCHAR(45) NULL DEFAULT NULL ,
  `time_starts` BIGINT(20) NULL DEFAULT NULL ,
  `time_ends` BIGINT(20) NULL DEFAULT NULL ,
  `alarm_data` VARCHAR(1024) NULL DEFAULT NULL ,
  `alarm_time` BIGINT(20) NULL DEFAULT NULL ,
  `recurrance_type` ENUM('DAILY','WEEKLY','MONTHLY','YEARLY','HOURLY','MINUTELY') NULL DEFAULT NULL ,
  `recurrance_interval` INT(11) NULL DEFAULT NULL ,
  `recurrance_data` VARCHAR(1024) NULL DEFAULT NULL ,
  `recurrance_end` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,


 * @author claygraham
 *
 */
@Entity
@Table(name="event")
public class Event extends BaseEntity {
	
	private enum RecurranceType {DAILY,WEEKLY,MONTHLY,YEARLY,HOURLY,MINUTELY}
	
	private String name;
	
	private String url;
	
	private Float cost;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	private String whenText;
	
	@Column(name="category_attachment_key")
	private String categoryAttachmentKey;
	
	@Column(name="image_attachment_key")
	private String imageAttachmentKey;

	@Column(name="time_starts")
	private Long timeStarts;
	
	@Column(name="time_ends")
	private Long timeEnds;
	
//	@Column(name="recurrance_type")
//	@Enumerated(EnumType.STRING)
//	private RecurranceType recurranceType;
	
	@Column(name="recurrance_interval")
	private Integer recurranceInterval;
	
	@Column(name="recurrance_data")
	private String recurranceData;
	
	@Column(name="recurrance_end")
	private Long recurranceEnd;
	
	private String phone;
	
	@ManyToOne
	@JoinColumn(name = "place_id")
	private Place place;
	
	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
	}
	public Float getCost() {
		return cost;
	}
	public void setCost(Float cost) {
		this.cost = cost;
	}
	public String getCategoryAttachmentKey() {
		return categoryAttachmentKey;
	}
	public void setCategoryAttachmentKey(String categoryAttachmentKey) {
		this.categoryAttachmentKey = categoryAttachmentKey;
	}
	public String getImageAttachmentKey() {
		return imageAttachmentKey;
	}
	public void setImageAttachmentKey(String imageAttachmentKey) {
		this.imageAttachmentKey = imageAttachmentKey;
	}
	public Long getTimeStarts() {
		return timeStarts;
	}
	public void setTimeStarts(Long timeStarts) {
		this.timeStarts = timeStarts;
	}
	public Long getTimeEnds() {
		return timeEnds;
	}
	public void setTimeEnds(Long timeEnds) {
		this.timeEnds = timeEnds;
	}
//	public RecurranceType getRecurranceType() {
//		return recurranceType;
//	}
//	public void setRecurranceType(RecurranceType recurranceType) {
//		this.recurranceType = recurranceType;
//	}
	public Integer getRecurranceInterval() {
		return recurranceInterval;
	}
	public void setRecurranceInterval(Integer recurranceInterval) {
		this.recurranceInterval = recurranceInterval;
	}
	public String getRecurranceData() {
		return recurranceData;
	}
	public void setRecurranceData(String recurranceData) {
		this.recurranceData = recurranceData;
	}
	public Long getRecurranceEnd() {
		return recurranceEnd;
	}
	public void setRecurranceEnd(Long recurranceEnd) {
		this.recurranceEnd = recurranceEnd;
	}
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public String getWhenText() {
		return whenText;
	}
	public void setWhenText(String whenText) {
		this.whenText = whenText;
	}
	
	
	
}