<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ratecred" uri="/WEB-INF/ratecred.tld" %>
<c:set value="http://ratecred.com/styles" var="stylesUrl"/>
<c:set value="http://ratecred.com/images" var="imagesUrl"/>
<html>
<head>
<title>RateCred Offer : ${offer.programName}, ${offer.name}</title>


<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">

var mMap = null;

$(document).ready(function() {
	$('#map_canvas').width( 300 );
	$('#map_canvas').height( 300 );
	mMap = initializeMap(${mapCenter.latitudeFractional},${mapCenter.longitudeFractional});	
<c:forEach items="${offer.advertiser.locations}" var="location" varStatus="status">	
	addMarker(${location.lat}, ${location.lng}, mMap);
</c:forEach>	
});


function initializeMap(latitude, longitude) {
	
	var myOptions = {
	  zoom: 13,
	  center: new google.maps.LatLng(latitude, longitude),
	  mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	
	var map = new google.maps.Map(document.getElementById('map_canvas'),
			  myOptions);

	return map;

}

function addMarker(latitude, longitude, map) {

	var image = '/images/mapicon_32.png';

	var myLatLng = new google.maps.LatLng(latitude, longitude);
	
	var locMarker = new google.maps.Marker({
		position: myLatLng,
		map: map,
		icon: image
	});
}



</script>
<meta content='width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;' name='viewport' />
<link href="${stylesUrl}/mobile/m_basic.css" media="screen" rel="stylesheet" type="text/css" />
<link href="${stylesUrl}/mobile/m_ratecred.css" media="screen" rel="stylesheet" type="text/css" />
<link href="${imagesUrl}/icon.png" rel="apple-touch-icon-precomposed" />
<link href="${imagesUrl}/ic_fav_alpha_32.png" rel="icon" type="image/png" />
<link href="${imagesUrl}/startup.png" rel="apple-touch-startup-image" />
</head>
<body>

<div class="topbar">
<img src="http://media.ratecred.com.s3.amazonaws.com/download/demo/oaklandly/mobile_logo_oaklandly.png"/>
</div>      		
<div class="padding-5">
		
		<%-- offer --%>
		<div class="offer-box">
			<div class="text-10">${offer.advertiser.name}</div>
      		<div class="text-10">${offer.name}</div>
      		<div><span class="text-10">${offer.type}</span></div>		        		
      		<c:if test="${not empty offer.illustrationUrl}"><div class="align-center margin-10"><img src="${offer.illustrationUrl}" width="90%"/></div></c:if>		         		

      		<div class="align-center">
      			<div class="value-box" >
	      			<div class="margin-5 value-box align-center">
	      				<div class="text-10">you pay</div>
	      				<div class="strong-14">$<fmt:formatNumber type="number" value="${offer.price}" pattern="0.00"/></div>
	      			</div>
	      			<div class="margin-5 value-box align-center">
	      				<div class="text-10">value of</div>
	      				<div class="strong-14">$<fmt:formatNumber type="number" value="${offer.value}" pattern="0.00"/></div>
	      			</div>
	      			<div class="margin-5 value-box align-center">
	      				<div class="text-10">you save</div>
	      				<div class="strong-14">%<fmt:formatNumber type="number" value="${(1.0 - (offer.price/offer.value))*100}" pattern="0"/></div>
	      			</div>	      				      			
      			</div>
      		</div>
      		<div class="align-center">       		 
			<ratecred:azcart 
				itemTitle="${offer.advertiser.name}, ${offer.name}" 
				itemSku="${offer.externalId}" 
				itemDescription="${offer.description}" 
				itemPrice="${offer.price}" 
				environment="SANDBOX"  />					
      		</div>
      		<div class="text-10">${offer.description}</div>
      		
      		
      	</div>
      	
		<%-- spacer --%>
		<div class="padding-5"><img src="/images/spacer.gif" height="5" border="0"/></div>

      	<%-- items --%>
      	<div class="offer-box">
      		<div class="text-14">What's The Deal?</div>
      		<c:forEach items="${offer.items}" var="item" varStatus="status">
      			<div class="text-12">${item.title}</div>
      			<div>${item.description}</div>
      			<div class="align-right"><span class="text-8">quantity:</span><span class="text-12">${item.quantity}</span></div>
      			<div ><img src="${imagesUrl}/spacer.gif" height="5" border="0"/></div>       		
      			<c:if test="${not status.last}"><div class="margin-5 grey-sepline"><img src="${imagesUrl}/spacer.gif" height="2" border="0"/></div></c:if>     			
      		</c:forEach>
      		
      	</div>
      	
      	<%-- spacer --%>
		<div class="padding-5"><img src="/images/spacer.gif" height="5" border="0"/></div>
      	
      	<%-- locations --%>
      	<div class="offer-box">     		
      		
      		<div class="text-14">Locations & Map</div>
      		<div class="align-center" id="map_canvas"></div>
      		<div class="text-10">
      		<c:forEach items="${offer.advertiser.locations}" var="location">
      			<hr/>
      			<div>${location.addressOne}</div>
      			<div>${location.city}, ${location.state}</div>       		
      		</c:forEach>
      		</div>
      	</div>

		<%-- spacer --%>
		<div class="padding-5"><img src="${imagesUrl}/spacer.gif" height="5" border="0"/></div>
      	
      	<%-- business --%>
      	<div class="offer-box">
      		
      		<%-- business --%>
      		<div class="text-14">${offer.advertiser.name}</div>
      		<div class="text-10">${offer.advertiser.description}</div>
     
      	</div>

		
   		        		

</div>	    
<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-13101726-3']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
</body>
</html>