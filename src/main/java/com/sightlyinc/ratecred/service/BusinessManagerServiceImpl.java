package com.sightlyinc.ratecred.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.date.DateUtils;
import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.compare.BusinessMetricsStartDateComparitor;
import com.sightlyinc.ratecred.dao.AwardDao;
import com.sightlyinc.ratecred.dao.AwardTypeDao;
import com.sightlyinc.ratecred.dao.BusinessDao;
import com.sightlyinc.ratecred.dao.BusinessLocationDao;
import com.sightlyinc.ratecred.dao.BusinessMetricsDao;
import com.sightlyinc.ratecred.dao.OfferDao;
import com.sightlyinc.ratecred.dao.PatronBusinessMetricsDao;
import com.sightlyinc.ratecred.dao.PatronDao;
import com.sightlyinc.ratecred.dao.PlaceDao;
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

@Service("BusinessManagerService")
@Transactional
public class BusinessManagerServiceImpl implements BusinessManagerService {
	
	private static final long MILLS_DAY = 86400000l;
	
	static Logger logger = 
		Logger.getLogger(BusinessManagerServiceImpl.class);
	
	@Autowired
	private BusinessDao businessDao;
	
	@Autowired
	private BusinessLocationDao businessLocationDao;
	
	@Autowired
	private BusinessMetricsDao businessMetricsDao;
	
	@Autowired
	private PatronBusinessMetricsDao raterBusinessMetricsDao;
	
	@Autowired
	private PatronDao raterDao;	
	
	@Autowired
	private PlaceDao placeDao;
	
	@Autowired
	private AwardTypeDao awardTypeDao;
	
	@Autowired
	private AwardDao awardDao;
	
	@Autowired
	private OfferDao awardOfferDao; 
	
	@Value("imageUrl=/images/awards/award_")
	private String imageBase;
	
	@Value("_64.png")
	private String imageBaseType;
	

	@Override
	public List<BusinessLocation> findBusinessLocationByInfo(String name,
			String address, String city, String state, String postalCode)
			throws BLServiceException {
		BusinessLocation example = new BusinessLocation();
		example.setName(name);
		List<BusinessLocation> locations = businessLocationDao.findByExample(example);
		return locations;
	}


	@Override
	public Business findBusinessByAdvertiserIdAndSource(String advertiserId,
			String advertiserSource) throws BLServiceException {
		return businessDao.findByAdvertiserIdAndSource(advertiserId, advertiserSource);
	}


	@Override
	public List<PatronBusinessMetrics> findMinedRaterBusinesssLocationMetricsTrailingDaysRaters(
			BusinessLocation bl, int days, Long[] raterIds)
			throws BLServiceException {
		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(86400000l*days);
		
		List<PatronBusinessMetrics> all =
			new ArrayList<PatronBusinessMetrics>();
		
		try {
			return raterBusinessMetricsDao.mineBusinessLocationMetricsForRaters(
							bl, 
							raterIds, 
							Calendar.getInstance().getTime(), 
							new Date(startTime));
			
		} catch (Exception e) {
			logger.error("bad", e);
		}		
		
		return all;
	}


	@Override
	public List<PatronBusinessMetrics> findMinedRaterBusinesssMetricsTrailingDaysRaters(
			Business b, int days, Long[] raterIds)
			throws BLServiceException {
		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(86400000l*days);
		
		List<PatronBusinessMetrics> all =
			new ArrayList<PatronBusinessMetrics>();
		
		try {
			return raterBusinessMetricsDao.mineBusinessMetricsForRaters(
							b, raterIds, new Date(startTime), Calendar.getInstance().getTime());
		} catch (Exception e) {
			logger.error("bad", e);
		}		
		
		return all;
	}


	@Override
	public List<PatronBusinessMetrics> findMinedRaterBusinesssMetricsTrailingDays(
			Business b, int days) throws BLServiceException {

		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(86400000l*days);
		
		List<PatronBusinessMetrics> all =
			new ArrayList<PatronBusinessMetrics>();
		
		try {
			return raterBusinessMetricsDao.mineBusinessMetricsForDateRange(
							b, new Date(startTime), Calendar.getInstance().getTime());
		} catch (Exception e) {
			logger.error("bad", e);
		}		
		
		return all;
	}


