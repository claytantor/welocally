/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/
function WELOCALLY_UserPlacesSearch (cfg) {		
	
	this.cfg;
	this.jqxhr;
	this.observers;
		
	this.init = function() {
		
		var errors = this.initCfg(cfg);
		 
		// Get current script object
		var script = jQuery('SCRIPT');
		script = script[script.length - 1];
				
		if(errors){
			jQuery(script).parent().before('<div class="wl_error">The was a problem with the configuration: '+errors[0]+'</div>');
		} else {
			// Build Widget
			this.wrapper = this.makeWrapper();	
			jQuery(script).parent().before(this.wrapper);
		
		}
		
		return this;
		
	};
	
}


WELOCALLY_UserPlacesSearch.prototype.initCfg = function(cfg) {
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

WELOCALLY_UserPlacesSearch.prototype.makeWrapper = function() {
	
	
};

//curl -XGET --header "Content-Type: application/json" http://gaudi.welocally.com/geodb/place/2_0/publisher?key=oaklandly 
WELOCALLY_UserPlacesSearch.prototype.search = function() {
	
	
	//requires event data has the search instance
	var _instance = this;
	
	if(!_instance.cfg.key){
		jQuery.each(_instance.cfg.observers, function(i,observer) {
			observer.setStatus(observer.getStatusArea(), 'Publisher key not set','wl_error',true);
		});	
		
		return false;
	}
	
	if(!_instance.cfg.token){
		jQuery.each(_instance.cfg.observers, function(i,observer) {
			observer.setStatus(observer.getStatusArea(), 'Publisher token not set','wl_error',true);
		});	
		
		return false;
	}
				
	var surl = _instance.cfg.endpoint +
		'/geodb/place/2_0/publisher?key=?'+_instance.cfg.key+"&callback=?";
	
	//notify all observers
	jQuery.each(_instance.cfg.observers, function(i,observer) {
		observer.setStatus(observer.getStatusArea(), 'Finding places','wl_update',true);
	});	
						
	_instance.jqxhr = jQuery.ajax({
			  url: surl,
			  dataType: "json",
			  beforeSend: function(jqXHR){
				_instance.jqxhr = jqXHR;
				_instance.jqxhr.setRequestHeader("publisher-key", _instance.cfg.key);
				_instance.jqxhr.setRequestHeader("publisher-token", _instance.cfg.token);
			  },
			  success: function(data) {
				//set to result bounds if enough results
				if(data != null && data.length>0){							
					//notify all observers
					jQuery.each(_instance.cfg.observers, function(i,observer) {
						observer.setStatus(observer.getStatusArea(), '','wl_message',false);
						observer.setPlaces(data);
					});
					
				} else {
					jQuery.each(_instance.cfg.observers, function(i,observer) {
						observer.setStatus(observer.getStatusArea(), 'No results were found matching your search.','wl_warning',false);						
					});	
					
				}														
			}
	});
	
	
	return false;

};
