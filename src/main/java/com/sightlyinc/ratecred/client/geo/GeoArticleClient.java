package com.sightlyinc.ratecred.client.geo;

import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;

public interface GeoArticleClient {
	

    public Article makeArticleFromPlace(Place place, 
    		String articleName, 
    		String description, 
    		String url, 
    		Publisher pub);

}
