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
		<div class="span-6">.</div>
		<div class="span-12">
		<h2>Give Award</h2>	
		<c:url value='/do/admin/award' var="awardAction"/>		
		<form:form modelAttribute="awardForm" action="${awardAction}" method="post">
		  	<fieldset>		
				<legend>Award Info</legend>
				<p>
					<form:label	for="username" path="username" cssErrorClass="error">User Name</form:label><br/>
					<form:input path="username" id="username"/> <form:errors path="username" class="error"/>			
				</p>				
				<p>
					<form:label	for="type" path="type" cssErrorClass="error">Award Type</form:label><br/>
					<form:input path="type" id="type"/> <form:errors path="type" class="error" />			
				</p>
				<p>
					<form:label	for="offerId" path="offerId" cssErrorClass="error">Offer Id</form:label><br/>
					<form:input path="offerId" id="offerId"/> <form:errors path="offerId" class="error" />			
				</p>				
				<p>
					<form:label	for="note" path="note" cssErrorClass="error">Note</form:label><br/>
					<form:textarea path="note" rows="1" cols="10" /> <form:errors path="note" class="error" />			
				</p>												
				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>
		</div>
		<div class="span-6">.</div>
	</div>	
	<div class="span-24 last">
	<c:forEach var="offer" items="${offers}">
		<c:if test="${not empty offer.couponCode}">
		<div class="span-24 last">
			<div class="strong-12 span-4">${offer.externalId}</div>
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