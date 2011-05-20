package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "version")
	private Integer version = new Integer(0);
	
	@Column(name = "time_created")
	private Long timeCreated;
	
	@Column(name = "time_updated")
	private Long timeUpdated;

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

	public Long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Long getTimeUpdated() {
		return timeUpdated;
	}

	public void setTimeUpdated(Long timeUpdated) {
		this.timeUpdated = timeUpdated;
	}
	
	
	
	

}
