package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Patron;

public interface OrderDao extends BaseDao<Order> {
	
	public Order findByChannelAndExternalId(String channel, String externalId); 
	
	public Order findByTxId(String txId); 
	
	public List<Order> findByOwner(Patron towards);	

}
