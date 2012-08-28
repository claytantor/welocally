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
<c:set var="pageTitle" value="Publisher Edit"/>
<jsp:include page="../head.jsp"/>
<script type="text/javascript">
$(document).ready(function() {

	$('#keygen').click(function(){
		$('#key').val(WELOCALLY.util.keyGenerator());
		return false;
	});
	
	$('#tokengen').click(function(){
		$('#jsonToken').val(WELOCALLY.util.tokenGenerator());
		return false;
	});
	
});
</script>
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2>
		<c:if test="${not empty(publisherForm.id)}"><a href="<c:url value='/home' />">${publisherForm.networkMember.name}</a> : edit publication</c:if>
		<c:if test="${empty(publisherForm.id)}">create publisher</c:if>
		</h2>
		<c:url value='/publisher/publisher' var="publisherAction"/>		
		<form:form modelAttribute="publisherForm" action="${publisherAction}" method="post">
		  	<fieldset>		
				<legend>Publication Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="networkMember.id" />
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Publisher Name</form:label><br/>
					<form:input path="name" id="name" class="textinput"/> <form:errors path="name" class="error" />			
				</p>	
				<p>
					<form:label	for="key" path="key" cssErrorClass="error">Site Key</form:label><br/>
					<form:input path="key" id="key" class="textinput"/> <form:errors path="key" class="error" />
					<span class="actions"><a id="keygen" href="">generate</a></span>			
				</p>	
				<p>
					<form:label	for="jsonToken" path="jsonToken" cssErrorClass="error">Site Token</form:label><br/>
					<form:input path="jsonToken" id="jsonToken" class="textinput"/> <form:errors path="jsonToken" class="error" />
					<span class="actions"><a id="tokengen" href="">generate</a></span>			
				</p>															
				<p>
					<form:label	for="description" path="description" cssErrorClass="error">Description</form:label><br/>
					<form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />			
				</p>	
				<sec:authorize ifAnyGranted="ROLE_ADMIN">
				<p>
					<form:label	for="subscriptionStatus" path="subscriptionStatus" cssErrorClass="error">Subscription Status</form:label><br/>
					<form:select path="subscriptionStatus">
					<form:option value="NONE" label="Select Status" />
					<form:options items="${subscriptionStatusTypes}" />
					</form:select>	
				</p>		
				</sec:authorize>		
																																																		
				<div class="actions">	
					<input type="submit" />
				</div>
			</fieldset>
		</form:form>

	</div>
</div>

</body>