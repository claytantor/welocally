package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.util.ArrayList;
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

import com.noi.utility.math.Rounding;
import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.dao.PlaceAttributeDao;
import com.sightlyinc.ratecred.dao.PlaceCityStateDao;
import com.sightlyinc.ratecred.dao.PlaceDao;
import com.sightlyinc.ratecred.dao.PlaceRatingDao;
import com.sightlyinc.ratecred.index.PlaceDirectoryIndexer;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceAttribute;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.PlacePage;
import com.sightlyinc.ratecred.model.PlaceRating;
import com.sightlyinc.ratecred.model.Rating;


public class PlaceManagerServiceImpl implements PlaceManagerService {
	
	static Logger logger = 
		Logger.getLogger(PlaceManagerServiceImpl.class);
	
	private PlaceCityStateDao placeCityStateDao;
	private PlaceDao placeDao;
	private PlaceAttributeDao placeAttributeDao;
	private PlaceRatingDao placeRatingDao;
	
	private Directory placeSearchDirectory;	
	private PlaceDirectoryIndexer placeDirectoryIndexer;
	
/*	private String appConsumerKey;
	private String appSecretKey;*/

	
	
	/**
	 * will attempt to find in the database, if its there then dont go to twitter
	 * otherwise go to twitter, get it using the user's auth token, and save it to the data
	 * base so its bounded. If neither exists return null;
	 * 
	 * @TODO should check if access token is actually required for this, if not it would 
	 * be a cleaner approach, secondly this should be asynchronous because we dont want a 
	 * call like this blocking
	 * 
	 */
	@Override
	public com.sightlyinc.ratecred.model.Place findByTwitterId(
			String twitterId) throws BLServiceException {
		
		
		//get the place
		//try to find in in the store
		com.sightlyinc.ratecred.model.Place place = 
			placeDao.findByTwitterId(twitterId);
		
		/*if(place == null)
		{
		
			try {
				AccessToken accessToken = 
					new AccessToken(
							accessUserToken, 
							accessSecret);
	
				Twitter twitter = 
					new TwitterFactory().getOAuthAuthorizedInstance(
							appConsumerKey, appSecretKey, accessToken);	 
				
				//make the place
				place = new com.sightlyinc.ratecred.model.Place();
				twitter4j.Place p = twitter.getGeoDetails(twitterId);
				
				for (int i = 0; i < p.getContainedWithIn().length; i++) {
					twitter4j.Place wrapper = p.getContainedWithIn()[i];
					
					if(wrapper.getPlaceType().equals("city"))
					{
						String[] cs = wrapper.getFullName().split(",");						
						place.setCity(cs[0]);
						place.setState(cs[1].replace(" ",""));
					}
					
				}
								
				place.setTwitterId(p.getId());
				place.setAddress(p.getStreetAddress());
				place.setName(p.getName());
				place.setPhone(p.getPhone());
				place.setZip(p.getPostalCode());
				place.setLatitude(p.getGeometryCoordinates()[1]);
				place.setLongitude(p.getGeometryCoordinates()[0]);
				place.setTimeCreated(Calendar.getInstance().getTime());
				
				placeDao.save(place);
				
				
			} catch (TwitterException e) {
				logger.error("cannot find place with id:"+twitterId, e);
				
			}
		}*/
		
		return place;
	}





	@Override
	public Place findPlaceByNameAddressCityState(String name, String address,
			String city, String state) throws BLServiceException {
		List<Place> places = 
			placeDao.findByNameCityState(
					name, 
					city, 
					state);
		for (Place place : places) {
			if(place.getAddress().equalsIgnoreCase(address))
				return place;
		}
		if(places.size()>0)
			return places.get(0);
		
		return null;
	}

	
	@Override
	public List<Place> findPlacesByNamePrefix(String namePrefix)
			throws BLServiceException {
		return placeDao.findByNamePrefix(namePrefix);
	}



