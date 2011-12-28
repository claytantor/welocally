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
<script type="text/javascript"
    src="http://maps.googleapis.com/maps/api/js?key=AIzaSyACXX0_pKBA6L0Z2ajyIvh5Bi8h9crGVlg&sensor=false">
</script>
<script>
var jsonObjFeatures = []; //declare features array
var markersArray = [];
var selectedFeatureIndex = 0;
var selectedCategories='';
var map;
var geocoder;
var selectedGeocode;
var selectedPlace = {
	properties: {},
	type: "Place",
	classifiers: [],
	geometry: {
			type: "Point",
			coordinates: []
	}
};

var currentCategories;

/*var selectedPlace = {
			properties: {
				phone: "+1 907 452 3532",
				website: "http://google.com",
				address: "1410 S Cushman St",
				name: "Thrifty Liquor Store",
				province: "AK",
				owner: "welocally",
				postcode: "99701",
				city: "Fairbanks",
				country: "US"
			},
			type: "Place",
			classifiers: [
            {
                category: "Food & Beverages",
                subcategory: "Liquor & Beverages",
                type: "Retail Goods"
            }
       		],
            geometry: {
				type: "Point",
				coordinates: [
					-147.718066,
					64.836155
				]
			}
		};*/

function addMarker(location) {
  marker = new google.maps.Marker({
    position: location,
    map: map
  });
  markersArray.push(marker);
}

// Removes the overlays from the map, but keeps them in the array
function clearOverlays() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }
  }
}

// Shows any overlays currently in the array
function showOverlays() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(map);
    }
  }
}

// Deletes all markers in the array by removing references to them
function deleteOverlays() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }
    markersArray.length = 0;
  }
}



function getLocationsByAddress(address, keyword, radiusKm) {	
	jQuery('#welocally-post-error').removeClass('welocally-error');
	jQuery('#welocally-post-error').show();
	jQuery('#welocally-post-error').html('<em>Loading Places...</em>');
	jQuery('#selectable').empty();
	jsonObjFeatures = [];
	
	
	var missingRequired = false;
	var fields = '';
	if (!jQuery("#place-address").val().match(/\S/)) {
		missingRequired = true;
		fields = fields+'Place Address - ';
	}
	if (!jQuery("#place-search").val().match(/\S/)) {
		missingRequired = true;
		fields = fields+'Search Term - ';
	}
		
	if(missingRequired){
		buildMissingFieldsErrorMessages(fields);
		return false;
	}
	    
    var options = {
		address : address,
		radius: radiusKm,
		query : keyword
	};
				
	$.ajax({
	  type: 'POST',
	  url: '<c:url value="/publisher/place/queryplaces2.json"/>',
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
	  		if(data.places != null && data.places.length == 0) {
	  			jQuery('#welocally-post-error').append('<div class="welocally-context-help"></div>');
	  			jQuery('#welocally-post-error').append('Sorry no places were found that match your query, try again or add this as a new place.');
	  			jQuery('#welocally-post-error').addClass('welocally-update updated fade');
	  			
	  		} else if(data.places != null && data.places.length > 0) {
				jQuery.each(data.places, function(i,item){
					console.log(JSON.stringify(item));
					jsonObjFeatures.push(item);	    		
					jQuery('#selectable').append(buildListItemForPlace(item,i));
					jQuery("#results").show();	
				});
			} else if(data.places == null && data.errors != null) {
			
				buildErrorMessages(data.errors);	
					
			}
	  }
	});
    
    
}

function buildListItemForPlace(place,i) {
        var itemLabel = '<b>'+place.name+'</b>';
        if (place.address) {
            itemLabel += "<br>" + place.address;
        }
		return '<li class=\"ui-widget-content\" id="f'+i+'" title="select place">'+itemLabel+'</li>';
}

function buildSelectedInfoForPlace(place) {
        var itemLabel = '<b>'+place.name+'</b>';
        if (place.address) {
            itemLabel += "<br>" + place.address;
        }
		return '<div class=\"selected-place-info\">'+itemLabel+'</div>';
}

