package com.sightlyinc.ratecred.service;

import java.util.Date;
import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Business;
import com.sightlyinc.ratecred.model.BusinessAttribute;
import com.sightlyinc.ratecred.model.BusinessLocation;
import com.sightlyinc.ratecred.model.BusinessMetrics;
import com.sightlyinc.ratecred.model.Offer;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.pojo.BusinessMetricsMetadata;
import com.sightlyinc.ratecred.pojo.PatronBusinessMetrics;

public interface BusinessManagerService {
		
	public Business findBusinessByPrimaryKey(Long id) throws BLServiceException;
	public Business findBusinessByAdvertiserIdAndSource( String advertiserId,
			 String advertiserSource) throws BLServiceException;	
	public Business findBusinessByAuthId(String guid) throws BLServiceException;	
	public Business findBusinessByUsername(String username) throws BLServiceException;
	
	public List<Business> findAllBusinesss() throws BLServiceException;		
	
	public List<Business> findBusinesssByGeoBounding(
			Double minLat, Double minLong, Double maxLat, Double maxLong) 
			throws BLServiceException;
	
	public BusinessLocation findBusinessLocationByPrimaryKey(Long id) throws BLServiceException;

	public List<BusinessMetrics> findMinedBusinesssLocationMetricsTrailingDays(
			BusinessLocation bl, int days) throws BLServiceException;
	
	public List<BusinessMetrics> findMinedBusinesssMetricsTrailingDays(
			Business b, int days) throws BLServiceException;
	
	public List<BusinessMetrics> findBusinesssMetricsTrailingDays(
			Business b, int days) throws BLServiceException;
	
	public List<BusinessMetrics> findBusinesssLocationMetricsTrailingDays(
			BusinessLocation bl, int days) throws BLServiceException;

	public List<PatronBusinessMetrics> findMinedRaterBusinesssLocationMetricsTrailingDays(
			BusinessLocation bl, int days) throws BLServiceException;	
	
	public List<PatronBusinessMetrics> findMinedRaterBusinesssLocationMetricsTrailingDaysRaters(
			BusinessLocation bl, int days, Long[] raterIds) throws BLServiceException;	

	public List<PatronBusinessMetrics> findMinedRaterBusinesssMetricsTrailingDaysRaters(
			Business b, int days, Long[] raterIds) throws BLServiceException;	 

	public List<PatronBusinessMetrics> findMinedRaterBusinesssMetricsTrailingDays(
			Business b, int days) throws BLServiceException;	
	
	public List<BusinessMetrics> findMinedBusinesssLocationMetricsSince(
			BusinessLocation bl, Date d) throws BLServiceException;
	
	public Long findLastCreatedBusinesssLocationMetricsTime(
			BusinessLocation bl) throws BLServiceException;
	
	public Long findLastMinedBusinesssLocationMetricsTime(
			BusinessLocation bl) throws BLServiceException;
	
	public Long findFirstMinedBusinesssLocationMetricsTime(
			BusinessLocation bl) throws BLServiceException;
	
	public BusinessMetrics findBusinessMetricsByLocationAndStartTime(
			BusinessLocation bl, Long startTime)
			throws BLServiceException;
	
	
	public BusinessMetricsMetadata makeMetadataFromBussinessMetrics(List<BusinessMetrics> metrics)
		throws BLServiceException;
	
	
	public List<Patron> findBusinessRatersOverPeriod(Business b, Date stateDate, Date endDate)
		throws BLServiceException;
	
	public List<Patron> findBusinessLocationRatersOverPeriod(BusinessLocation bl, Date stateDate, Date endDate)
		throws BLServiceException;

	
	public List<Patron> findBusinessRatersDaysTrailing(Business b, Integer period)
		throws BLServiceException;

	public List<Patron> findBusinessLocationRatersDaysTrailing(BusinessLocation bl, Integer period)
		throws BLServiceException;	
	
	public List<AwardType> findBusinessAwardTypes() throws BLServiceException;
	public List<Offer> findBusinessAwardOffers(Business b) throws BLServiceException;
	
	public List<Award> findBusinessAwards(Business b) throws BLServiceException;
	
	public Offer findAwardOfferByPrimaryKey(Long awardOfferId) throws BLServiceException;
	
	public AwardType findAwardTypeByKey(String key) throws BLServiceException;
	
	public List<BusinessLocation> findBusinessLocationByInfo(
			String name, String address, String city, String state, String postalCode ) throws BLServiceException;
				
	public Long saveBusiness(Business p) throws BLServiceException;
	public void savePlaceForBusinessLocation(Business b, Place p) throws BLServiceException;
	public void saveBusinessLocation(BusinessLocation location) throws BLServiceException;
	
	public void saveBusinessAwardOffer(Offer ao) throws BLServiceException;
	public void saveBusinessAward(Offer ao, String notes, Date expires, Patron t) throws BLServiceException;
	public Long saveBusinessMetrics(BusinessMetrics bm) throws BLServiceException; 

	public void deleteBusinessAttribute(Business p, BusinessAttribute attrs) throws BLServiceException;
	public void deleteBusiness(Business p) throws BLServiceException;
	public void deleteBusinessLocation(BusinessLocation locaction) throws BLServiceException;
		
}
