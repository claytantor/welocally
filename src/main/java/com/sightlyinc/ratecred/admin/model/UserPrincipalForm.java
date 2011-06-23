package com.sightlyinc.ratecred.admin.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

import com.sightlyinc.ratecred.authentication.Role;
import com.sightlyinc.ratecred.authentication.UserPrincipal;



public class UserPrincipalForm  {
	
	static Logger logger = Logger.getLogger(UserPrincipalForm.class);
	

	private List<String> roleNames = new ArrayList<String>();
	
	private UserPrincipal entity;

	public UserPrincipalForm() {
		super();
		this.entity = new UserPrincipal();
	}
	
	public UserPrincipalForm(com.sightlyinc.ratecred.authentication.UserPrincipal userPrincipal) {
		this.entity = userPrincipal;
	}

	public List<String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}

	public String getEmail() {
		return entity.getEmail();
	}

	public void setEmail(String email) {
		entity.setEmail(email);
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return entity.getPassword();
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return entity.getUsername();
	}

	
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return entity.getId();
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Long id) {
		entity.setId(id);
	}

	
	/**
	 * @return Returns the version.
	 */
	public java.lang.Integer getVersion() {
		return entity.getVersion();
	}

	/**
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(java.lang.Integer version) {
		entity.setVersion(version);
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		entity.setPassword(password);
	}

	/**
	 * @param username
	 *            The username to set.
	 */
	public void setUsername(String username) {
		entity.setUsername(username);
	}
	
	public String getUserClass() {
		return entity.getUserClass();
	}

	public void setUserClass(String userClass) {
		entity.setUserClass(userClass);
	}

	
//	public Boolean getExpired() {
//		return entity.getExpired();
//	}
//
//	public void setExpired(Boolean expired) {
//		entity.setExpired(expired);
//	}

	public Boolean getCredentialsExpired() {
		return entity.getCredentialsExpired();
	}

	public void setCredentialsExpired(Boolean credentialsExpired) {
		entity.setCredentialsExpired(credentialsExpired);
	}

	public Boolean getLocked() {
		return entity.getLocked();
	}

	public void setLocked(Boolean locked) {
		entity.setLocked(locked);
	}

	@JsonIgnore
	public Boolean getEnabled() {
		return entity.getEnabled();
	}

	@JsonIgnore
	public void setEnabled(Boolean enabled) {
		entity.setEnabled(enabled);
	}

	public UserPrincipal getEntity() {
		return entity;
	}
	
	public Set<Role> getRoles() {
		return entity.getRoles();
	}

	
	
}
