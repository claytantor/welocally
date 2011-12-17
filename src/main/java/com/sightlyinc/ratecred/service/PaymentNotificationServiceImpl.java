package com.sightlyinc.ratecred.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sightlyinc.ratecred.dao.PaymentNotificationDao;
import com.sightlyinc.ratecred.model.PaymentNotification;


@Service("paymentNotificationService")
@Transactional
public class PaymentNotificationServiceImpl implements PaymentNotificationService {
	
	static Logger logger = 
		Logger.getLogger(PaymentNotificationServiceImpl.class);
	
	@Autowired
	PaymentNotificationDao paymentNotificationDao;

	@Override
	public List<PaymentNotification> findByPublisherKey(String publisherKey) {
		return paymentNotificationDao.findByPublisherKey(publisherKey);
	}

	@Override
	public PaymentNotification findByTxId(String txId) {
		return paymentNotificationDao.findByTxId(txId);
	}
	
	public Long save(PaymentNotification notfication){
		return paymentNotificationDao.save(notfication);
	}
		

}
