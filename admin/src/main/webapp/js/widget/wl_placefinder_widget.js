/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/

function WELOCALLY_PlaceFinderWidget (cfg) {
	this.cfg;
	this.geocoder;	
	this.searchLocation;
	this.statusArea;
	this.locationField;
	this.searchField;
	this.multiPlacesWidget;
	this.selectedPlace = {
			properties: {},
			type: "Place",
			classifiers: [
				{
					type: '',
					category: '',
					subcategory: ''
				}
			],
			geometry: {
					type: "Point",
					coordinates: []
			}
		};
	
	this.init = function() {
		
		var errors = this.initCfg(cfg);
		
		// Get current script object
		var script = jQuery('SCRIPT');
		script = script[script.length - 1];
				
		if(errors){
			 var statusArea = jQuery("<div></div>");
			 WELOCALLY.ui.setStatus(statusArea, errors[0],'wl_error',false);
			 jQuery(script).parent().before(statusArea);
		} else {
			// Build Widget
			this.wrapper = this.makeWrapper();	
			jQuery(script).parent().before(this.wrapper);
		
		}
		
		return this;
		
	};
		
}


WELOCALLY_PlaceFinderWidget.prototype.initCfg = function(cfg) {
	var errors = [];
	if (!cfg) {
		errors.push('Please provide a configuration');
	}
	
	
	if (!cfg.id) {
		cfg.id = "wl_placefinder_"+WELOCALLY.util.keyGenerator();
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
	
	if(!cfg.radius){
		cfg.radius=20;
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
	
	this.geocoder = new google.maps.Geocoder();
	
	this.searchLocation = 
		new google.maps.LatLng(); 	
	
	if(errors.length>0)
		return errors;
	
	this.cfg = cfg;
}

WELOCALLY_PlaceFinderWidget.prototype.makeWrapper = function() {
	// Build Widget
	var _instance = this;
	
	var wrapper = jQuery('<div class="wl_placefinder_wrapper"></div>');
	
	_instance.statusArea = jQuery('<div class="wl_place_edit_view_status"></div>');
	jQuery(wrapper).append(this.statusArea);
						
	//status
	_instance.statusArea = 
		jQuery('<div></div>');
    jQuery(this.statusArea).css('display','none');
    jQuery(wrapper).append(this.statusArea);
    
    //location field
    _instance.locationField =
		jQuery('<input type="text" name="location"/>');
    jQuery(wrapper).append('<div class="wl_field_description">Enter a location to search such as "New York NY". You can even provide a full address for more refined searches.</div>');
    jQuery(this.locationField).attr('class','wl_widget_field wl_placefinder_search_field');
    jQuery(this.locationField).bind('change' , {instance: this}, this.locationFieldInputHandler);   
    
    
    jQuery(wrapper).append(this.locationField);
	
	//search field
    this.searchField =
		jQuery('<input type="text" name="search" id="wl_finder_search_field"/>');
	jQuery(wrapper).append('<div class="wl_field_description">Enter what you are searching for, this can be a type of place like "Restaurant", what they sell like "Pizza", or the name of the place like "Seward Park".</div>');       
	jQuery(this.searchField).attr('class','wl_widget_field wl_placefinder_search_field');
	jQuery(this.searchField).bind('change' , {instance: this}, this.searchHandler);  
		
	jQuery(wrapper).append(this.searchField);
	
	//bind focus
	jQuery(this.locationField).keypress(function(e){
        if ( e.which == 13 ){
        	jQuery(this.locationField).trigger('change' , {instance: this}, this.locationFieldInputHandler);
        	jQuery('#wl_finder_search_field').focus();
        	return false;
        }
    });
	
	jQuery(this.searchField).keypress(function(e){
        if ( e.which == 13 ){
        	jQuery(this.searchField).trigger('change' , {instance: _instance}, this.searchHandler);
        	jQuery('#wl_finder_search_button').focus();
        	return false;
        }
    });
	
	
			
	var buttonDiv = jQuery('<div></div>').attr('class','wl_finder_search_button_area'); 	
	var fetchButton = jQuery('<button id="wl_finder_search_button">Search</div>');
	
	jQuery(fetchButton).attr('class','wl_finder_search');
	jQuery(fetchButton).bind('click' , {instance: this}, this.searchHandler);
	jQuery(buttonDiv).append(fetchButton); 
	jQuery(wrapper).append(buttonDiv);  
	

	
	//now the mutli place component
	_instance.multiPlacesWidget = 
		new WELOCALLY_PlacesMultiWidget();
	
	
	_instance.multiPlacesWidget.initCfg({
		id: 'wl_search_add_multi_1',
		hidePlaceSectionMap: true,
		imagePath: 'images/marker_all_base.png',
		observers:_instance.cfg.observers
	});
		
	//the component wrapper
	jQuery(wrapper).append(_instance.multiPlacesWidget.makeWrapper());
	
	//the selector
	_instance.cfg.placeSelector.initCfg(cfg);
	placesMulti.getSelectedArea().append(_instance.cfg.placeSelector.makeWrapper());
			
	if(_instance.cfg.defaultLocation){
		jQuery(this.locationField).val(_instance.cfg.defaultLocation);
		jQuery(this.locationField).trigger('change' , {instance: _instance}, this.locationFieldInputHandler); 
	}
	
	return wrapper;
	
}

WELOCALLY_PlaceFinderWidget.prototype.locationFieldInputHandler = function(event) {
	
	var _instance = event.data.instance;
	
	var addressValue = jQuery(this).val();
	
	if(addressValue){
		_instance.setStatus(_instance.statusArea, 'Geocoding','wl_update',true);
		
		jQuery(_instance._selectedSection).hide();
		
		_instance.geocoder.geocode( { 'address': addressValue}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK &&  _instance.validGeocodeForSearch(results[0])) {			
			
				jQuery(_instance.locationField).val(results[0].formatted_address);
				_instance._formattedAddress = results[0].formatted_address;
				
					
				//set the model
				_instance.selectedPlace.properties.address = 
					_instance.getShortNameForType("street_number", results[0].address_components)+' '+
					_instance.getShortNameForType("route", results[0].address_components);
				
				_instance.selectedPlace.properties.city = 
					_instance.getShortNameForType("locality", results[0].address_components);
				
				_instance.selectedPlace.properties.province = 
					_instance.getShortNameForType("administrative_area_level_1", results[0].address_components);
		
				_instance.selectedPlace.properties.postcode = 
					_instance.getShortNameForType("postal_code", results[0].address_components);
				
				_instance.selectedPlace.properties.country = 
					_instance.getShortNameForType("country", results[0].address_components);
				
				_instance.selectedPlace.geometry.coordinates = [];
				_instance.selectedPlace.geometry.coordinates.push(results[0].geometry.location.lng());
				_instance.selectedPlace.geometry.coordinates.push(results[0].geometry.location.lat());
				
				_instance.searchLocation = 
					new google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry.location.lng());
							
				//setup the map
				//if map ist initted the do so
				if(!_instance.multiPlacesWidget.map){
					_instance.multiPlacesWidget.initMap(_instance.multiPlacesWidget.map_canvas)
				}
				
								
				var sl = _instance.searchLocation;
				_instance.multiPlacesWidget.map.setCenter(sl);
				
				var pm = _instance.multiPlacesWidget.placeMarkers;
				
				//set the zoom
				if(results[0].address_components.length<=5){
					_instance.multiPlacesWidget.map.setZoom(14);						
					
				} else {
					_instance.multiPlacesWidget.map.setZoom(16);
				}		

				//reset overlays
				_instance.multiPlacesWidget.resetOverlays(
					sl,
					pm); 

				_instance.setStatus(_instance.statusArea, '','wl_message',true);
				
				if(_instance.searchField.val()){
					jQuery(_instance.searchField).trigger('change');
				}
								
				jQuery( _instance.multiPlacesWidget.map_canvas).show();
				
				_instance.multiPlacesWidget.refreshMap(_instance.searchLocation);
				
							
				
			} else {
				_instance.setStatus(_instance.statusArea, 'Could not geocode:'+status,'wl_warning',false);
			} 
		});
	} else {
		_instance.setStatus(_instance.statusArea, 'Please choose a location to start your search.','wl_warning',false);
	}
	
	
	
	
	return false;
	
};



