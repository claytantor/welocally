package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Event;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.ArticleService;
import com.sightlyinc.ratecred.service.EventService;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import com.sightlyinc.ratecred.service.PublisherService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for widget related pages, like the widget test page,
 * and the publish widget iframe content.
 *
 * @author sam
 * @version $Id$
 */
@Controller
@RequestMapping("/widget")
public class WidgetController {

    private static final Logger LOGGER = Logger.getLogger(WidgetController.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private EventService eventService;
    @Autowired
    private PlaceManagerService placeManagerService;
    @Autowired
    private PublisherService publisherService;

    @Value("${applicationProperties.widget.hostname}")
    private String hostname;

    @ModelAttribute("hostname")
    public String getHostName(){
    	return hostname;
    }

    @RequestMapping("/test")
    public void testArticleWidget() {
    }

    @RequestMapping("/test/event")
    public String testEventWidget() {
        return "widget/event_widget_test";
    }

    /**
     * Handles an article publish widget iframe request.
     *
     * @param url required article URL
     * @param name required article name
     * @param publisherId required article publisher id
     * @param placeId required article place id
     * @param summary optional article summary
     * @return model and view
     */
    @RequestMapping("/publish/article")
    public ModelAndView publishArticleWidgetContent(
            @RequestParam("url") String url,
            @RequestParam("name") String name,
            @RequestParam("publisher") Long publisherId,
            @RequestParam("place") Long placeId,
            @RequestParam(value = "summary", required = false) String summary
    ) {
        ModelAndView modelAndView = new ModelAndView("widget/publish");

        LOGGER.debug("publish widget loaded for url " + url);

        Article article = articleService.findByUrl(url);
        if (article == null) {
            LOGGER.debug("publish widget could not find existing article, creating new article");
            // save the article, which will trigger an asynchronous publish
            article = new Article();
            article.setUrl(url);
            article.setName(name);
            article.setSummary(summary);
            article.setDescription(summary);

                // look up the selected publisher
            Publisher publisher = publisherService.findByPrimaryKey(publisherId);
            if (publisher != null) {
                if (url.startsWith(publisher.getUrl())) {
                    article.setPublisher(publisher);

                    // look up the selected place
                    Place place = placeManagerService.findPlaceByPrimaryKey(placeId);
                    if (place != null) {
                        article.setPlace(place);

                        articleService.save(article);

                        LOGGER.debug("publish widget saved new article with url " + url);
                        modelAndView.addObject("status", "Queued for publish");
                    } else {
                        LOGGER.debug("publish widget could not find place with id " + placeId);
                        modelAndView.addObject("status", "Invalid place: " + placeId);
                    }
                } else {
                    LOGGER.debug("publish widget article URL does not start with publisher URL");
                    modelAndView.addObject("status", "Article URL must start with " + publisher.getUrl());
                }
            } else {
                LOGGER.debug("publish widget could not find publisher with id " + publisherId);
                modelAndView.addObject("status", "Invalid publisher: " + publisherId);
            }
        } else {
            LOGGER.debug("publish widget found existing article");
            if (article.getPublished() == null || !article.getPublished()) {
                LOGGER.debug("publish widget existing article not published yet");
                modelAndView.addObject("status", "Queued for publish");
            }
        }

        return modelAndView;
	}

    /**
     * Handles an event publish widget iframe request.
     *
     * @param url required event URL
     * @param name required event name
     * @param publisherId required event publisher id
     * @param placeId required event place id
     * @param timeStarts the time the event starts
     * @param timeEnds the time the event ends
     * @param summary optional event summary
     * @return model and view
     */
    @RequestMapping("/publish/event")
    public ModelAndView publishEventWidgetContent(
            @RequestParam("url") String url,
            @RequestParam("name") String name,
            @RequestParam("publisher") Long publisherId,
            @RequestParam("place") Long placeId,
            @RequestParam("timeStarts") long timeStarts,
            @RequestParam("timeEnds") long timeEnds,
            @RequestParam(value = "summary", required = false) String summary
    ) {
        ModelAndView modelAndView = new ModelAndView("widget/publish");

        LOGGER.debug("publish widget loaded for url " + url);

        Event event = eventService.findByUrl(url);
        if (event == null) {
            LOGGER.debug("publish widget could not find existing event, creating new event");
            // save the event, which will trigger an asynchronous publish
            event = new Event();
            event.setUrl(url);
            event.setName(name);
            event.setTimeStarts(timeStarts);
            event.setTimeEnds(timeEnds);
//            event.setSummary(summary);
            event.setDescription(summary);

                // look up the selected publisher
            Publisher publisher = publisherService.findByPrimaryKey(publisherId);
            if (publisher != null) {
                if (url.startsWith(publisher.getUrl())) {
                    event.setPublisher(publisher);

                    // look up the selected place
                    Place place = placeManagerService.findPlaceByPrimaryKey(placeId);
                    if (place != null) {
                        event.setPlace(place);

                        eventService.save(event);

                        LOGGER.debug("publish widget saved new event with url " + url);
                        modelAndView.addObject("status", "Queued for publish");
                    } else {
                        LOGGER.debug("publish widget could not find place with id " + placeId);
                        modelAndView.addObject("status", "Invalid place: " + placeId);
                    }
                } else {
                    LOGGER.debug("publish widget event URL does not start with publisher URL");
                    modelAndView.addObject("status", "Event URL must start with " + publisher.getUrl());
                }
            } else {
                LOGGER.debug("publish widget could not find publisher with id " + publisherId);
                modelAndView.addObject("status", "Invalid publisher: " + publisherId);
            }
        } else {
            LOGGER.debug("publish widget found existing event");
            if (event.getPublished() == null || !event.getPublished()) {
                LOGGER.debug("publish widget existing event not published yet");
                modelAndView.addObject("status", "Queued for publish");
            }
        }

        return modelAndView;
	}

    @RequestMapping("/generator/article")
    public ModelAndView publishArticleWidgetGenerator(@RequestParam(value = "publisherId", required = false) Long publisherId) {
        ModelAndView modelAndView =  new ModelAndView("widget/generator");
        Publisher publisher = null;
        if (publisherId != null) {
            publisher = publisherService.findByPrimaryKey(publisherId);
        }
        modelAndView.addObject(publisher);

        return modelAndView;
    }

    @RequestMapping("/generator/event")
    public ModelAndView publishEventWidgetGenerator(@RequestParam(value = "publisherId", required = false) Long publisherId) {
        ModelAndView modelAndView =  new ModelAndView("widget/event_generator");
        Publisher publisher = null;
        if (publisherId != null) {
            publisher = publisherService.findByPrimaryKey(publisherId);
        }
        modelAndView.addObject(publisher);

        return modelAndView;
    }
}
