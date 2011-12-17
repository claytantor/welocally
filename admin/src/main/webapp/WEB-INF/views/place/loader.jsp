<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Place Finder"/>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>welocally</title>
		
		<link type="text/css" href="<c:url value='/css/custom-theme/jquery-ui-1.8.13.custom.css' />" rel="stylesheet" />	
		<link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css' />" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value='/css/blueprint/print.css' />" type="text/css" media="print">
		<link rel="stylesheet" href="<c:url value='/css/welocally.css' />" type="text/css"> 

		<script type="text/javascript" src="<c:url value='/js/jquery-1.5.1.min.js' />"></script>
		<script type="text/javascript" src="<c:url value='/js/jquery-ui-1.8.13.custom.min.js' />"></script>

</head>
<style type="text/css">
	
	#place-intro { margin-top: 5px; margin-bottom: 5px; }
	
	#placeForm { 
		border-color:#dfdfdf;
		background-color:#F9F9F9;
		border-width:1px;
		border-style:solid;
		-moz-border-radius:3px;
		-khtml-border-radius:3px;
		-webkit-border-radius:3px;
		border-radius:3px;
		margin: 0;
		width:95%;
		border-style:solid;
		border-spacing:0;
		padding: 10px;
	 }
	 
	.welocally-error { 
		border-color:#996666;
		background-color:#F9AAAA;
		border-width:1px;
		border-style:solid;
		-moz-border-radius:3px;
		-khtml-border-radius:3px;
		-webkit-border-radius:3px;
		border-radius:3px;
		margin: 0;
		width:95%;
		border-style:solid;
		border-spacing:0;
		padding: 10px;
		margin-bottom: 5px;
		color:#996666;
		display:none;
	 } 
	
    #add-span { }
    #edit-place-action { }
    #selection { margin-bottom: 10px; }
    #add-form-div { margin-top: 5px; display:none; }
    
       
    /* ------ titles */   
    .meta-title2 {
    	border-bottom:1px solid #cccccc; padding-bottom:3px;
    	margin-bottom: 10px;
		font-weight:bold;
		font-size:1.2em;
		text-transform:uppercase;
	}
	
	/* ------- is place */
	#all_place_info { width: 100%; }	
	
	/* ------ selection form */
	#place-selector { margin-bottom: 10px;}
	#scroller-places { height: 240px; width: 100%; overflow-y: scroll;}
	.search-field { width: 100%; margin-bottom: 10px; }
    
    #results { margin-top: 5px; display:none; }
    
    .result_place { border-bottom:1px solid #cccccc; padding-bottom:3px; }
    
    
	/*---- selectable ----*/
	.selectable { list-style-type: none; margin: 0; padding: 0; width: 100%; }
	.selectable li { 
			padding: 5px;
			height: 100px; 
			width: 100px; 
			display:inline-block; 
			vertical-align:top; 
            overflow:hidden; 
            margin:3px;
            border: 1px solid #777777; 
    }	
    .selectable .ui-selecting { background: #C4C4C4; }
	.selectable .ui-selected { background: #e4e4e4; }
    
    #results ul, #results ul, { margin: 0px;  }
    
    #content-body ul, #content-body ol { margin: 0px;  }




	
	#categories-choice {  margin-bottom: 10px; display:none; }	
		
	/* ------ selected place */
	.selected-place { margin-bottom: 10px; width: 100%; background: #B8E866; }
	.unselected-place { margin-bottom: 10px; width: 100%; background: #B8B8B8; }
	
	
	#btn-new-select { margin-bottom: 10px; margin-top: 10px; }
	#selected-place-categories { margin-bottom: 10px; }
    #selected-place-info { margin-bottom: 10px; }
	#show_place_info { margin-bottom: 10px; }
	
	/* ------ add new place */
	#edit-place-form { margin-bottom: 10px; width: 100%;  }
	
	.selected{background: #AAAAAA;}
	 	
</style>
<script>
var rows=new Array();
var results=new Array();

function buildErrorMessages(errorDiv, errors) {
	errorDiv.html('');
	errorDiv.append('<ul>');
	jQuery.each(errors, function(i,error){
		errorDiv.append('<li>error:'+error.errorMessage+'</li>');
		
	});
	errorDiv.append('</ul>');
	errorDiv.addClass('welocally-error error fade');	
	errorDiv.show();
}

function buildMissingFieldsErrorMessages(errorDiv,fields) {
	errorDiv.html('');
	errorDiv.append('<ul>');
	errorDiv.append('<li>error '+fields+' missing</li>');
	errorDiv.append('</ul>');
	errorDiv.addClass('welocally-error error fade');	
	errorDiv.show();	
}

function openAddPlaceDialogForSearch(rowId) {
	console.log('row clicked:'+rowId);
	var result = results[rowId];
	$("#edit-place-row-id").val(rowId);
	
	$("#edit-place-name").val(result.query);
	$("#edit-place-street").val(result.address);
	$("#edit-place-city").val(result.city);
	$("#edit-place-state").val(result.state);
	$("#edit-place-zip").val(result.postalCode);
	$("#edit-place-phone").val(result.phone);
	$("#edit-place-web").val(result.website);
	$("#edit-place-web-type").val('website');
	$("#edit-place-cats").val(result.category);
	
	$("#dialog").dialog({ modal: true });
	
	return false;
}

function addToNotes(rowId) {
	console.log('row clicked:'+rowId);
	var result = results[rowId];
	var row = rows[rowId];
	var notes = $("#notes").val();
	
	$("#notes").val(notes+rowId+','+result.query+','+result.address+','+result.city+' '+result.state+' '+result.postalCode+','+result.phone+'\n');

	row.addClass('unselected-place');
	$('#row'+rowId+' input').remove();
	$('#row'+rowId+' span').remove();
	$('#row'+rowId+' a').remove();
		
	return false;
}


function setRowSelected(rowId) {
	console.log('row clicked:'+rowId);
	var row = rows[rowId];
	row.addClass('selected-place');
	$('#row'+rowId+' input').remove();
	$('#row'+rowId+' span').remove();
	$('#row'+rowId+' a').remove();
	
	return false;
}

function buildResultRow(i,searchResponse) {

	var resultRow= $(document.createElement('div'));
	resultRow.attr("id",'row'+i);

	resultRow.addClass('result_place');

	resultRow.append(searchResponse.query+', '+searchResponse.address+
			' '+searchResponse.city+' '+searchResponse.state+' '+searchResponse.postalCode+'<br/>');

	if(searchResponse.results != null && searchResponse.results.length > 0){
		jQuery.each(searchResponse.results, function(j,place){
			resultRow.append('<input type="radio" name="group'+i+'" value="'+place.externalId+'" onclick="javascript:setRowSelected('+i+');"><span>'+place.name+', '+place.address+'</span><br>');
		});
	} else {
		resultRow.append('<a href="#" onclick="javascript:openAddPlaceDialogForSearch('+i+');">add this place</a>'+
				'&nbsp;<a href="#" onclick="javascript:addToNotes('+i+');">add to notes</a>');		
	}
	

	return resultRow;	
	
}


jQuery(document).ready(function(jQuery) {

	jQuery("#welocally_default_search_radius").val('8');	


	//saves a new place
    jQuery( "#save-place-action" ).click(function() {   
    
    	jQuery('#welocally-add-error').removeClass('welocally-error welocally-update error updated fade');
	    jQuery('#welocally-add-error').html('<em>Saving New Place...</em>');
    
    	var missingRequired = false;
    	var fields = '';
 		if (!jQuery("#edit-place-name").val().match(/\S/)) {
            missingRequired = true;
            fields = fields+'Place Name - ';
            //return false;
        }
        if (!jQuery("#edit-place-street").val().match(/\S/)) {
            //alert("Please enter the new place's street address");
            //return false;
            missingRequired = true;
            fields = fields+'Street Address - ';
        }
        if (!jQuery("#edit-place-city").val().match(/\S/)) {
            //alert("Please enter the new place's city");
            //return false;
             missingRequired = true;
            fields = fields+'City - ';
        }
        if (!jQuery("#edit-place-state").val().match(/\S/)) {
            //alert("Please enter the new place's state");
            //return false;
             missingRequired = true;
             fields = fields+'State or Provence - ';
        }
        if (!jQuery("#edit-place-zip").val().match(/\S/)) {
            //alert("Please enter the new place's zip code");
            //return false;
            missingRequired = true;
             fields = fields+'Postal Code - ';
        }
        
        if(missingRequired){
			buildMissingFieldsErrorMessages(jQuery('#welocally-add-error'),fields);
			return false;
		}

                
        var options = { 
            externalId: jQuery('#edit-place-external-id').val(),
        	name: jQuery('#edit-place-name').val(),
        	address: jQuery('#edit-place-street').val(),
        	city: jQuery('#edit-place-city').val(),
        	state: jQuery('#edit-place-state').val(),
        	zip: jQuery('#edit-place-zip').val(),
 	      	phone: jQuery('#edit-place-phone').val(),
    	   	web: jQuery('#edit-place-web').val(),
    	   	webType: jQuery('#edit-place-web-type').val(),
    	   	category: jQuery('#edit-place-cats').val()        		
        };

        
		jQuery.ajax({
		  type: 'POST',
		  url: '<c:url value="/publisher/place/saveplace2.json"/>',
		  dataType: 'json',
		  contentType: 'application/json',
		  data: JSON.stringify(options),
		  error : function(jqXHR, textStatus, errorThrown) {
					console.error(textStatus);
					jQuery('#welocally-add-error').html('ERROR : '+textStatus);
					jQuery('#welocally-add-error').addClass('welocally-error error fade');
		  },		  
		  success : function(data, textStatus, jqXHR) {
		  	jQuery('#welocally-post-error').html('');
			if(data.errors != null) {
				buildErrorMessages(jQuery('#welocally-add-error'), data.errors);		
			} else {
				var rowId = $("#edit-place-row-id").val();
				setRowSelected(rowId);
				$("#dialog").dialog( "close" )
				
			}
		  }
		});
        
        
        return false;
    });
    
    //saves a new place
    jQuery( "#search-places-action" ).click(function() {   
    
    	jQuery('#welocally-post-error').removeClass('welocally-error welocally-update error updated fade');
	    jQuery('#welocally-post-error').html('<img src="<c:url value="/images/ajax-loading.gif"/>"/><em>Loading Places...</em>');
    
    	var missingRequired = false;
    	var fields = '';

    	
    	
 		if (!jQuery("#spreadsheet-key").val().match(/\S/)) {
            missingRequired = true;
            fields = fields+'Spreadsheet Key - ';
            //return false;
        } 
        
        
        if(missingRequired){
			buildMissingFieldsErrorMessages(jQuery('#welocally-post-error'),fields);
			return false;
		}
                
        var options = {
        	spreadsheetKey: jQuery('#spreadsheet-key').val(),
       		radius: jQuery('#welocally_default_search_radius').val()
       	};
       				
       	$.ajax({
       	  type: 'POST',
       	  url: '<c:url value="/publisher/place/loadspreadsheet.json"/>',
       	  dataType: 'json',
       	  contentType: 'application/json',
       	  data: JSON.stringify(options),
       	  error : function(jqXHR, textStatus, errorThrown) {
       			console.error(textStatus);

       			jQuery('#welocally-post-error').html('ERROR : '+textStatus);
       			jQuery('#welocally-post-error').addClass('welocally-error error fade');
       			jQuery('#welocally-post-error').show();
       	  },
       	  success : function(data, textStatus, jqXHR) {
       		    jQuery('#welocally-post-error').removeClass('welocally-error');
       		    jQuery('#welocally-post-error').hide();
       	  		jQuery('#welocally-post-error').html('');
				
       	  		
       	  		if(data != null && data.length == 0) {
       	  			jQuery('#welocally-post-error').append('<div class="welocally-context-help"></div>');
       	  			jQuery('#welocally-post-error').append('Sorry no places were found that match your query, try again or add this as a new place.');
       	  			jQuery('#welocally-post-error').addClass('welocally-update updated fade');
       	  			
       	  		} else if(data != null && data.length > 0) {
       				jQuery.each(data, function(i,result){
       					console.log(JSON.stringify(result)); 
       					results[i] = result; 
       					   
       					var resultRow = buildResultRow(i,result);
       					     	       			
       					rows[i] = resultRow; 
       	       			
       					jQuery('#results').append(resultRow); 					      					   					
       				});
       				jQuery('#results').show();
       			} else if(data != null && data.errors != null) {
       			
       				buildErrorMessages(jQuery('#welocally-post-error'), data.errors);	
       					
       			}
       	  }
       	});
        
        
        return false;
    });


});




</script>

<body>
	<div class="container">
		<div class="span-24">	
			<div id="welocally-post-error" class="welocally-error">No Errors...</div>				
				<div id="placeForm">
				<input type="hidden" id="place-selected" name="PlaceSelected">
				<input type="hidden" id="place-categories-selected" name="PlaceCategoriesSelected">
				<div id="all_place_info">
					<!-- start place loader -->
					<div id="place-loader">
						<div class="meta-title2">Choose Place Data To Search From</div>
						<div>	
							<div>*<em>Please enter the key of of the spreadsheet you would like to load</div>
							<div style="margin-bottom:5px">
								<input type="text" id="spreadsheet-key" 
									class="search-field" 
									value="0Au9a580BQZPYdFlGU0QwYWlKOURJb2RZdURBUkk1emc">
								<select id="welocally_default_search_radius" name="welocally_default_search_radius" >
									<option value="2">2 km</option>
									<option value="4">4 km</option>
									<option value="8">8 km</option>
									<option value="12">12 km</option>
									<option value="16">16 km</option>
									<option value="25">25 km</option>
									<option value="50">50 km</option>
								</select>&nbsp;<em>Search Radius in Km</em> 	
									
							</div>	
							<div>
								<button id="search-places-action">find places</button>
							</div>
						</div>	
					</div> 
					<!-- end place selector -->

		</div>

	    </div>
		<div class="span-24">	
			<textarea name="notes" id="notes" class="span-24"></textarea>
		</div>	
	    
		<div id="results" class="span-24">	
		</div>	
	</div>
	<div id="dialog" title="Add Place" style="display:none">
		<!-- errors -->
		<div id="welocally-add-error" class="welocally-error">No Errors...</div>
	
	<!-- add place form -->	
				    <div id="edit-place-form">	
				    	<input type="hidden" id="edit-place-row-id" name="edit-place-row-id"> 	   
				    	<input type="hidden" id="edit-place-external-id" name="PlaceSelected"> 	
				    	<div id="place-form-title" class="meta-title2">Add New Place</div>
				        Place Name: <em>Required</em></br>
				        <input type="text" id="edit-place-name" name="edit-place-name" class="search-field"></br>
				        Street Address: <em>Required</em></br>
				        <input type="text" id="edit-place-street" name="edit-place-street" class="search-field"></br>
				        City: <em>Required</em></br>
				        <input type="text" id="edit-place-city" name="edit-place-city" class="search-field"></br>
				        State or Provence: <em>Required</em></br>
				        <input type="text" id="edit-place-state" name="edit-place-state" class="search-field"></br>
				        Zip or Postal Code: <em>Required</em></br>
				        <input type="text" id="edit-place-zip" name="edit-place-zip" class="search-field"></br>
				        Phone Number: (optional):</br>
				        <input type="text" id="edit-place-phone" name="edit-place-phone" class="search-field"></br>
				        Website: (optional):</br>
				        <input type="text" id="edit-place-web" name="edit-place-web" class="search-field">
				        <select id="edit-place-webtype" name="edit-place-webtype">
				        	<option>website</option>
				        	<option>facebook</option>
				        	<option>menu</option>	        	
				        </select>
				        
				        </br>
				        Categories (comma seperated):</br>
				        <input type="text" id="edit-place-cats" name="edit-place-cats" class="search-field"></br>	        
				        <button id="save-place-action" href="#">Add Place</button>
			   
				    </div>
	
	
	</div>
</body>
</html>


