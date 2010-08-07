package com.sightlyinc.ratecred.service;

import java.util.List;

import com.noi.utility.spring.service.BLServiceException;
import com.sightlyinc.ratecred.model.Place;
import com.sightlyinc.ratecred.model.PlaceAttribute;
import com.sightlyinc.ratecred.model.PlaceCityState;
import com.sightlyinc.ratecred.model.PlacePage;

public interface PlaceManagerService {
		
	public List<Place> findAllPlacesForCity(PlaceCityState cs) throws BLServiceException;
	
/*	public PlacePage findPlacesByCityStateRatedByType(
			PlaceCityState cs, 
			String type, 
			Integer i, 
			Integer pageSize, 
			String string, 
			boolean b) throws RaterBLServiceException;*/
	
	public PlacePage findPlacesRatedByType(
			String type, 
			Integer pageNum, 
			Integer pageSize, 
			boolean b) throws BLServiceException;
	
	public PlacePage findCityStatePlacesRatedByType(
			PlaceCityState cs,
			String type, 
			Integer pageNum, 
			Integer pageSize, 
			boolean b) throws BLServiceException;
	
	public PlacePage findPlacesByText(
			String text, 
			Integer pageNum, 
			Integer pageSize, 
			boolean b) throws BLServiceException;
	
	
	
	public List<PlaceCityState> findAllPlaceCityStates()  throws BLServiceException;
	public List<PlaceCityState> findMostActivePlaceCityStates(Integer pageSize)  
		throws BLServiceException;
		
	public Place findPlaceByPrimaryKey(Long id) throws BLServiceException;
	public Place findPlaceByNameAddressCityState(String name, String address, String city, String state) throws BLServiceException;
	
	public List<Place> findAllPlaces() throws BLServiceException;
		
	public List<Place> findPlacesByGeoBounding(Double minLat, Double minLong, Double maxLat, Double maxLong) throws BLServiceException;
	
	public List<Place> findPlacesByNamePrefix(String namePrefix) throws BLServiceException;
	
	public Long savePlace(Place p) throws BLServiceException;
	public void savePlaceDuplicateConsolidation(Place p, Place duplicatePlace) throws BLServiceException;	

	public void deletePlaceAttribute(Place p, PlaceAttribute attrs) throws BLServiceException;
	public void deletePlace(Place p) throws BLServiceException;
		
}
