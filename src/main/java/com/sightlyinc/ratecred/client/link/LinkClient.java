package com.sightlyinc.ratecred.client.link;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

public interface LinkClient {
	
	//public List<Link> getLinks();
	public NetworkResponse getNetworkResponse(LinkClientRequest requestModel);

}
