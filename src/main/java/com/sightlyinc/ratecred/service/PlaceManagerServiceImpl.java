package com.sightlyinc.ratecred.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	private PlaceCityStateDao placeCityStateDao;
	private PlaceDao placeDao;
	private PlaceAttributeDao placeAttributeDao;
	private PlaceRatingDao placeRatingDao;
	
	private Directory placeSearchDirectory;	
	private PlaceDirectoryIndexer placeDirectoryIndexer;

	
	
	
	@Override
	public Place findByTwitterId(String twitterId) throws BLServiceException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
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
	
}
