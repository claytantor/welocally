<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>welocally</title>
				
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
		<script src="https://maps.google.com/maps/api/js?key=AIzaSyACXX0_pKBA6L0Z2ajyIvh5Bi8h9crGVlg&sensor=true&language=en"
	  		type="text/javascript"></script>
	  		
	  	<!-- welocally-developer scripts  -->	
		<script src="<c:url value='/js/welocally-places-developer/wl_base.js' />" type="text/javascript"></script>   
		<script src="<c:url value='/js/welocally-places-developer/wl_place_widget.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/welocally-places-developer/wl_places_multi_widget.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/welocally-places-developer/wl_infobox.js' />" type="text/javascript"></script>
			
		<script src="<c:url value='/js/widget/wl_dealfinder_widget.js' />" type="text/javascript"></script> 
		<script src="<c:url value='/js/widget/wl_placefinder_widget.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/widget/wl_place_selection_listener.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/widget/wl_addplace_widget.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/widget/wl_geodb_search.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/widget/wl_userplaces_search.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/widget/wl_place_edit_view.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/widget/wl_searchadd.js' />" type="text/javascript"></script>
		<script src="<c:url value='/js/widget/wl_userplacesmgr.js' />" type="text/javascript"></script>
		
		<script type="text/javascript" src="<c:url value='/js/jquery-ui-1.8.13.custom.min.js' />"></script>
		
		<script type="text/javascript" src="<c:url value='/js/welocally.js' />"></script>
		
		<!--  -->
		<link type="text/css" href="<c:url value='/css/custom-theme/jquery-ui-1.8.13.custom.css' />" rel="stylesheet" media="screen" />
		<link href='http://fonts.googleapis.com/css?family=Ubuntu+Mono:400,700|Carme|Fjord+One' rel='stylesheet' type='text/css'>
		<link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css' />" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value='/css/blueprint/print.css' />" type="text/css" media="print">
		<!--[if lt IE 8]>
			<link rel="stylesheet" href="<c:url value='/css/blueprint/ie.css' />" type="text/css" media="screen, projection">
		<![endif]-->
		
		<!-- welocally-developer style  -->
		<link rel="stylesheet" href="<c:url value='/css/welocally-places-developer/css/wl_places.css' />" type="text/css">
		<link rel="stylesheet" href="<c:url value='/css/welocally-places-developer/css/wl_places_multi.css' />" type="text/css">
		<link rel="stylesheet" href="<c:url value='/css/welocally-places-developer/css/wl_places_place.css' />" type="text/css">
		
		<link rel="stylesheet" href="<c:url value='/css/admin/wl_places_finder.css' />" type="text/css">
		<link rel="stylesheet" href="<c:url value='/css/admin/wl_places_addplace.css' />" type="text/css">
		<link rel="stylesheet" href="<c:url value='/css/admin/wl_userplacesmgr.css' />" type="text/css">
		
		<link rel="stylesheet" href="<c:url value='/css/welocally.css' />" type="text/css"> 
		
		<link rel="shortcut icon" href="<c:url value='/images/we_16.png' />"/> 
		
		
	
	</head>

<c:set value="/images" var="imageUrl" scope="request"/>