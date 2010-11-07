package com.sightlyinc.ratecred.client.offers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

import com.noi.utility.date.DateUtils;
import com.noi.utility.string.StringUtils;
import com.noi.utility.xml.XmlEncoder;
import com.sightlyinc.ratecred.client.link.LinkClient;
import com.sightlyinc.ratecred.client.link.LinkClientRequest;
import com.sightlyinc.ratecred.client.link.NetworkResponse;
import com.sightlyinc.ratecred.model.AffiliateLink;

/**
 * URL url = new URL("http", "feeds.pepperjamnetwork.com", 80, "/coupon/download/?affiliate_id=59161&program_ids=259-1687-1801-2110-2708-2803-3456-4750-4826-5173");			
 * @author claygraham
 *
 */
public class ComissionJunctionOfferClient implements OfferClient {
	
	static Logger logger = 
		Logger.getLogger(ComissionJunctionOfferClient.class);
	
	private LinkClient linkClient;
	
	private LinkClientRequest webLinksRequest;
	
	private String sourceName;
	
	@Override
	public List<Offer> getOffers() throws OfferFeedException {
		
		List<Offer> offers = new ArrayList<Offer>();
		LinkClientRequest requestModel = new LinkClientRequest();
		NetworkResponse response = linkClient.getNetworkResponse(webLinksRequest);
		for (AffiliateLink link : response.getLinks().getLinks()) {
			Offer o = new Offer();
			o.setExternalSource(sourceName);
			if(StringUtils.isEmpty(link.getStartDateString()))
			{
				Calendar c = Calendar.getInstance();
				String start = DateUtils.dateToString(c.getTime(), DateUtils.DESC_SIMPLE_FORMAT);			
				o.setBeginDateString(start);
				c.add(Calendar.DATE,90);
				String end = DateUtils.dateToString(c.getTime(), DateUtils.DESC_SIMPLE_FORMAT);
				o.setExpireDateString(end);
			} else {
				o.setBeginDateString(link.getStartDateString().split(" ")[0]);
				o.setExpireDateString(link.getEndDateString().split(" ")[0]);
			}
			
			o.setExternalId(link.getLinkId().toString());
			o.setProgramName(link.getAdvertiserName());
			o.setProgramId(link.getAdvertiserId());
			o.setName(link.getLinkName());
			o.setCouponCode("BYWME");
			
			String decoded = XmlEncoder.getInstance().decodeString(link.getLinkCodeHtml());
			Parser p = Parser.createParser(decoded, "UTF-8");
			
			try {
				NodeIterator nodes = (NodeIterator) p.elements();
				while(nodes.hasMoreNodes())
				{
					Node n = nodes.nextNode();
					if(n instanceof LinkTag)
					{
						o.setUrl(((LinkTag)n).getLink());
						o.setDescription(((LinkTag)n).getLinkText().replaceAll("[^a-zA-Z0-9 ]", ""));						
						
					}					
				}
			} catch (ParserException e) {
				logger.error("problem parsing offer", e);
			}
			
			//dont add expired offers
			Date expriresDate = DateUtils.stringToDate(o.getExpireDateString(), DateUtils.DESC_SIMPLE_FORMAT);
			if(expriresDate.getTime()>Calendar.getInstance().getTimeInMillis())			
				offers.add(o);
			
			
		}
		
		return offers;
	}



	public void setLinkClient(LinkClient linkClient) {
		this.linkClient = linkClient;
	}

	public void setWebLinksRequest(LinkClientRequest webLinksRequest) {
		this.webLinksRequest = webLinksRequest;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

}
