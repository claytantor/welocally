package com.sightlyinc.ratecred.admin.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.sightlyinc.ratecred.model.Publisher;


public class PublisherForm {
	
	static Logger logger = Logger.getLogger(PublisherForm.class);

	private Long id;
	
	private Long version;
	
	private String url;
	
	private String siteName;
	
	private String description;
	
	private String summary;
	
	private Integer monthlyPageviews;

	public PublisherForm() {
		super();
	}
	
	public PublisherForm(Publisher p) {
		try {
			BeanUtils.copyProperties(this, p);
		} catch (IllegalAccessException e) {
			logger.error("load error", e);
		} catch (InvocationTargetException e) {
			logger.error("load error", e);
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Integer getMonthlyPageviews() {
		return monthlyPageviews;
	}

	public void setMonthlyPageviews(Integer monthlyPageviews) {
		this.monthlyPageviews = monthlyPageviews;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	

}
