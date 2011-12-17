package com.sightlyinc.ratecred.authentication;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

import com.sightlyinc.ratecred.model.BaseEntity;

/**
 * 
 * 
 * @author cgraham
 * 
 */
@Entity
@Table(name="user_principal")
public class UserPrincipal extends BaseEntity implements Authentication, UserDetails {


    @Transient
	private Boolean authenticated;

    
    @Column(name = "user_name")
	private String username;
    
	private String password;
	
	private String email;
	
    @Column(name = "user_class")
	private String userClass;

//    @Column(columnDefinition = "tinyint")
//	private Boolean expired;
    
    @Column(name = "credentials_expired", columnDefinition = "tinyint")
	private Boolean credentialsExpired = new Boolean(false);
    
    @Column(columnDefinition = "tinyint")
	private Boolean locked = new Boolean(false);
    
    @Column(columnDefinition = "tinyint")
	private Boolean enabled = new Boolean(true);
    
    
    /**
     * 
     *  * twitter_id` BIGINT(20) NULL DEFAULT NULL ,
  `twitter_username` VARCHAR(45) NULL DEFAULT NULL ,
  `twitter_token` VARCHAR(255) NULL DEFAULT NULL ,
  `twitter_secret` VARCHAR(255) NULL DEFAULT NULL ,
  `twitter_verify` VARCHAR(255) NULL DEFAULT NULL ,
  `twitter_profile_img` VARCHAR(255) NULL DEFAULT NULL ,
     * 
     */
    @Column(name = "twitter_id")
    private Long twitterId;
    
    @Column(name = "twitter_username")
    private String twitterUsername;
    
    @Column(name = "twitter_profile_img")
    private String twitterProfileImg;
    
    @Column(name = "twitter_token")
    private String twitterToken;
    
    @Column(name = "twitter_secret")
    private String twitterSecret;
    
    @Column(name = "twitter_verify")
    private String twitterVerify;
       

    @Column(name = "guid")
	private String authGuid;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Role> roles = new HashSet<Role>();

	public String getAuthGuid() {
		return authGuid;
	}

	public void setAuthGuid(String authGuid) {
		this.authGuid = authGuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	



	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param username
	 *            The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

    @JsonIgnore
	public Set<Role> getRoles() {
		return this.roles;
	}

    @JsonIgnore
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	@Override
	public GrantedAuthority[] getAuthorities() {
		GrantedAuthority[] authorities = new GrantedAuthority[roles.size()];

		Role[] roleArray = (Role[]) roles.toArray(new Role[roles.size()]);
		for (int i = 0; i < roleArray.length; i++) {
			Role r = roleArray[i];
			GrantedAuthority ga = new GrantedAuthorityImpl(r.getRole());
			authorities[i] = ga;
		}

		return authorities;
	}

	@Override
	public Object getCredentials() {
		return password;
	}

	@Override
	public Object getDetails() {
		return this;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	@JsonIgnore
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	@JsonIgnore	
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		this.authenticated = arg0;

	}

	@Override
	public String getName() {
		return username;
	}

	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	@JsonIgnore
	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}

//	public Boolean getExpired() {
//		return expired;
//	}
//
//	public void setExpired(Boolean expired) {
//		this.expired = expired;
//	}

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

	@JsonIgnore
	public Boolean getEnabled() {
		return enabled;
	}

	@JsonIgnore
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !getCredentialsExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return !getLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !getCredentialsExpired();
	}

	@Override
	public boolean isEnabled() {
		return getEnabled();
	}



	public Long getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(Long twitterId) {
		this.twitterId = twitterId;
	}

	public String getTwitterUsername() {
		return twitterUsername;
	}

	public void setTwitterUsername(String twitterUsername) {
		this.twitterUsername = twitterUsername;
	}

	public String getTwitterProfileImg() {
		return twitterProfileImg;
	}

	public void setTwitterProfileImg(String twitterProfileImg) {
		this.twitterProfileImg = twitterProfileImg;
	}

	public String getTwitterToken() {
		return twitterToken;
	}

	public void setTwitterToken(String twitterToken) {
		this.twitterToken = twitterToken;
	}

	public String getTwitterVerify() {
		return twitterVerify;
	}

	public void setTwitterVerify(String twitterVerify) {
		this.twitterVerify = twitterVerify;
	}

	public String getTwitterSecret() {
		return twitterSecret;
	}

	public void setTwitterSecret(String twitterSecret) {
		this.twitterSecret = twitterSecret;
	}
	
	

}
