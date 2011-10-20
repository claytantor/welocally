package com.sightlyinc.ratecred.client.geo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.simplegeo.client.types.Geometry;
import com.simplegeo.client.types.Point;
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
public class SimpleGeoLocationClient implements GeoPlacesClient,SimpleGeoPlaceManager {
	
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
	public List<Place> findPlacesByLocation(double lat, double lon, double radiusInKMeters) {
		return findPlacesSynchronous(lat, lon, radiusInKMeters);
	}
	

    @Override
	public List<Place> findPlacesByQuery(String address, String query, String category, double radiusInKMeters) {
    	final List<Place> places = new ArrayList<Place>();		
		
		try {
			FeatureCollection afeatures = client.searchByAddress(address, query, category, radiusInKMeters);
			for (Feature feature : afeatures.getFeatures()) {
				
				logger.debug(feature.getProperties());		
				Place p = new Place();
				transformFeature(feature,p);
				p.setType("SG");
				places.add(p);
			}
			
			
		} catch (IOException e) {
			logger.error("problem getting places", e);
		}
		
		return places;
	}

	@Override
    public Place findById(String id) {
        return findByIdSynchronous(id);
    }

    public Place findByIdSynchronous(String id) {
        Place place = null;
        try {
			place = new Place();
			transformFeature(client.getPlace(id),place);
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
                    Place p = new Place();
                    transformFeature(feature,p);
                    places.add(p);
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
				Place p = new Place();
				transformFeature(feature,p);
				places.add(p);
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
	public void transformFeature(Feature f, Place p){
		
		logger.debug(f.getProperties().get("classifiers").getClass().getName());
		//Place p = new Place();
		p.setSimpleGeoId(f.getSimpleGeoId());
		p.setName(f.getProperties().get("name").toString());
		p.setAddress(f.getProperties().get("address").toString());
		p.setLatitude(f.getGeometry().getPoint().getLat());
		p.setLongitude(f.getGeometry().getPoint().getLon());
		if(f.getProperties().get("city") != null)
			p.setCity(f.getProperties().get("city").toString());
		if(f.getProperties().get("province") != null)
			p.setState(f.getProperties().get("province").toString());
		if(f.getProperties().get("postcode") != null)
			p.setPostalCode(f.getProperties().get("postcode").toString());
		
		if(f.getProperties().get("phone") != null)
			p.setPhone(f.getProperties().get("phone").toString());
		if(f.getProperties().get("url") != null && f.getProperties().get("website").toString().startsWith("http"))
			p.setWebsite(f.getProperties().get("website").toString().toLowerCase());
		else if(f.getProperties().get("website") != null)
			p.setWebsite("http://"+f.getProperties().get("website").toString().toLowerCase());
		else if(f.getProperties().get("menulink") != null && f.getProperties().get("website") == null)
			p.setWebsite(f.getProperties().get("menulink").toString().toLowerCase());
		
		//use first cat
		JSONArray classifiers = (JSONArray)f.getProperties().get("classifiers");
		try {
			if(classifiers != null && !classifiers.isNull(0)) {
				JSONObject first = classifiers.getJSONObject(0);
				p.getCategories().add(first.getString("category"));
			}
		} catch (JSONException e) {
			logger.debug(e.getMessage());
		}
		
		//return p;
	}
	
	public Map<String, Object> addPlace(Place place) {
        Feature feature = transformPlace(place);
        Map<String, Object> result = null;
        try {
            result = client.addPlace(feature);
            result.put("feature", feature);
        } catch (IOException e) {
            logger.error("cannot add place",e);
        } catch (JSONException e) {
        	logger.error("cannot add place",e);
        }
        return result;
    }

    private Feature transformPlace(Place place) {
        Feature feature = new Feature();

        Geometry geometry = new Geometry(new Point(place.getLatitude(), place.getLongitude()));
        feature.setGeometry(geometry);

        // TODO is it necessary to set this? is this the correct value?
        feature.setType("Feature");

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("address", place.getAddress());
        properties.put("city", place.getCity());
        properties.put("province", place.getState());
        properties.put("postcode", place.getPostalCode());
        properties.put("name", place.getName());
        properties.put("phone", place.getPhone());
        properties.put("website", place.getWebsite());
        properties.put("private", false);
        feature.setProperties(properties);

        return feature;
    }



	public void setRatecredConsumerKey(String ratecredConsumerKey) {
		this.ratecredConsumerKey = ratecredConsumerKey;
	}



	public void setRatecredConsumerSecret(String ratecredConsumerSecret) {
		this.ratecredConsumerSecret = ratecredConsumerSecret;
	}
    
    
}
