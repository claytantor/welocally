//extends data tables
(function( $ ){

  $.fn.getTable = function( cfg ) {  

    return this.each(function() {

      var $this = $(this);
      $this.cfg = cfg;
      
      var btnDelete = cfg.buttonDelete; 
      
      jQuery.ajax({
    	  url: $this.cfg.endpoint,
    	  cache: false
      }).done(function( html ) {
    	  	
    	  	jQuery($this).html(html);
    	  	
    	  	/* Add a click handler to the rows - this could be used as a callback */
    	  	$($this).find("tbody tr").click( function( e ) {
    	        if ( $(this).hasClass('row_selected') ) {
    	            $(this).removeClass('row_selected');
    	        } else {
    	        	$($this).find($this.cfg.table).find('tr.row_selected').removeClass('row_selected');
    	            $(this).addClass('row_selected');
    	        }
    	    });
    	     

    	  	 /* Add a click handler for the delete row */
    	  	$this.cfg.buttonDelete.bind('click', $this.cfg.data, $this.cfg.deleteHandler);
    	  	$this.cfg.buttonView.bind('click', $this.cfg.data, $this.cfg.viewHandler);
    	  	$this.cfg.buttonEdit.bind('click', $this.cfg.data, $this.cfg.editHandler);
    	  	
    	  	
    	  	//make a data table
    	  	$this.cfg.data.table = $($this).find($this.cfg.table).dataTable({
    	  		"bJQueryUI": true,
    	  		"sPaginationType": "full_numbers",
    	  		"aoColumnDefs": [ 
                     { "bSearchable": false, "bVisible": false, "aTargets": [ 0 ] }
                 ] 
  			});
    	  	
    	  	//bind events
    	  	$($this).find($this.cfg.table)
    	  		.bind(
    	  			'page', 
    	  			function(){
    	  				$($this).find($this.cfg.table).find('tr.row_selected').removeClass('row_selected');
    	  			});
    	  	

    	     	  	
      });
    });
  };
  
 
  
  
  

})( jQuery );