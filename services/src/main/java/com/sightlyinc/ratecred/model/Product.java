package com.sightlyinc.ratecred.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/*
 * acts as a template for filling orders
 */
@Entity
@Table(name="product")
public class Product extends BaseEntity {
	
	public enum ProductStatus { AVAILABLE, CANCELED, LIMITED, ON_ORDER }
	
	@Column(name = "is_active")
	private Boolean active;	
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
	
	@Column(name = "product_sku")
	private String productSku;
	
	@Column(name = "production_payment_code")
	private String productionPaymentCode;
	
	@Column(name = "sandbox_payment_code")
	private String sandboxPaymentCode;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description",columnDefinition="TEXT")
	private String description;
	
	@Column(name = "notes",columnDefinition="TEXT")	
	private String notes;
	
	@Column(name = "roles")    
    private String roles;
	
	@OneToMany
	@JoinColumn(name = "product_id")	
	private Set<ProductLine> productItems;

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public String getProductionPaymentCode() {
		return productionPaymentCode;
	}

	public void setProductionPaymentCode(String productionPaymentCode) {
		this.productionPaymentCode = productionPaymentCode;
	}

	public String getSandboxPaymentCode() {
		return sandboxPaymentCode;
	}

	public void setSandboxPaymentCode(String sandboxPaymentCode) {
		this.sandboxPaymentCode = sandboxPaymentCode;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Set<ProductLine> getProductItems() {
		return productItems;
	}

	public void setProductItems(Set<ProductLine> productItems) {
		this.productItems = productItems;
	}

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

	
}
