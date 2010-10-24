<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="Give Award"/>
<jsp:include page="head.jsp"/>
<script>
$(document).ready(function(){
	var dataUserNames = "${users}".split(",");
	var dataAwardTypes = "${awardTypes}".split(",");
	var dataOfferIds = "${offerIds}".split(",");
	$("#username").autocomplete({
	    source: dataUserNames
	});
	$("#type").autocomplete({
	    source: dataAwardTypes
	});
	$("#offerId").autocomplete({
	    source: dataOfferIds
	});
});
</script>
<body>
<div class="container">
	<div class="span-24 last">
	<c:forEach var="offer" items="${offers}">
		<c:if test="${offer.visible == true}">
		<div class="span-24 last">
			<div class="strong-16 span-2">${offer.externalId}</div>
			<div class="strong-12 span-4">${offer.couponCode}</div>
			<div class="strong-8 span-4">${offer.programName}</div>
			<div class="span-10">
				<div class="span-10"><a href="${offer.url}">${offer.name}</a></div>
				<div class="span-10">${offer.description}</div>				
			</div>
			<div class="span-2 last">${offer.expireDateString}</div>	
		</div>
		</c:if>
	</c:forEach>
	</div>	
</div>

</body>
</html>