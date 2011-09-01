package com.sightlyinc.ratecred.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.dao.OrderDao;
import com.sightlyinc.ratecred.dao.VoucherDao;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.Voucher;


@Service("orderManagerService")
@Transactional
public class OrderManagerServiceImpl implements OrderManagerService {
	
	static Logger logger = 
		Logger.getLogger(OrderManagerServiceImpl.class);
		
	@Autowired
	private OrderDao orderDao;



	@Override
	public List<Order> findOrderByPublisherKey(String publisherKey) {
		return orderDao.findByPublisherKey(publisherKey);
	}

	@Override
	public Long saveOrder(Order order) throws BLServiceException {
		orderDao.save(order);
		return order.getId();
	}

	@Override
	public Order findOrderByChannelAndExternalId(String channel, String externalId)
			throws BLServiceException {
		return orderDao.findByChannelAndExternalId(channel, externalId);
	}

	@Override
	public Order findOrderByTxId(String txId) throws BLServiceException {
		return orderDao.findByTxId(txId);
	}

	@Override
	public Order findOrderByPrimaryKey(Long id) throws BLServiceException {
		return orderDao.findByPrimaryKey(id);
	}

	
	






}
