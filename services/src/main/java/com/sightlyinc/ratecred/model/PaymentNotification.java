package com.sightlyinc.ratecred.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sightlyinc.ratecred.interceptor.PersistenceObservable;

/**
 * 
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT NULL,
  `external_key` VARCHAR(255) NULL DEFAULT NULL ,
  `publisher_key` VARCHAR(255) NULL DEFAULT NULL ,
  `txn_type` VARCHAR(255) NULL DEFAULT NULL ,
  `notification_body` TEXT NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */

@PersistenceObservable
@Entity
@Table(name="payment_notification")
public class PaymentNotification extends BaseEntity {
	
	@Column(name="external_key")
	private String externalKey;
	
	@Column(name="publisher_key")
	private String publisherKey;
	
	@Column(name="txn_type")
	private String transactionType;
	
	@Column(name="notification_body", columnDefinition="TEXT")
	private String notficationBody;

	public String getExternalKey() {
		return externalKey;
	}

	public void setExternalKey(String externalKey) {
		this.externalKey = externalKey;
	}

	public String getPublisherKey() {
		return publisherKey;
	}

	public void setPublisherKey(String publisherKey) {
		this.publisherKey = publisherKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getNotficationBody() {
		return notficationBody;
	}

	public void setNotficationBody(String notficationBody) {
		this.notficationBody = notficationBody;
	}
	

	
		

}
