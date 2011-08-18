package com.sightlyinc.ratecred.admin.model.wordpress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.mvc.controller.PlaceController;
import com.sightlyinc.ratecred.client.geo.SimpleGeoEventClient;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.noi.utility.date.DateUtils;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.geocoding.Geocoder;
import com.sightlyinc.ratecred.admin.geocoding.GeocoderException;
import com.sightlyinc.ratecred.client.geo.GeoEventClient;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.pojo.Location;
import com.sightlyinc.ratecred.service.DefaultPatronAwardsService;
import com.sightlyinc.ratecred.service.EventService;
import com.simplegeo.client.SimpleGeoPlacesClient;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

@Service("jsonModelProcessor")
public class WordpressJsonModelProcessor implements JsonModelProcessor {

    static Logger logger = Logger.getLogger(DefaultPatronAwardsService.class);

	@Autowired
	private EventService eventService;
	
	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	public String sgoauthkey;
	
	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	public String sgoauthsecret;
	
	private SimpleGeoPlacesClient client;

	public Integer radiusInKMeters = 1;
	
	@Autowired
	@Qualifier("yahooGeocoder")
	public Geocoder geocoder;	
	

	@Autowired
	@Qualifier("geoEventClient")
	private GeoEventClient geoEventClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlaceManagerService placeManagerService;

    @PostConstruct
    public void init() {
		client = SimpleGeoPlacesClient.getInstance();
		client.getHttpClient().setToken(sgoauthkey, sgoauthsecret);
	}

	@Override
	public boolean isArticlePost(JSONObject jsonPost) {
		try {
			JSONObject custom = jsonPost.getJSONObject("custom_fields");
			if(!custom.isNull("_isWLPlace") && custom.isNull("_isEvent")){
				return custom.getString("_isWLPlace").equals("true");
			}		
			
		} catch (JSONException e) {
			logger.error("JSONException", e);
		}
		return false;
	}

