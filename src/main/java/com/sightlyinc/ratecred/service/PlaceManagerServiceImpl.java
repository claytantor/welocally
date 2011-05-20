package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

import com.noi.utility.spring.service.BLServiceException;
import com.noi.utility.string.StringUtils;
import com.sightlyinc.ratecred.client.geo.GeoPlacesClient;
import com.sightlyinc.ratecred.compare.PobabilisticNameAndLocationPlaceComparitor;
import com.sightlyinc.ratecred.dao.PlaceAttributeDao;
import com.sightlyinc.ratecred.dao.PlaceCityStateDao;
import com.sightlyinc.ratecred.dao.PlaceDao;
import com.sightlyinc.ratecred.index.PlaceDirectoryIndexer;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceAttribute;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.Rating;
import com.sightlyinc.ratecred.pojo.Page;

@Service("PlaceManagerService")
@Transactional(readOnly = true)
public class PlaceManagerServiceImpl implements PlaceManagerService {
	
	static Logger logger = 
		Logger.getLogger(PlaceManagerServiceImpl.class);
	
	@Autowired
	private PlaceCityStateDao placeCityStateDao;
	
	@Autowired
	private PlaceDao placeDao;
	
	@Autowired
	private PlaceAttributeDao placeAttributeDao;
	
	//@Autowired
	//private PlaceRatingDao placeRatingDao;
	
	@Autowired
	@Qualifier("placeDirectory")
	private Directory placeSearchDirectory;	
	
	//@Autowired
	//@Qualifier("placeDirectoryIndexer")
	//private PlaceDirectoryIndexer placeDirectoryIndexer;
	
	@Autowired
	@Qualifier("locationPlacesClient")
	private GeoPlacesClient locationClient;
		
	@Transactional(readOnly = false)
	public void saveNewLocationInfo(Long placeId) throws BLServiceException
	{
		Place p = findPlaceByPrimaryKey(placeId);
		List<Place> nPlaces = locationClient.findPlaces(p.getLatitude(), p.getLongitude(), 0.5);
		Collections.sort(nPlaces, new PobabilisticNameAndLocationPlaceComparitor(p));
		if(nPlaces != null && nPlaces.size()>0 &&
				StringUtils.compareStrings(p.getName(), nPlaces.get(0).getName())>0.65)
		{
			logger.debug("updating place:"+p.getName());
			Place top = nPlaces.get(0);
			if(top.getAddress() != null)
				p.setAddress(top.getAddress());
			if(top.getCategory() != null)
				p.setCategory(top.getCategory());
			//if(top.getCategoryType() != null)
			//	p.setCategoryType(top.getCategoryType());
			if(top.getCity() != null)
				p.setCity(top.getCity());
			if(top.getState() != null)
				p.setState(top.getState());
			p.setLatitude(top.getLatitude());
			p.setLongitude(top.getLongitude());
			p.setSimpleGeoId(top.getSimpleGeoId());
			p.setSubcategory(top.getSubcategory());
			p.setWebsite(top.getWebsite());
			placeDao.save(p);
							
		}
		
	}
	
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
		
