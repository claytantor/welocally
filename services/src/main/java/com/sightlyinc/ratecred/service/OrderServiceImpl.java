package com.sightlyinc.ratecred.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.dao.OrderDao;
import com.sightlyinc.ratecred.dao.OrderLineDao;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.OrderLine;
import com.sightlyinc.ratecred.model.Product;
import com.sightlyinc.ratecred.model.ProductLine;
import com.sightlyinc.ratecred.model.OrderLine.OrderLineStatus;


@Service("orderManagerService")
@Transactional
public class OrderServiceImpl implements OrderService {
	
	static Logger logger = 
		Logger.getLogger(OrderServiceImpl.class);
		
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderLineDao orderLineDao;



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

	@Override
	public Order makeOrderFromProduct(Product p) throws BLServiceException {	
		if(p.getStatus().equals(Product.ProductStatus.AVAILABLE)){
			Order o = new Order();
			o.setProduct(p);
			Set<OrderLine> olines = new HashSet<OrderLine>();
			for (ProductLine pline :p.getProductItems()) {
				OrderLine ol = new OrderLine();
				ol.setItemSku(pline.getItemSku());
				ol.setQuantityOrig(pline.getQuantity());
				ol.setQuantityUsed(0);
				ol.setStatus(OrderLineStatus.AVAILABLE);
				ol.setStartTime(Calendar.getInstance().getTimeInMillis());
				ol.setEndTime(Calendar.getInstance().getTimeInMillis()+pline.getItemSku().getPeriod());
				ol.setTimeCreated(Calendar.getInstance().getTimeInMillis());
				ol.setTimeUpdated(Calendar.getInstance().getTimeInMillis());
				orderLineDao.save(ol);
				olines.add(ol);
			}
			o.setOrderLines(olines);
			return o;
		}		
		return null;
	}

	
	






}
