package com.sightlyinc.ratecred.dao;

import java.util.List;

import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Voucher;

public interface VoucherDao {
	
	public Voucher findByPrimaryKey(Long id);			
	public void delete(Voucher entity);	
	public void save(Voucher entity);
	public List<Voucher> findAll();	
}
