package com.sightlyinc.ratecred.client.offers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sightlyinc.ratecred.model.Location;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

/**
 * URL url = new URL("http", "feeds.pepperjamnetwork.com", 80, "/coupon/download/?affiliate_id=59161&program_ids=259-1687-1801-2110-2708-2803-3456-4750-4826-5173");			
 * @author claygraham
 *
 */
@Component("ratecredOfferClient")
public class RatecredOfferClient implements OfferClient {
	
	static Logger logger = 
		Logger.getLogger(RatecredOfferClient.class);
	
	private SimpleGeoStorageClient client;
	
	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	private String ratecredConsumerKey;
	
	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	private String ratecredConsumerSecret;
	
	@Value("${offers.layersNames}")
	private String offersLayerNames="com.ratecred.offer.b93acd058af5.restaurant.dev,com.ratecred.offer.b93acd058af5.shopping.dev";
	
	@Value("${offers.lat}")
	private String offersLat="37.804431";
	
	@Value("${offers.lon}")
	private String offersLon="-122.270751";
	
	@PostConstruct
	public void init() {
		client = SimpleGeoStorageClient.getInstance();	
		client.getHttpClient().setToken(ratecredConsumerKey, ratecredConsumerSecret);
	}
	


	@Override
	public List<Offer> getOffers() throws OfferFeedException {
		
		final List<Offer> offers = new ArrayList<Offer>();
		
		try {
			
			double lat = Double.parseDouble(offersLat);
			double lon = Double.parseDouble(offersLon);
			double radiusInKMeters = 20.0;
			
			String cursor = "";
			String[] layers = offersLayerNames.split(",");
			for (int i = 0; i < layers.length; i++) {
				FeatureCollection collection = 
					client.search(lat, lon, layers[i],radiusInKMeters, 100, cursor);
				for (Feature feature : collection.getFeatures()) {
					Offer o = transformOffer(feature);
					if(o != null)
						offers.add(o);
				}
			}
			
			
			
		} catch (IOException e) {
			throw new OfferFeedException(e);
		}
				
		
		return offers;
	}
	
	
/*
 * 
{
    "visible": "true",
    "state": "CA",
    "type": "deal",
    "externalId": "",
    "city": "Oakland",
    "id": "0",
    "programId": "5c045a9e377a",
    "discountType": "Deal",
    "description": "When you choose 3 designs you will recieve a free limited edition celebrity poodle necklace.",
    "name": "Three Designs and our Limited Edition Poodle Design for $39.99",
    "beginDateString": "2011-04-10",
    "value": "120.0",
    "quantity": null,
    "externalSource": "RATECRED",
    "programName": "Oakland Unwrapped",
    "expire": "Tue Apr 10 00:00:00 PDT 2012",
    "ends": "Tue Apr 10 00:00:00 PDT 2012",
    "offerLocation": {
        "addressTwo": null,
        "postalCode": "94611",
        "addressOne": "4096 Piedmont Ave.",
        "state": "CA",
        "lng": "-122.252121",
        "comments": null,
        "lat": "37.826625",
        "city": "Oakland"
    },
    "expireDateString": "2012-04-10",
    "extraDetails": "Not valid for any of our designs that include gold.",
    "illustrationUrl": "http://www.celebritypoodle.com/images/jewelry/45rpm_blue_thumb.jpg",
    "begin": "Sun Apr 10 00:00:00 PDT 2011",
    "price": "39.99",
    "checkinsRequired": null,
    "items": "[]",
    "advertiser": {
        "id": null,
        "siteUrl": null,
        "contactPhone": null,
        "locations": [
            {
                "addressTwo": null,
                "postalCode": "94611",
                "addressOne": "4096 Piedmont Ave.",
                "state": "CA",
                "lng": "-122.252121",
                "comments": null,
                "lat": "37.826625",
                "city": "Oakland"
            }
        ],
        "description": "We specialize in fabulous jewelry (for fabulous humans) with a focus on the Glam Rock scene & a bit of the Hip Hop world.",
        "categoryId": null,
        "name": "Celebrity Poodle",
        "advertiserLogoUrl": null,
        "externalId": null
    },
    "endDateString": "2012-04-10"
}
	
	
 */
	private Offer transformOffer(Feature f) {
		try {
			JSONObject offerObject = (JSONObject)f.getProperties().get("offer");
			Offer o = new Offer();
			o.setBeginDateString(offerObject.getString("beginDateString"));
			o.setCity(offerObject.getString("city"));
			o.setDescription(offerObject.getString("description"));
			o.setDiscountType(offerObject.getString("discountType"));
			o.setValue(new Float(offerObject.getDouble("value")));
			o.setPrice(new Float(offerObject.getDouble("price")));
			o.setEndDateString(offerObject.getString("endDateString"));
			o.setExpireDateString(offerObject.getString("expireDateString"));
			o.setExternalId(offerObject.getString("externalId"));
			o.setExternalSource(offerObject.getString("externalSource"));
			o.setExtraDetails(offerObject.getString("extraDetails"));
			o.setIllustrationUrl(offerObject.getString("illustrationUrl"));
			o.setName(offerObject.getString("name"));
			o.setProgramId(offerObject.getString("programId"));
			o.setProgramName(offerObject.getString("programName"));
			o.setState(offerObject.getString("state"));
			o.setType(offerObject.getString("type"));
			
			JSONObject jOfferLocation = offerObject.getJSONObject("offerLocation");
			Location offerLocation = new Location();
			offerLocation.setAddressOne(jOfferLocation.getString("addressOne"));
			offerLocation.setCity(jOfferLocation.getString("city"));
			offerLocation.setState(jOfferLocation.getString("state"));
			offerLocation.setPostalCode(jOfferLocation.getString("postalCode"));
			offerLocation.setLat(jOfferLocation.getDouble("lat"));
			offerLocation.setLng(jOfferLocation.getDouble("lng"));			
			o.setOfferLocation(offerLocation);
			
			JSONObject jOfferAdvertiser = offerObject.getJSONObject("advertiser");
			Advertiser offerAdvertiser = new Advertiser();
			offerAdvertiser.getLocations().add(offerLocation);
			offerAdvertiser.setName(jOfferAdvertiser.getString("name"));
			offerAdvertiser.setDescription(jOfferAdvertiser.getString("description"));
			o.setAdvertiser(offerAdvertiser);
			
			return o;
			
		} catch (JSONException e) {
			logger.error("problem deserializing feature");
		}
		return null;
	}



}
