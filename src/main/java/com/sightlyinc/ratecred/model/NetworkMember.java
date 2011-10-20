package com.sightlyinc.ratecred.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.interceptor.PersistenceObservable;


/**
 * `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `user_principal_id` BIGINT(20) NOT NULL ,
  `name` VARCHAR(255) NULL DEFAULT NULL ,
  `member_key` VARCHAR(255) NULL DEFAULT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `icon_url` VARCHAR(1024) NULL DEFAULT NULL ,
  `member_type` ENUM('PUBLISHER','AFFILIATE','MERCHANT') NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
  
  properties
  	"name",	
	"memberKey",	
	"description",	
	"iconUrl",	
	"mapIconUrl",	
	"primaryEmail",	
	"paypalEmail",
	"publishers",	
	"affiliates",	
	"merchants",
  
 * 
 * @author claygraham
 *
 */
@PersistenceObservable
@Entity 
@Table(name="network_member")
public class NetworkMember extends BaseEntity {
	
	//public enum MemberType{PUBLISHER,AFFILIATE,MERCHANT}
	
	private String name;
	
	@Column(name="member_key")
	private String memberKey;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	@Column(name="primary_email")
	private String primaryEmail;
	
	@Column(name="paypal_email")
	private String paypalEmail;
	

	@OneToMany
	@JoinColumn(name="network_member_id")
	@JsonIgnore
	@OrderBy("subscriptionStatus, siteName")
	private Set<Publisher> publishers;
	
	@OneToMany
	@JoinColumn(name="network_member_id")
	@JsonIgnore
	private Set<Affiliate> affiliates;
	
	@OneToMany
	@JoinColumn(name="network_member_id")
	@JsonIgnore
	private Set<Merchant> merchants;

	
	@OneToOne
	@JoinColumn(name="user_principal_id")
	@JsonIgnore
	private UserPrincipal userPrincipal;
	


	public NetworkMember() {
		super();
		this.publishers = new HashSet<Publisher>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemberKey() {
		return memberKey;
	}

	public void setMemberKey(String memberKey) {
		this.memberKey = memberKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getPaypalEmail() {
		return paypalEmail;
	}

	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}

	@JsonIgnore
	public UserPrincipal getUserPrincipal() {
		return userPrincipal;
	}

	@JsonIgnore
	public void setUserPrincipal(UserPrincipal userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	@JsonIgnore
	public Set<Publisher> getPublishers() {
		return publishers;
	}

	@JsonIgnore
	public void setPublishers(Set<Publisher> publishers) {
		this.publishers = publishers;
	}

	@JsonIgnore
	public Set<Affiliate> getAffiliates() {
		return affiliates;
	}
	
	@JsonIgnore
	public void setAffiliates(Set<Affiliate> affiliates) {
		this.affiliates = affiliates;
	}
	
	@JsonIgnore
	public Set<Merchant> getMerchants() {
		return merchants;
	}

	@JsonIgnore
	public void setMerchants(Set<Merchant> merchants) {
		this.merchants = merchants;
	}


	
	
}
