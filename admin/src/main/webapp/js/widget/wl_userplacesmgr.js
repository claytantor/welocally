/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/
//foo
function WELOCALLY_UserPlacesManager (cfg) {		
	
	this.cfg;
	this.wrapper;
	
	
	this.init = function() {
								
		var errors = this.initCfg(cfg);
		
		// Get current script object
		var placeBefore = jQuery('SCRIPT');
		placeBefore = placeBefore[placeBefore.length - 1];
		
		if(cfg.placeBefore){
			placeBefore = cfg.placeBefore;
		} 
						
		if(errors){
			 var statusArea = jQuery("<div></div>");
			 WELOCALLY.ui.setStatus(statusArea, errors[0],'wl_error',false);
			 jQuery(placeBefore).parent().before(statusArea);
		} else {
			// Build Widget
			this.wrapper = this.makeWrapper();	
			jQuery(placeBefore).parent().before(this.wrapper);
		
		}
		
		return this;
					
	};
	
}

WELOCALLY_UserPlacesManager.prototype.initCfg = function(cfg) {
	var errors = [];
	if (!cfg) {
		errors.push('Please provide a configuration');
	}
	
	
	// summary (optional) - the summary of the article
	// hostname (optional) - the name of the host to use
	if (!cfg.endpoint) {
		cfg.endpoint = 'http://stage.welocally.com';
	}
	
	if (!cfg.imagePath) {
		cfg.imagePath = 'http://placehound.com/images/marker_all_base.png';
	}
	
	if (!cfg.zoom) {
		cfg.zoom = 16;
	}
	
	if (!cfg.location) {
		cfg.location = new google.maps.LatLng(
		38.548165, 
		-96.064453);
	}
	
	if (!cfg.showShare) {
		cfg.showShare = false;
	}
	
	if (!cfg.siteKey || !cfg.siteToken) {
		error = "Please include your site key and token in the configuration to add a places.";
	}
	
	if(errors.length>0)
		return errors;
	
	this.cfg = cfg;
}

WELOCALLY_UserPlacesManager.prototype.makeWrapper = function() {
	// Build Widget
	var _instance = this;
	
	var wrapper = jQuery('<div></div>');	
	jQuery(wrapper).attr('class','wl_userplacesmgr');
	jQuery(wrapper).attr('id','wl_userplacesmgr');
	
	//add button
	this.commandArea = jQuery('<div id="wl_userplacesmgr_actions" class="wl_userplacesmgr_commands"></div>');
	jQuery(wrapper).append(this.commandArea);
	

	var placeSelected = new WELOCALLY_PlaceEditView();
	placeSelected.initCfg({
		endpoint: _instance.cfg.endpoint,
		imagePath:_instance.cfg.imagePath,							
		key: _instance.cfg.key,
		token: _instance.cfg.token,
		selector: placeSelected,
		parent: _instance
	});
	
	jQuery(wrapper).append(placeSelected.makeWrapper());
	
	var placesMultiWidget =
		  new WELOCALLY_PlacesMultiWidget();
	
	//var placesGrid = new WELOCALLY_PlacesGrid();
		
	placesMultiWidget.initCfg({
		showSelection: true,
		endpoint: _instance.cfg.endpoint,
		imagePath:_instance.cfg.imagePath,							
		key: _instance.cfg.key,
		token: _instance.cfg.token,
		selector: placeSelected,
		observers: [placeSelected]
	});
	
	jQuery(wrapper).append(placesMultiWidget.makeWrapper());
	var statusarea = placesMultiWidget.getStatusArea();

	//make the search
	this.placesSearch = new WELOCALLY_UserPlacesSearch({
		endpoint: _instance.cfg.endpoint,
		imagePath:_instance.cfg.imagePath,							
		key: _instance.cfg.key,
		token: _instance.cfg.token,
		observers: [placesMultiWidget]
	}).init();

	this.placesSearch.search();
	
	
	this.wrapper = wrapper;

	return wrapper;
	
};

WELOCALLY_UserPlacesManager.prototype.getCommandArea = function() {
	return this.commandArea;
};



WELOCALLY_UserPlacesManager.prototype.updateSearchHandler = function(event,ui){
	_instance = event.data.instance;
	_instance.placesSearch.search();
};