	@Override
	public void deletePlaceAttribute(Place p, PlaceAttribute attr)
			throws BLServiceException {
		p.getAttributes().remove(attr);
		placeDao.save(p);
		placeAttributeDao.delete(attr);
	}
	
	

	@Override
	public void deletePlace(Place p) throws BLServiceException {
		placeDao.delete(p);		
	} 



	@Override
	public List<PlaceCityState> findMostActivePlaceCityStates(Integer pagesize) {
		return placeCityStateDao.findMostRatedOrdered(0,pagesize);
	}

	


	@Override
	public PlacePage findPlacesRatedByType( 
			String type, 
			Integer pageNum,
			Integer pageSize, 
			boolean isAcending)
			throws BLServiceException {
		
		PlacePage tp = new PlacePage(); 
		tp.setPageSize(pageSize);
		tp.setAscending(isAcending);
				
		List<PlaceRating> ratings = placeRatingDao.findByTypePaged(type,
				pageNum, pageSize, isAcending);
		
		
		for (PlaceRating placeRating : ratings) {
			tp.getPlaces().add(placeRating.getPlace());
		}
	
		
		return tp;

	}
	

	@Override
	public PlacePage findPlacesRated(Integer pageNum, Integer pageSize,
			boolean isAcending) throws BLServiceException {
		PlacePage tp = new PlacePage(); 
		tp.setPageSize(pageSize);
		tp.setAscending(isAcending);
				
		List<PlaceRating> ratings = placeRatingDao.findAllPaged(
				pageNum, pageSize, isAcending);
				
		for (PlaceRating placeRating : ratings) {
			tp.getPlaces().add(placeRating.getPlace());
		}
		
		return tp;
	}


	@Override
	public PlacePage findPlacesByText(String text, Integer pageNum,
			Integer pageSize, boolean isAcending) throws BLServiceException {
		
		PlacePage page = new PlacePage(); 
		
		try {
			Map<String,Rating> ratingResult = new HashMap<String,Rating>();
			Query q = new QueryParser("indexContent", new StandardAnalyzer())
					.parse(text);

			Searcher s = new IndexSearcher(placeSearchDirectory);
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
				page.setPlaces(placeDao.findByPrimaryKeys(new ArrayList(ratingIds)));

		} catch (ParseException e) {
			throw new BLServiceException(e);
		} catch (IOException e) {
			throw new BLServiceException(e);
		}
		
		
		return page;
	}



	@Override
	public List<Place> findAllPlaces() throws BLServiceException {
		return placeDao.findAll();
	}

	@Override
	public Place findPlaceByPrimaryKey(Long id) throws BLServiceException {
		return placeDao.findByPrimaryKey(id);
	}

	@Override
	public Long savePlace(Place p) throws BLServiceException {
		placeDao.save(p);
		return p.getId();
	}
		
	
	@Override
	public PlacePage findCityStatePlacesRatedByType(PlaceCityState cs,
			String type, Integer pageNum, Integer pageSize, boolean isAscending)
			throws BLServiceException {
		PlacePage tp = new PlacePage(); 
		tp.setPageSize(pageSize);
		tp.setAscending(isAscending);
		
		Long totalPlaces = placeDao.findByCityStateCount(cs.getCity(), cs.getState());
		tp.setTotalResults(totalPlaces);
	
		
		Float pagesAll = 
			totalPlaces.floatValue()
			/ pageSize.floatValue();
		
		Float pages = 
			Rounding.roundFloat(pagesAll,0); 
		
		if(pagesAll>pages)
			pages=pages+1.0f;
		
		tp.setPageNumber(pageNum);			
		tp.setTotalPages(pages.intValue());	
		
		List<PlaceRating> ratings = placeRatingDao.findByCityStateTypePaged(
				cs.getCity(), cs.getState(), type,
				pageNum, pageSize, type, isAscending);
		
		for (PlaceRating placeRating : ratings) {
			tp.getPlaces().add(placeRating.getPlace());
		}
			
		return tp;
	}


	
	
