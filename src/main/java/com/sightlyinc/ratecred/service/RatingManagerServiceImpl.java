package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

import com.noi.utility.date.DateUtils;
import com.noi.utility.math.Rounding;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.dao.AwardDao;
import com.sightlyinc.ratecred.dao.AwardOfferDao;
import com.sightlyinc.ratecred.dao.AwardTypeDao;
import com.sightlyinc.ratecred.dao.ComplimentDao;
import com.sightlyinc.ratecred.dao.PlaceCityStateDao;
import com.sightlyinc.ratecred.dao.PlaceDao;
import com.sightlyinc.ratecred.dao.RaterDao;
import com.sightlyinc.ratecred.dao.RaterMetricsDao;
import com.sightlyinc.ratecred.dao.RatingDao;
import com.sightlyinc.ratecred.index.RatingDirectoryIndexer;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.PlaceRating;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.RaterMetrics;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.model.RatingPage;

public class RatingManagerServiceImpl implements RatingManagerService {
	
	static Logger logger = 
		Logger.getLogger(RatingManagerServiceImpl.class);
	
	private RatingDao ratingDao;
	private RaterMetricsDao raterMetricsDao;
	private AwardTypeDao awardTypeDao; 
	private AwardDao awardDao;
	private AwardOfferDao awardOfferDao;
	private RaterDao raterDao;
    private ComplimentDao complimentDao;
    private PlaceCityStateDao placeCityStateDao;
	private PlaceDao placeDao;
	
	private String appConsumerKey;
	private String appSecretKey;
	private String ratingUrlPrefix;
	
	
	private Directory ratingDirectory;	
	private RatingDirectoryIndexer ratingDirectoryIndexer;
	
	
	//@Autowired
	//private CheckinService checkinService;
	
	
	public void saveUpdatePlaceRating(Long id)  {
		Place p = placeDao.findByPrimaryKey(id);
				
		logger.debug("place:"+p.getName());
		// ok this is painful to admit, we had a 
		// design that was too flexibile, really there 
		// should be one field, I will probably
		// add it to the row but for now lets just
		// get this working
		if(p.getPlaceRatings() == null) 			
			p.setPlaceRatings(new HashSet<PlaceRating>());
		
		if(p.getRatings() == null)
			p.setRatings(new HashSet<Rating>());
		
		PlaceRating ratingForType = new PlaceRating();

		Float sum = 0.0f;

		for (Rating rating : p.getRatings()) {
				sum+=rating.getRaterRating();
		}

		Float avg = 2.5f;
		try {
			avg = sum/p.getRatings().size();
		} catch (Exception e) {
			logger.error("NaN", e);
			avg = 2.5f;
		}
		if(!avg.isNaN())
			ratingForType.setRating(avg);
		else
			ratingForType.setRating(2.5f);
		
		logger.debug("new rating value:"+ratingForType.getRating());
		
		if(p.getPlaceRatings().size() == 0) {
			ratingForType.setPlace(p);
			ratingForType.setType("Other");
			p.getPlaceRatings().add(ratingForType);
		} else {
			PlaceRating pr = p.getPlaceRatings().iterator().next();
			pr.setRating(ratingForType.getRating());				
		}

		placeDao.save(p);

	}
	
	@Override
	public Rating findRatingByCheckinTxId(String checkinTxId)
			throws BLServiceException {
		return ratingDao.findByTxId(checkinTxId);
	}

	//we are going to put an init method here because 
	//our ratings were wrong, this should normalize it
	public void saveUpdatePlaceRatings()  {
		List<Place> allPlaces = placeDao.findAll();
		for (Place p : allPlaces) {			
			saveUpdatePlaceRating(p.getId());
			
		}
	}
	
	
	@Override
	public List<Rating> findRatingsSince(Long millis) throws BLServiceException {
		return ratingDao.findSince(millis);
	}


