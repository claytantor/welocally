<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<c:set var="pageTitle" value="Place Finder"/>
<jsp:include page="../head.jsp"/>
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<sec:authorize ifAllGranted="ROLE_ADMIN">
		Authorized
		<div>
		<script type="text/javascript">
		var cfg = { 
				placehoundPath: 'http://placehound.com',
				showShare: true,
				endpoint: 'http://gaudi.welocally.com'
				imagePath:'http://placehound.com/images/marker_all_base.png'	
			};
			var addPlaceWidget = new WELOCALLY_AddPlaceWidget(cfg).init();
		</script>
		</div>
			
		</sec:authorize>
		
		<sec:authorize ifNotGranted="ROLE_ADMIN">
		Not Authorized.
		</sec:authorize>	
			
	</div>
</div>
</body>
</html>