	@Override
	public List<PatronBusinessMetrics> findMinedRaterBusinesssLocationMetricsTrailingDays(
			BusinessLocation bl, int days) throws BLServiceException {
		
		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(86400000l*days);
		
		List<PatronBusinessMetrics> all =
			new ArrayList<PatronBusinessMetrics>();
		
		try {
			return raterBusinessMetricsDao.mineBusinessLocationMetricsForDateRange(
							bl, new Date(startTime), Calendar.getInstance().getTime());
		} catch (Exception e) {
			logger.error("bad", e);
		}		
		
		return all;

	}


	@Override
	public BusinessMetrics findBusinessMetricsByLocationAndStartTime(
			BusinessLocation bl, Long startTime)
			throws BLServiceException {
		return businessMetricsDao.findBusinessMetricsByLocationAndStartTime(bl, startTime);
	}


	@Override
	public List<BusinessMetrics> findBusinesssLocationMetricsTrailingDays(
			BusinessLocation bl, int days) throws BLServiceException {
		
		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(86400000l*days);
		
		List<BusinessMetrics> all =
			new ArrayList<BusinessMetrics>();
		
		all.addAll(
				businessMetricsDao.findDailyByBusinessLocationDateRange(
						bl, new Date(startTime), Calendar.getInstance().getTime()));		
		
		return all;
	}


	@Override
	public List<BusinessMetrics> findBusinesssMetricsTrailingDays(Business b,
			int days) throws BLServiceException {
		
		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(MILLS_DAY*days);
		
		List<BusinessMetrics> all =
			new ArrayList<BusinessMetrics>();
		
		//zachs
		try {			
			for (BusinessLocation bl : b.getLocations()) {	
				List<BusinessMetrics> mloc =
					businessMetricsDao.findDailyByBusinessLocationDateRange(
							bl, 
							new Date(startTime), 
							new Date(startTime+((days+1)*MILLS_DAY)));
					
				all.addAll(mloc);
			}
			
		} catch (Exception e) {
			logger.error("problem mining data", e);
			throw new BLServiceException(e);
		}
		
		List<BusinessMetrics> combined = 
			combineLocationMetricsAcrossLocations(all);
		return combined;
	}


	@Override
	public Long findLastMinedBusinesssLocationMetricsTime(BusinessLocation bl)
			throws BLServiceException {
		return businessMetricsDao.mineLastBusinessMetricsTime(bl);
	}


	@Override
	public Long findFirstMinedBusinesssLocationMetricsTime(BusinessLocation bl)
			throws BLServiceException {
		return businessMetricsDao.mineFirstBusinessMetricsTime(bl);
	}


	@Override
	public Long saveBusinessMetrics(BusinessMetrics bm)
			throws BLServiceException {
		
		
		
		return businessMetricsDao.save(bm);
	}


	@Override
	public Long findLastCreatedBusinesssLocationMetricsTime(BusinessLocation bl)
			throws BLServiceException {
		return businessMetricsDao.findLastBusinessLocationMetricsTime(bl);
	}

	
	
	@Override
	public List<BusinessMetrics> findMinedBusinesssLocationMetricsSince(
			BusinessLocation bl, Date d) throws BLServiceException {
		
		//make the start mdnight of the day in question
		
		List<BusinessMetrics> mined = 
				mineBusinessLocationMetricsBetweenDates(
					bl, 
					DateUtils.midnight(d,Calendar.getInstance().getTimeZone()), 
					Calendar.getInstance().getTime());
		
		return mined;
	}
	
	private List<BusinessMetrics> mineBusinessLocationMetricsBetweenDates(
			BusinessLocation bl, Date start, Date end)
	{
		List<BusinessMetrics> mined =  new ArrayList<BusinessMetrics>();
		
		//midnight
		Long counter = start.getTime();
		
		
		Long ender = end.getTime();
		
		
		
		//Long now = Calendar.getInstance().getTimeInMillis();
		Long index = 0l;
		
		//one for each day since start
		while (counter<ender) {
			
			
			BusinessMetrics minedMetrics =
				businessMetricsDao.mineMetricsForDateRange(
						bl, 
						new Date(start.getTime()+(index*MILLS_DAY)), 
						new Date(start.getTime()+((index+1)*MILLS_DAY)));
			
			mined.add(minedMetrics);
			counter = start.getTime()+((index+1)*MILLS_DAY);
			index++;	
		}
		
		
		return mined;
	}


