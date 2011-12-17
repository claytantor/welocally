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
	<script type="text/javascript" src="http://cdn.simplegeo.com/js/1.3/simplegeo.places.jq.min.js"></script>
		
<style type="text/css">
body {
	width: 100%; 
	height: 100%; 
	padding: 0; 
	margin: 0; 
	background-color: white;
	font-family: verdana, arial, helvetica, sans-serif;
	font-style: normal;
}
</style>	
</head>
<script type="text/javascript">
var client = new simplegeo.PlacesClient('bb8HCTrBtUZs78EwUVJvXG6ugWkrjNvM');

$(document).ready(function() {
	var lat = 37.797933;
	var lon = -122.423937;
	 
	client.search(lat, lon, function(err, data) {
	    if (err) {
	        console.error(err);
	    } else {
	        console.log(JSON.stringify(data));
	    }
	});
});

</script>

<body>
<div>


</div>			
</body>         		
</html>
