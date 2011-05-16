package com.sightlyinc.ratecred.client.link;

import java.util.List;

public class Links {
	
	//private List<AffiliateLink> links;
	
	//total-matched="15" 
	private Integer totalMatched;
	
	//records-returned="10" 
	private Integer recordsReturned;
	
	//page-number="1"
	private Integer pageNumber;
	
	

	public Integer getTotalMatched() {
		return totalMatched;
	}

	public void setTotalMatched(Integer totalMatched) {
		this.totalMatched = totalMatched;
	}

	public Integer getRecordsReturned() {
		return recordsReturned;
	}

	public void setRecordsReturned(Integer recordsReturned) {
		this.recordsReturned = recordsReturned;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

/*	public List<AffiliateLink> getLinks() {
		return links;
	}

	public void setLinks(List<AffiliateLink> links) {
		this.links = links;
	}*/
	
}