	@Override
	public boolean isEventPost(JSONObject jsonPost) {
		try {
			JSONObject custom = jsonPost.getJSONObject("custom_fields");
			if(!custom.isNull("_isWLPlace") && !custom.isNull("_isEvent")){
				return (custom.getString("_isWLPlace").equals("true") &&
						custom.getString("_isEvent").equals("yes"));
			}		
			
		} catch (JSONException e) {
			logger.error("JSONException", e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.admin.model.wordpress.JsonModelProcessor#saveJsonEventsAsPostsForPublisher(org.json.JSONObject, com.sightlyinc.ratecred.model.Publisher)
	 */
	public List<Event> saveJsonEventsAsPostsForPublisher(
			JSONObject obj, Publisher p) {

		List<Event> events = new ArrayList<Event>();

		try {
			
			JSONArray posts = obj.getJSONArray("posts");
			for (int i = 0; i < posts.length(); i++) {
				events.add(saveEventFromPost(posts.getJSONObject(i),p));
			}

		} catch (JSONException e) {
			logger.error("JSONException", e);
		}

		return events;
	}

    /**
     * Saves Event and Place instances based on the provided JSONObject. This object should
     * be created from the JSON posted to our servers from an instance of our Wordpress
     * plug-in, and should contain event and place info. The place info will be stored in
     * a SimpleGeo feature JSON object in a custom field.
     *
     * @param jsonObject
     * @param publisher
     */
    @Override
    public void saveEventAndPlaceFromPostJson(JSONObject jsonPost, Publisher publisher, String status) {
        try {
        	JSONObject custom = jsonPost.getJSONObject("custom_fields");
			if (!custom.isNull("_isEvent")) {
			
				String name = jsonPost.getString("title_plain");
				String description = jsonPost.getString("excerpt");
				String url = jsonPost.getString("url");
				
				
				Date ts = DateUtils.stringToDate(custom.getJSONArray(
						"_EventStartDate").getString(0), "yyyy-MM-dd HH:mm:ss");
				Date te = DateUtils.stringToDate(custom.getJSONArray(
						"_EventEndDate").getString(0), "yyyy-MM-dd HH:mm:ss");

				Place place = savePlaceFromPost(jsonPost);
				
				if (place != null && ts.after(Calendar.getInstance().getTime())) {
					Event e = geoEventClient.makeEventFromPlace(place, name,
							description, url, ts.getTime(), te.getTime(),
							publisher);
					eventService.save(e);
				}
			}


        } catch (JSONException ex) {
            logger.error("JSONException", ex);
        } 
    }
    
    private Place savePlaceFromPost(JSONObject jsonPost) {
    	Place place = null;
    	try {
	    	JSONObject custom = jsonPost.getJSONObject("custom_fields");
	
			
	
			if (!custom.isNull("_isWLPlace")) {
				String featureJson = custom
						.getString("_PlaceSelected")
						.substring(2,
								custom.getString("_PlaceSelected").length() - 2)
						.replaceAll("\\x5c\\x22", "\"");
	
				logger.debug(featureJson);
				com.sightlyinc.ratecred.admin.model.Feature feature = objectMapper
						.readValue(
								featureJson,
								com.sightlyinc.ratecred.admin.model.Feature.class);
	
				try {
					place = placeManagerService.findBySimpleGeoId(feature
							.getId());
					if (place == null) {
						place = new Place();
					}
					PlaceController.transformFeature(feature, place);
					placeManagerService.savePlace(place);
				} catch (BLServiceException e) {
					logger.error("error finding or saving place", e);
					throw new RuntimeException(e);
				}
			}
    	} catch (JSONException ex) {
			logger.error("JSONException", ex);
		} catch (IOException ex) {
			logger.error("IOException", ex);
		}
		return place;
    }
    
    @Override
	public void saveArticleAndPlaceFromPostJson(JSONObject jsonPost,
			Publisher publisher, String status) {
    	
		try {
	
			JSONObject custom = jsonPost.getJSONObject("custom_fields");
			
			String name = jsonPost.getString("title_plain");
			String description = jsonPost.getString("excerpt");
			String url = jsonPost.getString("url");
			
			
			Date ts = DateUtils.stringToDate(custom.getJSONArray(
					"_EventStartDate").getString(0), "yyyy-MM-dd HH:mm:ss");
			Date te = DateUtils.stringToDate(custom.getJSONArray(
					"_EventEndDate").getString(0), "yyyy-MM-dd HH:mm:ss");

			Place place = savePlaceFromPost(jsonPost);
			
			if (place != null && ts.after(Calendar.getInstance().getTime())) {
				Event e = geoEventClient.makeEventFromPlace(place, name,
						description, url, ts.getTime(), te.getTime(),
						publisher);
				eventService.save(e);
			}

		} catch (JSONException ex) {
			logger.error("JSONException", ex);
		} 

		
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	/**
{
            "id": 300,
            "type": "post",
            "slug": "the-muffs",
            "url": "http://oaklandly.com/wordpress/?p=300",
            "status": "publish",
            "title": "The Muffs",
            "title_plain": "The Muffs",
            "content": "<p>with TurbonegrA, The Bruises, and Talky Tina $12, $15</p>\n",
            "excerpt": "with TurbonegrA, The Bruises, and Talky Tina $12, $15",
            "date": "2011-07-31 12:52:21",
            "modified": "2011-07-31 12:52:21",
            "categories": [
                {
                    "id": 41,
                    "slug": "events",
                    "title": "Events",
                    "description": "",
                    "parent": 0,
                    "post_count": 62
                }
            ],
            "tags": [],
            "author": {
                "id": 2,
                "slug": "clay",
                "name": "clay",
                "first_name": "Clay",
                "last_name": "Graham",
                "nickname": "clay",
                "url": "http://oaklandly.com/",
                "description": ""
            },
            "comments": [],
            "attachments": [],
            "comment_count": 0,
            "comment_status": "open",
            "custom_fields": {
                "_EventVenue": [
                    "The Uptown",
                    "The Uptown"
                ],
                "_EventStartDate": [
                    "2011-07-30 21:00:00",
                    "2011-07-30 21:00:00"
                ],
                "_EventEndDate": [
                    "2011-07-30 22:00:00",
                    "2011-07-30 22:00:00"
                ],
                "_EventAddress": [
                    "1928 Telegraph Ave., Oakland CA",
                    "1928 Telegraph Ave., Oakland CA"
                ],
                "_EventCity": [
                    "Oakland",
                    "Oakland"
                ],
                "_EventState": [
                    "CA",
                    "CA"
                ],
                "_EventZip": [
                    "",
                    ""
                ],
                "_EventCost": [
                    "",
                    ""
                ],
                "_EventPhone": [
                    "510-451-8100",
                    "510-451-8100"
                ]
            }
        }	 
	 * 
	 * @param obj
	 * @return
	 */
	public Event saveEventFromPost(JSONObject obj, Publisher p) {
		Event e = new Event();

		try {
			logger.debug(obj.toString());
			JSONObject custom = obj.getJSONObject("custom_fields");
			
			String name = obj.getString("title_plain");
			String status = "ACTIVE";
			String url = obj.getString("url");
			
			String phone = null;
			if(!custom.isNull("_EventPhone"))
				phone = custom.getJSONArray("_EventPhone").getString(0).toString();
			String placeName = custom.getJSONArray("_EventVenue").getString(0).toString();
			String description = obj.getString("excerpt");
						
			String city = "Oakland";
			if(!StringUtils.isEmpty(custom.getJSONArray("_EventCity").getString(0).toString()))
				city = custom.getJSONArray("_EventCity").getString(0).toString();			
			
			String state = "CA";
			if(!StringUtils.isEmpty(custom.getJSONArray("_EventState").getString(0).toString()))
				state = custom.getJSONArray("_EventState").getString(0).toString();			
			
			String address = custom.getJSONArray("_EventAddress").getString(0).toString()
			.replaceAll(city, "").replace(state, "").replace("\\,","");
			
			Date ts = DateUtils.stringToDate(custom.getJSONArray("_EventStartDate").getString(0).toString(),
					"yyyy-MM-dd HH:mm:ss");
			Date te = DateUtils.stringToDate(custom.getJSONArray("_EventEndDate").getString(0).toString(),
					"yyyy-MM-dd HH:mm:ss");
			
			Location loc = null;
			int radius = this.radiusInKMeters;
			try {
				loc = geocoder.geocode(address+" "+city+" "+state);
			} catch (GeocoderException e1) {
				loc = new Location();
				loc.setLat(37.780068);
				loc.setLng(-122.220047);
				radius = 10;
			}
			
			if(ts.after(Calendar.getInstance().getTime())) {
				saveEvent(p, name, status, url, phone, placeName, 
						description, address, city, state, ts, te, loc.getLat(), loc.getLng(),radius);
			}


		} catch (JSONException ex) {
			logger.error("JSONException", ex);
		} catch (IOException ex) {
			logger.error("IOException", ex);
		}

		return e;
	}
	
	public Event saveEvent(Publisher p,
			String name,
			String status,
			String url,
			String phone,
			String placeName,
			String description,
			String address ,
			String city ,
			String state,
			Date ts,
			Date te,
			Double lat,
			Double lon,
			Integer searchRadius
			) throws IOException {

		if (status.equalsIgnoreCase("ACTIVE") && p != null) {

			FeatureCollection collection = client.search(lat.doubleValue(), lon.doubleValue(),
					placeName.trim(), "", searchRadius);

			ArrayList<Feature> features = collection.getFeatures();
			// if (features.size() == 1) {
			for (Feature feature : features) {

				String fname = (String) feature.getProperties().get("name");
				logger.debug("found:" + fname);

				// this is lame, should probably use distance or something
				// better
				if (fname.equals(placeName)
						&& feature.getProperties().get("city").toString()
								.contains(city)) {

					Event e = geoEventClient.makeEventFromFeature(feature,
							name, status, url, phone, placeName,
							description, address, city, state,
							ts.getTime(), te.getTime(), p);

					eventService.save(e);
					return e;
				}

			}

		}

		return null;
	}

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPlaceManagerService(PlaceManagerService placeManagerService) {
        this.placeManagerService = placeManagerService;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public void setSgoauthkey(String sgoauthkey) {
        this.sgoauthkey = sgoauthkey;
    }

    public void setSgoauthsecret(String sgoauthsecret) {
        this.sgoauthsecret = sgoauthsecret;
    }

    public void setGeoEventClient(SimpleGeoEventClient geoEventClient) {
        this.geoEventClient = geoEventClient;
    }
}