package com.sightlyinc.ratecred.client.link;

import org.apache.log4j.Logger;




public class CommisionJunctionLinkClientTest {
	
	static Logger logger = 
		Logger.getLogger(CommisionJunctionLinkClientTest.class);

	/*@Override
	protected String[] getSpringResources() {
		return new String[] {
				"/linkClient-beans.xml",
				"/com/sightlyinc/ratecred/client/link/linktest-property-beans.xml"
		};
	}

	public void testLoadLinks()
	{
		logger.debug("testLoadLinks");
		try {
			LinkClient linkClient = (LinkClient)getBeanFactory().getBean("linkClient"); 
			LinkClientRequest webLinksRequestModel = (LinkClientRequest)getBeanFactory().getBean("webLinksRequestModel"); 
			NetworkResponse response = linkClient.getNetworkResponse(webLinksRequestModel);
			List<AffiliateLink> links = response.getLinks().getLinks();
			for (AffiliateLink link : links) {
				logger.debug(link.toString());
			}
		} catch (Exception e) {
			logger.error("fail", e);
			fail(e.getMessage());
		} 
	}*/

}
