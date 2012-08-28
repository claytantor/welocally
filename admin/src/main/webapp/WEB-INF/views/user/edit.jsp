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
<style>
#wl_dialog_form input[type="text"] {  
	border: 1px solid #aaaaaa;
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px;
	padding: 4px;
	background-color: #f2f2f2;
	width: 100%; }
</style>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery('a','.actions').button();
	jQuery('input[type="submit"]').button();
});
</script>
	<div class="wl_dialog_form" id="wl_dialog_form">
		<h2>
		<c:if test="${not empty(userForm.id)}">edit user</c:if>
		<c:if test="${empty(userForm.id)}">create user</c:if>
		</h2>

		<c:url value='/signup/4_0' var="userAction"/>		
		<form:form modelAttribute="userPrincipalForm" action="${userAction}" method="post">
		  	<fieldset>		
				<legend>User Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<p>
					<form:label	for="username" path="username" cssErrorClass="error">User Name</form:label><br/>
					<form:input class="wl_dialog_form_field" path="username" id="username"/> <form:errors path="username" class="error" />			
				</p>
				<p>
					<form:label	for="email" path="email" cssErrorClass="error">Email</form:label><br/>
					<form:input path="email" id="email"/> <form:errors path="email" class="error" />			
				</p>					
				<p>
					<form:label	for="password" path="password" cssErrorClass="error">Password</form:label><br/>
					<form:input path="password" id="password"/> <form:errors path="password" class="error"/>			
				</p>
				<p>
					<form:label	for="credentialsExpired" path="credentialsExpired" cssErrorClass="error">Expired</form:label><br/>
					<form:checkbox path="credentialsExpired"/> <form:errors path="credentialsExpired" class="error"/>			
				</p>
				<p>
					<form:label	for="enabled" path="enabled" cssErrorClass="error">Enabled</form:label><br/>
					<form:checkbox path="enabled"/> <form:errors path="enabled" class="error"/>			
				</p>
				<p>
					<form:label	for="locked" path="locked" cssErrorClass="error">Locked</form:label><br/>
					<form:checkbox path="locked"/> <form:errors path="locked" class="error"/>			
				</p>
				<p>
					Current Roles:<c:forEach var="role" items="${userPrincipalForm.entity.roles}">${role.role}, </c:forEach>					
				</p>					
				<p>
					<form:label	for="roleNames" path="roleNames" cssErrorClass="error">Roles</form:label><br/>
					<form:select path="roleNames" multiple="true" items="${availableRoles}"/>
					<form:errors path="roleNames" class="error"/>
				</p>																																																							
				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>

	</div>
