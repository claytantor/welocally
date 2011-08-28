package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.Voucher;

public interface OrderManagerService {
	
	public Order findOrderByPrimaryKey(Long id) throws BLServiceException;
	
	public Order findOrderByTxId(String txId) throws BLServiceException;
	
	public Order findOrderByChannelAndExternalId(String channel, String externalId) 
		throws BLServiceException;
	public List<Order> findOrdersByRater(Patron rater) throws BLServiceException;
	
	public Long saveOrder(Order order) throws BLServiceException;
	
	public Voucher findVoucherByPrimaryKey(Long id) throws BLServiceException;

}
