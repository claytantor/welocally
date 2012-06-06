/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
	
	searchadd lets you search our geodb and add places to your personal store	
	
*/
function WELOCALLY_SearchAdd (cfg) {		
	
	this.cfg;
	this.wrapper;
	this.statusArea;
	
	this.init = function() {
							
		var errors = this.initCfg(cfg);
		
		// Get current script object
		var placeBefore = jQuery('SCRIPT');
		placeBefore = placeBefore[placeBefore.length - 1];
		
		if(cfg.placeBefore){
			placeBefore = cfg.placeBefore;
		} 
		
		
		if(errors){
			jQuery(placeBefore)
			 .parent()
			 .before('<div class="error">Problem with the configuration: '+errors[0]+'</div>');
		} else {
			// Build Widget
			this.wrapper = this.makeWrapper();	
			jQuery(placeBefore).parent().before(this.wrapper);		
		}
		
		return this;
					
	};
	
};

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
	
	
	var placeSelected = new WELOCALLY_PlaceWidget();
    var cfg = { 
			id:'finder_1',
			endpoint: _instance.cfg.endpoint,
			searchPlacesRequestPath: '/geodb/place/3_0/search.json',
			showLetters: true,
			showShare: true,		
	    	showSelection: true,
	    	parent: _instance,
			hidePlaceSectionMap: true,
			imagePath: 'images/marker_all_base.png',
			observers:[placeSelected, _instance]	    					
    };
    
    _instance.placesFinder = 
		  new WELOCALLY_PlaceFinderWidget();
    
    _instance.placesFinder.initCfg(cfg);
    
    //setup the selected area   
    placeSelected.initCfg(cfg);
      
    jQuery(wrapper).append(_instance.placesFinder.makeWrapper());
    _instance.placesFinder.getSelectedArea().append(placeSelected.makeWrapper());
    
	
	_instance.wrapper = wrapper;

	return wrapper;
};

WELOCALLY_SearchAdd.prototype.search = function() {
	var _instance = this;
	_instance.searcher.search();	
};

WELOCALLY_SearchAdd.prototype.show = function(selectedPlace) {	
	var _instance = this;
	jQuery(_instance.saveButton).show();
	jQuery(_instance.saveButton).bind('click' , {place: selectedPlace, instance: this}, this.addToMyPlacesHandler);  
};

WELOCALLY_SearchAdd.prototype.addToMyPlacesHandler = function(event,ui) {
	var _instance = event.data.instance;
	var place = event.data.place;

//	//the adder
	var adder = new WELOCALLY_AddPlaceWidget();
	adder.initCfg({
		endpoint: _instance.cfg.endpoint,						
		key: _instance.cfg.key,
		token: _instance.cfg.token
	});
	adder.savePlace(place);
	
};

WELOCALLY_PlacesMultiWidget.prototype.triggerResize = function(){
	var _instance = this;
	
	_instance.placesFinder.triggerResize();
	
};




