package com.sightlyinc.ratecred.admin.job;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVReader;

import com.noi.utility.data.DigestUtils;
import com.noi.utility.date.DateUtils;
import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.sightlyinc.ratecred.client.offers.Advertiser;
import com.sightlyinc.ratecred.client.offers.OfferOld;
import com.sightlyinc.ratecred.model.Location;
import com.simplegeo.client.SimpleGeoStorageClient;
import com.simplegeo.client.types.Geometry;
import com.simplegeo.client.types.Point;
import com.simplegeo.client.types.Record;

@Component("syncSimpleGeoStorageOffersJob")
public class SyncSimpleGeoStorageOffersJob  {
	
	static Logger logger = Logger.getLogger(SyncSimpleGeoStorageOffersJob.class);
	
	//this is used to check if there were 
	//changes
	private String lastHash;
			
	@Value("${simpleGeo.rateCredOAuth.appConsumerKey}")
	private String ratecredConsumerKey;
	
	@Value("${simpleGeo.rateCredOAuth.appSecretKey}")
	private String ratecredConsumerSecret;
	
	@Value("${offers.spreadsheetUrl}")
	private String spreadsheetUrl="http://spreadsheets.google.com/tq?tqx=out:csv&key=0Au9a580BQZPYdHkxRE1jQnFpLS1IS3VNaUVMalNiRmc&hl=en";
		
		
	protected void executeInternal()
			throws JobExecutionException {
		try {
			logger.debug("running syncSimpleGeoStorageOffersJob:"+
					Calendar.getInstance().getTimeInMillis());
			
			ClientResponse response = 
				SimpleHttpClient.get(spreadsheetUrl, null, null);
			
			String hash = DigestUtils.makeDigest(response.getResponse());
			if(lastHash == null || !lastHash.equals(hash)) {
				
				StringReader sreader = new StringReader(new String(response.getResponse()));
				CSVReader reader = new CSVReader(sreader);
				reader.readNext();
				
				String [] offerLine;
			    while ((offerLine = reader.readNext()) != null) {
			        // nextLine[] is an array of values from the line
			    	logger.debug("offer status:"+offerLine[1]);
			    	if(offerLine[1].equals("ACTIVE"))
			    	{
			    		logger.debug("data has changed UPDATE");
			    		OfferOld o = transformOffer(offerLine);
				        
				        Point p = new Point();			        
				        p.setLat(Double.parseDouble(offerLine[14]));
						p.setLon(Double.parseDouble(offerLine[15]));
						
								        					
				        saveOfferToStorage(
				    			o, 
				    			p,
				    			"com.ratecred.offer.b93acd058af5."+offerLine[22].toLowerCase()+".dev",
				    			offerLine[0]);
				        
			    	} else if(offerLine[1].equals("INACTIVE")) {
			    		deleteOfferFromStorage("", offerLine[0]);
			    	} 
			        
			        
			    }
				
				lastHash = hash;
			}
			
		} catch (Exception e) {
			logger.error("problem getting getting unlocks", e);
			throw new JobExecutionException(e);
		} 
				
	}
	
	private void deleteOfferFromStorage(String layerId, String recordId){
		SimpleGeoStorageClient client = SimpleGeoStorageClient.getInstance();	
		client.getHttpClient().setToken(ratecredConsumerKey, ratecredConsumerSecret);
		Record r = new Record();
		
		try {
			client.deleteRecord(
					layerId, 
					recordId);
			
		} catch (Exception e) {
			logger.error("problem", e);
		}
	}
	
	private void saveOfferToStorage(
			OfferOld o, 
			Point p,
			String layerId,
			String recordId) {
		SimpleGeoStorageClient client = SimpleGeoStorageClient.getInstance();	
		client.getHttpClient().setToken(ratecredConsumerKey, ratecredConsumerSecret);
		Record r = new Record();
		
		try {
			HashMap<String,Object> model = new HashMap<String,Object>();
			JSONObject offer = serializeOffer(o);
			model.put("offer", offer);
			r.setProperties(model);
			Geometry g = new Geometry();
			g.setPoint(p);	
			r.setLayer(layerId);
			r.setRecordId(recordId);
			r.setGeometry(g);			
			client.addOrUpdateRecord(r);
		} catch (Exception e) {
			logger.error("problem", e);
		}
	}
	
