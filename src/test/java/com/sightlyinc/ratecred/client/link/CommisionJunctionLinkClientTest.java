package com.sightlyinc.ratecred.client.link;

import java.util.List;

import org.apache.log4j.Logger;

import com.noi.utility.spring.test.AbstractBeanFactoryTestCase;




public class CommisionJunctionLinkClientTest extends AbstractBeanFactoryTestCase {
	
	static Logger logger = 
		Logger.getLogger(CommisionJunctionLinkClientTest.class);

	@Override
	protected String[] getSpringResources() {
		return new String[] {
				"/linkClient-beans.xml"
		};
	}

	public void testLoadLinks()
	{
		logger.debug("testLoadLinks");
		LinkClient linkClient = (LinkClient)super.getBeanFactory().getBean("linkClient"); 
		List<Link> links = linkClient.getLinks();
		for (Link link : links) {
			logger.debug(link.toString());
		}
	}

}
