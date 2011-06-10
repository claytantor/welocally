<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>${pageTitle}</title>
	<link rel="stylesheet" href="<c:url value='/css/blueprint/screen.css' />" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="<c:url value='/css/blueprint/print.css' />" type="text/css" media="print">
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="<c:url value='/css/blueprint/ie.css' />" type="text/css" media="screen, projection">
	<![endif]-->
	<link rel="stylesheet" href="<c:url value='/css/welocally.css' />" type="text/css">
	<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
  	
	
	<script type="text/javascript" src="<c:url value='/js/jquery-1.4.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui-1.8.2.custom.min.js' />"></script>
</head> 
<c:url value="/images" var="imageUrl" scope="request"/>