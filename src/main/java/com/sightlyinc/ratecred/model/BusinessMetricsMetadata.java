package com.sightlyinc.ratecred.model;
/**
 * non persistent class
 * 
 * @author claygraham
 *
 */
public class BusinessMetricsMetadata {

	private Integer maxRates;
	private Integer minRates;
	private Integer maxYays;
	private Integer minYays;
	private Integer maxBoos;
	private Integer minBoos;	
	
	public BusinessMetricsMetadata() {
	}

	public Integer getMaxRates() {
		return maxRates;
	}

	public void setMaxRates(Integer maxRates) {
		this.maxRates = maxRates;
	}

	public Integer getMinRates() {
		return minRates;
	}

	public void setMinRates(Integer minRates) {
		this.minRates = minRates;
	}

	public Integer getMaxYays() {
		return maxYays;
	}

	public void setMaxYays(Integer maxYays) {
		this.maxYays = maxYays;
	}

	public Integer getMinYays() {
		return minYays;
	}

	public void setMinYays(Integer minYays) {
		this.minYays = minYays;
	}

	public Integer getMaxBoos() {
		return maxBoos;
	}

	public void setMaxBoos(Integer maxBoos) {
		this.maxBoos = maxBoos;
	}

	public Integer getMinBoos() {
		return minBoos;
	}

	public void setMinBoos(Integer minBoos) {
		this.minBoos = minBoos;
	}

	
	
}
