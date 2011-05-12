package com.sightlyinc.ratecred.model;

import com.sightlyinc.ratecred.model.Place;

/**
 * <event>
   <name>Sun Prairie</name>
   <url>http://www.eastbayexpress.com/ebx/sun-prairie/Event?oid=2627434</url>
   <categories>Rock &amp; Pop</categories>
   <when>Thu., May 12, 9 p.m.</when>
   <phone>510-444-6174</phone>
   <locationName>Stork Club</locationName>
   <description>with Woods and One $5</description>
   <address>2330 Telegraph Ave., Oakland CA</address>
</event>

 * @author claygraham
 *
 */
public class Event {
	private String name;
	private String url;
	private String categories;
	private String when;
	private String phone;
	//private String locationName;
	private String description;
	//private String address;
	private Place place;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/*public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}*/
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/*public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}*/
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
	}
	
	
	
}
