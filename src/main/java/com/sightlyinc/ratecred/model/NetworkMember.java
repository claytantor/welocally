package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.noi.utility.spring.UserPrincipal;

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
 * 
 * @author claygraham
 *
 */
@Entity 
@Table(name="network_member")
public class NetworkMember extends BaseEntity {
	
	public enum MemberType{PUBLISHER,AFFILIATE,MERCHANT}
	
	private String name;
	
	@Column(name="member_key")
	private String memberKey;
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	@Column(name="icon_url")
	private String iconUrl;
	
	@Column(name="map_icon_url")
	private String mapIconUrl;
	
	@Column(name="primary_email")
	private String primaryEmail;
	
	@Column(name="paypal_email")
	private String paypalEmail;
	
	/**
	 * so we are choosing to have three foreign keys to the entity types
	 * possible for a member and then letting business logic manage what 
	 * entity it is, this may be a good use case for table inheritance 
	 * with hibernate. right now we are going to have three keys to support it
	 * but they will live in the external table that is being referenced
	 * 
	 * http://docs.jboss.org/hibernate/core/3.3/reference/en/html/inheritance.html
	 * 
	 */
	@Column(name = "member_type", columnDefinition="enum ('PUBLISHER','AFFILIATE','MERCHANT')")
	@Enumerated(EnumType.STRING)
	private MemberType type;
	
	@OneToOne
	@JoinTable(name="publisher")
	@JoinColumn(name="network_member_id")
	private Publisher publisher;
	
	@OneToOne
	@JoinTable(name="affiliate")
	@JoinColumn(name="network_member_id")
	private Affiliate affiliate;

	@OneToOne
	@JoinTable(name="merchant")
	@JoinColumn(name="network_member_id")
	private Merchant merchant;
		
	
	
	/*@ManyToOne
	@JoinColumn(name = "user_principal_id")
	private UserPrincipal principal;*/

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

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getMapIconUrl() {
		return mapIconUrl;
	}

	public void setMapIconUrl(String mapIconUrl) {
		this.mapIconUrl = mapIconUrl;
	}

	public MemberType getType() {
		return type;
	}

	public void setType(MemberType type) {
		this.type = type;
	}

	/*public UserPrincipal getPrincipal() {
		return principal;
	}

	public void setPrincipal(UserPrincipal principal) {
		this.principal = principal;
	}*/

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

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public Affiliate getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(Affiliate affiliate) {
		this.affiliate = affiliate;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	
	
	
}
