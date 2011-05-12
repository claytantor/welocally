package com.sightlyinc.ratecred.client.geo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.model.Place;
import com.simplegeo.client.SimpleGeoPlacesClient;
import com.simplegeo.client.callbacks.SimpleGeoCallback;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

// @TODO refactor name
@Component("locationPlacesClient")
public class SimpleGeoLocationClient implements GeoPlacesClient {
	
	static Logger logger = 
		Logger.getLogger(SimpleGeoLocationClient.class);
	
	
	private SimpleGeoPlacesClient client;

	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	private String ratecredConsumerKey;

	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	private String ratecredConsumerSecret;


    @PostConstruct
    public void init() {
		client = SimpleGeoPlacesClient.getInstance();
		client.getHttpClient().setToken(ratecredConsumerKey, ratecredConsumerSecret);
	}

	
	
	@Override
	public List<Place> findPlaces(double lat, double lon, double radiusInKMeters) {
		return findPlacesSynchronous(lat, lon, radiusInKMeters);
	}

    @Override
    public Place findById(String id) {
        return findByIdSynchronous(id);
    }

    public Place findByIdSynchronous(String id) {
        Place place = null;
        try {
            place = trasformFeature(client.getPlace(id));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("problem getting place by id", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return place;
    }

    public Place findByIdAsynchronous(String id) {
        final List<Place> places = new ArrayList<Place>(1);
        try {
            client.getPlace(id, new SimpleGeoCallback<Feature>() {
                @Override
                public void onSuccess(Feature feature) {
                    logger.debug(feature.getProperties());
                    places.add(trasformFeature(feature));
                }

                @Override
                public void onError(String errorMessage) {
                    System.out.println(errorMessage);
                }
            });
        } catch (IOException e) {
            logger.error("problem getting place by id", e);
        }
        return (places.isEmpty() ? null : places.get(0));
    }

	
	protected List<Place> findPlacesSynchronous(double lat, double lon, double radiusInKMeters) {		
		
		final List<Place> places = new ArrayList<Place>();		
		
		try {
			FeatureCollection afeatures = client.search(lat, lon, "", "", radiusInKMeters);
			for (Feature feature : afeatures.getFeatures()) {
				
				//logger.debug("found feature:"+feature.getProperties().get("name"));
				logger.debug(feature.getProperties());								
				places.add(trasformFeature(feature));
			}
			
			
		} catch (IOException e) {
			logger.error("problem getting places", e);
		}
		
		return places;
	}
	
	/**
{
    tags=[
        "periodical"
    ],
    distance=0.010740107345578108,
    phone=+1 415 824 8906,
    classifiers=[
        {
            "category": "Manufacturing",
            "subcategory": "Printing",
            "type": "Manufacturing & Wholesale Goods"
        }
    ],
    address=2463 Van Ness Ave,
    name=Windows User,
    province=CA,
    owner=simplegeo,
    postcode=94109,
    href=http://api.simplegeo.com/1.0/features/SG_6mDvPGAuTouPfra6hBaLWa_37.797927_-122.424059@1294081369.json,
    country=US,
    city=San Francisco
}

	 * @param f
	 * @return
	 */
	private Place trasformFeature(Feature f){
		
		logger.debug(f.getProperties().get("classifiers").getClass().getName());
		Place p = new Place();
		p.setSimpleGeoId(f.getSimpleGeoId());
		p.setName(f.getProperties().get("name").toString());
		p.setAddress(f.getProperties().get("address").toString());
		p.setLatitude(f.getGeometry().getPoint().getLat());
		p.setLongitude(f.getGeometry().getPoint().getLon());
		if(f.getProperties().get("city") != null)
			p.setCity(f.getProperties().get("city").toString());
		if(f.getProperties().get("province") != null)
			p.setState(f.getProperties().get("province").toString());
		if(f.getProperties().get("phone") != null)
			p.setPhone(f.getProperties().get("phone").toString());
		if(f.getProperties().get("website") != null && f.getProperties().get("website").toString().startsWith("http"))
			p.setWebsite(f.getProperties().get("website").toString().toLowerCase());
		else if(f.getProperties().get("website") != null)
			p.setWebsite("http://"+f.getProperties().get("website").toString().toLowerCase());
		
		//use first cat
		JSONArray classifiers = (JSONArray)f.getProperties().get("classifiers");
		try {
			if(classifiers != null && !classifiers.isNull(0)) {
				JSONObject first = classifiers.getJSONObject(0);
				p.setCategory(first.getString("category"));
				p.setSubcategory(first.getString("subcategory"));
				p.setCategoryType(first.getString("type"));
			}
		} catch (JSONException e) {
			logger.debug(e.getMessage());
		}
		
		return p;
	}
	
	
}
