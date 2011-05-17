package com.sightlyinc.ratecred.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adility.resources.model.OrderItem;

/**
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `version` INT(11) NULL DEFAULT NULL ,
  `offer_id` BIGINT(20) NULL DEFAULT NULL ,
  `cust_order_id` BIGINT(20) NULL DEFAULT NULL ,
  `order_item_id` BIGINT(20) NULL DEFAULT NULL ,
  `redemption_code` VARCHAR(255) NULL DEFAULT NULL ,
  `metadata` TEXT NULL DEFAULT NULL ,
  `notes` TEXT NULL DEFAULT NULL ,
  `image_url` VARCHAR(255) NULL DEFAULT NULL ,
  `status` VARCHAR(45) NULL DEFAULT NULL ,
  `time_expires` BIGINT(20) NULL DEFAULT NULL ,
  `time_aquired` BIGINT(20) NULL DEFAULT NULL ,
  `time_redeemed` BIGINT(20) NULL DEFAULT NULL ,
  `time_cancelled` BIGINT(20) NULL DEFAULT NULL ,
  `time_created` BIGINT(20) NULL DEFAULT NULL ,
  `time_updated` BIGINT(20) NULL DEFAULT NULL ,
 * @author claygraham
 *
 */
@Entity
@Table(name="voucher")
public class Voucher extends BaseEntity{
	
	
	private String redemptionCode;
	     
	private String metadata;
	private String notes;
	private String imageUrl;
	private String status;
	private String printUrl;
	private Long timeExpires;
	private Long timeAquired;
	private Long timeRedeemed;
	private Long timeCancelled;
	
	@ManyToOne
	@JoinColumn(name = "offer_id")
	private Offer offer;
	
	@ManyToOne
	@JoinColumn(name = "cust_order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "order_item_id")
	private OrderItem orderItem;
	

	public String getRedemptionCode() {
		return redemptionCode;
	}
	public void setRedemptionCode(String redemptionCode) {
		this.redemptionCode = redemptionCode;
	}
	
	public String getPrintUrl() {
		return printUrl;
	}
	public void setPrintUrl(String printUrl) {
		this.printUrl = printUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Long getTimeExpires() {
		return timeExpires;
	}
	public void setTimeExpires(Long timeExpires) {
		this.timeExpires = timeExpires;
	}
	public Long getTimeAquired() {
		return timeAquired;
	}
	public void setTimeAquired(Long timeAquired) {
		this.timeAquired = timeAquired;
	}
	public Long getTimeRedeemed() {
		return timeRedeemed;
	}
	public void setTimeRedeemed(Long timeRedeemed) {
		this.timeRedeemed = timeRedeemed;
	}
	public Long getTimeCancelled() {
		return timeCancelled;
	}
	public void setTimeCancelled(Long timeCancelled) {
		this.timeCancelled = timeCancelled;
	}
	public Offer getOffer() {
		return offer;
	}
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public OrderItem getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	
	

}
