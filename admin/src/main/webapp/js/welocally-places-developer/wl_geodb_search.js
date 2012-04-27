/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/
function WELOCALLY_GeoDbSearch (cfg) {		
	
	this.cfg;
	this.jqxhr;
	this.observers;
		
	this.init = function() {
		
		var error;
		if (!cfg) {
			error = "Please provide configuration for the widget";
			cfg = {};
		}
				
		if (!cfg.endpoint) {
			cfg.endpoint = 'https://api.welocally.com';
		}
		
		if (!cfg.requestPath) {
			cfg.requestPath = '/geodb/place/1_0/search.json';
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
					
		this.cfg = cfg;
				
		return this;
		
	};
	
}


WELOCALLY_GeoDbSearch.prototype.search = function() {
	
	//requires event data has the search instance
	var _instance = this;
		
	var query = {
		q: _instance.cfg.q,
		loc: _instance.cfg.loc[0]+'_'+_instance.cfg.loc[1],
		radiusKm: _instance.cfg.radiusKm
	};
		
	var surl = _instance.cfg.endpoint +
		_instance.cfg.requestPath+'?'+WELOCALLY.util.serialize(query)+"&callback=?";
	
	//notify all observers
	jQuery.each(_instance.cfg.observers, function(i,observer) {
		observer.setStatus(observer.getStatusArea(), 'Finding places','wl_update',true);
	});	
	
					
	jQuery.ajax({
			  url: surl,
			  dataType: "json",
			  success: function(data) {
				//set to result bounds if enough results
				if(data != null && data.errors != null) {
					//notify all observers
					jQuery.each(_instance.cfg.observers, function(i,observer) {
						observer.setStatus(observer.getStatusArea(),'ERROR:'+WELOCALLY.util.getErrorString(data.errors), 'wl_error', false);
					});
					
					
				} else if(data != null && data.length>0){							
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
