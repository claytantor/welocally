package com.sightlyinc.ratecred.client.link;

import java.util.List;

/*
 * 
<advertiser-id>2991426</advertiser-id>
<advertiser-name>BuyWithMe</advertiser-name>
<category>discounts</category>
<click-commission>0.0</click-commission>
<language>en</language>
<lead-commission>USD 2.00</lead-commission>
<link-code-html>&lt;a
	href="http://www.dpbolvw.net/click-4127816-10794209"&gt;&lt;b&gt;Shop
	BuyWithMe.com&lt;/b&gt; for a Philadelphia deal at J Matthew Salon -
	Men’s Cut for $25 ($50 value) or Women’s Cut &amp; Style for
	$35&lt;/a&gt;&lt;img
	src="http://www.tqlkg.com/image-4127816-10794209" width="1"
	height="1" border="0"/&gt;</link-code-html>
<link-code-javascript>&lt;form name="CJ10794209X1" method="POST"
	style="margin:0px;display:inline"
	action="http://www.jdoqocy.com/click"&gt;
	&lt;input type="hidden" name="aid" value="10794209"/&gt;
	&lt;input type="hidden" name="pid" value="4127816"/&gt;

	&lt;a href="javascript:CJ10794209X1.submit();"&gt;&lt;b&gt;Shop
	BuyWithMe.com&lt;/b&gt; for a Philadelphia deal at J Matthew Salon -
	Men’s Cut for $25 ($50 value) or Women’s Cut &amp; Style for
	$35&lt;/a&gt;&lt;/form&gt;&lt;img
	src="http://www.tqlkg.com/image-4127816-10794209" width="1"
	height="1" border="0"/&gt;</link-code-javascript>
<description>Shop BuyWithMe.com and enjoy a Men’s Haircut for $25
	($50 value) OR $35 for a Women’s Haircut and Style ($70 value)
</description>
<destination>http://www.buywithme.com/philadelphia/deals/964-jason-matthew-salon
</destination>
<link-id>10794209</link-id>
<link-name>PHL - 7/16 J Matthew Salon</link-name>
<link-type>Text Link</link-type>
<performance-incentive>true</performance-incentive>
<promotion-end-date></promotion-end-date>
<promotion-start-date></promotion-start-date>
<promotion-type></promotion-type>
<relationship-status>joined</relationship-status>
<sale-commission>8.00%</sale-commission>
<seven-day-epc>N/A</seven-day-epc>
<three-month-epc>N/A</three-month-epc>	*/

public class Link {
	private String advertiserId;
	private String advertiserName;
	private List<String> category;
	private String linkCodeHtml;
	private String description;
	private String destination;
	private String linkId;
	private String linkName;
	private String startDateString;
	private String endDateString;
	public String getAdvertiserId() {
		return advertiserId;
	}
	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}
	public String getAdvertiserName() {
		return advertiserName;
	}
	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}


	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public String getLinkCodeHtml() {
		return linkCodeHtml;
	}
	public void setLinkCodeHtml(String linkCodeHtml) {
		this.linkCodeHtml = linkCodeHtml;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getStartDateString() {
		return startDateString;
	}
	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}
	public String getEndDateString() {
		return endDateString;
	}
	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}
	@Override
	public String toString() {
		return "Link [advertiserId=" + advertiserId + ", advertiserName="
				+ advertiserName + ", category=" + category + ", description="
				+ description + ", destination=" + destination
				+ ", endDateString=" + endDateString + ", linkCodeHtml="
				+ linkCodeHtml + ", linkId=" + linkId + ", linkName="
				+ linkName + ", startDateString=" + startDateString + "]";
	}
	
	

}