	@Override
	public List<Rater> findRatersRatedSince(Long millis)
			throws BLServiceException {
		Set<Rater> since = new HashSet<Rater>();
		List<Rating> ratingSince = ratingDao.findSince(millis);
		for (Rating rating : ratingSince) {
			since.add(rating.getOwner());
		}
		return new ArrayList<Rater>(since);
		
	}


	@Override
	public Rating findRateByTime(Long time) {
		return ratingDao.findByTime(time);
	}


	@Override
	public List<Rater> findRatersByPrimaryKeys(Long[] ids)
			throws BLServiceException {
		return raterDao.findByPrimaryKeys(ids);
	}

	@Override
	public List<Rater> findRatersByScreenNames(String[] screenNames)
			throws BLServiceException {
		if(screenNames != null && screenNames.length>0)
			return raterDao.findByUserNames(screenNames);
		else return new ArrayList<Rater>();
	}
	
	

	@Override
	public void saveConvertRater(Rater fromRater, Rater toRater)
			throws BLServiceException {
		
		//ratings
		Set<Rating> ratings = fromRater.getRatings();
		for (Rating ratingFrom : ratings) {
			ratingFrom.setOwner(toRater);
		}		
		fromRater.setRatings(new HashSet<Rating>());
		
		//awards
		Set<Award> awards = fromRater.getAwards();
		for (Award awardFrom : awards) {
			awardFrom.setOwner(toRater);
		}
		fromRater.setAwards(new HashSet<Award>());
		
		//compliments
		Set<Compliment> compliments = fromRater.getCompliments();
		for (Compliment complimentFrom : compliments) {
			complimentFrom.setOwner(toRater);
		}
		fromRater.setCompliments(new HashSet<Compliment>());
		
		toRater.setScore(toRater.getScore()+fromRater.getScore());
		toRater.setTimeCreated(fromRater.getTimeCreated());
		
		raterDao.save(fromRater);
		raterDao.save(toRater);
		
	}

	@Override
	public List<Rater> findRatersByStatus(String status)
			throws BLServiceException {
		return raterDao.findByStatus(status);
	}

	@Override
	public Award findAwardById(Long awardId) throws BLServiceException {
		return awardDao.findByPrimaryKey(awardId);
	}

	@Override
	public void saveAward(Award entity) throws BLServiceException {
		awardDao.save(entity);	
	}
	

	@Override
	public Long findAwardCountByOwnerBetweenTimes(Rater towards, Date startTime,
			Date endTime) throws BLServiceException {
		return awardDao.findCountByOwnerBetweenTimes(towards, startTime, endTime);
	}

	@Override
	public List<Award> findBusinessAwardsByRaterBetweenTimes(Rater towards,
			Date startTime, Date endTime) throws BLServiceException {
		return awardDao.findByOwnerBetweenTimes(towards, startTime, endTime);
	}

	@Override
	public RaterMetrics findMetricsByRater(Rater t) {
		return findMetricsByRater(t, null);
	}
	
	/**
	 * the business logic here is a little wonky and has
	 * some legacy to it
	 * 
	 * @param t
	 * @param currentStatus
	 * @return
	 */
	private RaterMetrics findMetricsByRater(Rater t, String currentStatus) {
		
		RaterMetrics tm = raterMetricsDao.findByRater(t);
		
		if(tm == null)
			return null;
		
		Long score = (tm.getRatings()*10l)+(tm.getGiven()*5l)+(tm.getReceived()*5l);
				
		tm.setScore(score);
		return tm;
	}

	@Override
	public Rater createAnonymousRater() {
		Rater anonRater = new Rater();
		String uuid = UUID.randomUUID().toString();
		String[] parts = uuid.split("-");
		anonRater.setSecretKey(parts[0]);
		anonRater.setUserName(parts[1]);
		anonRater.setScore(0l);
		anonRater.setStatus("ANON");
		anonRater.setTimeCreated(Calendar.getInstance().getTime());
		return anonRater;
	}

