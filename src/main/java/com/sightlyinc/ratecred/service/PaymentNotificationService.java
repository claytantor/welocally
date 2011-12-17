package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PaymentNotification;
import com.sightlyinc.ratecred.model.Voucher;

public interface PaymentNotificationService {
	
	public PaymentNotification findByTxId(String txId); 
	
	public List<PaymentNotification> findByPublisherKey(String publisherKey); 	
	
	public Long save(PaymentNotification notfication);

		
}
