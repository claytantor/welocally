package com.sightlyinc.ratecred.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.authentication.UserPrincipal;
import com.sightlyinc.ratecred.authentication.UserPrincipalDao;
import com.sightlyinc.ratecred.dao.PatronDao;
import com.sightlyinc.ratecred.dao.PatronMetricsDao;
import com.sightlyinc.ratecred.dao.RatingDao;
import com.sightlyinc.ratecred.model.Award;
import com.sightlyinc.ratecred.model.Compliment;
import com.sightlyinc.ratecred.model.Patron;
import com.sightlyinc.ratecred.model.PatronMetrics;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rating;

@Service("patronManagerService")
public class PatronManagerServiceImpl implements PatronManagerService {
	

	static Logger logger = Logger.getLogger(PatronManagerServiceImpl.class);
	
	@Autowired
	private UserPrincipalDao userPrincipalDao;
	
	@Autowired
	private PatronDao patronDao;
	
	@Autowired
	private RatingDao ratingDao;

	@Autowired
	private PatronMetricsDao patronMetricsDao;
	
	
	
	@Override
	public List<Patron> findPatronsByScoreDesc(int size)
			throws BLServiceException {
		return patronDao.findByScorePaged(1, size, false);
	}
	
	@Override
	public void savePatron(Patron entity) {
		
		//compute score
		//get the metrics 
		if(entity.getId() != null)
		{
			PatronMetrics tm =
				findMetricsByPatron(entity);
			entity.setScore(tm.getScore());
		}
		
		patronDao.save(entity);
	}
	
	@Override
	public Patron findPatronByAuthId(String authId)
			throws BLServiceException {
		Patron t = patronDao.findByAuthId(authId);
		//findRaterAwards(t);
		return t;
	}

	@Override
	public Patron findPatronByUsername(String userName)
			throws BLServiceException {
		
		Patron t = patronDao.findByUserName(userName);
		return t;
	}
	
	@Override
	public Patron findPatronByPrimaryKey(Long id) {
		Patron t = patronDao.findByPrimaryKey(id);
		//findRaterAwards(t);
		return t;
	}
	
	@Override
	public List<Patron> findPatronsByCityStateScoreDesc(PlaceCityState cs,
			int size) throws BLServiceException {
		
		//first find all the ratings by a city state
		List<Patron> areaLeaders 
			= patronDao.findByCityStateScorePaged(cs.getCity(), cs.getState(), 1, size, false);

		return areaLeaders;
	}
	
	@Override
	public void deletePatron(Patron entity) {
		patronDao.delete(entity);
	}

	@Override
	public List<Patron> findAllPatrons() {
		return patronDao.findAll();
	}
	
	@Override
	public List<Patron> findPatronsRatedSince(Long millis)
			throws BLServiceException {
		Set<Patron> since = new HashSet<Patron>();
		List<Rating> ratingSince = ratingDao.findSince(millis);
		for (Rating rating : ratingSince) {
			since.add(rating.getOwner());
		}
		return new ArrayList<Patron>(since);
		
	}


	@Override
	public PatronMetrics findMetricsByPatron(Patron t) {
		return findMetricsByRater(t, null);
	}
	
	@Override
	public List<Patron> findPatronByStatus(String status)
			throws BLServiceException {
		return patronDao.findByStatus(status);
	}
	
	
	@Override
	public List<Patron> findPatronByPrimaryKeys(Long[] ids)
			throws BLServiceException {
		return patronDao.findByPrimaryKeys(ids);
	}

	@Override
	public List<Patron> findPatronsByScreenNames(String[] screenNames)
			throws BLServiceException {
		if(screenNames != null && screenNames.length>0)
			return patronDao.findByUserNames(screenNames);
		else return new ArrayList<Patron>();
	}
	
	

	@Override
	public void saveConvertPatron(Patron fromRater, Patron toRater)
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
		
		patronDao.save(fromRater);
		patronDao.save(toRater);
		
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
		
		PatronMetrics tm = patronMetricsDao.findByPatron(t);
		
		if(tm == null)
			return null;
		
		Long score = (tm.getRatings()*10l)+(tm.getGiven()*5l)+(tm.getReceived()*5l);
				
