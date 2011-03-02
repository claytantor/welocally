package com.sightlyinc.ratecred.service;

import java.util.Date;
import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rater;
import com.sightlyinc.ratecred.model.RaterMetrics;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.model.RatingPage;

public interface RatingManagerService {
	
	public void saveUpdatePlaceRatings();
	public void saveUpdatePlaceRating(Long id);
	
	public Rating findRatingByPrimaryKey(Long id) throws BLServiceException;
	
	public Rating findRatingByCheckinTxId(String checkinTxId) throws BLServiceException;
	
	public Rating findRateByTime(Long time);
	public List<Rating> findAllRates() throws BLServiceException;	
	public RatingPage findAllRatingsAsPage(
			Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending) 
			throws BLServiceException;
	
	public List<Rating> findRatingsByCityState(PlaceCityState cs) throws BLServiceException;
	
	public RatingPage findRatingsByCityState(
			PlaceCityState cs, Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending)
			throws BLServiceException;
	
	public List<Rating> findRatingsByCityStatePlaceInfo(PlaceCityState cs, Place tp) throws BLServiceException;
	public RatingPage findRatingsByCityStatePlaceInfo(Integer pageNum, Integer ratingsPerPage,  String sortField, boolean isAcending, PlaceCityState cs, Place tp) throws BLServiceException;
	
	public List<Rating> findRatingsSince(Long millis) throws BLServiceException;
	
	public RatingPage findRatingsByOwner(Integer pageNum, Integer ratingsPerPage,  String sortField, boolean isAcending, Rater rater) throws BLServiceException;
	public RatingPage findRatingsByOwners(Integer pageNum, Integer ratingsPerPage,  String sortField, boolean isAcending, List<Rater> raters) throws BLServiceException;

	//awards THESE SHOULD MOVE
	public List<Award> findAwardsLocalByRater(Rater t) throws BLServiceException;
	public List<Award> findAwardsByOwner(Rater t) throws BLServiceException;
	public Award findAwardById(Long awardId) throws BLServiceException;
	public void saveAward(Award entity) throws BLServiceException;
	//public void saveAwardOffer(AwardOffer entity) throws BLServiceException;
	
	//search functions
	public List<Rating> findRatesByText(String text) throws BLServiceException;
	public RatingPage findRatesByText(String text, 
			Integer pageNum, 
			Integer pageSize, 
			boolean b) throws BLServiceException;
	
	public void deleteRate(Rating entity) throws BLServiceException;
	public Long saveRating(Rating entity) throws BLServiceException;
	public Long saveRatingWithCheckin(Rating entity) throws BLServiceException;
	
	
	//compliment
	public void saveCompliment(Compliment c, Rating twords, Rater complementor) throws BLServiceException;
	public List<Compliment> findComplimentsByRaterBetweenTimes(Rater towards, Date startTime, Date endTime) throws BLServiceException;
	public Long findComplimentCountByRaterBetweenTimes(
			Rater towards, Date startTime, Date endTime) throws BLServiceException;
	
	//awards given
	public List<Award> findBusinessAwardsByRaterBetweenTimes(
			Rater towards, Date startTime, Date endTime) throws BLServiceException;
	public Long findAwardCountByOwnerBetweenTimes(
			Rater towards, Date startTime, Date endTime) throws BLServiceException;
		
	
	//rater functions
	public Rater findRaterByPrimaryKey(Long string) throws BLServiceException;			
	public List<Rater> findRatersByPrimaryKeys(final Long[] ids) throws BLServiceException;
	public List<Rater> findRatersByStatus(String status) throws BLServiceException;
	
	public void saveConvertRater(Rater fromRater, Rater toRater) throws BLServiceException;

	
	public Rater findRaterByUsername(String userName) throws BLServiceException;
	//public Rater findRaterByTwitterScreenName(String twitterScreenName) throws BLServiceException;
	
	public Rater findRaterByAuthId(String authId) throws BLServiceException;	
	
	//create anonymous rater, must be saved
	public Rater createAnonymousRater();
	
	public List<Rater> findAllRaters() throws BLServiceException;	
	
	public List<Rater> findRatersRatedSince(Long millis) throws BLServiceException;
	
	public List<Rater> findRatersByScreenNames(String[] screenNames) throws BLServiceException;
				
	public void deleteRater(Rater entity) throws BLServiceException;
	
	public void saveRater(Rater entity) throws BLServiceException;
	
	
			
	public List<Rater> findRatersByScoreDesc(int size) throws BLServiceException;
	
	public List<Rater> findRatersByCityStateScoreDesc(PlaceCityState cs, int size) throws BLServiceException;
		
	//rater metrics
	public RaterMetrics findMetricsByRater(Rater t);
	
	
}
