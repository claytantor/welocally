//extends data tables
(function( $ ){
	
  $.fn.getPlaces = function( cfg ) {  
    return this.each(function() {
    	var $this = $(this);
    	$this.cfg = cfg;
    	$this.cfg.data.instance = $this;
      	
      	
      	$this.append('<div id="wl_status"></div>');
      	$this.append('<div id="wl_table_buttons" class="wl_table_buttons" style="display:none;"><a href="#" id="wl_place_new" style="margin-right: 20px;">New</a><a id="wl_place_view" href="#">View</a><a id="wl_place_edit" href="#">Edit</a><a id="wl_place_delete" href="#">Delete</a><a id="wl_place_import" href="#">Import</a></div>');
      	$($this).find('#wl_table_buttons a').button();
      	$this.append('<div id="wl_publisher_places"></div>');
      	$this.append('<div id="wl-publisher-places-dialog"></div>');
      	    	
     	
	    //make the search
	  	$this.placesSearch = new WELOCALLY_UserPlacesSearch({						
	  		key: $this.cfg.key,
	  		token: $this.cfg.token,
	  		endpoint: $this.cfg.endpoint, 
	  		observers: [$($this)]
	  	}).init();
	  	
	  	$this.placesSearch.search();
	  	
	  	
    });
  };
  
  $.fn.setPlaces = function( places, cfg ) { 
	  
	    return this.each(function() {
	    	
	    	  var $this = $(this);	    	  
	    	  jQuery('#wl_table_buttons').show();
	    	  var tplaces = [];
			  jQuery.each(places, function(i,place){
				  var taddress = place.properties.address+' '+place.properties.city+' '+place.properties.province+' '+place.properties.postcode
				  var tplace = [
				         place._id,
				         place.properties.name,
				         taddress,
				         place.geometry.coordinates[1],
				         place.geometry.coordinates[0]];
				  
				  tplaces.push(tplace);			  
			  });
	
			  $($this).find('#wl_publisher_places').html( '<table cellpadding="0" cellspacing="0" border="0" class="display" id="wl_publisher_places_table"></table>' );
			  $this.placesTable = $($this).find('#wl_publisher_places_table').dataTable( {
				"bJQueryUI": true,
	    	  	"sPaginationType": "full_numbers",
		        "aaData": tplaces,
		        "aoColumns": [
		            { "sTitle": "WLID" },
		            { "sTitle": "Name" },
		            { "sTitle": "Address" },
		            { "sTitle": "Lat", "sClass": "center" },
		            { "sTitle": "Lng", "sClass": "center" }
		        ]
			  });	
			  
			  
    	  	$this.placesTable.$('td').hover( function() { 
    	  		WELOCALLY.util.log('hover');
    	  		$(this.parentNode).addClass( 'row_mouseover' );
    	    }, function() {
    	    	$(this.parentNode).removeClass('row_mouseover');
    	    });
    	  	
    	  	$this.placesTable.$('td').click(function() { 
    	  		//$(this.parentNode).addClass( 'row_mouseover' );
    	  		if ( $(this.parentNode).hasClass('row_selected') ) {
    	            $(this.parentNode).removeClass('row_selected');
    	        } else {
    	        	$($this.placesTable).find('tr.row_selected').removeClass('row_selected');
    	            $(this.parentNode).addClass('row_selected');   	               	            
    	            var anSelected = $($this.placesTable).find('tr.row_selected');
    	            if ( anSelected.length !== 0 ) {
    	            	//$this.ordersChangesItemClickHandler(anSelected, $this, $($this.placesTable));  	            	
    	            }
    	        }   	  		
    	    });
    	  	
    	  	var dialog = $this.find('#wl-publisher-places-dialog');

          	$($this).find('#wl_place_new').bind('click', {instance: $this, dialog:dialog, table: $this.placesTable, cfg: cfg}, $this.newPlaceHandler);      	
          	$($this).find('#wl_place_view').bind('click', {instance: $this,  dialog:dialog, table: $this.placesTable, cfg: cfg}, $this.viewPlaceHandler);
     
		  	
	    });

    
  };
  

  $.fn.getStatusArea = function(  ) {  
	  return this.each(function() {
		  	var $this = $(this);
	 	 	return $($this).find('#wl_status');
	  });

  };
  
  
  $.fn.newPlaceHandler = function(event) {  

		  	var $this = event.data.instance;
		  //add as an edit widget
			var addPlaceWidget = new WELOCALLY_AddPlaceWidget();
			addPlaceWidget.initCfg(event.data.cfg);	
			$(event.data.dialog).html(addPlaceWidget.makeWrapper());
			$(event.data.dialog).dialog({title: 'Add A New Place'});
			$(event.data.dialog).dialog('open');

  };
  
  $.fn.viewPlaceHandler = function(event) {  

	  	var $this = event.data.instance;
	  	
		$(event.data.dialog).dialog({title: 'Place Info'});
		
		var wlid = $(event.data.table.$('tr.row_selected').find('td')[0]).html();
		
		
		//instantiate it
		var placeWidget = 
		  new WELOCALLY_PlaceWidget();
		placeWidget.initCfg(event.data.cfg);	
		
		var surl = event.data.cfg.endpoint +
		'/geodb/place/3_0/user/'+wlid+'.json?key='+event.data.cfg.key+'&token='+event.data.cfg.token+'&callback=?';
		
		$this.jqxhr = jQuery.ajax({
			  url: surl,
			  dataType: "json",
			  success: function(data) {									
				$(event.data.dialog).html(placeWidget.makeWrapper());	
				placeWidget.load(data[0]);	
				
				$(event.data.dialog).append('<div id="wl_addplace"><textarea style="width:100%; height:100px;">Text to test.</textarea></div>');
				
				$(event.data.dialog).dialog('open');													
			}
		});
		
		
		
		
		
  };		

  


  
  
})( jQuery );