	@Override
	public List<Rater> findRatersByCityStateScoreDesc(PlaceCityState cs,
			int size) throws BLServiceException {
		
		//first find all the ratings by a city state
		List<Rater> areaLeaders 
			= raterDao.findByCityStateScorePaged(cs.getCity(), cs.getState(), 1, size, false);

		return areaLeaders;
	}

	@Override
	public List<Compliment> findComplimentsByRaterBetweenTimes(Rater towards,
			Date startTime, Date endTime) throws BLServiceException {
		return complimentDao.findByRaterBetweenTimes(towards, startTime, endTime);
	}

	@Override
	public Long findComplimentCountByRaterBetweenTimes(Rater towards, Date startTime,
			Date endTime) throws BLServiceException {
		if(startTime == null)
			startTime = DateUtils.stringToDate("2010-01-01", DateUtils.DESC_SIMPLE_FORMAT);
		if(endTime == null)
			endTime = Calendar.getInstance().getTime();
		return complimentDao.findCountByRaterBetweenTimes(towards, startTime, endTime);
	}

	@Override
	public void saveCompliment(Compliment c, Rating towards, Rater complementor)
			throws BLServiceException {		
		try {
			//should be this simple
			c.setTowards(towards);
			c.setOwner(complementor);
			complimentDao.save(c);
			
		} catch (Exception e) {
			logger.debug("problem saving compliment",e);
			throw new BLServiceException(e);
		}
	}

	@Override
	public void deleteRate(Rating entity) {	
		
		//need to re compute place rating
		Place p = placeDao.findByPrimaryKey(entity.getPlace().getId());
		Set<PlaceRating> ratings = p.getPlaceRatings();
		PlaceRating ratingForType = 
			RatingHelper.computeNewRatingRemove(
					new ArrayList<PlaceRating>(ratings), 
					new ArrayList<Rating>(p.getRatings()), 
					entity.getType(), 
					entity.getRaterRating());
		
		//this needs to be tested
		ratings.add(ratingForType);
		p.setPlaceRatings(ratings);
		p.getRatings().remove(entity);
		placeDao.save(p);
		
		//need to remove from document index
		ratingDirectoryIndexer.removeRate(entity);
		
		ratingDao.delete(entity);
	}

	@Override
	public void deleteRater(Rater entity) {
		raterDao.delete(entity);
	}

	@Override
	public List<Rater> findAllRaters() {
		return raterDao.findAll();
	}

	@Override
	public List<Rating> findAllRates() {
		return ratingDao.findAll();
	}

	@Override
	public RatingPage findAllRatingsAsPage(
			Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending) 
	throws BLServiceException 
	{
		logger.debug("pagenum:"+pageNum);
		
		RatingPage tp = new RatingPage(); 
		tp.setPageSize(ratingsPerPage);
		tp.setSortField(sortField);
		tp.setAscending(isAcending);
		
		Long totalRates = ratingDao.findAllCount();
		tp.setTotalResults(totalRates);
	
		
		Float pagesAll = 
			totalRates.floatValue()
			/ ratingsPerPage.floatValue();
		
		Float pages = 
			Rounding.roundFloat(pagesAll,0); 
		
		if(pagesAll>pages)
			pages=pages+1.0f;
		
		tp.setPageNumber(pageNum);			
		tp.setTotalPages(pages.intValue());			
	
		tp.setRatings(
				ratingDao.findAllPaged(
						pageNum, ratingsPerPage, sortField, isAcending));
		
		return tp;
	}
		
	
	@Override
	public RatingPage findRatingsByCityState(
			PlaceCityState cs, Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending)
			throws BLServiceException {
		
		logger.debug("pagenum:"+pageNum);
		
		RatingPage tp = new RatingPage(); 
		tp.setPageSize(ratingsPerPage);
		tp.setSortField(sortField);
		tp.setAscending(isAcending);
		
		Long totalRates = ratingDao.findByCityStateCount(cs.getCity(), cs.getState());
		tp.setTotalResults(totalRates);
	
		
		Float pagesAll = 
			totalRates.floatValue()
			/ ratingsPerPage.floatValue();
		
		Float pages = 
			Rounding.roundFloat(pagesAll,0); 
		
		if(pagesAll>pages)
			pages=pages+1.0f;
		
		tp.setPageNumber(pageNum);			
		tp.setTotalPages(pages.intValue());			
	
		tp.setRatings(
				ratingDao.findByCityStatePaged(
						cs.getCity(), cs.getState(), pageNum, ratingsPerPage, sortField, isAcending));
		
		return tp;
	}

