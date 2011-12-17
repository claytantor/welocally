package com.sightlyinc.ratecred.client.offers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.sightlyinc.ratecred.pojo.Location;

/*"advertiser": {
    "reviews": [
        {
            "source_title": "Facebook",
            "source_url": null,
            "text": "Cruise on Sunday 6/14 was a GREAT time...Thanks City Lights Cruises!!!"
        }
    ],
    "description": "<p>For the ultimate party on San Francisco Bay, look no further than City Lights Cruises and our incredible luxury yachts. Our three-hour party cruises are perfect when you feel like partying in style but without the stuffy atmosphere of a dinner cruise. Enjoy a premium bar, delicious appetizer buffet and the best DJ's in town. It all adds up San Francisco's best party on the water. NO JACKETS REQUIRED, just dress to impress and bring your dancing shoes!</p>\n\n<p>Our cruises truly offer something for everyone. Whether you're looking to celebrate a special occasion with a large group or you just want a romantic night out with that special someone, our cruises are sure to impress, and because over 90% of our guests are Bay Area residents, a great time and a great crowd is all but assured.</p>\n\n<p>Virtually all of our public cruises are on weekends, but we offer some weekday cruises on special occasions and holidays. Friday & Saturday nights are the most popular and offer an upscale nightclub vibe and incredible night skyline views, while our afternoon cruises are family-friendly and provide panoramic views of the entire bay, bridges, Alcatraz and more. Of course, we also offer Private Charters for groups of all sizes, which are available seven days a week and can embark from many Bay Area locations. Visit our Private Charters page for more details.</p>\n",
    "title": "City Lights Cruises - San Fran",
    "category_id": 1010289190,
    "logo": {
        "height": "200",
        "url": "http://subdomain-dev-49590.adilitydemo.com/business/logo/2001061515/logo_1297558864.jpg",
        "width": "150"
    },
    "contact_phone": null,
    "locations": [
        {
            "comments": "",
            "address_2": null,
            "zip_code": "94107",
            "state": "CA",
            "lat": 37.781671,
            "city": "San Francisco",
            "lng": -122.387787,
            "address_1": "Pier 40"
        },
        {
            "comments": "",
            "address_2": null,
            "zip_code": "94501",
            "state": "CA",
            "lat": 37.79057,
            "city": "Alameda",
            "lng": -122.276074,
            "address_1": "2400 Mariner Square Dr."
        }
    ],
    "site_url": "http://www.citylightscruises.com/default_sanfran.aspx",
    "id": 2001061515
},*/

public class Advertiser {
	
	@JsonProperty(value = "id")
	private String id;
	
	private String externalId;
	
	@JsonProperty(value = "category_id")
	private Long categoryId;

	@JsonProperty(value = "description")
	private String description;
	
	@JsonProperty(value = "contact_phone")
	private String contactPhone;

	@JsonProperty(value = "name")
	private String name;
	
	@JsonProperty(value = "site_url")
	private String siteUrl;
			
	@JsonProperty(value = "locations")
	private List<Location> locations = new ArrayList<Location>();
	
	@JsonProperty(value = "logo")
	private String advertiserLogoUrl;
			
	public String getId() {
		return id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public String getDescription() {
		return description;
	}



	public String getSiteUrl() {
		return siteUrl;
	}



	public List<Location> getLocations() {
		return locations;
	}


	public void setId(String id) {
		this.id = id;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getAdvertiserLogoUrl() {
		return advertiserLogoUrl;
	}

	public void setAdvertiserLogoUrl(String advertiserLogoUrl) {
		this.advertiserLogoUrl = advertiserLogoUrl;
	}


	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}


	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Advertiser [advertiserLogoUrl=" + advertiserLogoUrl
				+ ", categoryId=" + categoryId + ", contactPhone="
				+ contactPhone + ", description=" + description
				+ ", externalId=" + externalId + ", id=" + id + ", locations="
				+ locations + ", name=" + name + ", siteUrl=" + siteUrl + "]";
	}



	
	
}
