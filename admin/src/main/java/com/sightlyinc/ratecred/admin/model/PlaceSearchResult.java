package com.sightlyinc.ratecred.admin.model;

import java.util.List;

import com.sightlyinc.ratecred.model.Place;

public class PlaceSearchResult {
	
	private String id;
	private String query;
	private String address;
	private String city;
	private String state;
	private String postalCode;
	private String website;
	private String phone;
		
	private String category;
	private Long radius;
	
	private List<Place> results;
	
	
	public PlaceSearchResult(String id, String query, String address,
			String city, String state, String postalCode, String website,
			String phone, String category, Long radius, List<Place> results) {
		super();
		this.id = id;
		this.query = query;
		this.address = address;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.website = website;
		this.phone = phone;
		this.category = category;
		this.radius = radius;
		this.results = results;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getRadius() {
		return radius;
	}

	public void setRadius(Long radius) {
		this.radius = radius;
	}

	public List<Place> getResults() {
		return results;
	}

	public void setResult(List<Place> results) {
		this.results = results;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setResults(List<Place> results) {
		this.results = results;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	

}
