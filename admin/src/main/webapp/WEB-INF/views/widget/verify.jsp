<%@ page
        contentType="text/html; charset=iso-8859-1"
        language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="We Locally Article Publishing Widget Generator"/>
<c:url value="/images" var="imageUrl" scope="request"/>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>${pageTitle}</title>
		
		<link type="text/css" href="<c:url value='/css/custom-theme/jquery-ui-1.8.13.custom.css' />" rel="stylesheet" />	
		<link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css' />" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value='/css/blueprint/print.css' />" type="text/css" media="print">
		<!--[if lt IE 8]>
			<link rel="stylesheet" href="<c:url value='/css/blueprint/ie.css' />" type="text/css" media="screen, projection">
		<![endif]-->
		<link rel="stylesheet" href="<c:url value='/css/jquery-ui-timepicker.css' />" type="text/css">
		<link rel="stylesheet" href="<c:url value='/css/welocally.css' />" type="text/css"> 

		<script type="text/javascript" src="<c:url value='/js/jquery-1.5.1.min.js' />"></script>
		<script type="text/javascript" src="<c:url value='/js/jquery-ui-1.8.13.custom.min.js' />"></script>
		<script type="text/javascript" src="<c:url value='/js/jquery-ui-timepicker-addon.js' />"></script>
		<script type="text/javascript" src="http://cdn.simplegeo.com/js/1.2/simplegeo.places.jq.min.js"></script>
		<script type="text/javascript" src="http://cdn.simplegeo.com/js/1.2/simplegeo.storage.jq.min.js"></script>
		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>


	
	</head>
<script type="text/javascript" >

//-122.274004 lon,37.801121 lat old city oakland


var client = new simplegeo.StorageClient('bb8HCTrBtUZs78EwUVJvXG6ugWkrjNvM');

function displayData(err, data) {

    if (err) { 
        console.error(err);
    } else {
        console.log(JSON.stringify(data));
    }

}


