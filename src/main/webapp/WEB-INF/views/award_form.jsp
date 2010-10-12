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
	$("#username").autocomplete({
	    source: dataUserNames
	});
	$("#type").autocomplete({
	    source: dataAwardTypes
	});
});
</script>
<body>
<div class="container">
	<div class="span-12 last">
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
					<form:label	for="note" path="note" cssErrorClass="error">Note</form:label><br/>
					<form:textarea path="note" rows="3" cols="20" /> <form:errors path="note" class="error" />			
				</p>
					
							
				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>
	</div>	
	
</div>

</body>
</html>