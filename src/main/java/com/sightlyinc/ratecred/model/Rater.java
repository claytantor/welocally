package com.sightlyinc.ratecred.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.noi.utility.hibernate.ImageValue;


public class Rater  {
	
	@JsonProperty
	protected Long id;
	
	@JsonProperty
	protected Integer version = new Integer(0);
	
	@JsonProperty
	protected String userName;
	
	@JsonProperty
	protected String secretKey;
	
	@JsonProperty
	protected Date timeCreated;
	
	@JsonProperty
	protected Long score;
	
	@JsonProperty
	protected String authGuid;
	
	@JsonProperty
	protected String status;	
	
	@JsonProperty
	protected ImageValue raterImage;
	
	@JsonProperty
	protected java.lang.Long imageValueId;
	
	@JsonIgnore
	private String authFoursquare = "false";
	
	@JsonIgnore
	private String authGowalla = "false";
	
	@JsonIgnore
	protected Set<Rating> ratings= new HashSet<Rating>();
	
	@JsonIgnore
	protected Set<Award> awards = new HashSet<Award>();
	
	@JsonIgnore
	protected Set<Compliment> compliments = new HashSet<Compliment>();
	
	@JsonIgnore
	protected RaterMetrics metrics;
	

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getImageValueId()
	 */
	@JsonProperty
	public java.lang.Long getImageValueId() {
		return imageValueId;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setImageValueId(java.lang.Long)
	 */
	@JsonProperty
	public void setImageValueId(java.lang.Long imageValueId) {
		this.imageValueId = imageValueId;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getRaterImage()
	 */
	@JsonIgnore
	public ImageValue getRaterImage() {
		return raterImage;
	}
	

	@JsonIgnore
	public Set<Compliment> getCompliments() {
		return compliments;
	}

	@JsonIgnore
	public void setCompliments(Set<Compliment> compliments) {
		this.compliments = compliments;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setRaterImage(com.noi.utility.hibernate.ImageValue)
	 */
	@JsonIgnore
	public void setRaterImage(ImageValue raterImage) {
		this.raterImage = raterImage;
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
	 * @see com.sightlyinc.ratecred.model.Rater#getTimeCreated()
	 */
	@JsonIgnore
	public Date getTimeCreated() {
		return timeCreated;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setTimeCreated(java.util.Date)
	 */
	@JsonIgnore
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getVersion()
	 */
	@JsonProperty
	public Integer getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setVersion(java.lang.Integer)
	 */
	@JsonProperty
	public void setVersion(Integer version) {
		this.version = version;
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
	 * @see com.sightlyinc.ratecred.model.Rater#getId()
	 */
	@JsonProperty
	public Long getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setId(java.lang.Long)
	 */
	@JsonProperty
	public void setId(Long id) {
		this.id = id;
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
	public RaterMetrics getMetrics() {
		return metrics;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setMetrics(com.sightlyinc.ratecred.model.RaterMetrics)
	 */
	@JsonIgnore
	public void setMetrics(RaterMetrics metrics) {
		this.metrics = metrics;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Rater)
		{
			Rater inst = (Rater)obj;
			return inst.getId().equals(this.id);
			
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