	/*
	 * 
			0	recordId
			1	status
			2	name
			3	type
			4	price
			5	value
			6	beginDate
			7	endDate
			8	description
			9	offerDetails
			10	merchantName
			11	address1
			12	postalCode
			13	merchantContact
			14	lat
			15	lng
			16	merchantPhone
			17	merchantEmail
			18	merchantDescription
			19	offerImageUrl
			20	programName
			21	programId
	 */
	private OfferOld transformOffer(String [] offerLine) {
		OfferOld o = new OfferOld();
		o.setExternalId(offerLine[0]);
		
		//4/10/2011 beginDate [6]
		Date beginDate = DateUtils.stringToDate(offerLine[6], DateUtils.PATTERN_COMMON); 
		Date endDate = DateUtils.stringToDate(offerLine[7], DateUtils.PATTERN_COMMON); 
		
		//yyyy-MM-dd
		o.setBeginDateString(DateUtils.dateToString(beginDate, DateUtils.DESC_SIMPLE_FORMAT));
		o.setEndDateString(DateUtils.dateToString(endDate, DateUtils.DESC_SIMPLE_FORMAT));
		o.setExpireDateString(DateUtils.dateToString(endDate, DateUtils.DESC_SIMPLE_FORMAT));
		o.setType(offerLine[3]);
		o.setValue(Float.parseFloat(offerLine[5]));
		o.setPrice(Float.parseFloat(offerLine[4]));
		o.setCity("Oakland");
		o.setState("CA");
		o.setDescription(offerLine[8]);
		o.setDiscountType(offerLine[3]);		
		o.setExternalSource(offerLine[20]);
		o.setExtraDetails(offerLine[9]);
		o.setIllustrationUrl(offerLine[19]);
		o.setName(offerLine[2]);
		o.setProgramName(offerLine[20]);
		o.setProgramId(offerLine[21]);
				
		Location offerLocation = new Location();
		offerLocation.setLat(Double.parseDouble(offerLine[14]));
		offerLocation.setLng(Double.parseDouble(offerLine[15]));
		offerLocation.setAddressOne(offerLine[11]);
		offerLocation.setCity("Oakland");
		offerLocation.setState("CA");
		offerLocation.setPostalCode(offerLine[12]);
		o.setOfferLocation(offerLocation);
				
		Advertiser a = new Advertiser();
		a.setDescription(offerLine[18]);
		a.setName(offerLine[10]);
		a.getLocations().add(offerLocation);		
				
		o.setAdvertiser(a);

		return o;
	}
	
	private static JSONObject serializeOffer(OfferOld o) 
	 throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, JSONException {
		
		JSONObject jOffer = 
			new JSONObject(
					BeanUtils.describe(o));
		jOffer.remove("changes");
		jOffer.remove("class");
		jOffer.remove("discountValue");
		jOffer.remove("couponCode");
		jOffer.remove("url");
		jOffer.remove("score");
				
		Location offerLocation = o.getOfferLocation();
		
		JSONObject jOfferLocation = 
			new JSONObject(
					BeanUtils.describe(offerLocation));
		jOfferLocation.remove("class");
		jOffer.put("offerLocation", jOfferLocation);
				
		Advertiser a = o.getAdvertiser();
		
		JSONObject jOfferAdvertiser = 
			new JSONObject(
					BeanUtils.describe(a));
		
		jOfferAdvertiser.remove("class");
		jOfferAdvertiser.remove("comments");
				
		
		JSONArray locations = new JSONArray();
		locations.put(jOfferLocation);	
		jOfferAdvertiser.put("locations",locations);
		
		jOffer.put("advertiser", jOfferAdvertiser);
		
		
		return jOffer;
	}
	
	
	


}
