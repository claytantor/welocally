package com.sightlyinc.ratecred.admin.model.wordpress;

import com.noi.utility.io.InputOutputUtils;
import com.sightlyinc.ratecred.admin.geocoding.YahooGeocoder;
import com.sightlyinc.ratecred.client.geo.SimpleGeoEventClient;
import com.sightlyinc.ratecred.client.geo.SimpleGeoLocationClient;
import com.sightlyinc.ratecred.component.JacksonObjectMapper;
import com.sightlyinc.ratecred.dao.EventDao;
import com.sightlyinc.ratecred.dao.PlaceDao;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.EventService;
import com.sightlyinc.ratecred.service.EventServiceImpl;
import com.sightlyinc.ratecred.service.PlaceManagerServiceImpl;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * @author sam
 * @version $Id$
 */
public class TestWordpressJsonModelProcessor {
    
    private static final String REQUEST_JSON_LOCAL = "{ " +
            " \"id\": 300, " +
            " \"type\": \"post\", " +
            " \"slug\": \"the-muffs\", " +
            " \"url\": \"http://oaklandly.com/wordpress/?p=300\", " +
            " \"status\": \"publish\", " +
            " \"title\": \"The Muffs\", " +
            " \"title_plain\": \"The Muffs\", " +
            " \"content\": \"<p>with TurbonegrA, The Bruises, and Talky Tina $12, $15</p> \", " +
            " \"excerpt\": \"with TurbonegrA, The Bruises, and Talky Tina $12, $15\", " +
            " \"date\": \"2011-07-31 12:52:21\", " +
            " \"modified\": \"2011-07-31 12:52:21\", " +
            " \"categories\": [ " +
            "     { " +
            "         \"id\": 41, " +
            "         \"slug\": \"events\", " +
            "         \"title\": \"Events\", " +
            "         \"description\": \"\", " +
            "         \"parent\": 0, " +
            "         \"post_count\": 62\n" +
            "     } " +
            " ], " +
            " \"tags\": [], " +
            " \"author\": { " +
            "     \"id\": 2, " +
            "     \"slug\": \"clay\", " +
            "     \"name\": \"clay\", " +
            "     \"first_name\": \"Clay\", " +
            "     \"last_name\": \"Graham\", " +
            "     \"nickname\": \"clay\", " +
            "     \"url\": \"http://oaklandly.com/\", " +
            "     \"description\": \"\" " +
            " }, " +
            " \"comments\": [], " +
            " \"attachments\": [], " +
            " \"comment_count\": 0, " +
            " \"comment_status\": \"open\", " +
            " \"custom_fields\": { " +
            "     \"_EventVenue\": [ " +
            "         \"The Uptown\", " +
            "         \"The Uptown\" " +
            "     ], " +
            "     \"_EventStartDate\": [ " +
            "         \"2012-07-30 21:00:00\", " +
            "         \"2012-07-30 21:00:00\" " +
            "     ], " +
            "     \"_EventEndDate\": [ " +
            "         \"2012-07-30 22:00:00\", " +
            "         \"2012-07-30 22:00:00\" " +
            "     ], " +
            "     \"_EventAddress\": [ " +
            "         \"1928 Telegraph Ave., Oakland CA\", " +
            "         \"1928 Telegraph Ave., Oakland CA\" " +
            "     ], " +
            "     \"_EventCity\": [ " +
            "         \"Oakland\", " +
            "         \"Oakland\" " +
            "     ], " +
            "     \"_EventState\": [ " +
            "         \"CA\", " +
            "         \"CA\" " +
            "     ], " +
            "     \"_EventZip\": [ " +
            "         \"\", " +
            "         \"\" " +
            "     ], " +
            "     \"_EventCost\": [ " +
            "         \"\", " +
            "         \"\" " +
            "     ], " +
            "     \"_EventPhone\": [ " +
            "         \"510-451-8100\", " +
            "         \"510-451-8100\" " +
            "     ], " +
            "     \"_SGFeature\": { " +
            "          \"geometry\": {" +
            "               \"type\": \"Point\"," +
            "               \"coordinates\": [-122.224998,37.7845]" +
            "           }," +
            "           \"type\": \"Feature\"," +
            "           \"id\": \"SG_2DUIe09SC2OfuG9VNQz1RE_37.784500_-122.224998@1303263339\"," +
            "           \"properties\":{" +
            "               \"province\":\"CA\"," +
            "               \"distance\":0.20076299663649658," +
            "               \"name\":\"Wong's Auto Service\"," +
            "               \"tags\":[\"smog\",\"check\"]," +
            "               \"country\":\"US\"," +
            "               \"phone\":\"+1 510 436 7643\"," +
            "               \"href\":\"http://api.simplegeo.com/1.0/features/SG_2DUIe09SC2OfuG9VNQz1RE_37.784500_-122.224998@1303263339.json\"," +
            "               \"city\":\"Oakland\"," +
            "               \"address\":\"2801 Foothill Blvd\"," +
            "               \"owner\":\"simplegeo\"," +
            "               \"postcode\":\"94601\"," +
            "               \"classifiers\":[{" +
            "                   \"category\":\"Government\"," +
            "                   \"type\":\"Public Place\"," +
            "                   \"subcategory\":\"Office\"" +
            "               }]" +
            "           }" +
            "       }" +
            "     } " +
            " }";

    @Test
    public void testSaveEventAndPlaceFromPostJson() {
        WordpressJsonModelProcessor wordpressJsonModelProcessor = new WordpressJsonModelProcessor();

        // set dependencies
        wordpressJsonModelProcessor.setObjectMapper(new JacksonObjectMapper());

        PlaceManagerServiceImpl placeManagerService = new PlaceManagerServiceImpl();

        PlaceDao mockPlaceDao = mock(PlaceDao.class);

        placeManagerService.setPlaceDao(mockPlaceDao);

        wordpressJsonModelProcessor.setPlaceManagerService(placeManagerService);

        // set SimpleGeo keys, call init()
        wordpressJsonModelProcessor.setSgoauthkey("ZdGSMVGmne9ccTn6dykyGffHU8AXCAaC");
        wordpressJsonModelProcessor.setSgoauthsecret("kmbYEGBVbhA6473Y2ms3SwMS5SYYuWux");
        wordpressJsonModelProcessor.init();

        SimpleGeoEventClient simpleGeoEventClient = new SimpleGeoEventClient();

        EventServiceImpl eventService = new EventServiceImpl();

        EventDao mockEventDao = mock(EventDao.class);
        eventService.setEventDao(mockEventDao);
        wordpressJsonModelProcessor.setEventService((EventService)eventService);

        simpleGeoEventClient.setEventService(eventService);
        simpleGeoEventClient.setPlaceManagerService(placeManagerService);
        simpleGeoEventClient.setLocationPlacesClient(new SimpleGeoLocationClient());

        wordpressJsonModelProcessor.setGeoEventClient(simpleGeoEventClient);

        try {
        	String eventContents = new String(InputOutputUtils.getBytesFromStream(
					TestWordpressJsonModelProcessor.class.getResourceAsStream("/data/event_post.json")));
        	
            JSONObject jsonObject = 
            	new JSONObject(eventContents);

            Publisher publisher = new Publisher();
            JSONObject jsonPost = jsonObject.getJSONObject("post");
            wordpressJsonModelProcessor.saveEventAndPlaceFromPostJson(jsonPost, publisher);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
