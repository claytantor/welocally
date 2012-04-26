/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/

function WELOCALLY_IPlaceWidget (cfg) {	
		
	this.init = function() {
		
		var error = this.initCfg(cfg);
		
		// Get current script object
		var script = jQuery('SCRIPT');
		script = script[script.length - 1];
		
		if(error){
			jQuery(script).parent().before('<div class="error">Error during configiration: '+error[0]+'</div>');
		} else {
			// Build Widget
			this.wrapper = this.makeWrapper();	
			jQuery(script).parent().before(this.wrapper);		
		}
		
		return this;					
	};
	
}

WELOCALLY_IPlaceWidget.prototype.initCfg = function(cfg) {
	var errors = [];
	if (!cfg) {
		errors.push("Please provide configuration for the widget");
		cfg = {};
	}
	
	// summary (optional) - the summary of the article
	if (!cfg.id) {
		errors.push("No place id provided.");
	}
	
	
	
	if (!cfg.endpoint) {
		cfg.endpoint = 'http://gaudi.welocally.com/admin/place/1_0/iplace';
	}	

	if (!cfg.siteKey || !cfg.siteToken) {
		error = "Please include your site key and token in the configuration to add a places.";
	}
	
	this.cfg = cfg;

};

WELOCALLY_IPlaceWidget.prototype.makeWrapper = function() {
	// Build Widget
	var _instance = this;
	
	var wrapper = jQuery('<div></div>');		
	jQuery(wrapper).attr('class','welocally_iplace_widget');
	jQuery(wrapper).attr('id','welocally_iplace_widget_'+_instance.cfg.id+'.html');
	
	//iframe
	var iframe = jQuery('<iframe></iframe>');
	jQuery(iframe).attr('id','wl_iframe_'+this.cfg.id);
	jQuery(iframe).attr('src',_instance.cfg.endpoint+'?id='+_instance.cfg.id);
	jQuery(iframe).attr('width','310');
	jQuery(iframe).attr('height','435');
	jQuery(wrapper).append(iframe);	
	
	this.wrapper = wrapper;
	
	return wrapper;
};
