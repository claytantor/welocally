<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<jsp:include page="../head.jsp"/>
<body style="padding:0px; margin:0px">
<div style="width:300px;height:200px;">
<div>
		<script type="text/javascript" charset="utf-8">
		var cfg = { 
			id:  '${id}', 
			imagePath:'<c:url value='/css/welocally-places-developer/images/marker_all_base.png' />',	
			endpoint: '${config.ajaxServerEndpoint}',
			requestPath: '/geodb/place/3_0/user/',
			showShare: false,
			key: '${key}',
			token: '${token}',			
			placehoundPath: 'http://placehound.com'
		};			    		
		var placeWidget = 
			  new WELOCALLY_PlaceWidget(cfg)
		  		.init();

		placeWidget.loadRemote(); 	
		
		 		
		</script>
		</div>
</div>
</body>
</html>