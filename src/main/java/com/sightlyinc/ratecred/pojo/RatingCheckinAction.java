package com.sightlyinc.ratecred.pojo;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.noi.utility.data.Base64Utils;
import com.sightlyinc.ratecred.model.Rating;

public class RatingCheckinAction {
	
	static Logger logger = 
		Logger.getLogger(RatingCheckinAction.class);
	
	private String ratingString;
	private String provider;
	
	
	
	public RatingCheckinAction(String provider,Rating rating) {
		super();
		try {
			this.ratingString = Base64Utils.toString( (Serializable)rating );
		} catch (IOException e) {
			logger.error("problem serializing rating", e);
		} catch (Exception e) {
			logger.error("problem serializing rating", e);
		}		
		this.provider = provider;
	}
	

	public String getRatingString() {
		return ratingString;
	}

	public void setRatingString(String ratingString) {
		this.ratingString = ratingString;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	

}
