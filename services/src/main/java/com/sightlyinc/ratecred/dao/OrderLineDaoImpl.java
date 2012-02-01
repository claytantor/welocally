
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.OrderLine;

@Repository
public class OrderLineDaoImpl 
	extends AbstractDao<OrderLine> 
	implements OrderLineDao {

	static Logger logger = Logger.getLogger(OrderLineDaoImpl.class);
    
   
    
	public OrderLineDaoImpl() {
		super(OrderLine.class);
	}


    	
	  
}
