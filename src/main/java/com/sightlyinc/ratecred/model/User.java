package com.sightlyinc.ratecred.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
	private Long id;
	private Integer version = new Integer(0);	
	
	private String userName;
	private String password;
	private String email;
	private String twitterScreenName;
	private Long twitterId;
	private String twitterToken;
	private String twitterTokenSecret;
	private String twitterVerify;
	private String twitterProfileImage;
	private Long timeCreatedMills;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTwitterProfileImage() {
		return twitterProfileImage;
	}
	public void setTwitterProfileImage(String twitterProfileImage) {
		this.twitterProfileImage = twitterProfileImage;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTwitterScreenName() {
		return twitterScreenName;
	}
	public void setTwitterScreenName(String twitterScreenName) {
		this.twitterScreenName = twitterScreenName;
	}
	
	
	
	public Long getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(Long twitterId) {
		this.twitterId = twitterId;
	}
	public String getTwitterToken() {
		return twitterToken;
	}
	public void setTwitterToken(String twitterToken) {
		this.twitterToken = twitterToken;
	}
	public String getTwitterTokenSecret() {
		return twitterTokenSecret;
	}
	public void setTwitterTokenSecret(String twitterTokenSecret) {
		this.twitterTokenSecret = twitterTokenSecret;
	}
	public String getTwitterVerify() {
		return twitterVerify;
	}
	public void setTwitterVerify(String twitterVerify) {
		this.twitterVerify = twitterVerify;
	}
	public Long getTimeCreatedMills() {
		return timeCreatedMills;
	}
	public void setTimeCreatedMills(Long timeCreatedMills) {
		this.timeCreatedMills = timeCreatedMills;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	


}