$(document).ready(function() {
	
	//oakland
	var lat = 37.801121;
	var lon = -122.274004;

	//setup map
	$('#map_canvas').height( 400 );
	
	var options = {
	  zoom: 13,
	  center: new google.maps.LatLng(lat, lon),
	  mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	
	var map = new google.maps.Map(document.getElementById('map_canvas'),
			options);

	//home location
	addMarker(map, '${imageUrl}/here.png', lat, lon); 

	var optionsSearch = {
			  radius: 10
	}

	var publisherLayerPrefix = '${publisher.networkMember.memberKey}.'+'${publisher.siteName}'.replace(/ /g,'').toLowerCase(); 
  	
	populateFeaturesForType(
			'article',
			lat, lon,
			'#selectable', 
			publisherLayerPrefix+'.article', 
			optionsSearch, 
			'${imageUrl}/articles_32.png',
			map);

	populateFeaturesForType(
			'event',
			lat, lon,
			'#selectable', 
			publisherLayerPrefix+'.event', 
			optionsSearch, 
			'${imageUrl}/events_32.png',
			map);

	$( "#selectable" ).selectable({
		   selected: function(event, ui) { 
		   		if(ui.selected.id.indexOf("article") != -1) {
			   		var selectedItem = articles[ui.selected.id.replace("article","")];				
					$('#selected_content').html(buildContentForItem(selectedItem)); 
		   		} else if(ui.selected.id.indexOf("event") != -1) {
			   		var selectedItem = events[ui.selected.id.replace("event","")];				
					$('#selected_content').html(buildContentForItem(selectedItem)); 
		   		}
		   }
	});
  	
});

var events = [];
var articles = [];


function Item (type, feature, content) {
    this.type = type;
    this.feature = feature;
   	this.content = content;
}

//${imageUrl}/articles_32.png
function populateFeaturesForType(type, lat, lon, div, layer, optionsSearch, markerImage, map) {
	client.getNearby(layer, lat, lon, optionsSearch, function(err, data) {
  	    if (err) {
  	        console.error(err);
  	    } else {
  	    	
  	    	$.each(data.features, function(i,feature){ 
  	    		//console.log(JSON.stringify(item));	    		
  	    		
				var item = buildListItemForFeature(i,type,feature); 
				addItemMarker(type, i, map, markerImage, feature.geometry.coordinates[1], feature.geometry.coordinates[0], feature.properties.entity.name);
  	    		$(div).append(item.content);
  	    		if(type=='article')
					articles[i] = item;	   
  	    		else if(type=='event')
					events[i] = item;	   
				 		
  		    });
  	    }
  	});
	
}


function buildListItemForFeature(position, type, feature) {
	if (feature.properties.entity != null) {
		var content ='None';
		

		if(type == 'event'){
			content = '<li id=\"'+type+position+'\" class=\"span-5 ui-widget-content\"><strong>'+type+':'+
			feature.properties.entity.name+
			'</strong></br>'+
			feature.properties.entity.geoPlace.name+
			'</br>'+
			new Date(feature.properties.entity.startDateTime)+
		'</li>';
		} else if(type = 'article'){
			content = '<li id=\"'+type+position+'\" class=\"span-5 ui-widget-content\"><strong>'+type+':'+
			feature.properties.entity.name+'</strong></br>'+
			feature.properties.entity.geoPlace.name+'</br>'+
		'</li>';
		}
		
		var item = new Item(
			type,
			feature,	
			content);

		return item;
	}
	
}

function buildContentForItem(item) {
	
	if (item.feature.properties.entity != null && item.type == 'event') {
		return '<div><img src=\"${imageUrl}/'+item.type+'s_32.png\"/></br>'+
		'<strong>'+item.type+": "+item.feature.properties.entity.name+'</strong></br>'+
					item.feature.properties.entity.geoPlace.name+'</br>'+
					new Date(item.feature.properties.entity.startDateTime)+'</br>'+
					item.feature.properties.entity.description+
				'</div>';
	} else if (item.feature.properties.entity != null && item.type == 'article') {
		return '<div><img src=\"${imageUrl}/'+item.type+'s_32.png\"/></br>'+
		'<strong>'+item.type+": "+item.feature.properties.entity.name+'</strong></br>'+
					item.feature.properties.entity.geoPlace.name+'</br>'+
					item.feature.properties.entity.description+
				'</div>';
	} 	
}



function addMarker(map, image, latitude, longitude) {
	
	var myLatLng = new google.maps.LatLng(latitude, longitude);
	var mMarker = new google.maps.Marker({
		position: myLatLng,
		map: map,
		icon: image
	});
  }

function addItemMarker(type, index, map, image, latitude, longitude, title) {
	
	var myLatLng = new google.maps.LatLng(latitude, longitude);
	var mMarker = new google.maps.Marker({
		position: myLatLng,
		map: map,
		icon: image,
		title: title,
		type: type,
		index: index
	});
	
	google.maps.event.addListener(mMarker, 'click', function() {
		if(mMarker.type.indexOf("article") != -1) {
			var selectedItem = articles[mMarker.index];
	   		var content = buildContentForItem(selectedItem);				
			$('#selected_content').html(content);
			infowindow.content = content;	
			infowindow.open(map,mMarker); 
   		} else if(mMarker.type.indexOf("event") != -1) {
	   		var selectedItem = events[mMarker.index];
	   		var content = buildContentForItem(selectedItem);				
			$('#selected_content').html(content);
			infowindow.content = content;	
			infowindow.open(map,mMarker); 
   		}
		
		
	});
}

var infowindow = new google.maps.InfoWindow({
    content: "empty"
});

</script>
<style type="text/css">
	/*demo page css*/
	body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 50px;}
	.demoHeaders { margin-top: 2em; }
	#dialog_link {padding: .4em 1em .4em 20px;text-decoration: none;position: relative;}
	#dialog_link span.ui-icon {margin: 0 5px 0 0;position: absolute;left: .2em;top: 50%;margin-top: -8px;}
	ul#icons {margin: 0; padding: 0;}
	ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
	ul#icons span.ui-icon {float: left; margin: 0 4px;}
	#feedback { font-size: 1.0em; }
	#selectable .ui-selecting { background: #FECA40; }
	#selectable .ui-selected { background: #F39814; color: white; }
	#selectable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
	#selectable li { margin: 3px; padding: 0.4em; font-size: 1.0em; }	
	#selected_content { margin: 10; padding: 10; width: 100%; font-size: 1.4em; }
	#leftbar {width:240px; height:400px; overflow-y:auto; overflow-x:hidden; }	
</style>
<body>
<div class="container">
    <div class="span-24">
        <jsp:include page="../header.jsp"/>
    </div>
    <div class="span-24 last">
    	<h3>items</h3>
    </div>
    <div class="span-24 last">
	<%-- left bar --%>
	<div id="leftbar" class="span-6">
		<div class="span-10">
			<ol id="selectable">
			</ol>	
		</div>		
	</div>
	<%-- right bar --%>
	<div class="span-14 last">
		<div id="map_canvas" class="span-14"></div>
		<div id="selected_content" class="span-14"></div>
	</div>	

    </div>
</div>

</body>