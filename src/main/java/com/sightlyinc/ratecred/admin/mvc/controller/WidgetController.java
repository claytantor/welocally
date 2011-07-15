package com.sightlyinc.ratecred.admin.mvc.controller;

import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.ArticleService;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import com.sightlyinc.ratecred.service.PublisherService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private PlaceManagerService placeManagerService;
    @Autowired
    private PublisherService publisherService;

    @RequestMapping("/test")
    public void testWidget() {
    }

    /**
     * Handles a publish widget iframe request.
     *
     * @param url required article URL
     * @param name required article name
     * @param publisherId required article publisher id
     * @param placeId required article place id
     * @param description optional article summary
     * @param model
     */
    @RequestMapping("/publish")
    public void publishWidgetContent(
            @RequestParam("url") String url,
            @RequestParam("name") String name,
            @RequestParam("publisher") Long publisherId,
            @RequestParam("place") Long placeId,
            @RequestParam(value = "summary", required = false) String summary,
            Model model
    ) {

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
                        model.addAttribute("status", "Queued for publish");
                    } else {
                        LOGGER.debug("publish widget could not find place with id " + placeId);
                        model.addAttribute("status", "Invalid place: " + placeId);
                    }
                } else {
                    LOGGER.debug("publish widget article URL does not start with publisher URL");
                    model.addAttribute("status", "Article URL must start with " + publisher.getUrl());
                }
            } else {
                LOGGER.debug("publish widget could not find publisher with id " + publisherId);
                model.addAttribute("status", "Invalid publisher: " + publisherId);
            }
        } else {
            LOGGER.debug("publish widget found existing article");
            if (article.getPublished() == null || !article.getPublished()) {
                LOGGER.debug("publish widget existing article not published yet");
                model.addAttribute("status", "Queued for publish");
            }
        }
	}

    @RequestMapping("/generator")
    public Publisher publishWidgetGenerator(@RequestParam(value = "publisherId", required = false) Long publisherId) {
        Publisher publisher = null;
        if (publisherId != null) {
            publisher = publisherService.findByPrimaryKey(publisherId);
        }
        return publisher;
    }
}