WELOCALLY_PlaceFinderWidget.prototype.searchHandler = function(event) {
	
	var _instance = event.data.instance;

	jQuery(_instance._selectedSection).hide();
	
	if(_instance.selectedPlace.geometry.coordinates[1] && _instance.selectedPlace.geometry.coordinates[0]){
		
		if(!_instance.locationField) {
			_instance._formattedAddress = results[0].formatted_address;
		}
		
		var searchValue = WELOCALLY.util.replaceAll(jQuery(_instance.searchField).val(),' ','+');
		
		//_instance.setStatus(_instance.statusArea, 'Finding places','wl_update',true);
		jQuery(_instance.multiPlacesWidget.results).hide();
		
		_instance.multiPlacesWidget.resetOverlays(
					_instance.searchLocation,
					_instance.multiPlacesWidget.placeMarkers);
				
		_instance.searcher = 
			 new WELOCALLY_GeoDbSearch({
			   endpoint: _instance.cfg.endpoint,
			   requestPath: _instance.cfg.searchPlacesRequestPath,
			   q: searchValue,
			   loc: [_instance.selectedPlace.geometry.coordinates[1],_instance.selectedPlace.geometry.coordinates[0]],
			   radiusKm: _instance.multiPlacesWidget.getMapRadius(_instance.multiPlacesWidget.map),
			   observers: [_instance.multiPlacesWidget]
			 }).init();	
		
		_instance.searcher.search();
		
	
	} else {
		_instance.setStatus(_instance.statusArea, 'Please choose a location for search.','wl_warning',false);
	}
	
	return false;

};


