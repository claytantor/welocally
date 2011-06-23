package com.sightlyinc.ratecred.interceptor;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.sightlyinc.ratecred.model.BaseEntity;

public class PersistenceActivity {
	
	public static int ACTIVITY_CREATE=0;
	public static int ACTIVITY_UPDATE=1;
	public static int ACTIVITY_DELETE=2;
	
	
	
	private String clazzName;
	private Long entityId;
	private Integer activity;
	
	@Transient
	@JsonIgnore
	public transient BaseEntity entity;
	
	
	public String getClazzName() {
		return clazzName;
	}
	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public Integer getActivity() {
		return activity;
	}
	public void setActivity(Integer activity) {
		this.activity = activity;
	}
	
	@JsonIgnore
	public BaseEntity getEntity() {
		return entity;
	}
	
	@JsonIgnore
	public void setEntity(BaseEntity entity) {
		this.entity = entity;
	}
	
	
}