/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/
function WELOCALLY_PlaceEditView (cfg) {		
	
	this.cfg;
	this.wrapper;
	this.statusArea;
	this.jqxhr;
	this.parent;
		
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


WELOCALLY_PlaceEditView.prototype.initCfg = function(cfg) {
	var errors = [];
	if (!cfg) {
		errors.push('Please provide a configuration');
	}
	
	if (!cfg.parent) {
		errors.push('A parent reference is required to init WELOCALLY_PlaceEditView');
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

WELOCALLY_PlaceEditView.prototype.makeWrapper = function() {
	// Build Widget
	var _instance = this;
	
	var wrapper = jQuery('<div></div>');
	
	this.statusArea = jQuery('<div class="wl_place_edit_view_status"></div>');
	jQuery(wrapper).append(this.statusArea);
	
	//commands added to the parent command bar
	_instance.editButton = 
		jQuery('<a id="wl_place_edit_view_edit_btn" href="#" style="display:none">edit</a>')
		.button();
	jQuery(_instance.editButton ).bind( "click", {instance: this}, this.editHandler);
	jQuery(_instance.cfg.parent.getCommandArea()).append(_instance.editButton);
	
	
	//delete
	_instance.deleteButton = 
		jQuery('<a id="wl_place_edit_view_delete_btn" href="#" style="display:none">delete</a>')
		.button();
	jQuery(_instance.deleteButton ).bind( "click", {instance: this}, this.deleteHandler);
	jQuery(_instance.cfg.parent.getCommandArea()).append(_instance.deleteButton);
	
	
	//bind the update to the parent
	jQuery(_instance.cfg.parent ).bind( "update", {instance: _instance.cfg.parent}, _instance.cfg.parent.updateSearchHandler);
	
	
	_instance.cfg.hidePlaceSectionMap=true;
	//view
	this.placeWidget = new WELOCALLY_PlaceWidget();
	this.placeWidget.initCfg(_instance.cfg);	
	jQuery(wrapper).append(this.placeWidget.makeWrapper());
	
	//add as an edit widget
	this.addPlaceWidget = new WELOCALLY_AddPlaceWidget();
	this.addPlaceWidget.initCfg(_instance.cfg);		
	this.addPlaceWrapper = this.addPlaceWidget.makeWrapper();
	jQuery(this.addPlaceWrapper).hide();
	jQuery(wrapper).append(this.addPlaceWrapper);
	
	_instance.observers = [this.placeWidget];

	return wrapper;
	
}

WELOCALLY_PlaceEditView.prototype.editHandler = function(event,ui){
	_instance = event.data.instance;
	_instance.addPlaceWidget.show(_instance.selectedPlace);
	jQuery( _instance.addPlaceWrapper).dialog({
		title: 'edit place',
		minWidth: 600,
		modal: true,
		close: _instance.dialogClosedHandler
	});	
	return false;
}

WELOCALLY_PlaceEditView.prototype.dialogClosedHandler = function(event,ui){
	jQuery(_instance.cfg.parent ).trigger( "update", {instance: _instance.cfg.parent}, _instance.cfg.parent.updateSearchHandler);	
}

WELOCALLY_PlaceEditView.prototype.deleteDialogHandler = function(event,ui){
	
}

//curl -XGET --header "Content-Type: application/json" http://localhost:8082/geodb/place/2_0/delete/WL_mppcjc0h31qvvctmb63bkb_40.713636_-74.008634@1335034622.json
WELOCALLY_PlaceEditView.prototype.deleteHandler = function(event,ui){
	_instance = event.data.instance;
	
	WELOCALLY.ui.setStatus(_instance.statusArea,'Deleting Place...', 'wl_message', true);
	
	var ajaxurl = _instance.cfg.endpoint +
				'/geodb/place/2_0/user/delete/'+_instance.selectedPlace._id+'.json';
		   
	_instance.jqxhr = jQuery.ajax({
	  type: 'GET',		  
	  url: ajaxurl,
	  dataType : 'jsonp',
	  beforeSend: function(jqXHR){
		_instance.jqxhr = jqXHR;
		_instance.jqxhr.setRequestHeader("key", _instance.cfg.key);
		_instance.jqxhr.setRequestHeader("token", _instance.cfg.token);
	  },
	  error : function(jqXHR, textStatus, errorThrown) {
		if(textStatus != 'abort'){
			WELOCALLY.ui.setStatus(_instance.statusArea,'ERROR : '+textStatus, 'error', false);
		}		
	  },		  
	  success : function(data, textStatus, jqXHR) {
		if(data != null && data.errors != null) {
			WELOCALLY.ui.setStatus(_instance.statusArea,'Could not delete place:'+WELOCALLY.util.getErrorString(data.errors), 'wl_error', false);
		} else {
			WELOCALLY.ui.setStatus(_instance.statusArea,'Place has been deleted!', 'wl_message', false);
		}
	  }
	});
	
	return false;
}


WELOCALLY_PlaceEditView.prototype.show = function(selectedPlace) {	
	
	var _instance = this;	
	_instance.selectedPlace = selectedPlace;
	jQuery(_instance.editButton).show();
	jQuery(_instance.deleteButton).show();
	jQuery.each(_instance.observers, function(i,observer) {
		observer.show(selectedPlace);		
	});
	
	                        
};