WELOCALLY_PlaceFinderWidget.prototype.getSelectedArea = function() { 
	return this.multiPlacesWidget.getSelectedArea();
};

WELOCALLY_PlaceFinderWidget.prototype.setStatus = function(statusArea, message, type, showloading){
	var _instance  = this;
	
	jQuery(statusArea).html('');
	jQuery(statusArea).removeClass();
	jQuery(statusArea).addClass(type);
	
	//need a solution for this
	if(showloading){
//		jQuery(statusArea).append('<div><img class="wl_ajax_loading" src="'+
//				_instance.cfg.imagePath+'/ajax-loader.gif"/></div>');
	}
	
	jQuery(statusArea).append('<em>'+message+'</em>');
	
	if(message != ''){
		jQuery(statusArea).show();
	} else {
		jQuery(statusArea).hide();
	}	
	
};

WELOCALLY_PlaceFinderWidget.prototype.validGeocodeForSearch = function (geocode) {

	if(geocode.geometry.location.lat() && geocode.geometry.location.lng())
		return true;
	
};


WELOCALLY_PlaceFinderWidget.prototype.hasType = function(type_name, address_components){
	for (componentIndex in address_components) {
		var component = address_components[componentIndex];
		if(component.types[0] == type_name)
			return true;
	}
	return false;
};
	
WELOCALLY_PlaceFinderWidget.prototype.getShortNameForType = function(type_name, address_components){
	for (componentIndex in address_components) {
		var component = address_components[componentIndex];
		if(component.types[0] == type_name)
			return address_components[componentIndex].short_name;
	}
	return null;	
};

WELOCALLY_PlaceFinderWidget.prototype.triggerResize = function(){
	var _instance = this;
	
	_instance.multiPlacesWidget.triggerResize();
	
};
