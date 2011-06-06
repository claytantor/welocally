<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="Publisher Edit"/>
<jsp:include page="../head.jsp"/>
<body>

<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2>
		<c:if test="${not empty(publisherForm.id)}">edit publisher</c:if>
		<c:if test="${empty(publisherForm.id)}">create publisher</c:if>
		</h2>

		<c:url value='/admin/publisher' var="publisherAction"/>		
		<form:form modelAttribute="publisherForm" action="${publisherAction}" method="post">
		  	<fieldset>		
				<legend>Publisher Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<p>
					<form:label	for="siteName" path="siteName" cssErrorClass="error">Site Name</form:label><br/>
					<form:input path="siteName" id="siteName"/> <form:errors path="siteName" class="error" />			
				</p>	
				<p>
					<form:label	for="url" path="url" cssErrorClass="error">Site Url</form:label><br/>
					<form:input path="url" id="url"/> <form:errors path="url" class="error"/>			
				</p>							
				<p>
					<form:label	for="description" path="description" cssErrorClass="error">Description</form:label><br/>
					<form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />			
				</p>	
				<p>
					<form:label	for="summary" path="summary" cssErrorClass="error">Summary</form:label><br/>
					<form:textarea path="summary" rows="1" cols="10" /> <form:errors path="summary" class="error" />			
				</p>	
				<p>
					<form:label	for="monthlyPageviews" path="monthlyPageviews" cssErrorClass="error">Monthly Page Views</form:label><br/>
					<form:input path="monthlyPageviews" id="monthlyPageviews"/> <form:errors path="monthlyPageviews" class="error"/>			
				</p>
																																																		
				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>

	</div>
</div>

</body>