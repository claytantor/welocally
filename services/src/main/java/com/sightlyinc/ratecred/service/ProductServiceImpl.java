package com.sightlyinc.ratecred.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sightlyinc.ratecred.dao.BaseDao;
import com.sightlyinc.ratecred.dao.ProductDao;
import com.sightlyinc.ratecred.model.Product;

@Service
public class ProductServiceImpl extends AbstractTransactionalService<Product>
		implements ProductService {
	
	@Autowired
	private ProductDao productDao; 

	@Override
	public BaseDao<Product> getDao() {
		return productDao;
	}

	@Override
	public Product findProductBySku(String sku) {
		return productDao.findBySku(sku);
	}




}
