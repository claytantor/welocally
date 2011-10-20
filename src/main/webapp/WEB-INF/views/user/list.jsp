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
<c:set var="pageTitle" value="User List"/>
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
		<h2><a href="<c:url value='/home' />">home</a> : list of users</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/admin/user' />" class="button">create</a>
		</div>		
		<div class="span-24 last">
			<div class="span-24 last">
				<div class="strong-10 span-1">id</div>
				<div class="strong-10 span-8">username</div>
				<div class="strong-10 span-2">expired</div>
				<div class="strong-10 span-2">enabled</div>
				<div class="strong-10 span-2">locked</div>	
				<div class="strong-10 span-4">roles</div>				
			</div>
			<c:forEach var="user" items="${userPrincipals}"  varStatus="status">
			<c:choose>
			    <c:when test='${(status.index)%2 eq 0}'>
			      <c:set var="rowColor" value="even-row" scope="page"/>
			    </c:when>
			    <c:otherwise>
			      <c:set var="rowColor" value="odd-row" scope="page"/>
			    </c:otherwise>
		  	</c:choose>
			<div class="${rowColor} span-24 last">
				<div class="text-10 span-1">${user.id}</div>
				<div class="text-10  span-8"><a href="<c:url value='/admin/user/${user.id}'/>">${user.username}</a></div>
				<div class="text-10  span-2">${user.credentialsExpired}</div>
				<div class="text-10  span-2">${user.enabled}</div>
				<div class="text-10  span-2">${user.locked}</div>
				<div class="text-10  span-4"><c:forEach var="role" items="${user.roles}">${role.role}, </c:forEach></div>
			</div>
			</c:forEach>
		</div>	
	</div>
</div>

</body>
</html>