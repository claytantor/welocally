package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.Patron;

public interface PatronDao extends BaseDao<Patron> {	
	public List<Patron> findByPrimaryKeys( Long[] ids);
	
	public Patron findByUserName(String userName);	
	
	public List<Patron> findByUserNames(String[] userNames);
	public List<Patron> findByStatus(String status);
	
	public Patron findBySecretKey(String secretKey);	
	
	public Patron findByAuthId(String authId);	
	
	public List<Patron> findByBusinessDateRange(Business b, Date startDate, Date endDate);
	
	public List<Patron> findByBusinessLocationDateRange(BusinessLocation bl, Date startDate, Date endDate);
	
	public List<Patron> findByScorePaged(
			int pageNum, final int pageSize, boolean isAscending);
	
	public List<Patron> findByCityStateScorePaged(
			String city, String state, int pageNum, final int pageSize, boolean isAscending);
	
}
