/*
	copyright 2012 welocally. NO WARRANTIES PROVIDED
*/

function WELOCALLY_IPlaceWidget (cfg) {	
		
	this.init = function() {
		
		var error;
		if (!cfg) {
			error = "Please provide configuration for the widget";
			cfg = {};
		}
		
		if (cfg.id == null) {
			cfg.id = WELOCALLY.util.keyGenerator();
		}
			
		this.cfg = cfg;
		
		// Get current script object
		var script = jQuery('SCRIPT');
		script = script[script.length - 1];

		// Build Widget
		this.wrapper = jQuery('<div></div>');		
		jQuery(this.wrapper).attr('class','welocally_iplace_widget');
		jQuery(this.wrapper).attr('id','welocally_iplace_widget_'+this.cfg.id);
		
		//iframe
		var iframe = jQuery('<iframe></iframe>');
		jQuery(iframe).attr('id','wl_iframe_'+this.cfg.id);
		jQuery(iframe).attr('src','http://gaudi.welocally.com/admin/place/1_0/foobar');
		jQuery(iframe).attr('width','200');
		jQuery(iframe).attr('height','20');
		jQuery(this.wrapper).append(iframe);	
		jQuery(script).parent().before(this.wrapper);		
		return this;
					
	};
	
}
