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
<c:set var="pageTitle" value="Give Custom Award"/>
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
	<div class="span-24 last">
		<div class="span-6">.</div>
		<div class="span-12">
		<h2>Give Custom Award</h2>	
		<c:url value='/do/admin/award/custom' var="awardAction"/>		
		<form:form modelAttribute="customAwardForm" action="${awardAction}" method="post">
		  	<fieldset>		
				<legend>Custom Award Info</legend>
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
					<form:textarea path="note" rows="1" cols="10" /> <form:errors path="note" class="error" />			
				</p>				
				<%--
				private String externalId;
				private String externalSource;
				private String programId;
				private String programName;
				private String name;
				private String couponCode;
				private String description;
				private String url;
				private String beginDateString;
				private String expireDateString;				
				 --%>
				<p>
					<form:label	for="giveAward" path="giveAward" cssErrorClass="error">Give Award</form:label><br/>
					<form:radiobutton path="giveAward" value="true" label="true" />
					<form:radiobutton path="giveAward" value="false" label="false" />
				</p>
				<p>
				<p>
					<form:label	for="externalId" path="externalId" cssErrorClass="error">Offer External Id</form:label><br/>
					<form:input path="externalId" id="externalId"/> <form:errors path="externalId" class="error" />			
				</p>
				<p>
					<form:label	for="externalSource" path="externalSource" cssErrorClass="error">Offer External Source</form:label><br/>
					<form:input path="externalSource" id="externalSource"/> <form:errors path="externalSource" class="error" />			
				</p>
				<p>
					<form:label	for="programId" path="programId" cssErrorClass="error">Offer Program Id</form:label><br/>
					<form:input path="programId" id="programId"/> <form:errors path="programId" class="error" />			
				</p>	
				<p>
					<form:label	for="programName" path="programName" cssErrorClass="error">Offer Program Name</form:label><br/>
					<form:input path="programName" id="programName"/> <form:errors path="programName" class="error" />			
				</p>
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Offer Name</form:label><br/>
					<form:input path="name" id="name"/> <form:errors path="name" class="error" />			
				</p>
				<p>
					<form:label	for="couponCode" path="couponCode" cssErrorClass="error">Coupon Code</form:label><br/>
					<form:input path="couponCode" id="couponCode"/> <form:errors path="couponCode" class="error" />			
				</p>
				<p>
					<form:label	for="description" path="description" cssErrorClass="error">Description</form:label><br/>
					<form:input path="description" id="description"/> <form:errors path="couponCode" class="error" />			
				</p>
				<p>
					<form:label	for="url" path="url" cssErrorClass="error">Offer URL</form:label><br/>
					<form:input path="url" id="url"/> <form:errors path="url" class="error" />			
				</p>	
				<p>
					<form:label	for="beginDateString" path="beginDateString" cssErrorClass="error">Offer BEGINS (YYYY-MM-DD)</form:label><br/>
					<form:input path="beginDateString" id="beginDateString"/> <form:errors path="beginDateString" class="error" />			
				</p>
					<form:label	for="expireDateString" path="expireDateString" cssErrorClass="error">Offer ENDS (YYYY-MM-DD)</form:label><br/>
					<form:input path="expireDateString" id="expireDateString"/> <form:errors path="expireDateString" class="error" />			
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