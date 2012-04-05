package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.OrderLine;

public interface OrderLineDao extends BaseDao<OrderLine> {
    public List<OrderLine> findByNameAndOrder(String name, Order o);
    
}
