/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/
function WELOCALLY_PlacesGrid (cfg) {		
	
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
			jQuery(script).parent().before('<div class="wl_error">Error during configiration: '+error[0]+'</div>');
		} else {
			// Build Widget
			this.wrapper = this.makeWrapper();	
			jQuery(script).parent().before(this.wrapper);
		
		}		
		return this;			
	};
	
}

WELOCALLY_PlacesGrid.prototype.initCfg = function(cfg) {
	var errors = [];
	if (!cfg) {
		errors.push('Please provide a configuration');
	}
	
	if (!cfg.parent) {
		errors.push('A parent reference is required to init WELOCALLY_PlacesGrid');
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

WELOCALLY_PlacesGrid.prototype.makeWrapper = function() {
	// Build Widget
	var _instance = this;
	
	var wrapper = jQuery('<div></div>');
	
	this.statusArea = jQuery('<div class="wl_placesgrid_status"></div>');
	jQuery(wrapper).append(this.statusArea);
	
	//add button
	this.gridArea = jQuery('<table id="wl_userplacesmgr_grid" class="wl_userplacesmgr_grid"></table>');
	jQuery(wrapper).append(this.gridArea);
		
	jQuery(this.gridArea).jqGrid({
		datatype: "local",
		height: 250,
	   	colNames:['Result','Name', 'Address'],
	   	onSelectRow: function(id){ 
	        //set selected place for all observers
			var rowdata = jQuery(this.gridArea).jqGrid('getRowData',id);
			
			if(_instance.cfg.observers != null && _instance.cfg.observers.length>0){				
				jQuery.each(_instance.cfg.observers, function(i,item){
					item.show(rowdata.place);
				});
			}	
	    },
	   	colModel:[
	   		{name:'id',index:'id', width:60, sorttype:"int"},
	   		{name:'name',index:'name', width:160},
	   		{name:'address',index:'address', width:300}	
	   		{name:'place',index:'place', width:300, hidden:true}	
	   	],
	   	multiselect: false,
	   	caption: "My Places"
	});
	

	return wrapper;
	
}

//use map status area
WELOCALLY_PlacesGrid.prototype.getStatusArea = function (){
	var _instance = this;
	return _instance.statusArea;
};


WELOCALLY_PlacesGrid.prototype.deleteHandler = function(event,ui){
	_instance = event.data.instance;
	
	/*WELOCALLY.ui.setStatus(_instance.statusArea,'Deleting Place...', 'wl_message', true);
	
	var ajaxurl = _instance.cfg.endpoint +
				'/geodb/place/3_0/user/delete/'+_instance.selectedPlace._id+'.json';
		   
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
	});*/
	
	return false;
};


//selection observer
WELOCALLY_PlacesGrid.prototype.show = function(selectedPlace) {		
	var _instance = this;	
	_instance.selectedPlace = selectedPlace;    
	
	
};

//search observer
WELOCALLY_PlacesGrid.prototype.setPlaces = function(places) {
	var _instance = this;
	jQuery(this.gridArea).jqGrid('clearGridData');
	for(var i=0;i<places.length;i++){
	  	jQuery(this.gridArea).jqGrid('addRowData',i+1,
	  			{id:i+1, 
	  			name: places[i]['properties']['name'], 
	  			address:places[i]['properties']['address'],
	  			place:places[i]});
	}

};