	@Override
	public RatingPage findRatingsByCityStatePlaceInfo(
			Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending,
			PlaceCityState cs, Place tplace)
			throws BLServiceException {
		logger.debug("pagenum:"+pageNum);
		
		RatingPage tp = new RatingPage(); 
		tp.setPageSize(ratingsPerPage);
		tp.setSortField(sortField);
		tp.setAscending(isAcending);
		
		Long totalRates = 
			ratingDao.findByCityStatePlaceInfoCount(
					cs.getCity(), cs.getState(), tplace.getName());
		tp.setTotalResults(totalRates);
	
		
		Float pagesAll = 
			totalRates.floatValue()
			/ ratingsPerPage.floatValue();
		
		Float pages = 
			Rounding.roundFloat(pagesAll,0); 
		
		if(pagesAll>pages)
			pages=pages+1.0f;
		
		tp.setPageNumber(pageNum);			
		tp.setTotalPages(pages.intValue());			
	
		tp.setRatings(
				ratingDao.findByCityStatePlaceInfoPaged(
						cs.getCity(), cs.getState(), tplace.getName(), 
						pageNum, ratingsPerPage, sortField, isAcending));
		
		return tp;
	}
	
	

	@Override
	public RatingPage findRatingsByOwner(Integer pageNum, Integer ratingsPerPage, String sortField,
			boolean isAcending, Rater rater)
			throws BLServiceException {
		logger.debug("pagenum:"+pageNum);
		
		RatingPage tp = new RatingPage(); 
		tp.setPageSize(ratingsPerPage);
		tp.setSortField(sortField);
		tp.setAscending(isAcending);
		
		Long totalRates = ratingDao.findByOwnerCount(rater.getId());
		tp.setTotalResults(totalRates);
	
		
		Float pagesAll = 
			totalRates.floatValue()
			/ ratingsPerPage.floatValue();
		
		Float pages = 
			Rounding.roundFloat(pagesAll,0); 
		
		if(pagesAll>pages)
			pages=pages+1.0f;
		
		tp.setPageNumber(pageNum);			
		tp.setTotalPages(pages.intValue());			
	
		tp.setRatings(
				ratingDao.findByOwner(
						rater.getId(), pageNum, ratingsPerPage, sortField, isAcending));
		
		return tp;
	}
	
	
	@Override
	public RatingPage findRatingsByOwners(Integer pageNum, Integer ratingsPerPage, String sortField,
			boolean isAcending, List<Rater> raters)
			throws BLServiceException {
		
		RatingPage tp = new RatingPage(); 
		tp.setPageSize(ratingsPerPage);
		tp.setSortField(sortField);
		tp.setAscending(isAcending);		
		if(raters != null && raters.size()>0)
		{
			Long[] ownerIds = new Long[raters.size()];
			int i=0;
			for (Rater rater : raters) 
			{
				ownerIds[i] = rater.getId();
				i++;
			}
	
			tp.setRatings(ratingDao.findByOwners(
					ownerIds, pageNum, ratingsPerPage, sortField, isAcending));
		
		}
		else
			tp.setRatings(new ArrayList<Rating>());
		
		return tp;

		
	}

