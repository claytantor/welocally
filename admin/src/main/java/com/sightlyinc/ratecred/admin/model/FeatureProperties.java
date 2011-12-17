package com.sightlyinc.ratecred.admin.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * "properties": {
        "province": "CA",
        "distance": 0.3748500422932057,
        "name": "Fox Theatre",
        "tags": [
            "theatre"
        ],
        "country": "US",
        "phone": "+1 510 835 0887",
        "href": "http://api.simplegeo.com/1.0/features/SG_5PbkiDEAgs9dxmOlRSH1Oy_37.808338_-122.269882@1303263346.json",
        "city": "Oakland",
        "address": "1912 Telegraph Ave",
        "owner": "simplegeo",
        "postcode": "94612",
        "classifiers": [
            {
                "category": "Arts & Performance",
                "type": "Entertainment",
                "subcategory": "Theater"
            }
        ]
    }
 * @author claygraham
 *
 */
@JsonIgnoreProperties(value= {"href", "owner", "classifiers", "tags", "distance", "country", "menulink"})
public class FeatureProperties {
	private String province;
	private String name;
	private String country;
	private String phone;
	private String href;
	private String city;
	private String address;
	private String postcode;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	

}
