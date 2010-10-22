package com.sightlyinc.ratecred.client.link;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

public class CommisionJunctionLinkClient implements LinkClient {
	
	
	private String advertiserIds;
	
	private List<Link> links;
	
	static Logger logger = 
		Logger.getLogger(CommisionJunctionLinkClient.class);

	public CommisionJunctionLinkClient() {
		super();
	}

	public void initClient() {
		try {
			URL url = new URL(
					"https",
					"linksearch.api.cj.com",
					443,
					"/v2/link-search?website-id=4127816&advertiser-ids="+advertiserIds+"&link-type=Text%20Link");
			
			java.net.URLConnection con = url.openConnection();
			con.setRequestProperty(
							"Authorization",
							"008ab5c574b970628f6d347ad48b71aa62be64a3216a30824316d61a8e7a70fc8fa307bde745da14cffc38919051bcf1b20c617567ccda2cd8ee7a616985b9daa9/1b16500ef13f8f1faf15b67ed714454faf1a7bf8c481e08e35616e2230aaebc6df1d202d6dd51759f376342f385296de80873f4d7d762fb47713675a5e34d501");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(con.getInputStream(), baos);
			
			XStream xstream = new XStream();
			xstream.alias("cj-api", NetworkResponse.class);
			xstream.alias("links", Links.class);
			xstream.addImplicitCollection(Links.class, "links");
			xstream.alias("link", Link.class);
			xstream.aliasField("advertiser-id", Link.class, "advertiserId");
			xstream.aliasField("advertiser-name", Link.class, "advertiserName");
			xstream.aliasField("category", Link.class, "category");
			xstream.addImplicitCollection(Link.class, "category");
			xstream.aliasField("link-code-html", Link.class, "linkCodeHtml");
			xstream.aliasField("description", Link.class, "description");
			xstream.aliasField("destination", Link.class, "destination");
			xstream.aliasField("link-id", Link.class, "linkId");
			xstream.aliasField("link-name", Link.class, "linkName");
			
			
			//ignore
			xstream.omitField(Link.class, "click-commission");
			xstream.omitField(Link.class, "creative-height");
			xstream.omitField(Link.class, "creative-width");
			xstream.omitField(Link.class, "language");
			xstream.omitField(Link.class, "lead-commission");
			xstream.omitField(Link.class, "link-type");
			xstream.omitField(Link.class, "link-code-javascript");
			xstream.omitField(Link.class, "performance-incentive");
			xstream.omitField(Link.class, "promotion-end-date");
			xstream.omitField(Link.class, "promotion-start-date");
			xstream.omitField(Link.class, "promotion-type");
			xstream.omitField(Link.class, "relationship-status");
			xstream.omitField(Link.class, "sale-commission");
			xstream.omitField(Link.class, "seven-day-epc");
			xstream.omitField(Link.class, "three-month-epc");
			
			NetworkResponse response = (NetworkResponse)xstream.fromXML(baos.toString());
			this.links = response.getLinks().getLinks();
			

		} catch (MalformedURLException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} 
	}

	public List<Link> getLinks() {
		return links;
	}


	public void setAdvertiserIds(String advertiserIds) {
		this.advertiserIds = advertiserIds;
	}
	
	

}
