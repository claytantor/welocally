package com.sightlyinc.ratecred.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NOT NULL ,
  `user_principal_id` BIGINT(20) NOT NULL ,
  `username` VARCHAR(255) NULL DEFAULT NULL ,
  `secretkey` VARCHAR(255) NULL DEFAULT NULL ,
  `time_created` DATETIME NULL DEFAULT NULL ,
  `score` BIGINT(20) NULL DEFAULT '0' ,
  `imagevalue_id` BIGINT(20) NULL DEFAULT NULL ,
  `guid` VARCHAR(45) NULL DEFAULT NULL ,
  `status` VARCHAR(12) NULL DEFAULT NULL ,
  `auth_foursquare` VARCHAR(12) NULL DEFAULT 'false' ,
  `auth_gowalla` VARCHAR(12) NULL DEFAULT 'false' ,
 * @author claygraham
 *
 */
@Entity
@Table(name="patron")
public class Patron extends BaseEntity {
	
	
	@JsonProperty
	protected String userName;
	
	@JsonProperty
	protected String secretKey;
	
	@JsonProperty
	protected Long score;
	
	@JsonProperty
	@Column(name="guid")
	protected String authGuid;
	
	@JsonProperty
	protected String status;	
	
	@JsonProperty
	protected String profileImageAttachmentKey;
	
	@JsonProperty
	protected String imageAttachmentKey;
	
	@JsonProperty
	protected String categoryAttachmentKey;
	
	@JsonIgnore
	private String authFoursquare = "false";
	
	@JsonIgnore
	private String authGowalla = "false";
	
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "patron_id")
	protected Set<Rating> ratings= new HashSet<Rating>();
	
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "patron_id")
	protected Set<Award> awards = new HashSet<Award>();
	
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "patron_id")
	protected Set<Compliment> compliments = new HashSet<Compliment>();
	
	@JsonIgnore
	@Transient
	protected PatronMetrics metrics;
	

	@JsonIgnore
	public Set<Compliment> getCompliments() {
		return compliments;
	}

	@JsonIgnore
	public void setCompliments(Set<Compliment> compliments) {
		this.compliments = compliments;
	}


	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getAuthGuid()
	 */
	@JsonProperty
	public String getAuthGuid() {
		return authGuid;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setAuthGuid(java.lang.String)
	 */
	@JsonProperty
	public void setAuthGuid(String authGuid) {
		this.authGuid = authGuid;
	}


	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getSecretKey()
	 */
	@JsonProperty
	public String getSecretKey() {
		return secretKey;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setSecretKey(java.lang.String)
	 */
	@JsonProperty
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}


	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getUserName()
	 */
	@JsonProperty
	public String getUserName() {
		return userName;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setUserName(java.lang.String)
	 */
	@JsonProperty
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getRatings()
	 */
	@JsonProperty
	public Set<Rating> getRatings() {
		return ratings;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setRatings(java.util.Set)
	 */
	@JsonIgnore
	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getStatus()
	 */
	@JsonProperty
	public String getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setStatus(java.lang.String)
	 */
	@JsonProperty
	public void setStatus(String status) {
		this.status = status;
	}

	/*@Override
	public String toString() {
		return ReflectionUtils.toBeanString(this);
	}*/

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getScore()
	 */
	@JsonProperty
	public Long getScore() {
		return score;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setScore(java.lang.Long)
	 */
	@JsonProperty
	public void setScore(Long score) {
		this.score = score;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getAwards()
	 */
	@JsonIgnore
	public Set<Award> getAwards() {
		return awards;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setAwards(java.util.Set)
	 */
	@JsonIgnore
	public void setAwards(Set<Award> awards) {
		this.awards = awards;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getMetrics()
	 */
	@JsonIgnore
	public PatronMetrics getMetrics() {
		return metrics;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setMetrics(com.sightlyinc.ratecred.model.RaterMetrics)
	 */
	@JsonIgnore
	public void setMetrics(PatronMetrics metrics) {
		this.metrics = metrics;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Patron)
		{
			Patron inst = (Patron)obj;
			return inst.getId().equals(super.getId());
			
		} else 
			return false;


	}

	public String getAuthFoursquare() {
		return authFoursquare;
	}

	public void setAuthFoursquare(String authFoursquare) {
		this.authFoursquare = authFoursquare;
	}

	public String getAuthGowalla() {
		return authGowalla;
	}

	public void setAuthGowalla(String authGowalla) {
		this.authGowalla = authGowalla;
	}
	
	public void setAuthorizedFoursquare(Boolean auth) {
		this.authFoursquare = auth.toString();
	}
	
	public Boolean getAuthorizedFoursquare() {
		return Boolean.parseBoolean(this.authFoursquare);
	}	
	
	public void setAuthorizedGowalla(Boolean auth) {
		this.authGowalla = auth.toString();
	}
	
	public Boolean getAuthorizedGowalla() {
		return Boolean.parseBoolean(this.authGowalla);
	}	
	
}
