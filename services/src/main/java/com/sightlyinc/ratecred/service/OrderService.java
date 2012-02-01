package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Product;

public interface OrderService {
	
	public Order findOrderByPrimaryKey(Long id) throws BLServiceException;
	
	public Order findOrderByTxId(String txId) throws BLServiceException;
	
	public List<Order> findOrderByPublisherKey(String publisherKey);
	
	public Order makeOrderFromProduct(Product p) throws BLServiceException;
	
	public Order findOrderByChannelAndExternalId(String channel, String externalId) 
		throws BLServiceException;
	
	
	
	public Long saveOrder(Order order) throws BLServiceException;
	
	
}
