<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">

	<meta content="20" name="refresh" >
	<meta content="index,follow" name="robots" >
	<meta content="index,follow" name="googlebot" >
	<meta content="RateCred" name="generator" >
	<meta name="google-site-verification" content="u9YkTj5gr6aeYBst1Aac-B_5cCvJe_Ataauqep_EwEE" />	
	<meta name="description" content="ratecred widget" />
	
	<title>Ratecred Offer Widget</title>
 	<link rel="shortcut icon" href="/images/ic_fav_alpha_32.png" />
 	<link rel="stylesheet" href="<c:url value='/css/ratecred.css' />" type="text/css">
 	
 	
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="/styles/blueprint/ie.css" type="text/css" media="screen, projection">
	<![endif]-->	
    
</head>
<body>
RateCred Offer Widget
<script type="text/javascript" src="http://media.ratecred.com.s3.amazonaws.com/dev/widget/offer/offer_widget.js"></script>
<script type="text/javascript">
var cfg = {
		css: 'http://media.ratecred.com.s3.amazonaws.com/dev/widget/offer/ratecred_offer_widget.css',
		hostname: 'dev.ratecred.com',
		url: 'http://bayrater.blogspot.com/2011/01/adesso.html', 
		width: 675,
		height: 220,
		city: 'San%Francisco',
		state: 'CA',
		keywords: 'massage',
		view: 'offer_widget'
};
RATECRED.OfferWidget(cfg);
</script>	   
</body>
</html>