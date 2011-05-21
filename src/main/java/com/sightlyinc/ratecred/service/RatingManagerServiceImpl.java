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

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noi.utility.date.DateUtils;
import com.noi.utility.math.Rounding;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.dao.AwardDao;
import com.sightlyinc.ratecred.dao.AwardTypeDao;
import com.sightlyinc.ratecred.dao.ComplimentDao;
import com.sightlyinc.ratecred.dao.PatronDao;
import com.sightlyinc.ratecred.dao.PatronMetricsDao;
import com.sightlyinc.ratecred.dao.PlaceCityStateDao;
import com.sightlyinc.ratecred.dao.PlaceDao;
import com.sightlyinc.ratecred.dao.RatingDao;
import com.sightlyinc.ratecred.index.RatingDirectoryIndexer;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.AwardType;
import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PatronMetrics;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.pojo.Page;

// TODO remove @Qualifier annotations that are forcing the setting of a service
// bean id that matches the interface short class name? - sam 5/21/11
@Service("RatingManagerService")
@Transactional
public class RatingManagerServiceImpl implements RatingManagerService {
	
	static Logger logger = 
		Logger.getLogger(RatingManagerServiceImpl.class);
	
	@Autowired
	private RatingDao ratingDao;
	
	@Autowired
	private PatronMetricsDao raterMetricsDao;
	
	@Autowired
	private AwardTypeDao awardTypeDao; 
	
	@Autowired
	private AwardDao awardDao;

	@Autowired
	private PatronDao raterDao;
    
	@Autowired
	private ComplimentDao complimentDao;
    
	@Autowired
	private PlaceCityStateDao placeCityStateDao;
	
	@Autowired
	private PlaceDao placeDao;
	
	@Autowired
	@Qualifier("ratingDirectory")
	private Directory ratingDirectory;	
	
