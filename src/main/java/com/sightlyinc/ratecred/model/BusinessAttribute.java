package com.sightlyinc.ratecred.model;

public class BusinessAttribute {

	public BusinessAttribute() {
		super();
	}

	public BusinessAttribute(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	private Long id;
	private String name;
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

		buf.append("[id=" + this.id + " ");
		buf.append("name=" + this.name + " ");
		buf.append("value=" + this.value + "]");

		return buf.toString();
	}
}
