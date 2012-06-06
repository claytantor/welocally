/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/
//foo
function WELOCALLY_UserPlacesUI (cfg) {		
	
	this.cfg;
	
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
	
};

WELOCALLY_UserPlacesUI.prototype.initCfg = function(cfg) {
	var _instance = this;
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
	
	_instance.userPlacesManager = new WELOCALLY_UserPlacesManager();
	_instance.userPlacesManager.initCfg(cfg);
	
	_instance.searchAdd = new WELOCALLY_SearchAdd();
	_instance.searchAdd.initCfg(cfg);
	
	_instance.addPlaceWidget = new WELOCALLY_AddPlaceWidget();
	_instance.addPlaceWidget.initCfg(_instance.cfg);		
		
	
	if(errors.length>0)
		return errors;
	
	this.cfg = cfg;
};


WELOCALLY_UserPlacesUI.prototype.getUserPlacesManager = function(){
	var _instance = this;	
	return _instance.userPlacesManager;	
};


WELOCALLY_UserPlacesUI.prototype.getSearchAdd = function(){
	var _instance = this;	
	return _instance.searchAdd;
};

WELOCALLY_UserPlacesUI.prototype.getAddPlace = function(){
	var _instance = this;	
	return _instance.addPlaceWidget;	
};