		tm.setScore(score);
		return tm;
	}
	
	@Override
	public Patron savePatronByUser(UserPrincipal u) throws BLServiceException {
		Patron owner = patronDao.findByUserName(u.getUsername());
		//generate a secret if this is a first posting
		if(owner == null)
		{
			owner = new Patron();
			owner.setUserName(u.getUsername());
			//owner.setTimeCreated(Calendar.getInstance().getTime());
			owner.setStatus("USER");
			owner.setScore(10l);
			
			//we need to get the image url from twitter

//			try {
//				AccessToken accessToken = 
//					new AccessToken(
//							u.getTwitterToken(), 
//							u.getTwitterTokenSecret());
//
//				Twitter twitter = 
//					new TwitterFactory().getOAuthAuthorizedInstance(
//							appConsumerKey, appSecretKey, accessToken);	
//				
//				twitter4j.UserPrincipal twitterUserPrincipal = 
//					twitter.showUserPrincipal(u.getTwitterScreenName());
//				
//				/*URL profileImage = twitterUserPrincipal.getProfileImageURL();
//				if(profileImage != null)
//				{
//					ImageValue iv = new ImageValue();
//					iv.setType("TWITTER");
//					iv.setFilename(profileImage.toString());
//					imageValueDao.save(iv);										
//					//owner.setRaterImage(iv);
//					
//					throw new RuntimeException("NEED A NEW WAY TO DEAL WITH IMAGES");
//				}*/
//				
//			} catch (TwitterException e) {
//				logger.debug("could not get userPrincipal info from twitter");
//			}

			
			patronDao.save(owner);
		}
		return owner;
	}
	
	@Override
	public void saveUpdatePatronByUser(UserPrincipal u, Patron r)
			throws BLServiceException {
		
		/*//need to update image
		if(r.getRaterImage() == null || !r.getRaterImage().getType().equals("TWITTER"))
		{
			AccessToken accessToken = 
				new AccessToken(
						u.getTwitterToken(), 
						u.getTwitterTokenSecret());
			try {
				Twitter twitter = 
					new TwitterFactory().getOAuthAuthorizedInstance(
							appConsumerKey, appSecretKey, accessToken);	
				
				r.setStatus("USER");
				
				//make the image
				//r.setRaterImage(new ImageValue());
				//r.getRaterImage().setType("TWITTER");
				
				ResponseList<twitter4j.UserPrincipal> userPrincipalInfo
					= twitter.lookupUserPrincipals(new int[] {twitter.getId()});
				twitter4j.UserPrincipal userPrincipalMe = userPrincipalInfo.get(0);
				logger.debug("userPrincipal profile image:"+userPrincipalMe.getProfileImageURL().toString());
				
				//r.getRaterImage().setFilename(userPrincipalMe.getProfileImageURL().toString());
				
				patronDao.save(r);
				
				throw new RuntimeException("NEED A NEW WAY TO DEAL WITH IMAGES");
				
			} catch (TwitterException e) {
				logger.debug("could not get userPrincipal info from twitter");
			}
			
		}*/
		
		throw new RuntimeException("NO IMPL");
		
	}
	
	@Override
	public void saveUpdatePatronByUsername(String username)
			throws BLServiceException {
		UserPrincipal u = userPrincipalDao.findByUserName(username);
		Patron p = patronDao.findByUserName(username);
		if(u != null && u.getTwitterToken() != null && p != null)
			saveUpdatePatronByUser(u, p);
		
	}
	
	
	
	/*@Override
	public void deleteUserPrincipal(UserPrincipal u) throws BLServiceException {
		userPrincipalDao.delete(u);
	}*/

	/*@Override
	public List<UserPrincipal> findUserPrincipalsByTwitterIds(Long[] twitterids)
			throws BLServiceException {
		List<UserPrincipal> userPrincipals = userPrincipalDao.findByTwitterIds(twitterids);
		return userPrincipals;
	}*/

	/*@Override
	public UserPrincipal findUserPrincipalByPrimaryKey(Long id) throws BLServiceException {
		return userPrincipalDao.findById(id);
	}*/
	
	


	/*	@Override
	 * public void saveUpdatePatronByUserPrincipalname(String userPrincipalname)
			throws BLServiceException {
		UserPrincipal u = userPrincipalDao.findByUserPrincipalname(userPrincipalname);
		Patron r = patronDao.findByUserPrincipalName(userPrincipalname);
		if(u != null && u.getTwitterToken() != null && r != null)
			saveUpdateRaterByUserPrincipal(u, r);
		
	}*/

	/*@Override
	public void saveUpdateRaterByUserPrincipal(UserPrincipal u, Patron r)
			throws BLServiceException {
		
		//need to update image
		if(r.getRaterImage() == null || !r.getRaterImage().getType().equals("TWITTER"))
		{
			AccessToken accessToken = 
				new AccessToken(
						u.getTwitterToken(), 
						u.getTwitterTokenSecret());
			try {
				Twitter twitter = 
					new TwitterFactory().getOAuthAuthorizedInstance(
							appConsumerKey, appSecretKey, accessToken);	
				
				r.setStatus("USER");
				
				//make the image
				//r.setRaterImage(new ImageValue());
				//r.getRaterImage().setType("TWITTER");
				
				ResponseList<twitter4j.UserPrincipal> userPrincipalInfo
					= twitter.lookupUserPrincipals(new int[] {twitter.getId()});
				twitter4j.UserPrincipal userPrincipalMe = userPrincipalInfo.get(0);
				logger.debug("userPrincipal profile image:"+userPrincipalMe.getProfileImageURL().toString());
				
				//r.getRaterImage().setFilename(userPrincipalMe.getProfileImageURL().toString());
				
				patronDao.save(r);
				
				throw new RuntimeException("NEED A NEW WAY TO DEAL WITH IMAGES");
				
			} catch (TwitterException e) {
				logger.debug("could not get userPrincipal info from twitter");
			}
			
			
			
		//}
		
	}

	@Override
	public Patron saveRaterByUserPrincipal(UserPrincipal u) throws BLServiceException {
		
		Patron owner = patronDao.findByUserPrincipalName(u.getUserPrincipalName());
		//generate a secret if this is a first posting
		if(owner == null)
		{
			owner = new Patron();
			owner.setUserPrincipalName(u.getTwitterScreenName());
			//owner.setTimeCreated(Calendar.getInstance().getTime());
			owner.setStatus("USER");
			owner.setScore(10l);
			
			//we need to get the image url from twitter

			try {
				AccessToken accessToken = 
					new AccessToken(
							u.getTwitterToken(), 
							u.getTwitterTokenSecret());

				Twitter twitter = 
					new TwitterFactory().getOAuthAuthorizedInstance(
							appConsumerKey, appSecretKey, accessToken);	
				
				twitter4j.UserPrincipal twitterUserPrincipal = 
					twitter.showUserPrincipal(u.getTwitterScreenName());
				
				URL profileImage = twitterUserPrincipal.getProfileImageURL();
				if(profileImage != null)
				{
					ImageValue iv = new ImageValue();
					iv.setType("TWITTER");
					iv.setFilename(profileImage.toString());
					imageValueDao.save(iv);										
					//owner.setRaterImage(iv);
					
					throw new RuntimeException("NEED A NEW WAY TO DEAL WITH IMAGES");
				}
				
			} catch (TwitterException e) {
				logger.debug("could not get userPrincipal info from twitter");
			}

			
			patronDao.save(owner);
		}
		return owner;
	}
	
	@Override
	public UserPrincipal findUserPrincipalByTwitterScreenName(String twitterScreenName)
			throws BLServiceException {
		return userPrincipalDao.findByTwitterScreenName(twitterScreenName);
	}

	@Override
	public UserPrincipal findUserPrincipalByUserPrincipalname(String userPrincipalname) throws BLServiceException {
		return userPrincipalDao.findByUserPrincipalname(userPrincipalname);
	}

	@Override
	public Long saveUserPrincipal(UserPrincipal u) throws BLServiceException {
		//no userPrincipal name?
		if(u.getUserPrincipalName() == null)
			u.setUserPrincipalName(
					u.getTwitterScreenName());
		
		if(u.getTimeCreatedMills()==null)
			u.setTimeCreatedMills(
					Calendar.getInstance().getTimeInMillis());
		
		return userPrincipalDao.save(u);
	}*/

	
	

}