	@Override
	public List<Award> findBusinessAwards(Business b)
			throws BLServiceException {
		return awardDao.findByBusiness(b);
	}


	@Override
	public Offer findAwardOfferByPrimaryKey(Long awardOfferId)
			throws BLServiceException {
		return awardOfferDao.findByPrimaryKey(awardOfferId);
	}


	@Override
	public List<Offer> findBusinessAwardOffers(Business b)
			throws BLServiceException {
		return awardOfferDao.findByBusiness(b);
	}


	@Override
	public AwardType findAwardTypeByKey(String key)
			throws BLServiceException {
		return awardTypeDao.findByKeyname(key);
	}


	@Override
	public List<AwardType> findBusinessAwardTypes() {
		return awardTypeDao.findByType("business");
	}


	@Override
	public void saveBusinessAwardOffer(Offer ao)
			throws BLServiceException {
		//first save the award		
		awardOfferDao.save(ao);
			
	}


	@Override
	public void saveBusinessAward(Offer ao, String notes, Date expires, Patron t)
			throws BLServiceException {
		Award a = new Award();
		a.setAwardType(ao.getAward().getAwardType());
		a.setMetadata(imageBase+ao.getAward().getAwardType().getKeyname()+imageBaseType);
		a.getOffers().add(ao);
		a.setOwner(t);
		
		if(StringUtils.isNotEmpty(notes))
			a.setNotes(notes);
				
		//convert created time to gmt
		String gmtTime = DateUtils.dateToString(
				Calendar.getInstance().getTime(), 
				DateUtils.NOSPACE_TIMESTAMP_FORMAT, 
				new SimpleTimeZone(0, "GMT"))+"-0000";
		
		//done with interceptor
		/*Date gmtCreated = DateUtils.stringToDate(gmtTime, DateUtils.NOSPACE_TIMESTAMP_FORMAT_TZ);			
		a.setTimeCreated(gmtCreated);
		a.setTimeCreatedMills(gmtCreated.getTime());
		a.setTimeCreatedGmt(gmtTime);*/
		
		//convert expires
		String gmtExpires = DateUtils.dateToString(
				expires, 
				DateUtils.NOSPACE_TIMESTAMP_FORMAT, 
				new SimpleTimeZone(0, "GMT"))+"-0000";
		
		Date gmtExpiresDate = DateUtils.stringToDate(gmtExpires, DateUtils.NOSPACE_TIMESTAMP_FORMAT_TZ);			
		a.setExpires(gmtExpiresDate.getTime());
		
		/*a.setExpires(gmtExpiresDate);
		a.setExpiresMills(gmtExpiresDate.getTime());
		a.setExpiresGmt(gmtExpires);*/
		
		
		
		
		
		awardDao.save(a);
		
	}




	@Override
	public List<Patron> findBusinessLocationRatersDaysTrailing(
			BusinessLocation bl, Integer period)
			throws BLServiceException {
		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(86400000l*period);
		
		return raterDao.findByBusinessLocationDateRange(
				bl, new Date(startTime), Calendar.getInstance().getTime());
	}


	@Override
	public List<Patron> findBusinessRatersDaysTrailing(Business b,
			Integer period) throws BLServiceException {
		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(86400000l*period);
		
		List<Patron> braters = raterDao.findByBusinessDateRange(
				b, new Date(startTime), Calendar.getInstance().getTime());
		
		return braters;
	}

	private void populateRaterMetrics(List<Patron> braters)
	{
	
	}

	@Override
	public List<Patron> findBusinessLocationRatersOverPeriod(
			BusinessLocation bl, Date startDate, Date endDate)
			throws BLServiceException {
		return raterDao.findByBusinessLocationDateRange(bl, startDate, endDate);
	}


	@Override
	public List<Patron> findBusinessRatersOverPeriod(Business b,
			Date startDate, Date endDate) throws BLServiceException {
		return raterDao.findByBusinessDateRange(b, startDate, endDate);
	}


