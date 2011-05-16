package com.sightlyinc.ratecred.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.noi.utility.random.RandomMaker;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.client.link.LinkClient;
import com.sightlyinc.ratecred.client.link.LinkClientRequest;
import com.sightlyinc.ratecred.client.link.NetworkResponse;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;

public class DefaultLinkManagerService implements LinkManagerService {
	
	
	static Logger logger = 
		Logger.getLogger(DefaultLinkManagerService.class);
	
	/*private LinkClient linkClient;
	
	private LinkClientRequest webLinksRequest;
	
	//private List<AffiliateLink> linksCache =  new ArrayList<AffiliateLink>();
	
	private int maxLinks = 4;
	
	private Boolean fetchDisabled = false;
	
	
	
	public DefaultLinkManagerService() {
		super();
		logger.debug("constructor");
	}


	public void initLinkCache() {
		if(false)
		{

				NetworkResponse response = linkClient.getNetworkResponse(webLinksRequest);
				if(response != null)
				{
					logger.debug("total matched:"+response.getLinks().getTotalMatched());
					
					this.linksCache = 
							response.getLinks().getLinks();
				}
				else
					logger.debug("no linkes fetched!");
				
			
		}
	}

	@Override
	public List<AffiliateLink> getLinks() throws BLServiceException {
		return linksCache;
	}
	

	*//**
	 * LAME
	 *//*
	@Override
	public List<AffiliateLink> getLinksForCityState(PlaceCityState pcs)
			throws BLServiceException {
		Map<String,AffiliateLink> cslinks = new HashMap<String,AffiliateLink>();
		if(linksCache.size()>0)
		{
			//int count = 0;
			for (AffiliateLink link : linksCache) {
				if(link.getDescription().contains(pcs.getCity()) ||
						link.getDescription().contains(pcs.getState()))
					cslinks.put(link.getLinkId(),link);
	
				if(cslinks.entrySet().size()>=this.maxLinks)
					return new ArrayList<AffiliateLink>(cslinks.values());
			}
			
			//random sample cant target
			while(cslinks.entrySet().size()<this.maxLinks)
			{
				int pos = RandomMaker.nextInt(1, linksCache.size());
				AffiliateLink link = linksCache.get(pos);
				cslinks.put(link.getLinkId(),link);
			}
			
			return new ArrayList<AffiliateLink>(cslinks.values());
		}
		else 
			return this.linksCache;
	}

	@Override
	public List<AffiliateLink> getLinksForPlace(Place p) throws BLServiceException {
		PlaceCityState pcs = new PlaceCityState();
		pcs.setCity(p.getCity());
		pcs.setState(p.getState());
		return getLinksForCityState(pcs);
	}

	public void setLinkClient(LinkClient linkClient) {
		this.linkClient = linkClient;
	}


	public void setWebLinksRequest(LinkClientRequest webLinksRequest) {
		this.webLinksRequest = webLinksRequest;
	}


	public void setMaxLinks(int maxLinks) {
		this.maxLinks = maxLinks;
	}


	public void setFetchDisabled(Boolean fetchDisabled) {
		this.fetchDisabled = fetchDisabled;
	}	*/
	


}
