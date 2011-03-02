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
<c:set var="pageTitle" value="Admin Home"/>
<jsp:include page="head.jsp"/>
<body>

<div class="container">
	<div class="span-24">
		<h2>admin home</h2>
		<hr/>
		<div class="text-14">Awards</div>
		<div><a href="<c:url value='/do/admin/award'/>">give award</a></div>
		<div><a href="<c:url value='/do/admin/award/custom'/>">give custom award</a></div>
		<div><a href="<c:url value='/do/admin/award/type'/>">create award type</a></div>
		<div class="text-14">Users</div>
		<div><a href="<c:url value='/do/admin/rater/status/USER'/>">users</a></div>
		<div class="text-14">Places</div>
		<div><a href="<c:url value='/do/admin/place'/>">edit place</a></div>
	</div>
</div>

</body>
</html>