package com.sightlyinc.ratecred.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="cust_order_line")
public class OrderLine extends BaseEntity {
	
	public enum OrderLineStatus { AVAILABLE, CANCELED, LIMITED, ON_ORDER }
	
	@Column(name="qty_orig")
	private Integer quantityOrig;
	
	@Column(name="qty_used")
	private Integer quantityUsed;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private OrderLineStatus status;

	@Column(name="start_time")
	private Long startTime;

	@Column(name="end_time")
	private Long endTime;
	
	@ManyToOne	
	@JoinColumn(name = "item_sku_id")
	private ItemSku itemSku;
	
	@ManyToOne
	@JoinColumn(name = "cust_order_id")
	private Order order;

	public Integer getQuantityOrig() {
		return quantityOrig;
	}

	public void setQuantityOrig(Integer quantityOrig) {
		this.quantityOrig = quantityOrig;
	}

	public Integer getQuantityUsed() {
		return quantityUsed;
	}

	public void setQuantityUsed(Integer quantityUsed) {
		this.quantityUsed = quantityUsed;
	}



	public OrderLineStatus getStatus() {
		return status;
	}

	public void setStatus(OrderLineStatus status) {
		this.status = status;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public ItemSku getItemSku() {
		return itemSku;
	}

	public void setItemSku(ItemSku itemSku) {
		this.itemSku = itemSku;
	}
	
	@Transient
	public BigDecimal getLineTotal(){		
		return itemSku.getPrice().multiply(new BigDecimal(this.getQuantityOrig()));
	}

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
	
	

}
