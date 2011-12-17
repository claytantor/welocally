package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
	 `version` INT NOT NULL ,
	 `publisher_id` BIGINT(20) ,
	 `merchant_id` BIGINT(20)  ,
	 `affiliate_id` BIGINT(20) ,
	 `offer_id` BIGINT(20) ,
	 `publisher_revenue_percentage` FLOAT  ,
	 `affiliate_revenue_percentage` FLOAT  ,
	 `merchant_revenue_percentage` FLOAT ,
	 `time_created` BIGINT(20) NULL DEFAULT NULL ,
	 `time_updated` BIGINT(20) NULL DEFAULT NULL ,
 * 
 * @author claygraham
 *
 */
@Entity
@Table(name="offer_economics")
public class OfferEconomics extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;
	
	@ManyToOne
	@JoinColumn(name = "merchant_id")
	private Merchant merchant;
	
	@ManyToOne
	@JoinColumn(name = "affiliate_id")
	private Affiliate affiliate;
	
	@OneToOne
	@JoinColumn(name = "offer_id")
	private Offer offer;
	
	@Column(name="publisher_revenue_percentage")
	private Float publisherRevenuePercentage;
	
	@Column(name="merchant_revenue_percentage")
	private Float merchantRevenuePercentage;
	
	@Column(name="affiliate_revenue_percentage")
	private Float affiliateRevenuePercentage;
	
	
	public Publisher getPublisher() {
		return publisher;
	}
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	public Merchant getMerchant() {
		return merchant;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	public Affiliate getAffiliate() {
		return affiliate;
	}
	public void setAffiliate(Affiliate affiliate) {
		this.affiliate = affiliate;
	}
	public Offer getOffer() {
		return offer;
	}
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	public Float getPublisherRevenuePercentage() {
		return publisherRevenuePercentage;
	}
	public void setPublisherRevenuePercentage(Float publisherRevenuePercentage) {
		this.publisherRevenuePercentage = publisherRevenuePercentage;
	}
	public Float getMerchantRevenuePercentage() {
		return merchantRevenuePercentage;
	}
	public void setMerchantRevenuePercentage(Float merchantRevenuePercentage) {
		this.merchantRevenuePercentage = merchantRevenuePercentage;
	}
	public Float getAffiliateRevenuePercentage() {
		return affiliateRevenuePercentage;
	}
	public void setAffiliateRevenuePercentage(Float affiliateRevenuePercentage) {
		this.affiliateRevenuePercentage = affiliateRevenuePercentage;
	}
	
	

}
