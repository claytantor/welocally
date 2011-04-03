package com.sightlyinc.ratecred.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.client.offers.Offer;
import com.sightlyinc.ratecred.client.offers.OfferClient;
import com.sightlyinc.ratecred.client.offers.OfferFeedException;

@Service("offerPoolService")
public class DefaultOfferPoolService implements OfferPoolService {
		
	static Logger logger = 
		Logger.getLogger(DefaultOfferPoolService.class);
	
	private List<OfferClient> clients = new ArrayList<OfferClient>();
	
	private List<Offer> offerPool = new ArrayList<Offer>();
	
	private Boolean fetchDisabled = false;
	
	public DefaultOfferPoolService() {
		super();
		logger.debug("constructor");
	}	
	
	
	//we are doing this to get over autowiring problems with the 
	//client list
	
	@Autowired
	@Qualifier("adilityOfferClient")
	private OfferClient adilityOfferClient;
	
	
	@Override
	public void addOffersToPool(List<Offer> offers) {
		offerPool.addAll(offers);		
	}

	@PostConstruct
	public void refresh() {
		
		//get the clients
		if(clients.size() == 0) {
			//setup the adility requests			
			clients.add(adilityOfferClient);
		}
				
		//try to fetch
		try {
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
		} catch (Exception e) {
			logger.error("cannot refresh", e);
		}
		
		logger.debug("offers fetched");
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
		//Long extLong = Long.parseLong(externalId);
		for (Offer offer : offerPool) {
			
			logger.debug(offer.getExternalId()+":"+offer.getExternalSource());
			logger.debug(externalId+":"+sourceName);
			if(offer.getExternalSource().equals(sourceName) && offer.getExternalId().equals(externalId))
				return offer;
		}
		return null;
	}

	public void setFetchDisabled(Boolean fetchDisabled) {
		this.fetchDisabled = fetchDisabled;
	}
	
	

}
