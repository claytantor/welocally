package com.sightlyinc.ratecred.service;

import com.sightlyinc.ratecred.model.Product;

public interface ProductService extends BaseService<Product> {
	
	public Product findProductBySku(String sku);

}
