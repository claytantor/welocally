<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ratecred" uri="/WEB-INF/ratecred.tld" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<script type="text/javascript" src="<c:url value='/js/jquery-1.4.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui-1.8.2.custom.min.js' />"></script>	
	<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery.jcarousel.js' />"></script>	
	<link rel="stylesheet" href="${css}" type="text/css">	
	
</head>
<script type="text/javascript">
var mMap = null;

$(document).ready(function() {
	jQuery('#offercarousel').jcarousel({
    	wrap: 'circular',
    	scroll: 1
    });
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
	var map = new google.maps.Map(document.getElementById('rc_map_canvas'),myOptions);
	return map;
}

function addMarker(latitude, longitude, map) {

	var image = "<c:url value='/images/mapicon_32.png'/>";
	//var image = "http://localhost/rcadmin/images/mapicon_32.png";

	//37.791527, -122.277433
	var myLatLng = new google.maps.LatLng(latitude, longitude);
	//var myLatLng = new google.maps.LatLng(37.791527, -122.277433);
	
	var locMarker = new google.maps.Marker({
		position: myLatLng,
		map: map,
		title:'Offer'
	});
}
</script>

<body style="width: 100%; height: 100%; padding: 0; margin: 0;">
<div class="rc_offer_container">
<ul id="offercarousel" class="jcarousel-skin-rc"> 
<li>
	<div class="rc_offer_container">
	<table class="rc_offer_tmain">
	<tr>	
		<%--column 1 image --%>
		<c:if test="${not empty offer.illustrationUrl}">
		<td><img src="${offer.illustrationUrl}" class="rc_offer_image"/></td>
		</c:if>	
		<%--column 2 offer --%>
		<td>
			<table class="rc_offer_tdetails">
				<tr>
					<td>
						<div class="rc_offer_program_name">${offer.advertiser.name} <span class="rc_offer_offer_citystate">${offer.city}, ${offer.state}</span> </div>
	       				<div class="rc_offer_offer_name">${offer.name}</div>  
	       				<c:if test="${not empty offer.description}">   								
	       					<div class="rc_offer_offer_descr">${offer.description}</div>       					
	       				</c:if>      								
					</td>
				</tr>
				<tr>
					<td align="center" class="rc_offer_tdetails_rprice">
						<table class="rc_offer_tprice">
							<tr>
								<td class="rc_offer_tprice_pay_l">you pay</td>
								<td class="rc_offer_tprice_value_l">valued at</td>
								<td class="rc_offer_tprice_save_l">you save</td>
							</tr>
							<tr>
								<td class="rc_offer_tprice_pay_v">$<fmt:formatNumber type="number" value="${offer.price}" pattern="0.00"/></td>
								<td class="rc_offer_tprice_value_v">$<fmt:formatNumber type="number" value="${offer.value}" pattern="0.00"/></td>
								<td class="rc_offer_tprice_save_v">%<fmt:formatNumber type="number" value="${(1.0 - (offer.price/offer.value))*100}" pattern="0"/></td>
							</tr>						
						</table>
										
					</td>
				</tr>
				<tr>
					<td class="rc_offer_tbuttons">
						<ratecred:azcart itemTitle="${offer.advertiser.name}, ${offer.name}" itemSku="${fn:substring(offer.externalId, 0, 36)}" itemDescription="${offer.description}" itemPrice="${offer.price}" environment="SANDBOX"  />
						<div class="rc_offer_tbutton_float">				
						<a class="rc_offer_tbutton_buy" href="#" onclick="javascript:$('#form_${fn:substring(offer.externalId, 0, 36)}').submit();return false;">buy this!</a>
						</div>
					</td>			
				</tr>					
			</table>
		</td>
	</tr>
	</table>	
	</div>		
</li> 
<%-- offer items --%>

<li>
		<div class="rc_title_items_title1">What's The Deal?</div>		
		<%-- items --%>
		<div class="rc_title_items_text">${offer.extraDetails}</div>
		<c:if test="${fn:length(offer.items) > 0}">
      	<c:forEach items="${offer.items}" var="item">
      			<div>      			
       			<div class="rc_title_items_title2">${item.title}</div>
       			<div class="rc_title_items_text"><span class="text-12">quantity:</span> <span class="strong-16">${item.quantity}</span></div>
				<div class="rc_title_items_text">${item.description}</div>					
      			</div>       		
      	</c:forEach>
      	</c:if>					
</li> 
<li>
	<div id="rc_locations_container" >																											
			<div class="rc_locations_title">Locations & Business Info</div>
			<div class="rc_locations_cols">
				<table width="100%" border="0">
					<tr>
						<td width="300" align="left" valign="top"><div id="rc_map_canvas"></div></td>
						<td width="300" valign="top" align="left">
							<div class="rc_business_container">
								<div id="rc_business_info">
									<div class="rc_business_info_name">${offer.advertiser.name}</div>
									<div id="rc_locations_address">
									<c:forEach items="${offer.advertiser.locations}" var="location" varStatus="status">
										<div>
											<div class="rc_location_address_info">${location.addressOne}</div>
											<div class="rc_location_address_info">${location.city}, ${location.state}</div>
										</div>		
									</c:forEach>
									</div>
									<div class="rc_business_info_description">${offer.advertiser.description}</div>
									
								
								</div>
								
							</div>
						</td>
					</tr>
				
				</table>
			</div>						
	</div>
</li> 
</ul> 
	
</div>	
				
</body>         		
</html>