	@Override
	public Rating findRatingByPrimaryKey(Long id) {
		return ratingDao.findByPrimaryKey(id);
	}

	@Override
	public Rater findRaterByPrimaryKey(Long id) {
		Rater t = raterDao.findByPrimaryKey(id);
		//findRaterAwards(t);
		return t;
	}

	@Override
	public List<Award> findAwardsLocalByRater(Rater t)
	{
		
		Date gmtCreated = Calendar.getInstance().getTime();		
		
		
		
		//expire any old awards
		List<Award> all = new ArrayList<Award>();
		
		//find the network awards
		List<Rater> stars =
			raterDao.findByScorePaged(1, 10, false);
		int index =0;
		for (Rater rater : stars) {
			if(rater.getId().equals(t.getId()))
			{
				//give the star award 
				AwardType awardType = awardTypeDao.findByKeyname("star");				
				Award award = new Award();
				award.setAwardType(awardType);
				award.setNotes("Network Top Ten");
				award.setMetadata("imageUrl=/images/awards/award_star.png");
				award.setOwner(t);
				award.setStatus("ACTIVE");
				award.setTimeCreated(gmtCreated);
				award.setTimeCreatedMills(gmtCreated.getTime());
				all.add(award);
				if(index==0) //give the super star award 
				{
					//give the star award 
					AwardType sawardType = awardTypeDao.findByKeyname("superstar");				
					Award saward = new Award();
					saward.setAwardType(sawardType);
					saward.setNotes("Network Number One");
					saward.setMetadata("imageUrl=/images/awards/award_superstar.png");
					saward.setTimeCreated(gmtCreated);
					saward.setTimeCreatedMills(gmtCreated.getTime());
					saward.setOwner(t);
					all.add(saward);
				}
			}
			
			index++;
		}
		
		//find the cities that they have ratingd in
		List<PlaceCityState> raterCities =
			placeCityStateDao.findByRater(t);
		
		for (PlaceCityState cs : raterCities) {
			//check if this rater is the leader in each city
			List<Rater> leaders = 
				raterDao.findByCityStateScorePaged(
						cs.getCity(), cs.getState(), 
						1, 1, false);
			
			
			if(leaders != null 
					&& leaders.size() >0 
					&& leaders.get(0).getId().equals(t.getId()))
			{
				//give the award 
				AwardType cityKeyAwardType = awardTypeDao.findByKeyname("citykey");	
												
				Award mayorAward = new Award();
				mayorAward.setAwardType(cityKeyAwardType);
				mayorAward.setNotes("Ranked First "+cs.getCity());
				mayorAward.setMetadata("city="+cs.getCity()+"&state="+cs.getState()+"&imageUrl=/images/awards/award_citystar.png");
				mayorAward.setOwner(t);
				mayorAward.setTimeCreated(gmtCreated);
				mayorAward.setTimeCreatedMills(gmtCreated.getTime());
				all.add(mayorAward);
			}
				
		}
		
		
		return all;
		
	}

	
	
	
	@Override
	public List<Award> findAwardsByOwner(Rater t)
			throws BLServiceException {
		return awardDao.findByOwner(t);
	}

	@Override
	public Rater findRaterByAuthId(String authId)
			throws BLServiceException {
		Rater t = raterDao.findByAuthId(authId);
		//findRaterAwards(t);
		return t;
	}

	@Override
	public Rater findRaterByUsername(String userName)
			throws BLServiceException {
		
		Rater t = raterDao.findByUserName(userName);
		return t;
	}


	/**
	 * same operation with different name so that
	 * the advisor can work (if there is one)
	 */
	@Override
	public Long saveRatingWithCheckin(Rating entity) throws BLServiceException {
		return saveRating(entity);
	}