	@Override
	public BusinessMetricsMetadata makeMetadataFromBussinessMetrics(
			List<BusinessMetrics> metrics) throws BLServiceException {
			
		BusinessMetricsMetadata md = new BusinessMetricsMetadata();
		Integer[] allYays = new Integer[metrics.size()];
		Integer[] allBoos = new Integer[metrics.size()];
		Integer[] allRatings = new Integer[metrics.size()];
		int index=0;
		for (BusinessMetrics businessMetrics : metrics) {
			allYays[index] = businessMetrics.getYays();
			allBoos[index] = businessMetrics.getBoos();
			allRatings[index] = businessMetrics.getRatings();
			index++;
		}
		
		Arrays.sort(allYays);
		Arrays.sort(allBoos);
		Arrays.sort(allRatings);
		
		md.setMinYays(allYays[0]);
		md.setMaxYays(allYays[allYays.length-1]);
		md.setMinBoos(allBoos[0]);
		md.setMaxBoos(allBoos[allBoos.length-1]);
		md.setMinRates(allRatings[0]);
		md.setMaxRates(allRatings[allRatings.length-1]);
		
		
		return md;
	}


	@Override
	/**
	 * expensive!
	 * 
	 */
	public List<BusinessMetrics> findMinedBusinesssMetricsTrailingDays(
			Business b, int days) throws BLServiceException {
		
		
		long startTime = 
			Calendar.getInstance().getTimeInMillis() - 
			(MILLS_DAY*days);
		
		List<BusinessMetrics> all =
			new ArrayList<BusinessMetrics>();
		
		//zachs
		try {
			
			//BusinessLocation bl = b.getLocations().iterator().next();
			
			for (BusinessLocation bl : b.getLocations()) {
				//over 90 days
				for (int i = 0; i < days; i++) {
					BusinessMetrics minedMetrics =
						businessMetricsDao.mineMetricsForDateRange(
								bl, 
								new Date(startTime+(i*MILLS_DAY)), 
								new Date(startTime+((i+1)*MILLS_DAY)));
					
					all.add(minedMetrics);

				}
			}
			
						
			
		} catch (Exception e) {
			logger.error("problem mining data", e);
			throw new BLServiceException(e);
		}
		
		return combineLocationMetricsAcrossLocations(all);
	}


	@Override
	/**
	 * expensive!
	 * 
	 */
	public List<BusinessMetrics> findMinedBusinesssLocationMetricsTrailingDays(BusinessLocation bl,
			int days) throws BLServiceException {

		//we need to set this to midnight today (past)
		Date midnightToday = 
			midnight(
					Calendar.getInstance().getTime(), 
					Calendar.getInstance().getTimeZone());
		Date startTimeDate =
			addDays(midnightToday,  days, Calendar.getInstance().getTimeZone());
		
		long startTime = startTimeDate.getTime();
		
		List<BusinessMetrics> all =
			new ArrayList<BusinessMetrics>();
		
		//zachs
		try {
			
			//over 90 days
			for (int i = 0; i < days; i++) {
				BusinessMetrics minedMetrics =
					businessMetricsDao.mineMetricsForDateRange(
							bl, 
							new Date(startTime+(i*MILLS_DAY)), 
							new Date(startTime+((i+1)*MILLS_DAY)));
				
				all.add(minedMetrics);

			}
			
		} catch (Exception e) {
			logger.error("problem mining data", e);
			throw new BLServiceException(e);
		}
		
		return all;
	}
	
	/**
	 * Calculates midnight of the day in which date lies with respect
	 * to a time zone.
	 **/
	private Date midnight(Date date, TimeZone tz) {
	  Calendar cal = new GregorianCalendar(tz);
	  cal.setTime(date);
	  cal.set(Calendar.HOUR_OF_DAY, 0);
	  cal.set(Calendar.MINUTE, 0);
	  cal.set(Calendar.SECOND, 0);
	  cal.set(Calendar.MILLISECOND, 0);
	  return cal.getTime();
	}
	
	/**
	 * Adds a number of days to a date. DST change dates are handeled
	 * according to the time zone. That's necessary as these days don't
	 * have 24 hours.
	 */
	private Date addDays(Date date, int days, TimeZone tz) {
	  Calendar cal = new GregorianCalendar(tz);
	  cal.setTime(date);
	  cal.add(Calendar.DATE, days);
	  return cal.getTime();
	}
	
