package com.sightlyinc.ratecred.admin.velocity;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import com.noi.utility.web.UrlUtils;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.Offer;

public class AwardTool {
	
	private String imagePrefix;
	
	public AwardTool(String imagePrefix) {
		super();
		this.imagePrefix = imagePrefix;
	}
	
	public Offer getFirstOffer(Set<Offer> offers) {
		
		return (Offer)offers.toArray()[0];
		
	}

	public String makeImageUrl(Award a)
	{
		try {
			URI uri = new URI(imagePrefix+"?"+a.getMetadata());
			Map<String,String> map = 
				UrlUtils.convert(UrlUtils.parse(uri, "UTF-8"));
			
			return imagePrefix+map.get("imageUrl");
			
		} catch (Exception e) {
		}
		return "";
	}

}
