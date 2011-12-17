package com.welocally.geodb.services.spatial;

import org.json.JSONArray;

import com.welocally.geodb.services.index.DirectoryException;

public interface SpatialSearchService {

	public JSONArray find(String queryString, Point start, double km, String collectionName)
		throws SpatialSearchException,DirectoryException ;
	
}