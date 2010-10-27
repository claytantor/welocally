package com.sightlyinc.ratecred.model;

public class PlaceCityState {
	private Long id;
	private String city;
	private String state;
	private Integer count;
	
	
	

	public PlaceCityState(String city, String state, Integer count) {
		super();
		this.city = city;
		this.state = state;
		this.count = count;
	}
	
	public PlaceCityState() {
		super();
	}
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
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

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PlaceCityState)
		{
			String s1 = ((PlaceCityState)obj).getCity()+((PlaceCityState)obj).getState();
			String s2 = this.city+this.state;
			return s1.equals(s2);

		}
		else return false;
	}

	@Override
	public String toString() {
		return "PlaceCityState [city=" + city + ", count=" + count + ", id="
				+ id + ", state=" + state + "]";
	}
	
	
	

}
