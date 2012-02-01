
package com.sightlyinc.ratecred.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Product;
import com.sightlyinc.ratecred.model.Publisher;

@Repository
public class ProductDaoImpl 
extends AbstractDao<Product>  
	implements ProductDao {

	static Logger logger = Logger.getLogger(ProductDaoImpl.class);
    
    public ProductDaoImpl() {
		super(Product.class);
	}

	@Override
	public Product findBySku(final String sku) {
		return (Product)getHibernateTemplateOverride().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException 
			{

				Query query = session.createQuery(
					"select distinct entityimpl from "+Product.class.getName()+
					" as entityimpl where entityimpl.productSku = :productSku");
				
				query.setString("productSku", sku);
				
				
				Product t = (Product)query.uniqueResult();
							
				return t;
	
			}
		});	
	}

   
}