	@Override
	public PlacePage findCityStatePlacesRated(PlaceCityState cs,
			Integer pageNum, Integer pageSize, boolean isAscending)
			throws BLServiceException {
		PlacePage tp = new PlacePage(); 
		tp.setPageSize(pageSize);
		tp.setAscending(isAscending);
		
		Long totalPlaces = placeDao.findByCityStateCount(cs.getCity(), cs.getState());
		tp.setTotalResults(totalPlaces);
	
		
		Float pagesAll = 
			totalPlaces.floatValue()
			/ pageSize.floatValue();
		
		Float pages = 
			Rounding.roundFloat(pagesAll,0); 
		
		if(pagesAll>pages)
			pages=pages+1.0f;
		
		tp.setPageNumber(pageNum);			
		tp.setTotalPages(pages.intValue());	
		
		List<PlaceRating> ratings = placeRatingDao.findByCityStatePaged(
				cs.getCity(), cs.getState(), 
				pageNum, pageSize, isAscending);
		
		for (PlaceRating placeRating : ratings) {
			tp.getPlaces().add(placeRating.getPlace());
		}
			
		return tp;
	}





	/**
	 * consolodate duplicate place and move all relations
	 * 
	 */
	@Override
	public void savePlaceDuplicateConsolidation(Place p, Place duplicatePlace)
			throws BLServiceException {
		
		//orphan any ratings
		Set<Rating> ratings = 
			new HashSet<Rating>(duplicatePlace.getRatings());
		for (Rating rating : ratings) {
			duplicatePlace.getRatings().remove(rating);
			
			//now reconcile rating for place
			Set<PlaceRating> placeRatings = p.getPlaceRatings();
			PlaceRating ratingForType = 
				RatingHelper.computeNewRatingAdd(
						new ArrayList<PlaceRating>(placeRatings), 
						new ArrayList<Rating>(p.getRatings()), 
						rating.getType(), 
						rating.getRaterRating());
			
			//this needs to be tested
			placeRatings.add(ratingForType);
			p.setPlaceRatings(placeRatings);
			
			
			rating.setPlace(p);
		}
		
		placeDao.save(duplicatePlace);
		placeDao.save(p);
		placeDao.delete(duplicatePlace);
	}

	@Override
	public List<Place> findPlacesByGeoBounding(Double minLat, Double minLong,
			Double maxLat, Double maxLong) throws BLServiceException {
		return placeDao.findByGeoBounding(minLat, minLong, maxLat, maxLong);
	}

	@Override
	public List<Place> findAllPlacesForCity(PlaceCityState cs)
			throws BLServiceException {
		return placeDao.findByCityState(cs.getCity(), cs.getState());
	}

	@Override
	public List<PlaceCityState> findAllPlaceCityStates()
			throws BLServiceException {
		return placeCityStateDao.findAll();
	}
	
	public void setPlaceCityStateDao(PlaceCityStateDao placeCityStateDao) {
		this.placeCityStateDao = placeCityStateDao;
	}

	public void setPlaceDao(PlaceDao placeDao) {
		this.placeDao = placeDao;
	}

	public void setPlaceAttributeDao(PlaceAttributeDao placeAttributeDao) {
		this.placeAttributeDao = placeAttributeDao;
	}

	public void setPlaceRatingDao(PlaceRatingDao placeRatingDao) {
		this.placeRatingDao = placeRatingDao;
	}

	public void setPlaceSearchDirectory(Directory placeSearchDirectory) {
		this.placeSearchDirectory = placeSearchDirectory;
	}

	public void setPlaceDirectoryIndexer(PlaceDirectoryIndexer placeDirectoryIndexer) {
		this.placeDirectoryIndexer = placeDirectoryIndexer;
	}





/*	public void setAppConsumerKey(String appConsumerKey) {
		this.appConsumerKey = appConsumerKey;
	}





	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}*/
	
}
