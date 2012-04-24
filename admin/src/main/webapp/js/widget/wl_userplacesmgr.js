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
		var script = jQuery('SCRIPT');
		script = script[script.length - 1];
				
		if(errors){
			this.setStatus(this.statusArea, error,'wl_error',false);
		} else {
			// Build Widget
			this.wrapper = this.makeWrapper();	
			jQuery(script).parent().before(this.wrapper);
		
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
	this.commandArea = jQuery('<div id="wl_userplacesmgr_actions" class="wl_userplacesmgr_commands"><a id="wl_userplacesmgr_search_btn" href="#">copy public place</a><a id="wl_userplacesmgr_add_btn" href="#">create new</a></div>');
	jQuery(this.commandArea).find('a').button();
	jQuery(this.commandArea).find('#wl_userplacesmgr_add_btn').bind('click' , {instance: this}, this.addPlaceHandler);  
	jQuery(this.commandArea).find('#wl_userplacesmgr_search_btn').bind('click' , {instance: this}, this.searchPlacesHandler);  
	
	
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
		
	placesMultiWidget.initCfg({
		endpoint: _instance.cfg.endpoint,
		imagePath:_instance.cfg.imagePath,							
		key: _instance.cfg.key,
		token: _instance.cfg.token,
		selector: placeSelected,
		observers: [placeSelected]
	});
	
	jQuery(wrapper).append(placesMultiWidget.makeWrapper());
	var statusarea = placesMultiWidget.getStatusArea();
	
	
	//now the add place div (dialog)
	this.addPlaceDialog = jQuery('<div id="wl_userplacesmgr_add" style="display:none">add a place</div>');	
	jQuery(wrapper).append(this.addPlaceDialog);
	
	//now the search places div (dialog)
	this.searchPlacesDialog = jQuery('<div id="wl_userplacesmgr_search" style="display:none">search places</div>');	
	jQuery(wrapper).append(this.searchPlacesDialog);

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

WELOCALLY_UserPlacesManager.prototype.addPlaceHandler = function(event,ui){
	_instance = event.data.instance;
	//add as an edit widget
	this.addPlaceWidget = new WELOCALLY_AddPlaceWidget();
	this.addPlaceWidget.initCfg(_instance.cfg);		
	this.addPlaceWrapper = this.addPlaceWidget.makeWrapper();
	jQuery(_instance.addPlaceDialog).html(this.addPlaceWrapper);
	
	jQuery(_instance.addPlaceDialog).dialog({
		title: 'add place',
		autoResize: true,
        height: 'auto',
        width: 'auto',
		position: 'top',
		modal: true,
		close: function(event, ui) {
			jQuery(_instance).trigger( "update", {instance: _instance}, _instance.updateSearchHandler);	
		}
	});	
	return false;
	
};

WELOCALLY_UserPlacesManager.prototype.updateSearchHandler = function(event,ui){
	_instance = event.data.instance;
	_instance.placesSearch.search();
}

WELOCALLY_UserPlacesManager.prototype.searchPlacesHandler = function(event,ui){
	_instance = event.data.instance;
	
	var searchAdd = new WELOCALLY_SearchAdd();
	searchAdd.initCfg(_instance.cfg);
	jQuery(_instance.searchPlacesDialog).html(searchAdd.makeWrapper());
	
	
	jQuery(_instance.searchPlacesDialog).dialog({
		title: 'search places',
        height: 'auto',
        width: '830px',
		position: 'top',
		modal: true,
		close: function(event, ui) {
			jQuery(_instance).trigger( "update", {instance: _instance}, _instance.updateSearchHandler);	
		}
	});	
	return false;
	
};

