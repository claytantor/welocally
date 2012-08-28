package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.PaymentNotification;

public interface PaymentNotificationDao extends BaseDao<PaymentNotification> {
	
	public PaymentNotification findByTxId(String txId); 
	
	public List<PaymentNotification> findByPublisherKey(String publisherKey); 	

}
