package com.sightlyinc.ratecred.client.link;

import java.util.Map;

public class LinkClientRequest {
	private Map<String, String> linkRequestModel;

	public void setLinkRequestModel(Map<String, String> linkRequestModel) {
		this.linkRequestModel = linkRequestModel;
	}

	public Map<String, String> getLinkRequestModel() {
		return linkRequestModel;
	}

}