	@Autowired
	private RatingDirectoryIndexer ratingDirectoryIndexer;
		
	
	public void saveUpdatePlaceRating(Long id)  {
		Place p = placeDao.findByPrimaryKey(id);
				
		logger.debug("place:"+p.getName());
		// ok this is painful to admit, we had a 
		// design that was too flexibile, really there 
		// should be one field, I will probably
		// add it to the row but for now lets just
		// get this working
		//if(p.getPlaceRatings() == null) 			
		//	p.setPlaceRatings(new HashSet<PlaceRating>());
		
		if(p.getRatings() == null)
			p.setRatings(new HashSet<Rating>());
		
		//PlaceRating ratingForType = new PlaceRating();

		Float sum = 0.0f;

		for (Rating rating : p.getRatings()) {
				sum+=rating.getPatronRating();
		}

		Float avg = 2.5f;
		try {
			avg = sum/p.getRatings().size();
		} catch (Exception e) {
			logger.error("NaN", e);
			avg = 2.5f;
		}
		/*if(!avg.isNaN())
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
		}*/

		placeDao.save(p);
		throw new RuntimeException("NEED TO DEAL WITH NEW RATING MODEL");
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
	public List<Patron> findRatersRatedSince(Long millis)
			throws BLServiceException {
		Set<Patron> since = new HashSet<Patron>();
		List<Rating> ratingSince = ratingDao.findSince(millis);
		for (Rating rating : ratingSince) {
			since.add(rating.getOwner());
		}
		return new ArrayList<Patron>(since);
		
	}


	@Override
	public Rating findRateByTime(Long time) {
		return ratingDao.findByTime(time);
	}


	@Override
	public List<Patron> findRatersByPrimaryKeys(Long[] ids)
			throws BLServiceException {
		return raterDao.findByPrimaryKeys(ids);
	}

	@Override
	public List<Patron> findRatersByScreenNames(String[] screenNames)
			throws BLServiceException {
		if(screenNames != null && screenNames.length>0)
			return raterDao.findByUserNames(screenNames);
		else return new ArrayList<Patron>();
	}
	
	

	@Override
	public void saveConvertRater(Patron fromRater, Patron toRater)
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
	public List<Patron> findRatersByStatus(String status)
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
	public Long findAwardCountByOwnerBetweenTimes(Patron towards, Date startTime,
			Date endTime) throws BLServiceException {
		return awardDao.findCountByOwnerBetweenTimes(towards, startTime, endTime);
	}

	@Override
	public List<Award> findBusinessAwardsByRaterBetweenTimes(Patron towards,
			Date startTime, Date endTime) throws BLServiceException {
		return awardDao.findByOwnerBetweenTimes(towards, startTime, endTime);
	}

	@Override
	public PatronMetrics findMetricsByRater(Patron t) {
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
	private PatronMetrics findMetricsByRater(Patron t, String currentStatus) {
		
		PatronMetrics tm = raterMetricsDao.findByRater(t);
		
		if(tm == null)
			return null;
		
		Long score = (tm.getRatings()*10l)+(tm.getGiven()*5l)+(tm.getReceived()*5l);
				
		tm.setScore(score);
		return tm;
	}

	/*@Override
	public Patron createAnonymousRater() {
		Patron anonRater = new Patron();
		String uuid = UUID.randomUUID().toString();
		String[] parts = uuid.split("-");
		anonRater.setSecretKey(parts[0]);
		anonRater.setUserName(parts[1]);
		anonRater.setScore(0l);
		anonRater.setStatus("ANON");
		//anonRater.setTimeCreated(Calendar.getInstance().getTime());
		//return anonRater;
		throw new RuntimeException("NOT ALLOWED");
	}*/

	@Override
	public List<Patron> findRatersByCityStateScoreDesc(PlaceCityState cs,
			int size) throws BLServiceException {
		
		//first find all the ratings by a city state
		List<Patron> areaLeaders 
			= raterDao.findByCityStateScorePaged(cs.getCity(), cs.getState(), 1, size, false);

		return areaLeaders;
	}

	@Override
	public List<Compliment> findComplimentsByRaterBetweenTimes(Patron towards,
			Date startTime, Date endTime) throws BLServiceException {
		return complimentDao.findByRaterBetweenTimes(towards, startTime, endTime);
	}

	@Override
	public Long findComplimentCountByRaterBetweenTimes(Patron towards, Date startTime,
			Date endTime) throws BLServiceException {
		if(startTime == null)
			startTime = DateUtils.stringToDate("2010-01-01", DateUtils.DESC_SIMPLE_FORMAT);
		if(endTime == null)
			endTime = Calendar.getInstance().getTime();
		return complimentDao.findCountByRaterBetweenTimes(towards, startTime, endTime);
	}

	@Override
	public void saveCompliment(Compliment c, Rating towards, Patron complementor)
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
	public void deleteRatingByPrimaryKey(Long id) throws BLServiceException {
		Rating r = findRatingByPrimaryKey(id);
		deleteRating(r); 		
	}

	@Override
	public void deleteRating(Rating entity) {	
		
		//need to re compute place rating
		Place p = placeDao.findByPrimaryKey(entity.getPlace().getId());
		/*Set<PlaceRating> ratings = p.getPlaceRatings();
		PlaceRating ratingForType = 
			RatingHelper.computeNewRatingRemove(
					new ArrayList<PlaceRating>(ratings), 
					new ArrayList<Rating>(p.getRatings()), 
					entity.getType(), 
					entity.getRaterRating());
		
		//this needs to be tested
		ratings.add(ratingForType);
		p.setPlaceRatings(ratings);*/

		p.getRatings().remove(entity);
		placeDao.save(p);
		
		//need to remove from document index
		ratingDirectoryIndexer.removeRate(entity);
		
		ratingDao.delete(entity);
	}

	@Override
	public void deleteRater(Patron entity) {
		raterDao.delete(entity);
	}

	@Override
	public List<Patron> findAllRaters() {
		return raterDao.findAll();
	}

	@Override
	public List<Rating> findAllRates() {
		return ratingDao.findAll();
	}

	@Override
	public Page<Rating> findAllRatingsAsPage(
			Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending) 
	throws BLServiceException 
	{
		logger.debug("pagenum:"+pageNum);
		
		Page<Rating> tp = new Page<Rating>(); 
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
	
		tp.setItems(
				ratingDao.findAllPaged(
						pageNum, ratingsPerPage, sortField, isAcending));
		
		return tp;
	}
		
	
	@Override
	public Page<Rating> findRatingsByCityState(
			PlaceCityState cs, Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending)
			throws BLServiceException {
		
		logger.debug("pagenum:"+pageNum);
		
		Page<Rating> tp = new Page<Rating>(); 
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
	
		tp.setItems(
				ratingDao.findByCityStatePaged(
						cs.getCity(), cs.getState(), pageNum, ratingsPerPage, sortField, isAcending));
		
		return tp;
	}

	@Override
	public Page<Rating> findRatingsByCityStatePlaceInfo(
			Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending,
			PlaceCityState cs, Place tplace)
			throws BLServiceException {
		logger.debug("pagenum:"+pageNum);
		
		Page<Rating> tp = new Page<Rating>(); 
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
	
		tp.setItems(
				ratingDao.findByCityStatePlaceInfoPaged(
						cs.getCity(), cs.getState(), tplace.getName(), 
						pageNum, ratingsPerPage, sortField, isAcending));
		
		return tp;
	}
	
	

	@Override
	public Page<Rating> findRatingsByOwner(Integer pageNum, Integer ratingsPerPage, String sortField,
			boolean isAcending, Patron rater)
			throws BLServiceException {
		logger.debug("pagenum:"+pageNum);
		
		Page<Rating> tp = new Page<Rating>(); 
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
	
		tp.setItems(
				ratingDao.findByOwner(
						rater.getId(), pageNum, ratingsPerPage, sortField, isAcending));
		
		return tp;
	}
	
	
	@Override
	public Page<Rating> findRatingsByOwners(Integer pageNum, Integer ratingsPerPage, String sortField,
			boolean isAcending, List<Patron> raters)
			throws BLServiceException {
		
		Page<Rating> tp = new Page<Rating>(); 
		tp.setPageSize(ratingsPerPage);
		tp.setSortField(sortField);
		tp.setAscending(isAcending);		
		if(raters != null && raters.size()>0)
		{
			Long[] ownerIds = new Long[raters.size()];
			int i=0;
			for (Patron rater : raters) 
			{
				ownerIds[i] = rater.getId();
				i++;
			}
	
			tp.setItems(ratingDao.findByOwners(
					ownerIds, pageNum, ratingsPerPage, sortField, isAcending));
		
		}
		else
			tp.setItems(new ArrayList<Rating>());
		
		return tp;

		
	}

	@Override
	public Rating findRatingByPrimaryKey(Long id) {
		return ratingDao.findByPrimaryKey(id);
	}

	@Override
	public Patron findRaterByPrimaryKey(Long id) {
		Patron t = raterDao.findByPrimaryKey(id);
		//findRaterAwards(t);
		return t;
	}

	@Override
	public List<Award> findAwardsLocalByRater(Patron t)
	{
		
		Date gmtCreated = Calendar.getInstance().getTime();		
		
		
		
		//expire any old awards
		List<Award> all = new ArrayList<Award>();
		
		//find the network awards
		List<Patron> stars =
			raterDao.findByScorePaged(1, 10, false);
		int index =0;
		for (Patron rater : stars) {
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
				
				//done with interceptor
				//award.setTimeCreated(gmtCreated);
				//award.setTimeCreatedMills(gmtCreated.getTime());
				
				all.add(award);
				if(index==0) //give the super star award 
				{
					//give the star award 
					AwardType sawardType = awardTypeDao.findByKeyname("superstar");				
					Award saward = new Award();
					saward.setAwardType(sawardType);
					saward.setNotes("Network Number One");
					saward.setMetadata("imageUrl=/images/awards/award_superstar.png");
					
					//done with interceptor
					//saward.setTimeCreated(gmtCreated);
					//saward.setTimeCreatedMills(gmtCreated.getTime());
				
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
			List<Patron> leaders = 
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
				
				//done with interceptor
				//mayorAward.setTimeCreated(gmtCreated);
				//mayorAward.setTimeCreatedMills(gmtCreated.getTime());
				
				all.add(mayorAward);
			}
				
		}
		
		
		return all;
		
	}

	
	
	
	@Override
	public List<Award> findAwardsByOwner(Patron t)
			throws BLServiceException {
		return awardDao.findByOwner(t);
	}

	@Override
	public Patron findRaterByAuthId(String authId)
			throws BLServiceException {
		Patron t = raterDao.findByAuthId(authId);
		//findRaterAwards(t);
		return t;
	}

	@Override
	public Patron findRaterByUsername(String userName)
			throws BLServiceException {
		
		Patron t = raterDao.findByUserName(userName);
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
			Patron owner = entity.getOwner();
			Place p = entity.getPlace();

			
			// ok this is painful to admit, we had a 
			// design that was too flexibile, really there 
			// should be one field, I will probably
			// add it to the row but for now lets just
			// get this working
			/*if(p.getPlaceRatings() == null) 			
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
			}*/
			

			PatronMetrics tmcomp = findMetricsByRater(owner);
			if (tmcomp != null)
				owner.setScore(tmcomp.getScore());

			raterDao.save(owner);
			entity.setOwner(owner);
			
			
			
			
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
			
			//return entity.getId();

			throw new RuntimeException("NEED TO DEAL WITH NEW RATING MODEL");
			
			
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
	public Page<Rating> findRatesByText(String text, Integer pageNum,
			Integer pageSize, boolean b) throws BLServiceException {

		Page<Rating> page = new Page<Rating>();
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
				page.setItems(ratingDao.findByPrimaryKeys(new ArrayList(ratingIds)));

		} catch (ParseException e) {
			throw new BLServiceException(e);
		} catch (IOException e) {
			throw new BLServiceException(e);
		}
		
		return page;
	}

	@Override
	public void saveRater(Patron entity) {
		
		//compute score
		//get the metrics 
		if(entity.getId() != null)
		{
			PatronMetrics tm =
				findMetricsByRater(entity);
			entity.setScore(tm.getScore());
		}
		
		raterDao.save(entity);
	}
	
	//------------- injection ----------------//
	
	public void setRatingDao(RatingDao ratingDao) {
		this.ratingDao = ratingDao;
	}


	public void setRaterDao(PatronDao raterDao) {
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
	public List<Patron> findRatersByScoreDesc(int size)
			throws BLServiceException {
		return raterDao.findByScorePaged(1, size, false);
	}
	
	
	
/* ------------- injection methods ---------------- */	
	

	/**
	 * 
	 * @param ratingSearchDirectory
	 */
/*	public void setRatingSearchDirectory(Directory ratingSearchDirectory) {
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

	public void setRaterMetricsDao(PatronMetricsDao raterMetricsDao) {
		this.raterMetricsDao = raterMetricsDao;
	}

	public void setAwardDao(AwardDao awardDao) {
		this.awardDao = awardDao;
	}*/


	

	

}
