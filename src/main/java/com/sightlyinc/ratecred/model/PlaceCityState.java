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
			StringBuffer s1 = new StringBuffer();
			if(((PlaceCityState)obj).getCity() != null)
				s1.append(((PlaceCityState)obj).getCity());
			if(((PlaceCityState)obj).getState() != null)
				s1.append(((PlaceCityState)obj).getState());
			
			StringBuffer s2 = new StringBuffer();
			if(this.city != null)
				s2.append(this.city);
			if(this.state != null)
				s2.append(this.state);

			return s1.toString().toLowerCase().equals(s2.toString().toLowerCase());

		}
		else return false;
	}

	@Override
	public String toString() {
		return "PlaceCityState [city=" + city + ", count=" + count + ", id="
				+ id + ", state=" + state + "]";
	}
	
	
	

}
