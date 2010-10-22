package com.sightlyinc.ratecred.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.client.link.Link;
import com.sightlyinc.ratecred.client.link.LinkClient;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;

public class DefaultLinkManagerService implements LinkManagerService {
	
	
	static Logger logger = 
		Logger.getLogger(DefaultLinkManagerService.class);
	
	private LinkClient linkClient;
	
	private int maxLinks = 4;
	
	
	
	public DefaultLinkManagerService() {
		super();
		logger.debug("constructor");
	}



	@Override
	public List<Link> getLinks() throws BLServiceException {
		return linkClient.getLinks();
	}
	

	/**
	 * LAME
	 */
	@Override
	public List<Link> getLinksForCityState(PlaceCityState pcs)
			throws BLServiceException {
		Map<String,Link> cslinks = new HashMap<String,Link>();
		int count = 0;
		for (Link link : this.linkClient.getLinks()) {
			if(link.getDescription().contains(pcs.getCity()) ||
					link.getDescription().contains(pcs.getState()))
			{
				cslinks.put(link.getLinkId(),link);
				count++;
			}
			if(count>=this.maxLinks)
				return new ArrayList<Link>(cslinks.values());
		}
		if(count<this.maxLinks)
		{
			for (Link link : this.linkClient.getLinks()) {
				cslinks.put(link.getLinkId(),link);
				count++;
				if(count>=this.maxLinks)
					return new ArrayList<Link>(cslinks.values());
			}
		}
		
		return new ArrayList<Link>(cslinks.values());
	}

	@Override
	public List<Link> getLinksForPlace(Place p) throws BLServiceException {
		PlaceCityState pcs = new PlaceCityState();
		pcs.setCity(p.getCity());
		pcs.setState(p.getState());
		return getLinksForCityState(pcs);
	}

	public void setLinkClient(LinkClient linkClient) {
		this.linkClient = linkClient;
	}	
	
	
	
	
	

}
