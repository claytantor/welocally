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
<jsp:include page="../place/chooser.jsp"/>
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2>
		<c:if test="${not empty(merchantForm.id)}"><a href="<c:url value='/home' />">${merchantForm.networkMember.name}</a> : edit publication</c:if>
		<c:if test="${empty(merchantForm.id)}">create merchant</c:if>
		</h2>
		<c:url value='/merchant/merchant' var="merchantAction"/>		
		<form:form modelAttribute="merchantForm" action="${merchantAction}" method="post">
		  	<fieldset>		
				<legend>Merchant Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="networkMember.id" />
				<form:hidden id="place-id" path="place.id" />
                  <p>
                      <label for="place">Place:</label><br/>
                      <input id="place-name" value="${merchantForm.place.name}" class="textinput"/>
                  </p>  					
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Merchant Location Name</form:label><br/>
					<form:input path="name" id="name" class="textinput"/> <form:errors path="name" class="error" />
				</p>	
				<p>
					<form:label	for="description" path="description" cssErrorClass="error">Description</form:label><br/>
					<form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />
				</p>					
				<p>
					<form:label	for="url" path="url" cssErrorClass="error">Site Url</form:label><br/>
					<form:input path="url" id="url" class="textinput"/> <form:errors path="url" class="error"/>			
				</p>							
				<p>
					<form:label	for="facebookUrl" path="facebookUrl" cssErrorClass="error">Facebook Url</form:label><br/>
					<form:input path="facebookUrl" id="facebookUrl" class="textinput"/> <form:errors path="facebookUrl" class="error"/>			
				</p>																																																			
				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>

	</div>
</div>

</body>