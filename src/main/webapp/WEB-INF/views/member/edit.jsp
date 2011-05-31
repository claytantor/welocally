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
<c:set var="pageTitle" value="Member Edit"/>
<jsp:include page="../head.jsp"/>
<body>

<div class="container">
	<div class="span-24">
		<h2>
		<c:if test="${not empty(memberForm.id)}">edit member</c:if>
		<c:if test="${empty(memberForm.id)}">create member</c:if>
		</h2>

		<c:url value='/admin/member' var="memberAction"/>		
		<form:form modelAttribute="networkMemberForm" action="${memberAction}" method="post">
		  	<fieldset>		
				<legend>Member Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<p>
					<form:label	for="type" path="type" cssErrorClass="error">Member Type</form:label><br/>
					<form:radiobutton path="type" value="PUBLISHER" label="Publisher" />
					<form:radiobutton path="type" value="AFFILIATE" label="Affiliate" />
					<form:radiobutton path="type" value="MERCHANT" label="Merchant" />
				</p> 
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Member Name</form:label><br/>
					<form:input path="name" id="name"/> <form:errors path="name" class="error" />			
				</p>	
				<p>
					<form:label	for="memberKey" path="memberKey" cssErrorClass="error">Member Key</form:label><br/>
					<form:input path="memberKey" id="memberKey"/> <form:errors path="memberKey" class="error"/>			
				</p>											
				<p>
					<form:label	for="description" path="description" cssErrorClass="error">Description</form:label><br/>
					<form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />			
				</p>	
				<p>
					<form:label	for="iconUrl" path="iconUrl" cssErrorClass="error">Icon Url</form:label><br/>
					<form:input path="iconUrl" id="iconUrl"/> <form:errors path="iconUrl" class="error"/>			
				</p>
				<p>
					<form:label	for="mapIconUrl" path="mapIconUrl" cssErrorClass="error">Map Icon Url</form:label><br/>
					<form:input path="mapIconUrl" id="mapIconUrl"/> <form:errors path="mapIconUrl" class="error"/>			
				</p>									
				<p>
					<form:label	for="primaryEmail" path="primaryEmail" cssErrorClass="error">Primary Email</form:label><br/>
					<form:input path="primaryEmail" id="primaryEmail"/> <form:errors path="primaryEmail" class="error"/>			
				</p>
				<p>
					<form:label	for="paypalEmail" path="paypalEmail" cssErrorClass="error">PayPal Email</form:label><br/>
					<form:input path="paypalEmail" id="paypalEmail"/> <form:errors path="paypalEmail" class="error"/>			
				</p>
																																																		
				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>

	</div>
</div>

</body>