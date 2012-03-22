<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title>welocally</title>
		
		<script type="text/javascript" src="<c:url value='/js/jquery-1.5.1.min.js' />"></script>
		<script type="text/javascript" src="<c:url value='/js/jquery-ui-1.8.13.custom.min.js' />"></script>
		<script type="text/javascript" src="<c:url value='/js/jquery-ui-timepicker-addon.js' />"></script>
		<script type="text/javascript" src="<c:url value='/js/welocally.js' />"></script>
		
		<!-- http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/jquery-ui.min.js -->
		<link type="text/css" href="<c:url value='/css/custom-theme/jquery-ui-1.8.13.custom.css' />" rel="stylesheet" media="screen" />
		<link href='http://fonts.googleapis.com/css?family=Ubuntu+Mono:400,700|Carme|Fjord+One' rel='stylesheet' type='text/css'>
		<link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css' />" type="text/css" media="screen, projection">
		<link rel="stylesheet" href="<c:url value='/css/blueprint/print.css' />" type="text/css" media="print">
		<!--[if lt IE 8]>
			<link rel="stylesheet" href="<c:url value='/css/blueprint/ie.css' />" type="text/css" media="screen, projection">
		<![endif]-->
		<link rel="stylesheet" href="<c:url value='/css/welocally.css' />" type="text/css"> 
		
		<link rel="shortcut icon" href="<c:url value='/images/we_16.png' />"/> 
	
	</head>

<c:set value="/images" var="imageUrl" scope="request"/>