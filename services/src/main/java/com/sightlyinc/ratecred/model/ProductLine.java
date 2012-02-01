package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sightlyinc.ratecred.model.ItemSku.ItemStatus;

@Entity
@Table(name="product_line")
public class ProductLine extends BaseEntity {
	
	public enum ProductLineStatus { AVAILABLE, CANCELED, LIMITED, ON_ORDER }
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ProductLineStatus status;
	
	@Column(name = "is_active")
	private Boolean active;	
	
	@ManyToOne
	@JoinColumn(name = "item_sku_id")
	@Enumerated(EnumType.STRING)
	private ItemSku itemSku;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}



	public ProductLineStatus getStatus() {
		return status;
	}

	public void setStatus(ProductLineStatus status) {
		this.status = status;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public ItemSku getItemSku() {
		return itemSku;
	}

	public void setItemSku(ItemSku itemSku) {
		this.itemSku = itemSku;
	}
	
	
}