function buildCategorySelectionsForPlace(place, container) {
		container.html('');
		var index = -1;
		for (category in place.categories) {
			index = category;
			container.append('<li class=\"ui-widget-content\">'+place.categories[category]+'</li>');
		}
		
		//show if there are items
		if(index != -1){
			jQuery("#categories-choice").show();
		}
		
}


function setSelectedPlaceInfo(selectedItem) {
	//hide the selection area
	jQuery("#place-selector").hide();	
	jQuery("#edit-place-form").hide();	
	
	
	//set the form value
	jQuery("#place-selected").val(JSON.stringify(selectedItem));
	
	//show the *selected* area
	var info = buildSelectedInfoForPlace(selectedItem); 
	jQuery("#selected-place-info").html(info);
	
	//build the categories
	buildCategorySelectionsForPlace(selectedItem, jQuery("#selectable-cat")); 
	
	jQuery("#selected-place").show();		
	
}

function verifyGeocode(geocode) {
	
	
	var hasAll = hasType("street_number", geocode.address_components);
	if(hasAll){
		hasAll = hasType("route", geocode.address_components);
	}
	if(hasAll){
		hasAll = hasType("locality", geocode.address_components);
	}
	if(hasAll){
		hasAll = hasType("administrative_area_level_1", geocode.address_components);
	}
	if(hasAll){
		hasAll = hasType("postal_code", geocode.address_components);
	}
	if(hasAll){
		hasAll = hasType("country", geocode.address_components);
	}
	
	//verified
	if(hasAll){
		//jQuery('#verify-geocode-status').html('verified');
		//jQuery('#verify-geocode-status').removeClass();
		//jQuery('#verify-geocode-status').addClass('verified-geocode fade');	
		
		selectedGeocode = geocode;
		
		//set the model
		selectedPlace.properties.address = 
			getShortNameForType("street_number", geocode.address_components)+' '+
			getShortNameForType("route", geocode.address_components);
		
		selectedPlace.properties.city = 
			getShortNameForType("locality", geocode.address_components);
		
		selectedPlace.properties.province = 
			getShortNameForType("administrative_area_level_1", geocode.address_components);

		selectedPlace.properties.postcode = 
			getShortNameForType("postal_code", geocode.address_components);
		
		selectedPlace.properties.country = 
			getShortNameForType("country", geocode.address_components);
		
		selectedPlace.geometry.coordinates = [];
		selectedPlace.geometry.coordinates.push(geocode.geometry.location.lng());
		selectedPlace.geometry.coordinates.push(geocode.geometry.location.lat());
			
		//map it
		jQuery('#map_canvas').show();

		var myOptions = {
		  zoom: 15,
		  mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById("map_canvas"),
			myOptions);
				
		deleteOverlays();
		map.setCenter(geocode.geometry.location);								
		addMarker(geocode.geometry.location);
		
		jQuery('#street-name-input').hide(); 
		jQuery('#edit-place-street-selected').html(geocode.formatted_address);
		jQuery('#edit-place-street-selected').addClass('verified-geocode fade');	
		jQuery('#street-address-saved').show(); 
		
		//setup the cats
		jQuery('#categories-section').show(); 		
		getTypes();
				
		
	} else {		
		jQuery('#edit-place-street-title').addClass('error-txt');
		jQuery('#map_canvas').hide();
	}

}

function findCategory(categoryName){
	var cat;
	jQuery.each(currentCategories, function(key, val) {
		if(val.name == categoryName) {
			cat = val;
		}
	});
	return cat;
}

function getTypes() {

	selectedPlace.classifiers = [];
	var placeClassifier = {
		type: '',
		category: '',
		subcategory: ''
	};
	selectedPlace.classifiers.push(placeClassifier);
	jQuery.ajax({
		  type: 'GET',
		  url : '/geodb/category/1_0/types.json',
          contentType: 'application/json', // don't do this or request params won't get through
          dataType : 'json',
		  error : function(jqXHR, textStatus, errorThrown) {
					console.error(textStatus);
					jQuery('#welocally-post-error').html('ERROR : '+textStatus);
					jQuery('#welocally-post-error').addClass('welocally-error error fade');
		  },		  
		  success : function(data, textStatus, jqXHR) {
		  	jQuery('#welocally-post-error').html('');
			if(data.errors != null) {
				buildErrorMessages(data.errors);		
			} else {
				currentCategories = data;
				jQuery.each(data, function(key, val) {
					jQuery('#edit-place-categories-selection-list').append('<li style="display:inline-block;">'+val.name+'</li>');					
				});
							
				
			}
		  }
		});
}

