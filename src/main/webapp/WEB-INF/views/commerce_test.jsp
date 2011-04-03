<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<meta content="index,follow" name="robots" >
	<meta content="index,follow" name="googlebot" >
	<meta content="RateCred" name="generator" >
	<meta name="google-site-verification" content="u9YkTj5gr6aeYBst1Aac-B_5cCvJe_Ataauqep_EwEE" />	
	<meta name="description" content="ratecred widget" />
	
	<title>Commerce Test</title>
 	<link rel="shortcut icon" href="/images/ic_fav_alpha_32.png" />
 	
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="/styles/blueprint/ie.css" type="text/css" media="screen, projection">
	<![endif]-->	
        
    <style type="text/css">
.button_holder {
	margin: 30px;
} 

.buy_button {
	width: 100px;
	height: 30px;
	color: white;
	font: 0.8em verdana, arial, helvetica, sans-serif;
	background-color: gray;
	border: 1px;
	border-color: black;
}      
    </style>    
    
    <script type="text/javascript" src="<c:url value='/js/jquery-1.4.2.min.js' />"></script>
    
</head>
<body>
			<div class="button_holder">
			<form id="form_Pasta_Pelican_Waterfront" method="POST" action="http://payments-sandbox.amazon.com/checkout/A1K9S6OUGRSQ4N"> 
				<input type="hidden" name="aws_access_key_id" value="1H8AMWP9WTKKHVMV5782" /> 
				<input type="hidden" name="cart_custom_data" value="" /> 
				<input type="hidden" name="currency_code" value="USD" /> 
				<input type="hidden" name="item_custom_data" value="" /> 
				<input type="hidden" name="item_description_1" value="" /> 
				<input type="hidden" name="item_merchant_id_1" value="A1K9S6OUGRSQ4N" /> 
				<input type="hidden" name="item_price_1" value="20.99" /> 
				<input type="hidden" name="item_quantity_1" value="1" /> 
				<input type="hidden" name="item_sku_1" value="Pasta_Pelican_Waterfront" /> 
				<input type="hidden" name="item_title_1" value="Pasta Pelican Waterfront Restaurant Lounge 20.99 For 2 Dining Spectaculars and 12 Buy One Get One Entrees" /> 
				<input type="hidden" name="merchant_signature" value="yAZzfvVPGV/672p1MwgFZ/6iNjU=" /> 
				<%--<input type="image" src="https://payments.amazon.com/gp/cba/button?ie=UTF8&color=orange&background=white&size=medium" alt="Checkout with Amazon Payments" /> --%> 
			</form>
			</div> 
			<div class="buy_button" onclick="javascript:$('#form_Pasta_Pelican_Waterfront').submit()">buy this offer!</div>		

</body>
</html>