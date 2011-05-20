package com.sightlyinc.ratecred.service;

import java.util.Date;
import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PatronMetrics;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.pojo.Page;

public interface RatingManagerService {
	
	public void saveUpdatePlaceRatings();
	public void saveUpdatePlaceRating(Long id);
	
	public Rating findRatingByPrimaryKey(Long id) throws BLServiceException;
	
	public Rating findRatingByCheckinTxId(String checkinTxId) throws BLServiceException;
	
	public Rating findRateByTime(Long time);
	public List<Rating> findAllRates() throws BLServiceException;	
	public Page<Rating> findAllRatingsAsPage(
			Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending) 
			throws BLServiceException;
	
	public List<Rating> findRatingsByCityState(PlaceCityState cs) throws BLServiceException;
	
	public Page<Rating> findRatingsByCityState(
			PlaceCityState cs, Integer pageNum, Integer ratingsPerPage, String sortField, boolean isAcending)
			throws BLServiceException;
	
	public List<Rating> findRatingsByCityStatePlaceInfo(PlaceCityState cs, Place tp) throws BLServiceException;
	public Page<Rating> findRatingsByCityStatePlaceInfo(Integer pageNum, Integer ratingsPerPage,  String sortField, boolean isAcending, PlaceCityState cs, Place tp) throws BLServiceException;
	
	public List<Rating> findRatingsSince(Long millis) throws BLServiceException;
	
	public Page<Rating> findRatingsByOwner(Integer pageNum, Integer ratingsPerPage,  String sortField, boolean isAcending, Patron rater) throws BLServiceException;
	public Page<Rating> findRatingsByOwners(Integer pageNum, Integer ratingsPerPage,  String sortField, boolean isAcending, List<Patron> raters) throws BLServiceException;

	//awards THESE SHOULD MOVE
	public List<Award> findAwardsLocalByRater(Patron t) throws BLServiceException;
	public List<Award> findAwardsByOwner(Patron t) throws BLServiceException;
	public Award findAwardById(Long awardId) throws BLServiceException;
	public void saveAward(Award entity) throws BLServiceException;
	//public void saveAwardOffer(AwardOffer entity) throws BLServiceException;
	
	//search functions
	public List<Rating> findRatesByText(String text) throws BLServiceException;
	public Page<Rating> findRatesByText(String text, 
			Integer pageNum, 
			Integer pageSize, 
			boolean b) throws BLServiceException;
	
	public void deleteRatingByPrimaryKey(Long id) throws BLServiceException;
	
	public void deleteRating(Rating entity) throws BLServiceException;
	public Long saveRating(Rating entity) throws BLServiceException;
	public Long saveRatingWithCheckin(Rating entity) throws BLServiceException;
	
	
	//compliment
	public void saveCompliment(Compliment c, Rating twords, Patron complementor) throws BLServiceException;
	public List<Compliment> findComplimentsByRaterBetweenTimes(Patron towards, Date startTime, Date endTime) throws BLServiceException;
	public Long findComplimentCountByRaterBetweenTimes(
			Patron towards, Date startTime, Date endTime) throws BLServiceException;
	
	//awards given
	public List<Award> findBusinessAwardsByRaterBetweenTimes(
			Patron towards, Date startTime, Date endTime) throws BLServiceException;
	public Long findAwardCountByOwnerBetweenTimes(
			Patron towards, Date startTime, Date endTime) throws BLServiceException;
		
	
	//rater functions
	public Patron findRaterByPrimaryKey(Long string) throws BLServiceException;			
	public List<Patron> findRatersByPrimaryKeys(final Long[] ids) throws BLServiceException;
	public List<Patron> findRatersByStatus(String status) throws BLServiceException;
	
	public void saveConvertRater(Patron fromRater, Patron toRater) throws BLServiceException;

	
	public Patron findRaterByUsername(String userName) throws BLServiceException;
	//public Rater findRaterByTwitterScreenName(String twitterScreenName) throws BLServiceException;
	
	public Patron findRaterByAuthId(String authId) throws BLServiceException;	
	
	//create anonymous rater, must be saved
	//public Patron createAnonymousRater();
	
	public List<Patron> findAllRaters() throws BLServiceException;	
	
	public List<Patron> findRatersRatedSince(Long millis) throws BLServiceException;
	
	public List<Patron> findRatersByScreenNames(String[] screenNames) throws BLServiceException;
				
	public void deleteRater(Patron entity) throws BLServiceException;
	
	public void saveRater(Patron entity) throws BLServiceException;
	
	
			
	public List<Patron> findRatersByScoreDesc(int size) throws BLServiceException;
	
	public List<Patron> findRatersByCityStateScoreDesc(PlaceCityState cs, int size) throws BLServiceException;
		
	//rater metrics
	public PatronMetrics findMetricsByRater(Patron t);
	
	
}
