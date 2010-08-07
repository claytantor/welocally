package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.noi.utility.hibernate.ImageValue;
import com.noi.utility.reflection.ReflectionUtils;

public class Rater {
	
	private Long id;
	private Integer version = new Integer(0);
	private String userName;
	private String secretKey;
	private Date timeCreated;
	private Long score;
	private String authGuid;
	private String status;	
	private ImageValue raterImage;
	private java.lang.Long imageValueId;
	private Set<Rating> ratings= new HashSet<Rating>();
	private Set<Award> awards = new HashSet<Award>();
	private RaterMetrics metrics;
	
	
			
	public java.lang.Long getImageValueId() {
		return imageValueId;
	}

	public void setImageValueId(java.lang.Long imageValueId) {
		this.imageValueId = imageValueId;
	}

	public ImageValue getRaterImage() {
		return raterImage;
	}

	public void setRaterImage(ImageValue raterImage) {
		this.raterImage = raterImage;
	}

	public String getAuthGuid() {
		return authGuid;
	}

	public void setAuthGuid(String authGuid) {
		this.authGuid = authGuid;
	}



	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Set<Rating> getRatings() {
		return ratings;
	}
	
	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/*@Override
	public String toString() {
		return ReflectionUtils.toBeanString(this);
	}*/

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Set<Award> getAwards() {
		return awards;
	}

	public void setAwards(Set<Award> awards) {
		this.awards = awards;
	}

	public RaterMetrics getMetrics() {
		return metrics;
	}

	public void setMetrics(RaterMetrics metrics) {
		this.metrics = metrics;
	}
	
	
	
}