	private List<BusinessMetrics> combineLocationMetricsAcrossLocations(
			List<BusinessMetrics> allLocationsMetrics)
	{
		
		
		Map<String,BusinessMetrics> combinedModel = 
			new HashMap<String,BusinessMetrics> ();
		
		for (BusinessMetrics bm : allLocationsMetrics) {
			
			//make the date string as a key
			String dateKey = DateUtils.dateToString(
					new Date(bm.getStartTime()), "yyyyMMdd");
			
			//lookup by start date
			BusinessMetrics lookup = 
				combinedModel.get(dateKey);
			
			//if doesnt exist add a clone
			if(lookup == null) {
				try {
					combinedModel.put(
							dateKey, 
							(BusinessMetrics)bm.clone());
				} catch (CloneNotSupportedException e) {
					logger.error("cannot clone", e);
				}
			}
			//if doesnt then add values to it
			else {
				lookup.setBoos(lookup.getBoos()+bm.getBoos());
				lookup.setYays(lookup.getYays()+bm.getYays());
				lookup.setRatings(lookup.getRatings()+bm.getRatings());
				
				combinedModel.put(
						dateKey, 
						lookup);				
			}
			
		}
				
		List<BusinessMetrics> all = new ArrayList<BusinessMetrics>(combinedModel.values());
		Collections.sort(all, new BusinessMetricsStartDateComparitor());
		
		return all;
	}


	@Override
	public void deleteBusinessLocation(BusinessLocation locaction)
			throws BLServiceException {
		businessLocationDao.delete(locaction);		
	}


	@Override
	public void saveBusinessLocation(BusinessLocation location)
			throws BLServiceException {		
		businessLocationDao.save(location);
	}


	@Override
	public BusinessLocation findBusinessLocationByPrimaryKey(Long id)
			throws BLServiceException {
		return businessLocationDao.findByPrimaryKey(id);
	}
	

	@Override
	public Business findBusinessByUsername(String username)
			throws BLServiceException {
		return businessDao.findByUsername(username);
	}

	@Override
	public Business findBusinessByAuthId(String guid)
			throws BLServiceException {
		return businessDao.findByAuthId(guid);
	}

	@Override
	public void deleteBusiness(Business p) throws BLServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBusinessAttribute(Business p, BusinessAttribute attrs)
			throws BLServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Business> findAllBusinesss() throws BLServiceException {
		return businessDao.findAll();
	}

	@Override
	public Business findBusinessByPrimaryKey(Long id)
			throws BLServiceException {
		return businessDao.findByPrimaryKey(id);
	}

	@Override
	public List<Business> findBusinesssByGeoBounding(Double minLat,
			Double minLong, Double maxLat, Double maxLong)
			throws BLServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void savePlaceForBusinessLocation(Business b, Place p)
			throws BLServiceException {

		//save the relationship back to the place
		for(BusinessLocation bl: b.getLocations())
		{
			if(bl.getPlace().equals(p))
			{
				p = bl.getPlace();
				p.setBusinessLocation(bl);
				placeDao.save(p);	
			}
		}
		
	}

	@Override
	public Long saveBusiness(Business b) throws BLServiceException {
		businessDao.save(b);
		return b.getId();
	}
	
	
	public void setBusinessDao(BusinessDao businessDao) {
		this.businessDao = businessDao;
	}

	public void setPlaceDao(PlaceDao placeDao) {
		this.placeDao = placeDao;
	}


	public void setBusinessLocationDao(BusinessLocationDao businessLocationDao) {
		this.businessLocationDao = businessLocationDao;
	}


	public void setBusinessMetricsDao(BusinessMetricsDao businessMetricsDao) {
		this.businessMetricsDao = businessMetricsDao;
	}


	public void setRaterDao(PatronDao raterDao) {
		this.raterDao = raterDao;
	}


	public void setAwardTypeDao(AwardTypeDao awardTypeDao) {
		this.awardTypeDao = awardTypeDao;
	}


	public void setAwardDao(AwardDao awardDao) {
		this.awardDao = awardDao;
	}


	public void setAwardOfferDao(OfferDao awardOfferDao) {
		this.awardOfferDao = awardOfferDao;
	}


	public void setImageBase(String imageBase) {
		this.imageBase = imageBase;
	}


	public void setImageBaseType(String imageBaseType) {
		this.imageBaseType = imageBaseType;
	}


	public void setRaterBusinessMetricsDao(
			PatronBusinessMetricsDao raterBusinessMetricsDao) {
		this.raterBusinessMetricsDao = raterBusinessMetricsDao;
	}
	
	
	

}
