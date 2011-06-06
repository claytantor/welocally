package com.sightlyinc.ratecred.admin.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;



public class UserPrincipalForm {
	
	static Logger logger = Logger.getLogger(UserPrincipalForm.class);
	private Long id;

	// properties
	private java.lang.Integer version = new Integer(0);
    
	private String username;
	private String password;
	private String email;
	private String userClass;

	private Boolean expired;
	private Boolean credentialsExpired;
	private Boolean locked;
	private Boolean enabled;
	


	private List<String> roles = new ArrayList<String>();

	public UserPrincipalForm() {
		super();
	}
	
	public UserPrincipalForm(com.sightlyinc.ratecred.authentication.UserPrincipal userPrincipal) {
		try {
			BeanUtils.copyProperties(this, userPrincipal);
		} catch (IllegalAccessException e) {
			logger.error("load error", e);
		} catch (InvocationTargetException e) {
			logger.error("load error", e);
		}
	}


	public Long getId() {
		return id;
	}

	public java.lang.Integer getVersion() {
		return version;
	}

	public void setVersion(java.lang.Integer version) {
		this.version = version;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	

	

}
