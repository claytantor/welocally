package com.sightlyinc.ratecred.dao;

import com.sightlyinc.ratecred.model.Product;


public interface ProductDao extends BaseDao<Product> {

    public Product findBySku(String sku);

}
