package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.Rater;

public interface OrderDao {
	
	public Order findByPrimaryKey(Long id);	
	public Order findByChannelAndExternalId(String channel, String externalId); 
	public List<Order> findByOwner(Rater towards);	
	public void delete(Order entity);	
	public void save(Order entity);
	public List<Order> findAll();	
}
