package com.sightlyinc.ratecred.service;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.dao.AwardDao;
import com.sightlyinc.ratecred.dao.AwardOfferDao;
import com.sightlyinc.ratecred.dao.AwardTypeDao;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardOffer;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;


public class AwardManagerServiceImpl implements AwardManagerService {
	
	static Logger logger = 
		Logger.getLogger(AwardManagerServiceImpl.class);
	
	@Autowired
	private AwardTypeDao awardTypeDao;
	
	@Autowired
	private AwardDao awardDao;
	
	@Autowired
	private AwardOfferDao awardOfferDao; 
	
	
	@Override
	public Long saveAward(Award award) throws BLServiceException {
		if(award.getId()==null)
		{
			award.setTimeCreatedMills(Calendar.getInstance().getTimeInMillis());			
		}
		
		awardDao.save(award);
		return award.getId();
	}


	@Override
	public List<Award> findBusinessAwards(Business b)
			throws BLServiceException {
		return awardDao.findByBusiness(b);
	}


	@Override
	public Award findAwardByPrimaryKey(Long awardId) throws BLServiceException {
		return awardDao.findByPrimaryKey(awardId);
		
	}


	@Override
	public AwardOffer findAwardOfferByPrimaryKey(Long awardOfferId)
			throws BLServiceException {
		return awardOfferDao.findByPrimaryKey(awardOfferId);
	}


	@Override
	public List<AwardOffer> findBusinessAwardOffers(Business b)
			throws BLServiceException {
		return awardOfferDao.findByBusiness(b);
	}


	@Override
	public AwardType findAwardTypeByKey(String key)
			throws BLServiceException {
		return awardTypeDao.findByKeyname(key);
	}
	
	@Override
	public List<AwardType> findAllAwardTypes() throws BLServiceException {
		return awardTypeDao.findAll();
	}


	@Override
	public List<AwardType> findBusinessAwardTypes() {
		return awardTypeDao.findByType("business");
	}
	
	
	@Override
	public void saveAwardOffer(AwardOffer entity) throws BLServiceException {
		awardOfferDao.save(entity);
	}	

	public void setAwardTypeDao(AwardTypeDao awardTypeDao) {
		this.awardTypeDao = awardTypeDao;
	}


	public void setAwardDao(AwardDao awardDao) {
		this.awardDao = awardDao;
	}


	public void setAwardOfferDao(AwardOfferDao awardOfferDao) {
		this.awardOfferDao = awardOfferDao;
	}



}
