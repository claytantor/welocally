package com.sightlyinc.ratecred.dao;

import java.util.Date;
import java.util.List;

import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.Rater;

public interface RaterDao {
	public Rater findByPrimaryKey(Long id);	
	public List<Rater> findByPrimaryKeys( Long[] ids);
	
	//public Rater findByTwitterScreenName(String twitterScreenName);	
	
	public Rater findByUserName(String userName);	
	
	public List<Rater> findByUserNames(String[] userNames);
	public List<Rater> findByStatus(String status);
	
	public Rater findBySecretKey(String secretKey);	
	
	public Rater findByAuthId(String authId);	
	
	public List<Rater> findByBusinessDateRange(Business b, Date startDate, Date endDate);
	
	public List<Rater> findByBusinessLocationDateRange(BusinessLocation bl, Date startDate, Date endDate);
	
	public List<Rater> findAll();	
		
	public Rater create();
		
	public void delete(Rater entity);
	
	public void save(Rater entity);
	
	public List<Rater> findByScorePaged(
			int pageNum, final int pageSize, boolean isAscending);
	
	public List<Rater> findByCityStateScorePaged(
			String city, String state, int pageNum, final int pageSize, boolean isAscending);
	
	

}