function getChildrenCats(parentId) {
	jQuery.ajax({
		  type: 'GET',
		  url : '/geodb/category/1_0/children.json?parentId='+parentId,
          contentType: 'application/json', // don't do this or request params won't get through
          dataType : 'json',
		  error : function(jqXHR, textStatus, errorThrown) {
					console.error(textStatus);
					jQuery('#welocally-post-error').html('ERROR : '+textStatus);
					jQuery('#welocally-post-error').addClass('welocally-error error fade');
		  },		  
		  success : function(data, textStatus, jqXHR) {
		  	jQuery('#welocally-post-error').html('');
			if(data.errors != null) {
				buildErrorMessages(data.errors);		
			} else {
				currentCategories = data;
				jQuery('#edit-place-categories-selection-list').html('');
				jQuery.each(data, function(key, val) {
					jQuery('#edit-place-categories-selection-list').append('<li style="display:inline-block;">'+val.name+'</li>');					
				});
							
				
			}
		  }
		});
}


function getShortNameForType(type_name, address_components){
	for (componentIndex in address_components) {
		var component = address_components[componentIndex];
		if(component.types[0] == type_name)
			return address_components[componentIndex].short_name;
	}
	return null;

}

function hasType(type_name, address_components){
	for (componentIndex in address_components) {
		var component = address_components[componentIndex];
		if(component.types[0] == type_name)
			return true;
	}
	return false;

}

function buildErrorMessages(errors) {
	jQuery('#welocally-post-error').html('');
	jQuery('#welocally-post-error').append('<ul>');
	jQuery.each(errors, function(i,error){
		jQuery('#welocally-post-error').append('<li>error:'+error.errorMessage+'</li>');
		
	});
	jQuery('#welocally-post-error').append('</ul>');
	jQuery('#welocally-post-error').addClass('welocally-error error fade');	
	jQuery('#welocally-post-error').show();
}

function buildMissingFieldsErrorMessages(fields) {
	jQuery('#welocally-post-error').html('');
	jQuery('#welocally-post-error').append('<ul>');
	jQuery('#welocally-post-error').append('<li>error '+fields+' missing</li>');
	jQuery('#welocally-post-error').append('</ul>');
	jQuery('#welocally-post-error').addClass('welocally-error error fade');	
	jQuery('#welocally-post-error').show();	
}


function setSelectedPlaceInfoForEditForm(place) {

	jQuery("#edit-place-external-id").val(place.externalId);
	
	//set the form value
	jQuery("#edit-place-name").val(place.name);
	//edit-place-street
	jQuery("#edit-place-street").val(place.address);
//edit-place-city"
	jQuery("#edit-place-city").val(place.city);
//edit-place-state" 
	jQuery("#edit-place-state").val(place.state);
//edit-place-zip" 
	jQuery("#edit-place-zip").val(place.postalCode);
//edit-place-phone"
	jQuery("#edit-place-phone").val(place.phone);
//edit-place-web" 
	jQuery("#edit-place-web").val(place.website);
    var categories = "";
	for (category in place.categories) {
			index = category;
			if(category<place.categories.length) {
				categories = place.categories[category]+", "
			}
	}
//edit-place-cats" 
	jQuery("#edit-place-cats").val(categories);
	
	
}

