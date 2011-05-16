package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	private String memberKey;
	private String description;
	private String iconUrl;
	private String mapIconUrl;
	
	@Column(name = "member_type")
	@Enumerated(EnumType.STRING)
	private MemberType type;
	
	@ManyToOne
	@JoinColumn(name = "user_principal_id")
	private UserPrincipal principal;

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

	public UserPrincipal getPrincipal() {
		return principal;
	}

	public void setPrincipal(UserPrincipal principal) {
		this.principal = principal;
	}
	
	
	
}
