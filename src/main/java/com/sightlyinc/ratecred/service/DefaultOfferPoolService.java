package com.sightlyinc.ratecred.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.client.offers.OfferClient;
import com.sightlyinc.ratecred.client.offers.OfferFeedException;

public class DefaultOfferPoolService implements OfferPoolService {
	
	
	static Logger logger = 
		Logger.getLogger(DefaultOfferPoolService.class);
	
	private List<OfferClient> clients;
	
	private List<Offer> offerPool = new ArrayList<Offer>();
	
	private Boolean fetchDisabled = false;
	
	public DefaultOfferPoolService() {
		super();
		logger.debug("constructor");
	}	
	
	public void refresh() {
		if(!fetchDisabled)
		{
			offerPool.clear();
			for (OfferClient client : clients) {			
				try {
					List<Offer> o = client.getOffers();
					logger.debug("offer count:"+o.size());
					offerPool.addAll(o);
				} catch (OfferFeedException e) {
					logger.error("OfferFeedException", e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.service.OfferPoolService#getOfferPool()
	 */
	public List<Offer> getOfferPool()
	{
		return offerPool;
		
	}


	public void setClients(List<OfferClient> clients) {
		this.clients = clients;
	}

	@Override
	public Offer getOfferByExternalIdSource(String externalId, String sourceName) {
		Long extLong = Long.parseLong(externalId);
		for (Offer offer : offerPool) {
			
			logger.debug(offer.getExternalId()+":"+offer.getExternalSource());
			if(offer.getExternalSource().equals(sourceName) && extLong.equals(offer.getExternalId()))
				return offer;
		}
		return null;
	}

	public void setFetchDisabled(Boolean fetchDisabled) {
		this.fetchDisabled = fetchDisabled;
	}
	
	

}