	@Override
	public Long saveRating(Rating entity) throws BLServiceException {

		try {
			Rater owner = entity.getOwner();
			Place p = entity.getPlace();

			
			// ok this is painful to admit, we had a 
			// design that was too flexibile, really there 
			// should be one field, I will probably
			// add it to the row but for now lets just
			// get this working
			if(p.getPlaceRatings() == null) 			
				p.setPlaceRatings(new HashSet<PlaceRating>());
			
			if(p.getRatings() == null)
				p.setRatings(new HashSet<Rating>());
			
			PlaceRating ratingForType = RatingHelper.computeNewRatingAdd(
					new ArrayList<PlaceRating>(p.getPlaceRatings()), 
					new ArrayList<Rating>(p.getRatings()), 
					entity.getType(), 
					entity.getRaterRating());
			
			logger.debug("new rating value:"+ratingForType.getRating());
			
			if(p.getPlaceRatings().size() == 0) {
				ratingForType.setPlace(p);
				ratingForType.setType(entity.getType());
				p.getPlaceRatings().add(ratingForType);
			} else {
				PlaceRating pr = p.getPlaceRatings().iterator().next();
				pr.setRating(ratingForType.getRating());				
			}
			

			RaterMetrics tmcomp = findMetricsByRater(owner);
			if (tmcomp != null)
				owner.setScore(tmcomp.getScore());

			raterDao.save(owner);
			entity.setOwner(owner);
			
			
			//make sure timestamp if new
			if(entity.getId()==null)
			{
				String gmtTime = DateUtils.dateToString(
						Calendar.getInstance().getTime(), 
						DateUtils.NOSPACE_TIMESTAMP_FORMAT, 
						new SimpleTimeZone(0, "GMT"))+"-0000";
				entity.setTimeCreatedGmt(gmtTime);
				entity.setTimeCreated(Calendar.getInstance().getTime());
				entity.setTimeCreatedMills(Calendar.getInstance().getTimeInMillis());
			}
			
			//fire and forget services send messages to the
			//queue so that the user does not have to wait
			//checkin foursquare 
			
			
			//save it
			ratingDao.save(entity);
			//index it
			ratingDirectoryIndexer.indexRate(entity);
			
			
			//this is a good use for an after intereceptor 
			/*if(checkin)
			{
				if(entity.getOwner().getAuthorizedFoursquare()) {
					logger.debug("authorized foursquare");
					RatingCheckinAction fsAction = new RatingCheckinAction("foursquare",entity);				
					checkinMessageProducer.generateMessage(fsAction);
				}
				
				//checkin gowalla 
				if(entity.getOwner().getAuthorizedGowalla()) {
					logger.debug("authorized gowalla");
					RatingCheckinAction fsAction = new RatingCheckinAction("gowalla",entity);				
					checkinMessageProducer.generateMessage(fsAction);
				}
			}*/
			
			return entity.getId();

			
			
			
		} catch (Exception e) {
			logger.error("problem", e);
			throw new BLServiceException(e);
		}
	}
		

	@Override
	public List<Rating> findRatesByText(String text)
			throws BLServiceException {
		
		Map<String,Rating> ratingResult = new HashMap<String,Rating>();
		
		try {
			Query q = new QueryParser("indexContent", new StandardAnalyzer())
					.parse(text);

			Searcher s = new IndexSearcher(ratingDirectory);
			Hits hits = s.search(q);
			for (int i = 0; i < hits.length(); ++i) {
				String id = hits.doc(i).get("id");
				Rating t = ratingDao.findByPrimaryKey(Long.parseLong(id));
				ratingResult.put(t.getId().toString(), t);
			}
			

		} catch (ParseException e) {
			throw new BLServiceException(e);
		} catch (IOException e) {
			throw new BLServiceException(e);
		}

		return new ArrayList<Rating>(ratingResult.values());
	}

	
	
