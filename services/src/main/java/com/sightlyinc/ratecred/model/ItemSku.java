package com.sightlyinc.ratecred.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sightlyinc.ratecred.model.Product.ProductStatus;

@Entity
@Table(name="item_sku")
public class ItemSku extends BaseEntity {
	
	public enum ItemType { SUPPORT, ADD_ON, PLUGIN, THEME, SERVICE_LEVEL, SUBSCRIPTION, LICENSE }
	
	public enum ItemStatus { AVAILABLE, CANCELED, LIMITED, ON_ORDER }
	
	@Column(name = "sku_code")
	private String skuCode;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description",columnDefinition="TEXT")	
	private String description;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ItemStatus status;
	
	@Column(name = "is_active")
	private Boolean active;	

	@Column(name = "item_type")
	@Enumerated(EnumType.STRING)
	private ItemType type;
	
	@Column(name = "period")
	private Long period;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ItemStatus getStatus() {
		return status;
	}

	public void setStatus(ItemStatus status) {
		this.status = status;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}
	
	

}
