package com.sightlyinc.ratecred.service;

import java.math.BigDecimal;
import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Product;
import com.sightlyinc.ratecred.model.Site;

public interface OrderService extends BaseService<Order> {
	
	
	public Order findOrderByTxId(String txId) throws BLServiceException;
	
	public List<Order> findOrderByPublisherKey(String publisherKey);
	
	public Order makeOrderFromProduct(Product p) throws BLServiceException;
	
	public BigDecimal calculateTotal(Order o);
	
	public void setOrderProduct(Order o, Product p) throws BLServiceException;
	
	public Order findOrderByChannelAndExternalId(String channel, String externalId) 
		throws BLServiceException;
	
	public List<Order> findByDaysTrailing(final int days);

	
}
