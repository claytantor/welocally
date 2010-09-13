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

import org.apache.commons.lang.StringUtils;
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
import com.sightlyinc.ratecred.model.Rater;
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
	private RaterDao raterDao;
    private ComplimentDao complimentDao;
    private PlaceCityStateDao placeCityStateDao;
	private PlaceDao placeDao;
	
	private String appConsumerKey;
	private String appSecretKey;
	private String ratingUrlPrefix;
	
	private Directory ratingDirectory;	
	private RatingDirectoryIndexer ratingDirectoryIndexer;
	
	
	
	
	//private Integer ratingsPerPage = 10;
	
	
	@Override
	public Rating findRateByTime(Long time) {
		return ratingDao.findByTime(time);
	}

/*	@Override
	public Rater findRaterByTwitterScreenName(String twitterScreenName)
			throws BLServiceException {
		Rater t = raterDao.findByUserName(userName);
		return t;
	}*/
	
	

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
	
	private RaterMetrics findMetricsByRater(Rater t, String currentStatus) {
		
		RaterMetrics tm = raterMetricsDao.findByRater(t);
		Long score = null;
		
		//when we have a user that is being created 
		if(StringUtils.isEmpty(currentStatus))
		{
			score = (tm.getRatings()*10l)+(tm.getGiven()*5l)+(tm.getReceived()*5l);
			if(tm.getStatus().equals("USER"))
				score=score+100;
		}
		else
		{
			score = (tm.getRatings()*10l)+(tm.getGiven()*5l)+(tm.getReceived()*5l);
			if(currentStatus.equals("USER"))
				score=score+100;
		}
		
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
			
			/*//get the metrics for complementor
			RaterMetrics tmcomp =
				findMetricsByRater(complementor);
			if(tmcomp != null)
				complementor.setScore(tmcomp.getScore());
			raterDao.save(complementor);
					
			//update the score of the person being complimented
			Rater rater = towards.getOwner();
			
			RaterMetrics tmrater =
				findMetricsByRater(rater);
			if(tmrater != null)
			{
				Long score2 = tmrater.getScore();
				rater.setScore(score2.longValue());	
				raterDao.save(rater);
			}*/
			
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
		
		logger.debug("pagenum:"+pageNum);
		RatingPage tp = new RatingPage(); 
		tp.setPageSize(ratingsPerPage);
		tp.setSortField(sortField);
		tp.setAscending(isAcending);		
		if(raters != null && raters.size()>0)
		{
			Long[] ownerIds = new Long[raters.size()];
			int i=0;
			for (Rater rater : raters) 
				ownerIds[i] = rater.getId();
	
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

	/*public void saveRatingHold(Rating entity) 
	throws BLServiceException
	{
		
		try {
			Rater owner = 
				raterDao.findByUserName(
						entity.getOwner().getUserName());
			

			
			//we really want to work on the matching here, this 
			//should make an attempt to match up to an existing 
			//place if it makes sense, use probability

			//1. if there is lat long get places close to here
			List<Place> places = null;
			if(entity.getPlace().getTwitterId() != null)
			{
				places = new ArrayList<Place>();
				//Place p = placeDao.f
				AccessToken accessToken = 
					new AccessToken(
							owner.getTwitterToken(), 
							owner.getTwitterTokenSecret());

				Twitter twitter = 
					new TwitterFactory().getOAuthAuthorizedInstance(
							appConsumerKey, appSecretKey, accessToken);	 
				
				//get the place
				Place p = twitter.getGeoDetails(r.getTwitterPlaceId());
			}
			else if(entity.getPlace().getLatitude() != null
					&& entity.getPlace().getLongitude() != null)
				places = 
					placeDao.findByGeoBounding(
							entity.getPlace().getLatitude() - 0.001, 
							entity.getPlace().getLongitude() - 0.001, 
							entity.getPlace().getLatitude() + 0.001, 
							entity.getPlace().getLongitude() + 0.001);
			else 
				//lame brute force
				places = 
					placeDao.findByNamePrefix(
							entity.getPlace().getName().substring(0, 3));
								
			
			//if its found use it, otherwise create
			if(places != null && places.size()>0)
			{
				Collections.sort(
						places,
						new PobabilisticNameAndLocationPlaceComparitor(
								entity.getPlace()));
				
				Place p = places.get(0);
				
				Set<PlaceRating> ratings = p.getPlaceRatings();
				PlaceRating ratingForType = 
					RatingHelper.computeNewRatingAdd(
							new ArrayList<PlaceRating>(ratings), 
							new ArrayList<Rating>(p.getRatings()), 
							entity.getType(), 
							entity.getRaterRating());
				
				//this needs to be tested
				ratings.add(ratingForType);
				p.setPlaceRatings(ratings);
				
				entity.setPlace(p);
				//deal with the place rating
			}
			else
			{
				PlaceRating rating = new PlaceRating();
				rating.setType(entity.getType());
				rating.setRating(entity.getRaterRating());
				Set<PlaceRating> ratings = new HashSet<PlaceRating>();
				ratings.add(rating);
				entity.getPlace().setPlaceRatings(ratings);
				placeDao.save(entity.getPlace());
			}
			
			//generate a secret if this is a first posting
			if(owner == null)
			{
				Rater newOwner = entity.getOwner();
				newOwner.setUserName(entity.getOwner().getSecretKey());
				newOwner.setTimeCreated(Calendar.getInstance().getTime());
				newOwner.setStatus("ANON");
				newOwner.setScore(10l);
				String uuid = UUID.randomUUID().toString();
				String[] parts = uuid.split("-");
				newOwner.setSecretKey(parts[0]);
				newOwner.setUserName(parts[1]);
				newOwner.setStatus("ANON");
				raterDao.save(newOwner);
			}
			else
			{
				

				RaterMetrics tmcomp =
					findMetricsByRater(owner);
				if(tmcomp != null)
					owner.setScore(tmcomp.getScore());
				
				raterDao.save(owner);
				entity.setOwner(owner);
			}
			
			ratingDao.save(entity);
			
			//compute score
			//get the metrics 
			RaterMetrics tm =
				findMetricsByRater(owner);
			if(tm != null)
				owner.setScore(tm.getScore());
			raterDao.save(owner);
			
			ratingDirectoryIndexer.indexRate(entity);
		} catch (Exception e) {
			logger.error("problem", e);
			throw new BLServiceException(e);
		}
	}*/

	
	@Override
	public void saveRating(Rating entity) throws BLServiceException {

		try {
			Rater owner = entity.getOwner();
			Place p = entity.getPlace();

			Set<PlaceRating> ratings = p.getPlaceRatings();
			
			PlaceRating ratingForType = RatingHelper.computeNewRatingAdd(
					new ArrayList<PlaceRating>(ratings), new ArrayList<Rating>(
							p.getRatings()), entity.getType(), entity
							.getRaterRating());

			// this needs to be tested
			ratings.add(ratingForType);
			p.setPlaceRatings(ratings);

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
			
			//save it
			ratingDao.save(entity);

			//index it
			ratingDirectoryIndexer.indexRate(entity);
			
			
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
	
	

	

}
