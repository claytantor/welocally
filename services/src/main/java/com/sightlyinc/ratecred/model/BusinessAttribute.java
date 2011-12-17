package com.sightlyinc.ratecred.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="business_attribute")
public class BusinessAttribute extends BaseEntity{

	public BusinessAttribute() {
		super();
	}

	public BusinessAttribute(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	private String name;
	
	@Column(name="attribute_value", columnDefinition="TEXT")
	private String value;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("[id=" + super.getId() + " ");
		buf.append("name=" + this.name + " ");
		buf.append("value=" + this.value + "]");

		return buf.toString();
	}
}
