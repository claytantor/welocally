package com.sightlyinc.ratecred.client.offers;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/*{
    "description": "You will receive eight complimentary Yacht Cruise tickets. Valid Friday, Saturday or Sunday. Restrictions may apply, $14.00 departure fee PLUS two beverage minimum per person are not included with ticket. Not valid on July 4th or New Year's Eve cruises. Additional charges may apply for Special Events. Taxes additional where applicable. Maximum value: $40.00/ticket.",
    "title": "Complimentary Yacht Cruise Ticket",
    "quantity": 8
}*/

public class Item {
	
	@JsonProperty(value = "description")
	private String description;

	@JsonProperty(value = "title")
	private String title;
		
	@JsonProperty(value = "quantity")
	private Integer quantity;

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
