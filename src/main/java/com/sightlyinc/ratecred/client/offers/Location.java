package com.sightlyinc.ratecred.client.offers;

import org.codehaus.jackson.annotate.JsonProperty;

/*
 * 
{
            "comments": "",
            "address_2": null,
            "zip_code": "94107",
            "state": "CA",
            "lat": 37.781671,
            "city": "San Francisco",
            "lng": -122.387787,
            "address_1": "Pier 40"
        }
 */
public class Location {
	
	@JsonProperty(value = "comments")
	private String comments;

	@JsonProperty(value = "address_1")
	private String addressOne;
		
	@JsonProperty(value = "address_2")
	private String addressTwo;

	@JsonProperty(value = "city")
	private String city;
	
	@JsonProperty(value = "state")
	private String state;
	
	@JsonProperty(value = "zip_code")
	private String postalCode;

	@JsonProperty(value = "lat")
	private Double lat;
	
	@JsonProperty(value = "lng")
	private Double lng;
	
	@JsonProperty(value = "name")
	private String name;

	public String getComments() {
		return comments;
	}

	public String getAddressOne() {
		return addressOne;
	}

	public String getAddressTwo() {
		return addressTwo;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public Double getLat() {
		return lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}

	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
	

}
