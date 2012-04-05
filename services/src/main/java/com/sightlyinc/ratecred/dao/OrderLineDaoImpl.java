
package com.sightlyinc.ratecred.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Order;
import com.sightlyinc.ratecred.model.OrderLine;

@Repository
public class OrderLineDaoImpl 
	extends AbstractDao<OrderLine> 
	implements OrderLineDao {

	static Logger logger = Logger.getLogger(OrderLineDaoImpl.class);
    
      
	public OrderLineDaoImpl() {
		super(OrderLine.class);
	}


    @Override
    public List<OrderLine> findByNameAndOrder(String name, Order o) {
        Query q = super.getSession().createQuery(
                "select e from " + OrderLine.class.getName()+
                " as e where e.itemSku.name = :name" +
                " and e.order.id = :orderId" );
        q.setString("name", name);
        q.setLong("orderId", o.getId());
        return q.list();
    }


//    @Override
//    public List<OrderLine> findByNameAndOrder(String name, Order o) {
//        Query q = super.getSession().createQuery("select e from "+OrderLine.class.getName()+
//                " as e where e.itemSku.name = :name");
//        q.setString("name", name);
//        return q.list();
//    }
  	
	  
}
