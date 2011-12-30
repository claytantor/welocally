package com.welocally.geodb.services.spatial;

import org.apache.lucene.search.IndexSearcher;
import org.json.JSONArray;

import com.welocally.geodb.services.index.DirectoryException;

public interface SpatialSearchService {

	public JSONArray find(IndexSearcher searcher, Point start, double km, String queryString) 
	throws SpatialSearchException ;
	public IndexSearcher getPlaceSearcher() throws SpatialSearchException;
	
}