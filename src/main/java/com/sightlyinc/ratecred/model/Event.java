package com.sightlyinc.ratecred.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sightlyinc.ratecred.client.geo.GeoPersistable;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.interceptor.PersistenceObservable;

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
  `recurrence_type` ENUM('DAILY','WEEKLY','MONTHLY','YEARLY','HOURLY','MINUTELY') NULL DEFAULT NULL ,
  `recurrence_interval` INT(11) NULL DEFAULT NULL ,
  `recurrence_data` VARCHAR(1024) NULL DEFAULT NULL ,
  `recurrence_end` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,


 * @author claygraham
 *
 */
@PersistenceObservable
@Entity
@Table(name="event")
public class Event extends BaseEntity implements GeoPersistable {
	
	private enum RecurrenceType {DAILY,WEEKLY,MONTHLY,YEARLY,HOURLY,MINUTELY}
	
	private String name;
	
	private String url;
	
	private Float cost;
	
	private Boolean published;
	
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
	
	
//	@Column(name="recurrence_type")
//	@Enumerated(EnumType.STRING)
//	private RecurrenceType recurrenceType;
	
	@Column(name="recurrence_interval")
	private Integer recurrenceInterval;
	
	@Column(name="recurrence_data")
	private String recurrenceData;
	
	@Column(name="recurrence_end")
	private Long recurrenceEnd;
	
	private String phone;
	
	@ManyToOne
	@JoinColumn(name = "place_id")
	private Place place;
	
	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;
	
	
    @Override
	public String getGeoRecordId() throws GeoPersistenceException {
    	if(getId() != null)
    		return getId().toString();
    	else 
    		throw new GeoPersistenceException("geo record id cannot be null");
	}

	@Override
	public String getMemberKey() throws GeoPersistenceException  {
		if(publisher != null)
			return getPublisherLayerPrefix(publisher);
		else
			throw new GeoPersistenceException("geo member key cannot be null");
	}
	
	

	@Override
	public Place getGeoPlace() throws GeoPersistenceException {
		if(place != null)
			return place;
		else
			throw new GeoPersistenceException("geo place cannot be null");
	}
	
	
	
	@Override
	public Long getExpiration() throws GeoPersistenceException {
		if(timeEnds != null)
			return timeEnds;
		else
			return -1l;
	}

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
//	public RecurrenceType getRecurrenceType() {
//		return recurrenceType;
//	}
//	public void setRecurrenceType(RecurrenceType recurrenceType) {
//		this.recurrenceType = recurrenceType;
//	}
//	public Integer getrecurrenceInterval() {
//		return recurrenceInterval;
//	}
//	public void setrecurrenceInterval(Integer recurrenceInterval) {
//		this.recurrenceInterval = recurrenceInterval;
//	}
//	public String getrecurrenceData() {
//		return recurrenceData;
//	}
//	public void setrecurrenceData(String recurrenceData) {
//		this.recurrenceData = recurrenceData;
//	}
//	public Long getrecurrenceEnd() {
//		return recurrenceEnd;
//	}
//	public void setrecurrenceEnd(Long recurrenceEnd) {
//		this.recurrenceEnd = recurrenceEnd;
//	}
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
	public Boolean getPublished() {
		return published;
	}
	public void setPublished(Boolean published) {
		this.published = published;
	}
	public Integer getRecurrenceInterval() {
		return recurrenceInterval;
	}
	public void setRecurrenceInterval(Integer recurrenceInterval) {
		this.recurrenceInterval = recurrenceInterval;
	}
	public String getRecurrenceData() {
		return recurrenceData;
	}
	public void setRecurrenceData(String recurrenceData) {
		this.recurrenceData = recurrenceData;
	}
	public Long getRecurrenceEnd() {
		return recurrenceEnd;
	}
	public void setRecurrenceEnd(Long recurrenceEnd) {
		this.recurrenceEnd = recurrenceEnd;
	}
	
	public Date getStartDateTime(){
		return (timeStarts == null ? null : new Date(timeStarts));
	}
	
	public Date getEndDateTime(){
		return (timeEnds == null ? null : new Date(timeEnds));
	}
	
	
	
}
