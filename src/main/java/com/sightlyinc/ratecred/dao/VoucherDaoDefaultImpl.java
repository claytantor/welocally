
package com.sightlyinc.ratecred.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sightlyinc.ratecred.model.Voucher;

@Repository("voucherDao")
public class VoucherDaoDefaultImpl 
extends AbstractDao<Voucher> 
	implements VoucherDao {

	static Logger logger = Logger.getLogger(VoucherDaoDefaultImpl.class);

	public VoucherDaoDefaultImpl(Class<Voucher> persistentClass) {
		super(Voucher.class);
	}
 	  
}
