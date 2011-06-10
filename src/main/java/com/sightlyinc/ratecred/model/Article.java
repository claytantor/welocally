package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sightlyinc.ratecred.client.geo.GeoPersistable;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.interceptor.PersistenceObservable;

/**
 *   `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NULL ,
  `publisher_id` BIGINT(20) NOT NULL ,
  `place_id` BIGINT(20) NOT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `summary` VARCHAR(1024) NULL DEFAULT NULL ,
  `url` VARCHAR(1024) NULL DEFAULT NULL ,   
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */

@PersistenceObservable
@Entity
@Table(name="article")
public class Article extends BaseEntity implements GeoPersistable {

    private String url;
    
    private String name;

	@Column(columnDefinition="TEXT")
	private String summary;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	public Boolean published;
	
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
			return publisher.getNetworkMember().getMemberKey();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}	
	
}
