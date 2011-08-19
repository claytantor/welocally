package com.sightlyinc.ratecred.client.geo;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Article;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.Publisher;
import com.sightlyinc.ratecred.service.ArticleService;
import com.sightlyinc.ratecred.service.PlaceManagerService;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Feature;
import com.simplegeo.client.types.Record;

@Component("geoArticleClient")
public class SimpleGeoArticleClient implements GeoArticleClient {

	public void setClient(SimpleGeoStorageClient client) {
		this.client = client;
	}

	public void setRatecredConsumerKey(String ratecredConsumerKey) {
		this.ratecredConsumerKey = ratecredConsumerKey;
	}

	public void setRatecredConsumerSecret(String ratecredConsumerSecret) {
		this.ratecredConsumerSecret = ratecredConsumerSecret;
	}

	public void setPlaceManagerService(PlaceManagerService placeManagerService) {
		this.placeManagerService = placeManagerService;
	}

	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public void setLocationPlacesClient(SimpleGeoPlaceManager locationPlacesClient) {
		this.locationPlacesClient = locationPlacesClient;
	}

	static Logger logger = Logger.getLogger(SimpleGeoLocationClient.class);

	private SimpleGeoStorageClient client;

	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	private String ratecredConsumerKey;

	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	private String ratecredConsumerSecret;

	@Autowired
	private PlaceManagerService placeManagerService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	@Qualifier("locationPlacesClient")
	private SimpleGeoPlaceManager locationPlacesClient;

	@PostConstruct
	public void init() {
		client = SimpleGeoStorageClient.getInstance();
		client.getHttpClient().setToken(ratecredConsumerKey,
				ratecredConsumerSecret);
	}

	@Override
	public Article makeArticleFromPlace(Place place, String articleName,
			String description, String url, Publisher pub) {
		
			Article article = articleService.findByUrl(url);
		
			if (article == null) {
				article = new Article();
				article.setUrl(url);
			}
	
			article.setDescription(description);
			article.setSummary(description);
			article.setName(articleName);

			article.setPlace(place);
			article.setPublisher(pub);
			article.setTimeCreated(Calendar.getInstance().getTimeInMillis());

			return article;

	}

//	@Override
//	public Article findById(String id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Articles findArticles(Double lat, Double lng, Float radiusInKm,
//			String sourceId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void publishArticles(Articles e, String sourceId) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void publishArticle(Article e, String sourceId) {
//		SimpleGeoStorageClient client = SimpleGeoStorageClient.getInstance();
//		client.getHttpClient().setToken(ratecredConsumerKey,
//				ratecredConsumerSecret);
//		Record r = new Record();
//
//		/*
//		 * HashMap<String,Object> model = new HashMap<String,Object>();
//		 * model.put("article", jArticle); r.setProperties(model); Geometry g =
//		 * new Geometry(); Point p = new Point(); p.setLat(loc.getLat());
//		 * p.setLon(loc.getLng()); g.setPoint(p);
//		 * r.setLayer(articleLayerPrefix+"."
//		 * +article.getReferrerId()+"."+articleType
//		 * .toLowerCase()+"."+articleLayerSuffix);
//		 * 
//		 * Bitly bitly = BitlyFactory.newInstance(bitlyUserName, bitlyKey);
//		 * 
//		 * BitlyUrl burl = bitly.shorten(article.getUrl());
//		 * 
//		 * r.setRecordId(burl.getHash()); r.setGeometry(g);
//		 * logger.debug("writing to storage:"
//		 * +article.getUrl()+" to layer "+articleLayerPrefix
//		 * +"."+article.getReferrerId()+"."+articleLayerSuffix);
//		 * client.addOrUpdateRecord(r);
//		 */
//
//	}
//
//	public Article makeArticleFromFeature(Feature f, String name,
//			String status, String url, String phone, String placeName,
//			String description, String address, String city, String state,
//			Long timeStarts, Long timeEnds, Publisher pub) {
//
//		Article article = articleService.findByUrl(url);
//
//		if (article == null) {
//			article = new Article();
//			article.setUrl(url);
//		}
//
//		// dont add a place that is already in the database
//		Place p = null;
//		try {
//			p = placeManagerService.findBySimpleGeoId(f.getSimpleGeoId());
//		} catch (BLServiceException ev) {
//			logger.error("cannot find place", ev);
//		}
//
//		try {
//			if (p == null) {
//				p = new Place();
//				locationPlacesClient.transformFeature(f, p);
//				placeManagerService.savePlace(p);
//			}
//		} catch (BLServiceException e) {
//			throw new RuntimeException(e.getMessage());
//		}
//
//		article.setDescription(description);
//		article.setName(name);
//		article.setPhone(phone);
//		article.setPlace(p);
//		article.setPublisher(pub);
//		article.setTimeCreated(Calendar.getInstance().getTimeInMillis());
//		article.setTimeStarts(timeStarts);
//		article.setTimeEnds(timeEnds);
//
//		return article;
//	}
//
//	public Article makeArticleFromPlace(Place place, String articleName,
//			String description, String url, Long timeStarts, Long timeEnds,
//			Publisher pub) {
//
//		Article article = articleService.findByUrl(url);
//
//		if (article == null) {
//			article = new Article();
//			article.setUrl(url);
//		}
//
//		article.setDescription(description);
//		article.setName(articleName);
//		article.setPhone(place.getPhone());
//		article.setPlace(place);
//		article.setPublisher(pub);
//		article.setTimeCreated(Calendar.getInstance().getTimeInMillis());
//		article.setTimeStarts(timeStarts);
//		article.setTimeEnds(timeEnds);
//
//		return article;
//	}
//
//	public void setRatecredConsumerKey(String ratecredConsumerKey) {
//		this.ratecredConsumerKey = ratecredConsumerKey;
//	}
//
//	public void setRatecredConsumerSecret(String ratecredConsumerSecret) {
//		this.ratecredConsumerSecret = ratecredConsumerSecret;
//	}
//
//	public void setArticleService(ArticleService articleService) {
//		this.articleService = articleService;
//	}
//
//	public void setPlaceManagerService(PlaceManagerService placeManagerService) {
//		this.placeManagerService = placeManagerService;
//	}
//
//	public void setLocationPlacesClient(
//			SimpleGeoPlaceManager locationPlacesClient) {
//		this.locationPlacesClient = locationPlacesClient;
//	}
}