//init
jQuery(document).ready(function(jQuery) {

	//37.840157,-122.167969 37.834192,-122.242813
	geocoder = new google.maps.Geocoder();
	
	 
	var selectedPlaceObject = null;
	
	
	$( "input:submit, button",".action" ).button();
	
	jQuery("#welocally_default_search_radius").val('8');
	
	jQuery( "#search-places-action" ).click(function() {
		getLocationsByAddress(
			jQuery('#place-address').val(),
			jQuery('#place-search').val(),
			jQuery('#welocally_default_search_radius').val());
		return false;		
	});

	

    jQuery( "#edit-place-action" ).click(function() {
    
    	jQuery('#welocally-post-error').removeClass('welocally-error welocally-update error updated fade');
		jQuery('#welocally-post-error').html('');
	
    	jQuery("#place-selector").hide();
    	jQuery("#edit-place-form").show();
    	jQuery('#map_canvas').height( 401 );
    	jQuery('#map_canvas').width( '100%' );
    	

		
			    	
        return false;
    });
    
    jQuery( "#cancel-add-link" ).click(function() {
        jQuery("#edit-place-form").hide();
        if(selectedPlaceObject == null) {
        	jQuery("#place-selector").show();
        } else {
        	jQuery("#place-selector").show();
        }
        return false;
    });

    
    jQuery( "#btn-new-select" ).click(function() {  
    	jQuery("#selected-place").hide();
    	jQuery("#place-selector").show();    	     
        return false;
    });
    
    //saves a new place
    jQuery( "#save-place-action" ).click(function() {   
    
    	jQuery('#welocally-post-error').removeClass('welocally-error welocally-update error updated fade');
	    jQuery('#welocally-post-error').html('<em>Saving New Place...</em>');
    
    	var missingRequired = false;
    	var fields = '';
 		/*if (!jQuery("#edit-place-name").val().match(/\S/)) {
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
			buildMissingFieldsErrorMessages(fields);
			return false;
		}*/

                
        /*var options = { 
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
       		
        };*/
        
        /*var newPlace = {
			properties: {
				phone: "+1 907 452 3532",
				address: "1410 S Cushman St",
				name: "Thrifty Liquor Store",
				province: "AK",
				owner: "welocally",
				postcode: "99701",
				city: "Fairbanks",
				country: "US"
			},
			type: "Place"
		};*/
		

        
		jQuery.ajax({
		  type: 'PUT',
		  url : '/geodb/place/1_0/',
          contentType: 'application/json', // don't do this or request params won't get through
          dataType : 'json',
          data: JSON.stringify(selectedPlace),
		  error : function(jqXHR, textStatus, errorThrown) {
					console.error(textStatus);
					jQuery('#welocally-post-error').html('ERROR : '+textStatus);
					jQuery('#welocally-post-error').addClass('welocally-error error fade');
		  },		  
		  success : function(data, textStatus, jqXHR) {
		  	jQuery('#welocally-post-error').html('');
			if(data.errors != null) {
				buildErrorMessages(data.errors);		
			} else {
				setSelectedPlaceInfo(data);
			}
		  }
		});
        
        
        return false;
    });
    
    jQuery("input[name='isWLPlace']").change(function(){
    	if (jQuery("input[name='isWLPlace']:checked").val() == 'true') { 
        	jQuery("#placeForm").show();
        	
        	//no place has been selected yet
        	if(selectedPlaceObject == null) {
        		jQuery("#place-selector").show();
        	}
        		
    	} else if (jQuery("input[name='isWLPlace']:checked").val() == 'false') { 
	        jQuery("#placeForm").hide();	
    	} 
    });   
    
    jQuery( "#selectable" ).selectable({
		   selected: function(event, ui) {
				selectedFeatureIndex = jQuery("#scroller-places li").index(ui.selected);
				setSelectedPlaceInfo(jsonObjFeatures[selectedFeatureIndex]);				
		   }
	});
	
	jQuery( "#selectable-cat" ).selectable({
		   selected: function(event, ui) {
			   	if(selectedCategories.indexOf(ui.selected.innerText) == -1) {
			   		selectedCategories = selectedCategories + ui.selected.innerText+",";
			   	}
		   		jQuery( "#place-categories-selected" ).val(selectedCategories);		
		   },
		   unselected: function(event, ui) {
		   		if(selectedCategories.indexOf(ui.unselected.innerText) != -1) {
		   			var replaceText =  ui.unselected.innerText+",";
		   			selectedCategories = selectedCategories.replace(new RegExp(replaceText, 'g'),"");
		   			jQuery( "#place-categories-selected" ).val(selectedCategories);	
		   		}		
		   }
	});
	
	jQuery( "#edit-place-categories-selection-list" ).selectable({
		   selected: function(event, ui) {
		   		var selectedCat = findCategory(ui.selected.innerText);
			   	if(selectedCat != null){
			   		
			   		if(selectedCat.type == 'Type'){
			   			selectedPlace.classifiers[0].type = selectedCat.name;
			   		} else if(selectedCat.type == 'Category'){
			   			selectedPlace.classifiers[0].category = selectedCat.name;
			   		} else if(selectedCat.type == 'Subcategory'){
			   			selectedPlace.classifiers[0].subcategory = selectedCat.name;
			   		}
			   		
			   		jQuery( "#edit-place-categories-selected")
			   			.append(
			   			'<li class="categories-selected-list-item">'+
			   			selectedCat.type+':'+selectedCat.name+'</li>');
			   		
			   		if(selectedPlace.classifiers[0].type != '' &&
			   			selectedPlace.classifiers[0].category != '' &&
			   			selectedPlace.classifiers[0].subcategory != '') {
			   			
			   			//finished
			   			jQuery( '#edit-place-categories-selection').hide();
			   			jQuery( '#save-place-action').show();
			   			jQuery( '#edit-place-optional-section').show();
			   			
			   		} else {
			   			getChildrenCats(selectedCat._id);
			   		}
			   	}
			   	
		   },
		   unselected: function(event, ui) {
		   		
		   }
	});

	jQuery( '#edit-place' ).click(function() {
		jQuery('#welocally-post-error').removeClass('welocally-error welocally-update error updated fade');
		jQuery('#welocally-post-error').html('');
		jQuery('#place-form-title').html('edit place');
		jQuery('#save-place-action').html('Save Place');
	
		setSelectedPlaceInfoForEditForm(jsonObjFeatures[selectedFeatureIndex]);	
		
    	jQuery("#place-selector").hide();
    	jQuery("#edit-place-form").show();
		return false;		
	});
	
	
	jQuery( '#save-place-name-action' ).click(function() {
		console.log("save-place-name-action");	
		
		if (!jQuery("#edit-place-name").val().match(/\S/)) {
            jQuery("#edit-place-name-title").removeClass(); 
            jQuery("#edit-place-name-title").addClass('error-txt')
            
        } else {
        	jQuery('#place-name-input').hide();
        	selectedPlace.properties.name = jQuery("#edit-place-name").val();
        	jQuery('#edit-place-name-selected').html(selectedPlace.properties.name);
        	
        	jQuery('#place-name-saved').css("display","inline-block");
        	jQuery('#place-name-saved').show();
        	        	
        	jQuery('#street-address-section').css("display","inline-block");
        	jQuery('#street-address-section').show();
        }
		
		
		
		
		return false;		
	});
	
	jQuery( '#geocode-action' ).click(function() {
		console.log("geocode action");
		
		jQuery('#welocally-post-error').removeClass('welocally-error welocally-update error updated fade');
		jQuery('#welocally-post-error').html('');
		
		var missingRequired = false;
    	var fields = '';
		
		if (!jQuery("#edit-place-street").val().match(/\S/)) {
            missingRequired = true;
            fields = fields+' Street Address ';
        }
        
        if(missingRequired){
			buildMissingFieldsErrorMessages(fields);
			return false;
		}
		
		var address = jQuery('#edit-place-street').val();
		geocoder.geocode( { 'address': address}, function(results, status) {
			console.log("geocode result");
			if (status == google.maps.GeocoderStatus.OK) {
				jQuery('#edit-place-street').val(results[0].formatted_address);
				verifyGeocode(results[0]);
				
				
			} else {
				console.log("Geocode was not successful for the following reason: " + status);
		  	} 
		});
		
		
		return false;		
	});


});




