package com.sightlyinc.ratecred.admin.mvc.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.noi.utility.geo.GeoPoint;
import com.noi.utility.geo.GeoPointBounds;
import com.noi.utility.geo.MapUtils;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.admin.jms.SaveArticleLocationMessageProducer;
import com.sightlyinc.ratecred.admin.model.Article;
import com.sightlyinc.ratecred.admin.model.TargetModel;
import com.sightlyinc.ratecred.client.offers.Location;
import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.service.AwardManagerService;
import com.sightlyinc.ratecred.service.OfferPoolService;
import com.sightlyinc.ratecred.service.RaterAwardsService;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Record;

@Controller
@RequestMapping(value="/offer")
public class OfferController {
	
	
	static Logger logger = Logger.getLogger(OfferController.class);

	
	@Autowired
	@Qualifier("offerPoolService")
	private OfferPoolService offerPoolService;
	
	@Autowired
	@Qualifier("saveArticleLocationMessageProducer")
	private SaveArticleLocationMessageProducer saveArticleLocationMessageProducer;
	
	@Autowired
	private AwardManagerService awardManagerService;
	
	@Autowired
	RaterAwardsService raterAwardsService;
	
	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	private String authConsumerKey;
	
	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	private String authSecretKey;
	
	@Autowired
    @Qualifier("jacksonMapper")
    private ObjectMapper jacksonMapper;
	
	
	//this will need to be fixed this is just a quick solution
	//so there are write messages every request 
	private Map<String,Article> articleWrittenCache = new HashMap<String,Article>();
	

	/**
	 * elt.src = 'http://'+cfg.hostname+'/rcadmin/do/offer/target?
	 * keywords='+cfg.keywords+
	 * '&view='+cfg.view+'&css='+encodeURIComponent(cfg.css)+cfg.view+
	 * '&address1='+encodeURIComponent(cfg.address1)+cfg.view+
	 * '&title='+encodeURIComponent(cfg.title)+'&teaser='+encodeURIComponent(cfg.teaser)+ 
	 * '&refererId=' + reffererId;
	 * 			                    
	 * 
	 * @param city
	 * @param state
	 * @param keywords
	 * @param view
	 * @param css
	 * @param model
	 * @return
	 * @throws BLServiceException
	 */
	@RequestMapping(value="/target",method=RequestMethod.GET)
	public String getTargetedOffer(
			@RequestParam(value="address1",required=true) String address1,
			@RequestParam(value="url",required=true) String url,
			@RequestParam(value="teaser",required=true) String teaser,
			@RequestParam(value="title",required=true) String title,
			@RequestParam(value="keywords",required=false) String keywords, 
			@RequestParam(value="view",required=false) String view, 
			@RequestParam(value="css",required=false) String css,
			@RequestParam(value="referrerId",required=true) String referrerId,
			Model model) throws BLServiceException {		
		
		TargetModel tmodel = new TargetModel();
		//tmodel.setCity(city);
		//tmodel.setState(state);
		if(!StringUtils.isEmpty(keywords)) {
			List<String> keywordsList = Arrays.asList(keywords.split(","));	
			tmodel.setKeywords(keywordsList);
		}
		
		try {
			
			Offer offer = raterAwardsService.targetOfferByTargetingModel(tmodel);
			model.addAttribute("offer", offer);
			model.addAttribute("css", css);
			
			//find the center and points
			//map bounds
			List<GeoPoint> points = new ArrayList<GeoPoint>();

			for (Location location : offer.getAdvertiser().getLocations()) {
				points.add(new GeoPoint(
						location.getLat(), location.getLng()));
			}
			
			if(points.size()>0)
			{
				GeoPointBounds bounds = MapUtils.computeBounds(points);
				model.addAttribute("mapCenter", MapUtils.computeCenter(bounds));	
				model.addAttribute("mapBounds", bounds);	
			}
			
			
			//ok if the url is not in the cache then 
			//send a article write to the queue, use the URL as the 
			//primary key
			
			if(articleWrittenCache.get(url) == null) {
				Article a = new Article();
				a.setAddress1(address1);
				a.setKeywords(keywords);
				a.setReferrerId(referrerId);
				a.setTeaser(teaser);
				a.setTitle(title);
				a.setUrl(url);
				saveArticleLocationMessageProducer.generateMessage(a);
				
			}
			
			
		} catch (BLServiceException e) {
			logger.error("problem targeting", e);
			throw e;
		} catch (JsonGenerationException e) {
			logger.error("problem saving article location to storage", e);
		} catch (JsonMappingException e) {
			logger.error("problem saving article location to storage", e);
		} catch (JMSException e) {
			logger.error("problem saving article location to storage", e);
		} catch (IOException e) {
			logger.error("problem saving article location to storage", e);
		}
		
		if(StringUtils.isEmpty(view))
			return "offer";
		else
			return view;
	}
	
	/**
	 * hack for demo, goto simple geo and get the offer
	 * 
	 * @param awardIexternalId
	 * @param view
	 * @param model
	 * @return
	 * @throws BLServiceException
	 */
	@RequestMapping(value="/sg/{layer}/{externalId}",method=RequestMethod.GET)
	public String getSGOffer(
			@PathVariable("layer") String layer,
			@PathVariable("externalId") String externalId,
			@RequestParam(value="view",required=false) String view, 
			Model model) throws BLServiceException {
		
		try {
			SimpleGeoStorageClient client = SimpleGeoStorageClient.getInstance();
			client.getHttpClient().setToken(
					authConsumerKey, 
					authSecretKey);
			
			Record record = 
				client.getRecord(layer, externalId);
			
			JSONObject offerRecord = 
				(JSONObject)record.getProperties().get("offer");
			
			/*Offer o = JSONDeserializer.deserializeOffer(offerRecord);
			model.addAttribute("offer",o);*/
			
			//deserialize the json
			Offer o = 
        		jacksonMapper.readValue(
        				new ByteArrayInputStream(
        						offerRecord.toString().getBytes()), Offer.class);
			
			model.addAttribute("offer",o);
			
		} catch (IOException e) {
			logger.error("cannot get places result",e);
		} catch (Exception e) {
			logger.error("cannot get places result",e);
		}
		
		
		if(StringUtils.isEmpty(view))
			return "offer";
		else
			return view;
		
		
	}
	
	

}
