package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.admin.util.GoogleSpreadsheetUtils;
import com.sightlyinc.ratecred.client.geo.GeoPersistenceException;
import com.sightlyinc.ratecred.client.geo.SimpleGeoPlaceManager;
import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Merchant;
import com.sightlyinc.ratecred.model.NetworkMember;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.ArticleService;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import com.sightlyinc.ratecred.service.PublisherService;
import com.simplegeo.client.SimpleGeoPlacesClient;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.FeatureCollection;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("/publisher/article")
public class ArticleController {

	static Logger logger = Logger.getLogger(ArticleController.class);

	public static String sgoauthkey = "ZdGSMVGmne9ccTn6dykyGffHU8AXCAaC";

	public static String sgoauthsecret = "kmbYEGBVbhA6473Y2ms3SwMS5SYYuWux";

	@Autowired
	private ArticleService articleService;

	@Autowired
	private PublisherService publisherService;

	@Autowired
	@Qualifier("locationPlacesClient")
	private SimpleGeoPlaceManager simpleGeoPlaceManager;

	@Autowired
	private PlaceManagerService placeManagerService;

	@RequestMapping(method = RequestMethod.GET)
	public String addArticle(@RequestParam Long publisherId, Model model) {
		Publisher publisher = publisherService.findByPrimaryKey(publisherId);
		model.addAttribute("publisher", publisher);
		Article article = new Article();
		article.setPublisher(publisher);
		model.addAttribute("articleForm", article);
		return "article/edit";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editArticle(@PathVariable Long id, Model model) {
		logger.debug("edit");
		Article a = articleService.findByPrimaryKey(id);
		model.addAttribute("publisher", a.getPublisher());
		model.addAttribute("articleForm", a);
		return "article/edit";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String saveArticle(@Valid Article article) {
		logger.debug("got post action");

		Long id = articleService.save(article);
		return "redirect:/publisher/article/" + id.toString();

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getArticleById(@PathVariable Long id, Model model) {
		logger.debug("view");
		Article a = articleService.findByPrimaryKey(id);
		model.addAttribute("publisher", a.getPublisher());
		model.addAttribute("article", a);
		return "article/view";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteArticle(@PathVariable Long id) {
		logger.debug("delete");
		Article article = articleService.findByPrimaryKey(id);
		articleService.delete(article);
		return "redirect:/publisher/article/list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam Long publisherId, Model model) {
		logger.debug("list");
		Publisher publisher = publisherService.findByPrimaryKey(publisherId);
		model.addAttribute("publisher", publisher);
		model.addAttribute("articles", publisher.getArticles());
		return "article/list";
	}

	@RequestMapping(value = "/load/{key}", method = RequestMethod.GET)
	public String loadArticlesFromGoogle(@PathVariable String key,
			@RequestParam Long publisherId,
			@ModelAttribute("member") NetworkMember member, Model model) {
		logger.debug("load");
		String spreadsheetUrl = "https://spreadsheets.google.com/tq?tqx=out:csv&key="
				+ key + "&hl=en";

		try {
			List<String[]> rows = GoogleSpreadsheetUtils
					.getSpreadsheetData(spreadsheetUrl);
			
			Publisher p = publisherService.findByPrimaryKey(publisherId);
			findAndSaveArticlesAsTable(rows, member);
			
			
			return "redirect:/publisher/article/list?publisherId="+publisherId;
		} catch (IOException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (JSONException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (GeoPersistenceException e) {
			model.addAttribute("error", e);
			return "error";
		} catch (BLServiceException e) {
			model.addAttribute("error", e);
			return "error";
		}

	}

	protected void findAndSaveArticlesAsTable(List<String[]> lines,
			NetworkMember member) throws IOException, JSONException,
			GeoPersistenceException, BLServiceException {

		StringBuffer places = new StringBuffer();
		double lat = 37.811373;
		double lon = -122.265698;

		double radiusInKMeters = 10.00;

		SimpleGeoPlacesClient client = SimpleGeoPlacesClient.getInstance();
		client.getHttpClient().setToken(sgoauthkey, sgoauthsecret);

		String line = null; // not declared within while loop
		/*
		 * readLine is a bit quirky : it returns the content of a line MINUS the
		 * newline. it returns null only for the END of the stream. it returns
		 * an empty String if two newlines appear in a row.
		 */

		for (String[] fields : lines) {

			String timestamp = fields[0].trim();
			String publisherKey = fields[1].trim();
			String articleTitle = fields[2].trim();
			String url = fields[3].trim();
			String placeName = fields[4].trim();
			String summary = fields[5].trim();
			String status = fields[6].trim();
			String city = fields[7].trim();
			String state = fields[8].trim();


			if (status.equalsIgnoreCase("ACTIVE")) {

				//should be city state
				FeatureCollection collection = client.search(lat, lon,
						placeName.trim(), "", radiusInKMeters);

				ArrayList<Feature> features = collection.getFeatures();

				for (Feature feature : features) {
					String name = (String) feature.getProperties().get("name");
					logger.debug("found:" + name);

					// this is lame, should probably use distance or something
					// better
					if (name.contains(placeName)
							&& feature.getProperties().get("city").toString()
									.contains(city)) {

						places.append(name + ","
								+ placeName.trim().replace(" ", ""));
						places.append(System.getProperty("line.separator"));

						
						//dont publish items that have already been published
						Article a = articleService.findByUrl(url);
						articleService.save(makeArticleFromFeature(a,feature,
								articleTitle,  summary,  url,  publisherKey,
								member));
					}

				}


			} 

		}

		logger.debug(places.toString());

	}

	private Article makeArticleFromFeature(Article a, Feature f,
			String name, String summary, String url, String publisherKey, 
			NetworkMember member) throws BLServiceException {

		if(a==null)
			a = new Article();
		
		a.setName(name);

		// dont add a place that is already in the database
		Place p = null;
		try {
			p = placeManagerService.findBySimpleGeoId(f.getSimpleGeoId());
		} catch (BLServiceException e) {
			logger.error("cannot find place", e);
		}

		if (p == null) {
			p = new Place();
			simpleGeoPlaceManager.transformFeature(f, p);
			placeManagerService.savePlace(p);
		}
		
		String[] keys = publisherKey.split("\\.");

        // look up the selected publisher
		Publisher pub = publisherService.findByNetworkKeyAndPublisherKey(
    		keys[0], keys[1]);
		a.setPublisher(pub);

		a.setPlace(p);
		a.setDescription(summary);
		a.setSummary(summary);
		a.setUrl(url);
		

		return a;
	}

}
