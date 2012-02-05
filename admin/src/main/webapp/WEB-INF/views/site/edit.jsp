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
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<c:set var="pageTitle" value="Site Edit"/>
<jsp:include page="../head.jsp"/>
<script type="text/javascript">
$(document).ready(function() {

	
	
});
</script>
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2>
		<c:if test="${not empty(siteForm.id)}">edit site for ${siteForm.publisher.name}</c:if>
		<c:if test="${empty(siteForm.id)}">create site for ${siteForm.publisher.name}</c:if>
		</h2>
		<c:url value='/site' var="siteAction"/>		
		<form:form modelAttribute="siteForm" action="${siteAction}" method="post">
		  	<fieldset>		
				<legend>Site Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="publisher.id" />
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Site Name</form:label><br/>
					<form:input path="name" id="name" class="textinput"/> <form:errors path="name" class="error" />			
				</p>															
				<p>
					<form:label	for="url" path="url" cssErrorClass="error">Site Url</form:label><br/>
					<form:input path="url" id="url" class="textinput"/> <form:errors path="url" class="error" />			
				</p>		

				<div class="span-24 last">
					<div class="span-4"><form:label for="verified" path="verified" cssErrorClass="error">Site Is Verified</form:label><br/>
					<form:checkbox path="verified"/><form:errors path="verified" class="error" /></div>			
					<div class="span-4"><form:label for="active" path="active" cssErrorClass="error">Site Is Active</form:label><br/>
					<form:checkbox path="active"/><form:errors path="active" class="error" /></div>	
				</div>		
				<p>&nbsp;</p>													
				<p>
					<form:label	for="description" path="description" cssErrorClass="error">Description</form:label><br/>
					<form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />			
				</p>									
				<p>
					<form:label	for="notes" path="notes" cssErrorClass="error">Notes</form:label><br/>
					<form:textarea path="notes" rows="1" cols="10" /> <form:errors path="notes" class="error" />			
				</p>				
																																																		
				<div class="actions">	
					<input type="submit" />
				</div>
			</fieldset>
		</form:form>

	</div>
</div>

</body>