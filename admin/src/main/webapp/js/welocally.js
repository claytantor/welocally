if (!window.WELOCALLY) {
        window.WELOCALLY = {
                util: {
                        update: function() {
                                var obj = arguments[0], i = 1, len=arguments.length, attr;
                                for (; i<len; i++) {
                                        for (attr in arguments[i]) {
                                                obj[attr] = arguments[i][attr];
                                        }
                                }
                                return obj;
                        },
                        escape: function(s) {
                                return ((s == null) ? '' : s)
                                        .toString()
                                        .replace(/[<>"&\\]/g, function(s) {
                                                switch(s) {
                                                        case '<': return '&lt;';
                                                        case '>': return '&gt;';
                                                        case '"': return '\"';
                                                        case '&': return '&amp;';
                                                        case '\\': return '\\\\';
                                                        default: return s;
                                                }
                                        });
                        },
                        notundef: function(a, b) {
                                return typeof(a) == 'undefined' ? b : a;
                        },
                        guidGenerator: function() {
                    	    return (WELOCALLY.util.S4()+WELOCALLY.util.S4()+"-"+
                    	    		WELOCALLY.util.S4()+"-"+WELOCALLY.util.S4()+"-"+
                    	    		WELOCALLY.util.S4()+"-"+
                    	    		WELOCALLY.util.S4()+WELOCALLY.util.S4()+WELOCALLY.util.S4());
                        },
                        keyGenerator: function() {
                    	    return (WELOCALLY.util.S4()+WELOCALLY.util.S4());
                        },
                        tokenGenerator: function() {
                        	 return (WELOCALLY.util.S4()+WELOCALLY.util.S4()+
                     	    		WELOCALLY.util.S4()+WELOCALLY.util.S4()+
                     	    		WELOCALLY.util.S4()+
                     	    		WELOCALLY.util.S4()+WELOCALLY.util.S4()+WELOCALLY.util.S4());
                        },
                        S4: function() {
                 	       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
                	    }
                }
        };
}

function WELOCALLY_EditPlaceWidget (cfg) {	
	
	this.statusArea;
	this.selectedPlaceArea;
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
			
			var error;
			if (!cfg) {
				error = "Please provide configuration for the widget";
				cfg = {};
			}
			
			if (!cfg.endpoint) {
				cfg.endpoint = 'http://stage.welocally.com';
			}
			
			this.cfg = cfg;
			
			this.statusArea = cfg.statusArea;
			this.selectedPlaceArea = cfg.selectedPlaceArea;
			
			return this;
			

					
	}
	
}

WELOCALLY_EditPlaceWidget.prototype.findPlaceById = function (id) {
	
	var _instance = this;
	
	_instance.setStatus(_instance.statusArea,'Getting Place...', 'wl_message', true);
	
	var ajaxurl = _instance.cfg.endpoint +
				'/geodb/place/1_0/'+id+'.json';
	
	console.log(ajaxurl);
	   
	jqxhr = jQuery.ajax({
	  type: 'GET',		  
	  url: ajaxurl,
	  dataType : 'jsonp',
	  beforeSend: function(jqXHR){
		jqxhr = jqXHR;
		jqxhr.setRequestHeader("site-key", _instance.cfg.siteKey);
		jqxhr.setRequestHeader("site-token", _instance.cfg.siteToken);
	  },
	  error : function(jqXHR, textStatus, errorThrown) {
		if(textStatus != 'abort'){
			_instance.setStatus(_instance.statusArea,'ERROR : '+textStatus, 'error', false);
		}	else {
			console.log(textStatus);
		}		
	  },		  
	  success : function(data, textStatus, jqXHR) {
		if(data != null && data.errors != null) {
			_instance.setStatus(_instance.statusArea,'Could not save place', 'wl_error', false);
		} else {
			_instance.setStatus(_instance.statusArea,'Your new place has been found!', 'wl_message', false);
			_instance.setSelectedPlace(data[0]);
		}
	  }
	});
	
}

WELOCALLY_EditPlaceWidget.prototype.savePlace = function (selectedPlaceString) {
	
	var selectedPlace = JSON.parse(selectedPlaceString);

	var _instance = this;
	
	_instance.setStatus(_instance.statusArea,'Saving Place...', 'wl_message', true);
	
	var ajaxurl = _instance.cfg.endpoint +
				'/geodb/place/1_0/save.json';
	
	console.log(ajaxurl);
	   
	jqxhr = jQuery.ajax({
	  type: 'GET',		  
	  url: ajaxurl,
	  data: selectedPlace,
	  dataType : 'jsonp',
	  beforeSend: function(jqXHR){
		jqxhr = jqXHR;
		jqxhr.setRequestHeader("site-key", _instance.cfg.siteKey);
		jqxhr.setRequestHeader("site-token", _instance.cfg.siteToken);
	  },
	  error : function(jqXHR, textStatus, errorThrown) {
		if(textStatus != 'abort'){
			_instance.setStatus(_instance.statusArea,'ERROR : '+textStatus, 'error', false);
		}	else {
			console.log(textStatus);
		}		
	  },		  
	  success : function(data, textStatus, jqXHR) {
		if(data != null && data.errors != null) {
			_instance.setStatus(_instance.statusArea,'Could not save place', 'wl_error', false);
		} else {
			console.log(data.id);
			_instance.setStatus(_instance.statusArea,'Your place has been saved! '+data.id, 'wl_message', false);
		}
	  }
	});
};

WELOCALLY_EditPlaceWidget.prototype.setStatus = function(statusArea, message, type, showloading){
	jQuery(statusArea).html('');
	jQuery(statusArea).removeClass();
	jQuery(statusArea).addClass(type);
	
	jQuery(statusArea).append(message);
	
	if(message != ''){
		jQuery(statusArea).show();
	} else {
		jQuery(statusArea).hide();
	}			
};

WELOCALLY_EditPlaceWidget.prototype.setSelectedPlace = function(selectedPlace) {

	var _instance=this;
	console.log(selectedPlace._id);
	_instance.selectedPlace = selectedPlace;
	jQuery(_instance.selectedPlaceArea).val(JSON.stringify(selectedPlace));
	
	
};

