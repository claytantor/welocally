package com.sightlyinc.ratecred.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.noi.utility.hibernate.ImageValue;


public class Rater {
	
	protected Long id;
	protected Integer version = new Integer(0);
	protected String userName;
	protected String secretKey;
	protected Date timeCreated;
	protected Long score;
	protected String authGuid;
	protected String status;	
	protected ImageValue raterImage;
	protected java.lang.Long imageValueId;
	
	protected Set<Rating> ratings= new HashSet<Rating>();
	
	protected Set<Award> awards = new HashSet<Award>();
	
	protected Set<Compliment> compliments = new HashSet<Compliment>();
	
	protected RaterMetrics metrics;
	

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getImageValueId()
	 */
	public java.lang.Long getImageValueId() {
		return imageValueId;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setImageValueId(java.lang.Long)
	 */
	public void setImageValueId(java.lang.Long imageValueId) {
		this.imageValueId = imageValueId;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getRaterImage()
	 */
	public ImageValue getRaterImage() {
		return raterImage;
	}
	
	

	public Set<Compliment> getCompliments() {
		return compliments;
	}

	public void setCompliments(Set<Compliment> compliments) {
		this.compliments = compliments;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setRaterImage(com.noi.utility.hibernate.ImageValue)
	 */
	public void setRaterImage(ImageValue raterImage) {
		this.raterImage = raterImage;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getAuthGuid()
	 */
	public String getAuthGuid() {
		return authGuid;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setAuthGuid(java.lang.String)
	 */
	public void setAuthGuid(String authGuid) {
		this.authGuid = authGuid;
	}



	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getTimeCreated()
	 */
	public Date getTimeCreated() {
		return timeCreated;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setTimeCreated(java.util.Date)
	 */
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getVersion()
	 */
	public Integer getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setVersion(java.lang.Integer)
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getSecretKey()
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setSecretKey(java.lang.String)
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getId()
	 */
	public Long getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setId(java.lang.Long)
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getUserName()
	 */
	public String getUserName() {
		return userName;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setUserName(java.lang.String)
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getRatings()
	 */
	public Set<Rating> getRatings() {
		return ratings;
	}
	
	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setRatings(java.util.Set)
	 */
	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getStatus()
	 */
	public String getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setStatus(java.lang.String)
	 */
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
	public Long getScore() {
		return score;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setScore(java.lang.Long)
	 */
	public void setScore(Long score) {
		this.score = score;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getAwards()
	 */
	public Set<Award> getAwards() {
		return awards;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setAwards(java.util.Set)
	 */
	public void setAwards(Set<Award> awards) {
		this.awards = awards;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#getMetrics()
	 */
	public RaterMetrics getMetrics() {
		return metrics;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.model.Rater#setMetrics(com.sightlyinc.ratecred.model.RaterMetrics)
	 */
	public void setMetrics(RaterMetrics metrics) {
		this.metrics = metrics;
	}
	
	
	
}
