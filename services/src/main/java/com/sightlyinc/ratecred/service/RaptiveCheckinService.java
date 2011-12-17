package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.OAuth.Parameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.web.UrlUtils;
import com.sightlyinc.ratecred.dao.RatingDao;
import com.sightlyinc.ratecred.model.Rating;

/**
 *
 * @author claygraham
 *
 */
@Service("checkinService")
@Transactional
public class RaptiveCheckinService implements CheckinService {
	
	private static final Log logger = LogFactory.getLog(RaptiveCheckinService.class);
	
	@Value("${aggegator.consumerKey}")	
	private String aggegatorConsumerKey;
	
	@Value("${aggegator.consumerSecret}")
	private String aggegatorConsumerSecret;
	
	@Value("${aggegator.baseUrl}")
	private String aggegatorBaseUrl;
	
	@Value("${aggegator.reqUrl}")
	private String reqUrl;
	
	@Value("${aggegator.accessUrl}")
	private String accessUrl;
	
	@Value("${aggegator.authzUrl}")
	private String authzUrl;
	
	@Value("${aggegator.checkinCallbackUrl}")
	private String checkinCallbackUrl;
	
	@Autowired
	private RatingDao ratingDao;
	
	/**
	 * Post the checkin data to a specific provider
	 * Required parameters in the end point
	 * 'provider' - The provider to check into - gowalla, foursquare, all
	 * 'pck' - The publisher consumer key
	 * Required query parameters are:
	 * 'pun' - The publisher username to check in
	 * 'lat' - The lattitude of the location
	 * 'lng' - The longitude of the location
	 * 'rad' - The radius to calculate the precision
	 * 'name' - The name of the location to match and check into
	 * 'cburl' - The callback URL to post the success message to
	 * 
	 * https://aggregator.raptive.com/v1/publisher/4c04292b5e41436224a9a641/checkin/foursquare?pun=raptive_publisher&provider=FOURSQUARE&lat=38.9085106333&lng=-77.21468345&rad=1&name=Westin%20Fitness&cburl=http%3A%2F%2Ftestapp.raptize.com%2Fcheckedin&comment=test%20checkin
	 * 
	 * 
	 */
	@Override
	public void checkinRating(String providerName, Rating rDetached)
			throws BLServiceException {
		
		try {
			
			//get the rating by id to make sure its bound
			Rating r = ratingDao.findByPrimaryKey(rDetached.getId());
			
			//empty callback url
			String callbackUrl = "";

			OAuthServiceProvider provider = new OAuthServiceProvider(aggegatorBaseUrl+reqUrl,
					aggegatorBaseUrl+authzUrl, aggegatorBaseUrl+accessUrl);
			OAuthConsumer consumer = new OAuthConsumer(callbackUrl, aggegatorConsumerKey, 
					aggegatorConsumerSecret,
					provider);
			OAuthAccessor accessor = new OAuthAccessor(consumer);		
			
			Map<String,String> qsModel = new HashMap<String,String>();
			qsModel.put("provider", providerName.toUpperCase());
			qsModel.put("pck", aggegatorConsumerKey);
			qsModel.put("pun", r.getOwner().getUserName());
			qsModel.put("lat", r.getPlace().getLatitude().toString());
			qsModel.put("lng", r.getPlace().getLongitude().toString());
			qsModel.put("rad", "100");
			qsModel.put("name", r.getPlace().getName());
			qsModel.put("comment", r.getNotes());
			qsModel.put("cburl", checkinCallbackUrl);
			
			String unsignedUrl = aggegatorBaseUrl + "/v1/publisher/"
				+ aggegatorConsumerKey + "/checkin/" + providerName + "?"+
				UrlUtils.toQueryString(qsModel);

			logger.debug(unsignedUrl);
			
			// RAM: huh? Weird way of adding name/value pairs.. whatever
			// /** Construct a list of Parameters from name, value, name,
			// value... */
			// public static List<Parameter> newList(String... parameters)
			// You can add more parameters if you want
			List<Parameter> parameters = OAuth.newList(OAuth.OAUTH_TOKEN, "");

			OAuthMessage msg = accessor.newRequestMessage("GET", unsignedUrl,
					parameters, null);
			logger.debug(msg);
			String checkinUrl = OAuth.addParameters(msg.URL, msg
					.getParameters());
			logger.debug(checkinUrl);
			
			ClientResponse cresponse = 
				SimpleHttpClient.get(checkinUrl, null, null);
			
			if(cresponse.getCode()!=200 && cresponse.getCode()!=500) {
				logger.debug("problem checking in:"+providerName);
				throw new BLServiceException("error during checkin RESPONSE:"+cresponse.getCode());
			} else if(cresponse.getCode()==500) {
				logger.debug("checkin failed:"+cresponse.getCode()+" provider:"+providerName);
			} else {
				//parse the result
				//{"SUCCESS":"Saved the data","txnid":"4d5872a8ce6fc87d0a000007"}
				String response = new String(cresponse.getResponse());
				logger.debug("response:"+response);
				String startsat = response.substring(response.indexOf("\"txnid\":\"")+"\"txnid\":\"".length());
				String txid = startsat.substring(0, startsat.lastIndexOf("\""));
				logger.debug("txid:"+txid);
									
				if(providerName.equalsIgnoreCase("foursquare")) {
					r.setCheckedinFoursquare("SENT");	
					r.setTxIdFoursquare(txid);
				} else if(providerName.equalsIgnoreCase("gowalla")) {
					r.setCheckedinGowalla("SENT");
					r.setTxIdGowalla(txid);
				}
				
				ratingDao.save(r);
			}
			
			
			
			
		} catch (OAuthException e) {
			logger.error("problem checking in",e);
			throw new BLServiceException(e);
		} catch (IOException e) {
			logger.error("problem checking in",e);
			throw new BLServiceException(e);
		} catch (URISyntaxException e) {
			logger.error("problem checking in",e);
			throw new BLServiceException(e);
		} catch (Exception e) {
			logger.error("problem checking in",e);
			throw new BLServiceException(e);
		}
		


	}

	public void setRatingDao(RatingDao ratingDao) {
		this.ratingDao = ratingDao;
	}
	
	

}
