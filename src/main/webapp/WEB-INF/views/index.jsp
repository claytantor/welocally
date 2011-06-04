<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
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
		<h1>admin home</h1>
		<hr/>
		<div class="span-24">
			<h2>network member</h2>
			<div><a href="<c:url value='/admin/member/list'/>">list all</a></div>
			<div><a href="<c:url value='/admin/member'/>">create</a></div>
		</div>		
		<div class="span-24">
			<h2>publisher</h2>
			<div><a href="<c:url value='/admin/publisher/list'/>">list all</a></div>
			<div><a href="<c:url value='/admin/publisher'/>">create</a></div>
		</div>
		<div class="span-24">
			<h2>event</h2>
			<div><a href="<c:url value='/admin/event/list'/>">list all</a></div>
			<div><a href="<c:url value='/admin/event'/>">create</a></div>
		</div>
	</div>
</div>

</body>
</html>