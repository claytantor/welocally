/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
	
	searchadd lets you search our geodb and add places to your personal store	
	
*/
function WELOCALLY_SearchAdd (cfg) {		
	
	this.cfg;
	this.wrapper;
	this.statusArea;
	
	this.init = function() {
							
		var errors = initCfg(cfg);
		
		// Get current script object
		var script = jQuery('SCRIPT');
		script = script[script.length - 1];
		
		if(errors){
			jQuery(script)
			 .parent()
			 .before('<div class="error">Problem with the configuration: '+errors[0]+'</div>');
		} else {
			// Build Widget
			this.wrapper = this.makeWrapper();	
			jQuery(script).parent().before(this.wrapper);		
		}
		
		return this;
					
	};
	
}

WELOCALLY_SearchAdd.prototype.initCfg = function(cfg) {
	
	var errors = [];
	if (!cfg) {
		errors.push("Please provide configuration for the widget");
		cfg = {};
	}
	
	// summary (optional) - the summary of the article
	// hostname (optional) - the name of the host to use
	if (!cfg.id) {
		cfg.id = WELOCALLY.util.keyGenerator;
	}
	
	if (!cfg.endpoint) {
		cfg.endpoint = 'http://stage.welocally.com';
	}
	
	if (!cfg.imagePath) {
		cfg.imagePath = 'http://placehound.com/images/marker_all_base.png';
	}
	
	if (!cfg.zoom) {
		cfg.zoom = 16;
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
	
	
};

WELOCALLY_SearchAdd.prototype.makeWrapper = function() {
	var _instance = this;
	
	// Build Widget
	var wrapper = jQuery('<div></div>');	
	jQuery(wrapper).attr('class','wl_searchadd_widget');
	jQuery(wrapper).attr('id','wl_searchadd_widget_'+_instance.cfg.id);
	
	_instance.statusArea = jQuery('<div></div>');
	jQuery(_instance.statusArea).css('display','none');	
	jQuery(wrapper).append(_instance.statusArea);
	
	_instance.saveButton = jQuery('<div id="wl_searchadd_buttons_area" class="wl_searchadd_buttons_area"><a id="wl_searchadd_button" href="#">add to my places</a></div>');
	jQuery(_instance.saveButton).css('display','none');	
	jQuery(_instance.saveButton).find('a').button();
	jQuery(wrapper).append(_instance.saveButton);
		
	//selected place
	//used for the place display when selected (observed)
	var placeSelected = new WELOCALLY_PlaceWidget();
	
	//setup the selected area   
	placeSelected.initCfg({
		imagePath: 'images/marker_all_base.png',
		hidePlaceSectionMap: true,
	});
	
	//multi places
	_instance.placesMulti = 
		 new WELOCALLY_PlacesMultiWidget();
	
	//setup the multi
	_instance.placesMulti.initCfg({
		id: 'wl_search_add_1',
		overrideSelectableStyle: 'margin: 2px; padding: 2px; float: left; width: 120px; height: 85px; font-size: 0.8em;',
		hidePlaceSectionMap: true,
		imagePath: 'images/marker_all_base.png',
		observers:[placeSelected, _instance]
	});
	
	jQuery(wrapper).append(_instance.placesMulti.makeWrapper());
	
	_instance.placesMulti.getSelectedArea()
	  .append(placeSelected.makeWrapper());
	
	_instance.searcher = 
		 new WELOCALLY_GeoDbSearch({
		   endpoint: 'http://gaudi.welocally.com',
		   q: "Sandwich",
		   loc: [40.714353,-74.005973],
		   radiusKm: 10.0,
		   observers: [_instance.placesMulti]
		 }).init();	
	
	_instance.searcher.search();	
	jQuery(wrapper).find('#wl_places_mutli_selectable').find('li').css('width','200px');
	
	
	_instance.wrapper = wrapper;

	return wrapper;
};

WELOCALLY_SearchAdd.prototype.search = function() {
	var _instance = this;
	_instance.searcher.search();	
};

//for place selectors
WELOCALLY_SearchAdd.prototype.show = function(selectedPlace) {	
	var _instance = this;
	jQuery(_instance.saveButton).show();
	jQuery(_instance.saveButton).bind('click' , {place: selectedPlace, instance: this}, this.addToMyPlacesHandler);  
};

WELOCALLY_SearchAdd.prototype.addToMyPlacesHandler = function(event,ui) {
	var _instance = event.data.instance;
	var place = event.data.place;
	
	//the adder
	var adder = new WELOCALLY_AddPlaceWidget();
	adder.initCfg({
		endpoint: _instance.cfg.endpoint,						
		key: _instance.cfg.key,
		token: _instance.cfg.token,
	});
	adder.savePlace(place);
	
};





