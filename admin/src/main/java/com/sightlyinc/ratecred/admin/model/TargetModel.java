package com.sightlyinc.ratecred.admin.model;

import java.util.List;

public class TargetModel {
	private String city;
	private String state;
	private Double lat;
	private Double lng;
	private List<String> keywords;
	private List<Integer> catgeories;
	
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
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public List<Integer> getCatgeories() {
		return catgeories;
	}
	public void setCatgeories(List<Integer> catgeories) {
		this.catgeories = catgeories;
	}
	
}