	@Override
	public RatingPage findRatesByText(String text, Integer pageNum,
			Integer pageSize, boolean b) throws BLServiceException {

		RatingPage page = new RatingPage();
		try {
			Map<String,Rating> ratingResult = new HashMap<String,Rating>();
			Query q = new QueryParser("indexContent", new StandardAnalyzer())
					.parse(text);

			Searcher s = new IndexSearcher(ratingDirectory);
			Hits hits = s.search(q);
			page.setTotalResults(hits.length());
			page.setPageSize(pageSize);
			page.setPageNumber(pageNum);
			int offset = (pageNum-1) * pageSize;
			int count = Math.min(hits.length() - offset, pageSize);
			Set<Long> ratingIds = new HashSet<Long>();
			
			for (int i = 0; i < count; ++i) {

				String id = hits.doc(i).get("id");
				if(ratingResult.get(id) == null)
					ratingIds.add(Long.parseLong(hits.doc(i).get("id")));
			}
			
			if(ratingIds.size()>0)
				page.setRatings(ratingDao.findByPrimaryKeys(new ArrayList(ratingIds)));

		} catch (ParseException e) {
			throw new BLServiceException(e);
		} catch (IOException e) {
			throw new BLServiceException(e);
		}
		
		return page;
	}

	@Override
	public void saveRater(Rater entity) {
		
		//compute score
		//get the metrics 
		if(entity.getId() != null)
		{
			RaterMetrics tm =
				findMetricsByRater(entity);
			entity.setScore(tm.getScore());
		}
		
		raterDao.save(entity);
	}
	
	//------------- injection ----------------//
	
	public void setRatingDao(RatingDao ratingDao) {
		this.ratingDao = ratingDao;
	}


	public void setRaterDao(RaterDao raterDao) {
		this.raterDao = raterDao;
	}

	@Override
	public List<Rating> findRatingsByCityStatePlaceInfo(
			PlaceCityState cs, Place tp)
			throws BLServiceException 
	{
		
		return ratingDao.findByCityStatePlaceInfo(
				cs.getCity(),
				cs.getState(),
				tp.getName());
	}	

	@Override
	public List<Rating> findRatingsByCityState(PlaceCityState cs)
			throws BLServiceException {
		return ratingDao.findByCityState(cs.getCity(),cs.getState());
	}
	

	@Override
	public List<Rater> findRatersByScoreDesc(int size)
			throws BLServiceException {
		return raterDao.findByScorePaged(1, size, false);
	}
	
	
	
/* ------------- injection methods ---------------- */	
	

	/**
	 * 
	 * @param ratingSearchDirectory
	 */
	public void setRatingSearchDirectory(Directory ratingSearchDirectory) {
		this.ratingDirectory = ratingSearchDirectory;
	}

	public void setRatingDirectoryIndexer(
			RatingDirectoryIndexer ratingDirectoryIndexer) {
		this.ratingDirectoryIndexer = ratingDirectoryIndexer;
	}


	public void setPlaceDao(PlaceDao placeDao) {
		this.placeDao = placeDao;
	}

	public void setComplimentDao(ComplimentDao complimentDao) {
		this.complimentDao = complimentDao;
	}

	public void setPlaceCityStateDao(PlaceCityStateDao placeCityStateDao) {
		this.placeCityStateDao = placeCityStateDao;
	}

	public void setAwardTypeDao(AwardTypeDao awardTypeDao) {
		this.awardTypeDao = awardTypeDao;
	}

	public void setRaterMetricsDao(RaterMetricsDao raterMetricsDao) {
		this.raterMetricsDao = raterMetricsDao;
	}

	public void setAwardDao(AwardDao awardDao) {
		this.awardDao = awardDao;
	}

	public void setAppConsumerKey(String appConsumerKey) {
		this.appConsumerKey = appConsumerKey;
	}

	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}

	public void setRatingUrlPrefix(String ratingUrlPrefix) {
		this.ratingUrlPrefix = ratingUrlPrefix;
	}


	public void setAwardOfferDao(AwardOfferDao awardOfferDao) {
		this.awardOfferDao = awardOfferDao;
	}
	
	

	

}
