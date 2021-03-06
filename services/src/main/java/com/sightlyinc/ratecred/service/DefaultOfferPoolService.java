package com.sightlyinc.ratecred.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.client.offers.OfferClient;
import com.sightlyinc.ratecred.client.offers.OfferFeedException;
import com.sightlyinc.ratecred.client.offers.OfferOld;

@Service("offerPoolService")
public class DefaultOfferPoolService implements OfferPoolService {
		
	static Logger logger = 
		Logger.getLogger(DefaultOfferPoolService.class);
	
	private List<OfferClient> clients = new ArrayList<OfferClient>();
	
	private List<OfferOld> offerPool = new ArrayList<OfferOld>();
	
	private Boolean fetchDisabled = false;
	
	public DefaultOfferPoolService() {
		super();
		logger.debug("constructor");
	}	
	
	
	@Autowired
	@Qualifier("ratecredOfferClient")
	private OfferClient offerClient;
	
	
	@Override
	public void addOffersToPool(List<OfferOld> offers) {
		offerPool.addAll(offers);		
	}

	
	public void refresh() {
		

		//get the clients
		if(clients.size() == 0) {
			//setup the adility requests			
			clients.add(offerClient);
		}
				
		//try to fetch
		try {
			if(!fetchDisabled)
			{
				offerPool.clear();
//				for (OfferClient client : clients) {			
//					try {
//						List<OfferOld> o = client.getOffers();
//						logger.debug("offer count:"+o.size());
//						offerPool.addAll(o);
//					} catch (OfferFeedException e) {
//						logger.error("OfferFeedException", e);
//					}
//				}
			}
		} catch (Exception e) {
			logger.error("cannot refresh", e);
		}
		
		logger.debug("offers fetched");
	}

	/* (non-Javadoc)
	 * @see com.sightlyinc.ratecred.service.OfferPoolService#getOfferPool()
	 */
	public List<OfferOld> getOfferPool()
	{
		return offerPool;		
	}


	public void setClients(List<OfferClient> clients) {
		this.clients = clients;
	}

	@Override
	public OfferOld getOfferByExternalIdSource(String externalId, String sourceName) {
		//Long extLong = Long.parseLong(externalId);
		for (OfferOld offer : offerPool) {
			
			if(offer.getExternalSource().equals(sourceName) && offer.getExternalId().equals(externalId))
				return offer;
		}
		return null;
	}

	public void setFetchDisabled(Boolean fetchDisabled) {
		this.fetchDisabled = fetchDisabled;
	}
	
	

}
