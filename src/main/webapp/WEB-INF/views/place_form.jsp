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

<body>
<div class="container">
	<div class="span-24 last">
		<div class="span-6">.</div>
		<div class="span-12">
		<h2>Update Place</h2>	
		<c:url value='/do/admin/place' var="placeAction"/>		
		<form:form modelAttribute="placeForm" action="${placeAction}" method="post">
		  	<fieldset>		
				<legend>Place Info</legend>
				<p>
					<form:label	for="placeId" path="placeId" cssErrorClass="error">Place Id</form:label><br/>
					<form:input path="placeId" id="placeId"/> <form:errors path="placeId" class="error"/>			
				</p>				
				<p>
					<form:label	for="businessServices" path="businessServices" cssErrorClass="error">Business Services (true/false)</form:label><br/>
					<form:input path="businessServices" id="businessServices"/> <form:errors path="businessServices" class="error" />			
				</p>
				<p>
					<form:label	for="email" path="email" cssErrorClass="error">Email</form:label><br/>
					<form:input path="email" id="email" size="50"/> <form:errors path="email" class="error" />			
				</p>															
				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>
		</div>
		<div class="span-6">.</div>
	</div>	
	
</div>

</body>
</html>