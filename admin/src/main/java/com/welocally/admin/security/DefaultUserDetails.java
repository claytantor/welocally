package com.welocally.admin.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


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

	public Collection<GrantedAuthority> getAuthorities() {
		return Arrays.asList(authorities);
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAccountNonExpired() {
		return expired == null || !expired;
	}

	public boolean isAccountNonLocked() {
		return locked == null || !locked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsExpired == null || !credentialsExpired;
	}

	public boolean isEnabled() {
		return enabled != null && enabled;
	}

}
