<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="Review Edit"/>
<jsp:include page="../head.jsp"/>
<body>

<div class="container">
	<div class="span-24">
		<h2>
		<c:if test="${not empty(reviewForm.id)}">edit review</c:if>
		<c:if test="${empty(reviewForm.id)}">create review</c:if>
		</h2>

		<c:url value='/admin/review' var="reviewAction"/>
		<form:form modelAttribute="reviewForm" action="${reviewAction}" method="post">
		  	<fieldset>		
				<legend>Review Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
                  <p>
                      <form:label for="url" path="url" cssErrorClass="error">URL:</form:label><br/>
                      <form:input path="url" id="url"/> <form:errors path="url" class="error" />
                  </p>
<%--
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Name:</form:label><br/>
					<form:input path="name" id="name"/> <form:errors path="name" class="error" />
				</p>
				<p>
					<form:label	for="url" path="url" cssErrorClass="error">URL:</form:label><br/>
					<form:input path="url" id="url"/> <form:errors path="url" class="error"/>			
				</p>							
--%>
                  <p>
                      <form:label for="description" path="description" cssErrorClass="error">Description</form:label><br/>
                      <form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />
                  </p>
                  <p>
                      <form:label for="summary" path="summary" cssErrorClass="error">Summary</form:label><br/>
                      <form:textarea path="summary" rows="1" cols="10" /> <form:errors path="summary" class="error" />
                  </p>

				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>

	</div>
</div>

</body>