</script>
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
	 
	 .error-txt { 
		color:#850F00;
		font-weight:bold;
		font-size:1.0em;
	 } 
	 
	 
	.verified-geocode { 
		border-color:#245E07;
		background-color:#B7ED9D;
		border-width:2px;
		border-style:solid;
		-moz-border-radius:3px;
		-khtml-border-radius:3px;
		-webkit-border-radius:3px;
		border-radius:3px;
		margin: 0px;
		border-style:solid;
		border-spacing:0;
		padding: 0px;
		margin-bottom: 5px;
		color:#245E07;
		display:inline-block;
	 }  
	 
	 .error-geocode { 
		border-color:#8A0E0E;
		background-color:#E68C8C;
		border-width:2px;
		border-style:solid;
		-moz-border-radius:3px;
		-khtml-border-radius:3px;
		-webkit-border-radius:3px;
		border-radius:3px;
		margin: 0px;
		border-style:solid;
		border-spacing:0;
		padding: 0px;
		margin-bottom: 0px;
		color:#8A0E0E;
		display:inline-block;
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
	#selectable .ui-selecting { background: #AAAAAA; color: black; }
	#selectable .ui-selected { background: #444444; color: white; }
	#selectable { list-style-type: none; margin: 0; padding: 0; }
	#selectable li { margin: 3px 3px 3px 0; padding: 0.2em; cursor: pointer; }	
	#categories-choice {  margin-bottom: 10px; display:none; }	
	
	
	
	
		
	/* ------ selected place */
	#selected-place { margin-bottom: 10px; width: 100%; display:none; }
	#btn-new-select { margin-bottom: 10px; margin-top: 10px; }
	#selected-place-categories { margin-bottom: 10px; }
    #selected-place-info { margin-bottom: 10px; }
    #selectable-cat .ui-selecting { background: #AAAAAA; color: black; }
	#selectable-cat .ui-selected { background: #444444; color: white; }
	#selectable-cat { list-style-type: none; margin: 0; padding: 0; }
	#selectable-cat li { margin: 3px 3px 3px 0; padding: 0.2em; cursor: pointer; }
	#show_place_info { margin-bottom: 10px; }
	
	/* ------ add new place */
	#edit-place-form { margin-bottom: 10px; width: 100%; display:none; }
	.edit-field { width: 800px; height: 30px; margin-bottom: 10px; display:inline-block; font-size:1.2em; }
	.selected-place-field { width: 800px; height: 30px; margin-bottom: 10px; display:inline-block; font-size:1.4em; }
	
	
	#edit-place-categories-selection-list .ui-selecting { background: #AAAAAA; color: black; }
	#edit-place-categories-selection-list .ui-selected { 
		background: #7A5207; color: white; 
	}
	#edit-place-categories-selection-list { list-style-type: none; margin: 0; padding: 0; }
	#edit-place-categories-selection-list li { 
		margin: 3px 3px 3px 0; padding: 0.2em; cursor: pointer; 
		border-color:#7A5207;
		background-color:#F5E6A6;
		border-width:1px;
		border-style:solid;
		-moz-border-radius:3px;
		-khtml-border-radius:3px;
		-webkit-border-radius:3px;
		border-radius:3px;
		border-style:solid;
		border-spacing:0;
		color:#7A5207;
		display:inline-block;
	}	
	
	.categories-selected-list-item {
	 	border-color:#737373;
		background-color:#DEDEDE;
		border-width:2px;
		border-style:solid;
		-moz-border-radius:3px;
		-khtml-border-radius:3px;
		-webkit-border-radius:3px;
		border-radius:3px;
		margin: 0px;
		border-style:solid;
		border-spacing:0;
		font-size:1.2em; 
		padding: 5px;
		margin-right: 5px;
		color:#737373;
		display:inline-block;
	 }
	 	
