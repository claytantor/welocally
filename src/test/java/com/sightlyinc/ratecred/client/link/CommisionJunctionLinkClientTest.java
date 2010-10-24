package com.sightlyinc.ratecred.client.link;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;

import com.noi.utility.spring.test.AbstractBeanFactoryTestCase;
import com.sightlyinc.ratecred.model.AffiliateLink;




public class CommisionJunctionLinkClientTest extends AbstractBeanFactoryTestCase {
	
	static Logger logger = 
		Logger.getLogger(CommisionJunctionLinkClientTest.class);

	@Override
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
		} catch (BeansException e) {
			logger.error("fail", e);
			fail(e.getMessage());
		} catch (MalformedURLException e) {
			logger.error("fail", e);
			fail(e.getMessage());
		} catch (IOException e) {
			logger.error("fail", e);
			fail(e.getMessage());
		}
	}

}
