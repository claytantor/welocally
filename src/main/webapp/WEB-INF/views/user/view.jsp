<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Publisher View"/>
<jsp:include page="../head.jsp"/>
<script>
	$(function() {
		$( "a", ".actions" ).button();
	});
</script>
<body>

<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2><a href="<c:url value='/admin/user/list' />">all users</a> : ${userPrincipal.username}</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/admin/user/edit/${userPrincipal.id}' />" class="button">edit</a>
			<a href="<c:url value='/admin/user/delete/${userPrincipal.id}' />" class="button">delete</a>
		</div>
		<div class="span-24 last">
			<div class="strong-12 span-4">${userPrincipal.id}</div>
			<div class="strong-12 span-4">
			<c:forEach var="role" items="${userPrincipal.roles}">
			 ${role.role},
			</c:forEach>
			</div>
		</div>
	</div>
</div>

</body>