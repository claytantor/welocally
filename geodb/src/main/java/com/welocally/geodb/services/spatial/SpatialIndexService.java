package com.welocally.geodb.services.spatial;

import org.json.JSONObject;

public interface SpatialIndexService {

	public void index(int maxDocs) throws SpatialIndexException;
	public void indexPlace(JSONObject place) throws SpatialIndexException;

}