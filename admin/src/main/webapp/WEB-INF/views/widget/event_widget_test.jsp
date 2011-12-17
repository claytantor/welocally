<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">

	<title>Ratecred Event Publish Widget</title>
 	<link rel="stylesheet" href="<c:url value='/css/ratecred.css' />" type="text/css">
 	
 	
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="<c:url value="/css/blueprint/ie.css"/>" type="text/css" media="screen, projection">
	<![endif]-->	
    
</head>
<body>
<div>RateCred Event Publish Widget Test</div>
<br>
<script type="text/javascript" src="http://localhost:8080/rcadmin/js/widget/event_widget.js"></script>
<script type="text/javascript">
var cfg = {
url: "http://samsite.com:8080/rcadmin/widget/test/event?pizzaParty",
title:"Pizza Party",
hostname:"localhost:8080",
publisher:1,
place:2,
timeStarts:1310713200000,
timeEnds:1310799600000,
summary:"kids pizza party free pizza beer and wine etc"
};
WELOCALLY.PublishWidget(cfg);
</script>

</body>
</html>