		return place;
	}

    @Override
    public Place findBySimpleGeoId(String simpleGeoId) throws BLServiceException {
		//get the place
		//try to find it in the store
		com.sightlyinc.ratecred.model.Place place =
			placeDao.findBySimpleGeoId(simpleGeoId);

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
	@Transactional(readOnly = false)
	public void deletePlaceAttribute(Place p, PlaceAttribute attr)
			throws BLServiceException {
		p.getAttributes().remove(attr);
		placeDao.save(p);
		placeAttributeDao.delete(attr);
	}
	
	

	@Override
	@Transactional(readOnly = false)
	public void deletePlace(Place p) throws BLServiceException {
		placeDao.delete(p);		
	} 



	@Override
	public List<PlaceCityState> findMostActivePlaceCityStates(Integer pagesize) {
		return placeCityStateDao.findMostRatedOrdered(0,pagesize);
	}

	


	@Override
	public Page<Place> findPlacesRatedByType( 
			String type, 
			Integer pageNum,
			Integer pageSize, 
			boolean isAcending)
			throws BLServiceException {
		
		/*PlacePage tp = new PlacePage(); 
		tp.setPageSize(pageSize);
		tp.setAscending(isAcending);
				
		List<PlaceRating> ratings = placeRatingDao.findByTypePaged(type,
				pageNum, pageSize, isAcending);
		
		
		for (PlaceRating placeRating : ratings) {
			tp.getPlaces().add(placeRating.getPlace());
		}
	
		
		return tp;*/
		throw new RuntimeException("TODO REMOVE PLACE RATING CONCEPT");

	}
	

	@Override
	public Page<Place> findPlacesRated(Integer pageNum, Integer pageSize,
			boolean isAcending) throws BLServiceException {
		/*PlacePage tp = new PlacePage(); 
		tp.setPageSize(pageSize);
		tp.setAscending(isAcending);
				
		List<PlaceRating> ratings = placeRatingDao.findAllPaged(
				pageNum, pageSize, isAcending);
				
		for (PlaceRating placeRating : ratings) {
			tp.getPlaces().add(placeRating.getPlace());
		}
		
		return tp;*/
		throw new RuntimeException("TODO REMOVE PLACE RATING CONCEPT");

	}


	@Override
	public Page<Place> findPlacesByText(String text, Integer pageNum,
			Integer pageSize, boolean isAcending) throws BLServiceException {
		
		Page<Place> page = new Page<Place>(); 
		
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
				page.setItems(placeDao.findByPrimaryKeys(new ArrayList(ratingIds)));

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
	@Transactional(readOnly = false)
	public Long savePlace(Place p) throws BLServiceException {
		placeDao.save(p);
		return p.getId();
	}
		
	
	@Override
	public Page<Place> findCityStatePlacesRatedByType(PlaceCityState cs,
			String type, Integer pageNum, Integer pageSize, boolean isAscending)
			throws BLServiceException {
		
		/*PlacePage tp = new PlacePage(); 
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
			
		return tp;*/
		throw new RuntimeException("TODO REMOVE PLACE RATING CONCEPT");

	}


	
	
	@Override
	public Page<Place> findCityStatePlacesRated(PlaceCityState cs,
			Integer pageNum, Integer pageSize, boolean isAscending)
			throws BLServiceException {
		
		/*PlacePage tp = new PlacePage(); 
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
			
		return tp;*/
		throw new RuntimeException("TODO REMOVE PLACE RATING CONCEPT");

	}





	/**
	 * consolodate duplicate place and move all relations
	 * 
	 */
	@Override
	@Transactional(readOnly = false)
	public void savePlaceDuplicateConsolidation(Place p, Place duplicatePlace)
			throws BLServiceException {
		
		//orphan any ratings
		Set<Rating> ratings = 
			new HashSet<Rating>(duplicatePlace.getRatings());
		for (Rating rating : ratings) {
			duplicatePlace.getRatings().remove(rating);
			
			/*//now reconcile rating for place
			Set<PlaceRating> placeRatings = p.getPlaceRatings();
			PlaceRating ratingForType = 
				RatingHelper.computeNewRatingAdd(
						new ArrayList<PlaceRating>(placeRatings), 
						new ArrayList<Rating>(p.getRatings()), 
						rating.getType(), 
						rating.getPatronRating());*/
			
			//this needs to be tested
			//placeRatings.add(ratingForType);
			//p.setPlaceRatings(placeRatings);
			
			
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
	
	/*public void setPlaceCityStateDao(PlaceCityStateDao placeCityStateDao) {
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
	}*/





/*	public void setAppConsumerKey(String appConsumerKey) {
		this.appConsumerKey = appConsumerKey;
	}





	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}*/
	
}
