package com.sightlyinc.ratecred.authentication;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;


public class DefaultUserDetails implements UserDetails {
	
	private GrantedAuthority[] authorities;
	private String username;		
	private String password;	
	private Boolean expired;
	private Boolean credentialsExpired;
	private Boolean locked;
	private Boolean enabled;	
	

	public DefaultUserDetails(
			GrantedAuthority[] authorities, 
			Boolean cexpired, 
			Boolean enabled, 
			Boolean expired, 
			Boolean locked, 
			String password, 
			String username) {
		super();
		this.authorities = authorities;
		this.credentialsExpired = cexpired;
		this.enabled = enabled;
		this.expired = expired;
		this.locked = locked;
		this.password = password;
		this.username = username;
	}

	public GrantedAuthority[] getAuthorities() {
		return authorities;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAccountNonExpired() {
		return !expired;
	}

	public boolean isAccountNonLocked() {
		return !locked;
	}

	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