</style>
<body>
	<div class="container">
		<div class="span-24">	
			<div id="welocally-post-error" class="welocally-error">No Errors...</div>				
				<div id="placeForm">
				<input type="hidden" id="place-selected" name="PlaceSelected">
				<input type="hidden" id="place-categories-selected" name="PlaceCategoriesSelected">
				<div id="all_place_info">
					<!-- start place selector -->
					<div id="place-selector">
						<div class="meta-title2">Select Place</div>
						<div>	
							<div>*<em>Please enter the closest address, this can just be the city and state (ie. Oakland, CA), or the full address...</em> REQUIRED</div>
							<div style="margin-bottom:5px">
								<input type="text" id="place-address" 
									class="search-field" 
									value="">
								<select id="welocally_default_search_radius" name="welocally_default_search_radius" >
									<option value="2">2 km</option>
									<option value="4">4 km</option>
									<option value="8">8 km</option>
									<option value="12">12 km</option>
									<option value="16">16 km</option>
									<option value="25">25 km</option>
									<option value="50">50 km</option>
								</select>&nbsp;<em>Distance in Km</em> 	
									
							</div>	
							<div>*<em>What is the name of the place you are writing about or a simillar keyword...</em> REQUIRED</div> 
							<div><input type="text" id="place-search" class="search-field"></div>
							<div>
								<button id="search-places-action">find places</button>
					            <span id="add-span">don't see a match?&nbsp;&nbsp;<button id="edit-place-action">add new place</button></span>
					      
							</div>
						</div>
						<div id="results">
							<div id="scroller-places">
								<ol id="selectable">
								</ol>	
							</div>
						</div>		
					</div> 
					<!-- end place selector -->
					<!-- start place selector -->
					<div id="selected-place">
						<div class="meta-title2">Selected Place</div>
						<div id="selected-place-info"></div>
						<div id="categories-choice">
							<div class="meta-title2">Choose categories for post</div>
							<ol id="selectable-cat"></ol> 
						</div>
						<button id="btn-new-select" href="#">new selection</button>
						<button id="edit-place" href="#">edit selected place</button>
					</div> 
					<!-- end place selector -->	
					<!-- add place form -->	
				    <div id="edit-place-form">
				    	<div id="place-form-title" class="meta-title2">Add New Place</div>
				    					    	
				    	<input type="hidden" id="edit-place-external-id" name="PlaceSelected">
				    	
				    	
				    	<div id="place-name-section">
				    		<div id="place-name-input" class="action" style="display:inline-block">
								<div id="edit-place-name-title">*Place Name: <em>Required</em></div>
								<input type="text" id="edit-place-name" name="edit-place-name" class="edit-field" value="Grinders Submarine Sandwiches">
								<button id="save-place-name-action" href="#">Save</button>
							</div>							
							<div id="place-name-saved" class="action" style="display:none">
								<div id="edit-place-name-selected" class="selected-place-field">&nbsp;</div>
								<button id="edit-place-name-action" href="#">Edit</button>
							</div>							
				        </div>
				        
				        <div id="street-address-section" style="display:none">
				        	<div id="street-name-input" class="action" style="display:inline-block">
				        		<div id="edit-place-street-title">*Full Address: <em>Required</em></div>
				        		<input type="text" id="edit-place-street" name="edit-place-street" class="edit-field" value="2069 Antioch Ct Oakland, CA 94611, USA">
				        		<button class="action" id="geocode-action" href="#">Geocode</button>				        	
				        	</div>
				        	<div id="map_canvas" style="width:100%; height:300px; display:none"></div>
				        	<div id="street-address-saved" class="action" style="display:none">				        		
				        		<div id="edit-place-street-selected" class="selected-place-field">&nbsp;</div>				        		
								<button class="action" id="edit-place-street-action" href="#">Edit</button>						
				        	</div>
				        </div>
				        
				        <div id="categories-section" style="display:none">
				        	<div style="margin-top:15px; margin-bottom:-15px; height:20px;">*Category Info: <em>Required</em></div>
				        	<div id="edit-place-categories-selected"><ul id="edit-place-categories-selected-list"></ul></div>
				        	<div id="edit-place-categories-selection"><ul id="edit-place-categories-selection-list" style="display:inline-block; list-style:none;"></ul></div>
				        </div>

						<div id="edit-place-optional-section" style="display:none">
							<div style="margin-top:10px;"><em>Although these fields are optional is is strongly reccomended that you include the phone number and website of the place if you can find it.</em></div>
							<div id="step-phone">
								Phone Number: (optional):</br>
								<input type="text" id="edit-place-phone" name="edit-place-phone" class="edit-field" value="(510) 339-3721">
							</div>
							<div id="step-web">
								Website: (optional):</br>
								<input type="text" id="edit-place-web" name="edit-place-web" class="edit-field" value="http://grindersmontclair.com">
							</div>
				        </div>
				        
	        			<div class="action" style="display:inline-block; margin-top:10px;">
							<button id="cancel-add-link" href="#">Cancel</button>
							<button id="save-place-action" href="#" style="display:none">Add Place</button>
				        </div>
			   
				    </div>
				   
				</div>	 <!-- end add place form -->		
			
			

		</div>
	</div>

</body>
</html>


