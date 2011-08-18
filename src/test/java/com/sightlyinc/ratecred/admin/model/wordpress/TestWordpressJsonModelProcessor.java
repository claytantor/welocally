package com.sightlyinc.ratecred.admin.model.wordpress;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Test;

import com.noi.utility.io.InputOutputUtils;
import com.sightlyinc.ratecred.client.geo.SimpleGeoArticleClient;
import com.sightlyinc.ratecred.client.geo.SimpleGeoEventClient;
import com.sightlyinc.ratecred.client.geo.SimpleGeoLocationClient;
import com.sightlyinc.ratecred.component.JacksonObjectMapper;
import com.sightlyinc.ratecred.dao.ArticleDao;
import com.sightlyinc.ratecred.dao.EventDao;
import com.sightlyinc.ratecred.dao.PlaceDao;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.ArticleService;
import com.sightlyinc.ratecred.service.ArticleServiceImpl;
import com.sightlyinc.ratecred.service.EventService;
import com.sightlyinc.ratecred.service.EventServiceImpl;
import com.sightlyinc.ratecred.service.PlaceManagerServiceImpl;

/**
 * @author sam
 * @version $Id$
 */
public class TestWordpressJsonModelProcessor {
    

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
            
            Assert.assertEquals(true, wordpressJsonModelProcessor.isEventPost(jsonPost));
            
            wordpressJsonModelProcessor.saveEventAndPlaceFromPostJson(jsonPost, publisher, "save_post");

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
    
    @Test
    public void testSaveArticleAndPlaceFromPostJson() {
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

        SimpleGeoArticleClient simpleGeoArticleClient = new SimpleGeoArticleClient();

        ArticleServiceImpl articleService = new ArticleServiceImpl();

        ArticleDao mockArticleDao = mock(ArticleDao.class);
        articleService.setArticleDao(mockArticleDao);
        wordpressJsonModelProcessor.setArticleService((ArticleService)articleService);

        simpleGeoArticleClient.setArticleService(articleService);
        simpleGeoArticleClient.setPlaceManagerService(placeManagerService);
        simpleGeoArticleClient.setLocationPlacesClient(new SimpleGeoLocationClient());

        wordpressJsonModelProcessor.setGeoArticleClient(simpleGeoArticleClient);

        try {
        	String articleContents = new String(InputOutputUtils.getBytesFromStream(
					TestWordpressJsonModelProcessor.class.getResourceAsStream("/data/article_post.json")));
        	
        	
            JSONObject jsonObject = 
            	new JSONObject(articleContents);

            Publisher publisher = new Publisher();
            JSONObject jsonPost = jsonObject.getJSONObject("post");
            
            Assert.assertEquals(true, wordpressJsonModelProcessor.isArticlePost(jsonPost));
            
            wordpressJsonModelProcessor.saveArticleAndPlaceFromPostJson(jsonPost, publisher, "save_post");

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
    
    
}
