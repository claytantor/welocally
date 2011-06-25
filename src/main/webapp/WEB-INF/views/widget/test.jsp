<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">

	<title>Ratecred Offer Widget</title>
 	<link rel="stylesheet" href="<c:url value='/css/ratecred.css' />" type="text/css">
 	
 	
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="<c:url value="/css/blueprint/ie.css"/>" type="text/css" media="screen, projection">
	<![endif]-->	
    
</head>
<body>
<div>RateCred Offer Widget Test</div>
<br>
<script type="text/javascript" src="<c:url value="/js/widget/publish_widget.js"/>"></script>
<script type="text/javascript">
var cfg = {
    url: location.href,
    title:"Luka's Taproom Is Delicious",
    publisher:2,
    place:1,
    summary:"Luka's Taproom serves a great selection of drinks and delicious food and is open late.",
    hostname: 'localhost:8080'
};
WELOCALLY.PublishWidget(cfg);
</script>	   
</body>
</html>