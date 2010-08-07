package com.sightlyinc.ratecred.model;

import java.util.ArrayList;
import java.util.List;

public class PlacePage {
	private int pageNumber;
	private int totalPages;
	private long totalResults;
	private int pageSize;
	private String sortField;
	private boolean isAscending;
	private String type;
	
	
	private List<Place> ratings = new ArrayList<Place>();
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public List<Place> getPlaces() {
		return ratings;
	}
	public void setPlaces(List<Place> ratings) {
		this.ratings = ratings;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public long getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public boolean isAscending() {
		return isAscending;
	}
	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
