package com.sightlyinc.ratecred.client.link;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.noi.utility.net.ClientResponse;
import com.noi.utility.net.SimpleHttpClient;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.AffiliateLink;
import com.thoughtworks.xstream.XStream;

/**
 * this needs o be transformed to a true client
 * 
 * @author claygraham
 *
 */
public class CommisionJunctionLinkClient implements LinkClient {
	
	private String apiKey;
			
	private List<AffiliateLink> links;
	
	static Logger logger = 
		Logger.getLogger(CommisionJunctionLinkClient.class);

	public CommisionJunctionLinkClient() {
		super();
	}

	public List<AffiliateLink> getLinks() {
		return links;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public NetworkResponse getNetworkResponse(LinkClientRequest requestModel) 
	{
		//get the network response for the first page
		requestModel.getLinkRequestModel().put("page-number", "1");
		NetworkResponse result = null;
		
		try {
			result = getNetworkResponseImpl(requestModel);
			
			
			//setup the page size
			Integer pageSize = 10;
			String pageSizeString = requestModel.getLinkRequestModel().get("records-per-page");
			if(pageSize != null)
				pageSize = Integer.parseInt(pageSizeString);
			else
				requestModel.getLinkRequestModel().put("records-per-page", pageSize.toString());
			
			if(result.getLinks().getRecordsReturned() == result.getLinks().getTotalMatched())
				return result;
			else if(result.getLinks().getTotalMatched()>0)//if results > pagesize get all results
			{
				int pages = result.getLinks().getTotalMatched()/pageSize;
				int remainder = (pages*pageSize)%result.getLinks().getTotalMatched();
				if(remainder>0)
					pages++;
				
				//start on pagnum 2
				for (int i = 2; i < pages-1; i++) {
					requestModel.getLinkRequestModel().put("page-number", ""+i);

					try {
						result.getLinks().getLinks().addAll(getNetworkResponseImpl(requestModel).getLinks().getLinks());
					} catch (Exception e) {
						logger.error("problem getting page:"+i, e);
					}
				}
				return result;
			}
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException", e);
		} catch (MalformedURLException e) {
			logger.error("NumberFormatException", e);
		} catch (IOException e) {
			logger.error("NumberFormatException", e);
		}
		
		//always return result (may be null)
		return result;
		
		
	}

	/**
	 * we do this to handle a single page
	 * 
	 * @param requestModel
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private NetworkResponse getNetworkResponseImpl(LinkClientRequest requestModel) 
	throws MalformedURLException, IOException {
		
		
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Authorization", apiKey);
		
		ClientResponse cresponse = 
			SimpleHttpClient.get(
					"https://linksearch.api.cj.com/v2/link-search", 
					requestModel.getLinkRequestModel(), 
					headers);
		
		if(cresponse.getCode()!=200) {
			logger.debug("problem getting links");
			throw new IOException("error during checkin RESPONSE:"+cresponse.getCode());
		}
				
		XStream xstream = new XStream();
		xstream.alias("cj-api", NetworkResponse.class);
		
		xstream.alias("links", Links.class);
		xstream.useAttributeFor(Links.class, "totalMatched");
        xstream.aliasField("total-matched", Links.class, "totalMatched");
        
        xstream.useAttributeFor(Links.class, "recordsReturned");
        xstream.aliasField("records-returned", Links.class, "recordsReturned");
        
        xstream.useAttributeFor(Links.class, "pageNumber");
        xstream.aliasField("page-number", Links.class, "pageNumber");
					
		xstream.addImplicitCollection(Links.class, "links");
		xstream.alias("link", AffiliateLink.class);
		xstream.aliasField("advertiser-id", AffiliateLink.class, "advertiserId");
		xstream.aliasField("advertiser-name", AffiliateLink.class, "advertiserName");
		xstream.aliasField("category", AffiliateLink.class, "category");
		xstream.addImplicitCollection(AffiliateLink.class, "category");
		xstream.aliasField("link-code-html", AffiliateLink.class, "linkCodeHtml");
		xstream.aliasField("description", AffiliateLink.class, "description");
		xstream.aliasField("destination", AffiliateLink.class, "destination");
		xstream.aliasField("link-id", AffiliateLink.class, "linkId");		
		xstream.aliasField("link-name", AffiliateLink.class, "linkName");
		xstream.aliasField("promotion-start-date", AffiliateLink.class, "startDateString");
		xstream.aliasField("promotion-end-date", AffiliateLink.class, "endDateString");
				
		//ignore
		xstream.omitField(AffiliateLink.class, "click-commission");
		xstream.omitField(AffiliateLink.class, "creative-height");
		xstream.omitField(AffiliateLink.class, "creative-width");
		xstream.omitField(AffiliateLink.class, "language");
		xstream.omitField(AffiliateLink.class, "lead-commission");
		xstream.omitField(AffiliateLink.class, "link-type");
		xstream.omitField(AffiliateLink.class, "link-code-javascript");
		xstream.omitField(AffiliateLink.class, "performance-incentive");
		xstream.omitField(AffiliateLink.class, "promotion-type");
		xstream.omitField(AffiliateLink.class, "relationship-status");
		xstream.omitField(AffiliateLink.class, "sale-commission");
		xstream.omitField(AffiliateLink.class, "seven-day-epc");
		xstream.omitField(AffiliateLink.class, "three-month-epc");
		
		return (NetworkResponse)xstream.fromXML(new String(cresponse.getResponse()));
	}
	
	

}
