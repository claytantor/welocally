<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="Place Edit"/>
<jsp:include page="../head.jsp"/>
<body>

<div class="container">
	<div class="span-24">
        <h2>
        <c:if test="${not empty(articleForm.id)}">edit article</c:if>
        <c:if test="${empty(articleForm.id)}">create article</c:if>
        </h2>

		<c:url value='/admin/place' var="placeAction"/>
		<form:form modelAttribute="placeForm" action="${placeAction}" method="post">
		  	<fieldset>		
				<legend>Place Info</legend>
                <form:hidden path="id" />
                <form:hidden path="version" />
                  <p>
                      <form:label for="name" path="name" cssErrorClass="error">Name</form:label><br/>
                      <form:input path="name" id="name" size="50"/> <form:errors path="name" class="error" />
                  </p>
                  <p>
                      <form:label for="address" path="address" cssErrorClass="error">Address</form:label><br/>
                      <form:input path="address" id="address" size="50"/> <form:errors path="address" class="error" />
                  </p>
                  <p>
                      <form:label for="city" path="city" cssErrorClass="error">City</form:label><br/>
                      <form:input path="city" id="city" size="50"/> <form:errors path="city" class="error" />
                  </p>
                  <p>
                      <form:label for="state" path="state" cssErrorClass="error">State</form:label><br/>
                      <form:input path="state" id="state" size="50"/> <form:errors path="state" class="error" />
                  </p>
                  <p>
                      <form:label for="zip" path="zip" cssErrorClass="error">ZIP</form:label><br/>
                      <form:input path="zip" id="zip" size="50"/> <form:errors path="zip" class="error" />
                  </p>
                  <p>
                      <form:label for="url" path="url" cssErrorClass="error">URL</form:label><br/>
                      <form:input path="url" id="url"/> <form:errors path="url" class="error" />
                  </p>
                  <p>
                      <form:label for="description" path="description" cssErrorClass="error">Description</form:label><br/>
                      <form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />
                  </p>
				<p>
					<form:label	for="phone" path="phone" cssErrorClass="error">Phone</form:label><br/>
					<form:input path="phone" id="phone" size="50"/> <form:errors path="phone" class="error" />
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