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
<c:set var="pageTitle" value="Order View"/>
<jsp:include page="../head.jsp"/>

<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div>
	<h2>
		<c:url value='/publisher/${order.owner.id}' var="publisherUrl"/>
		<a href="${publisherUrl}">${order.owner.name}</a>
	</h2>
	</div>
	<div class="span-24">
		<c:set var="order" scope="request" value="${order}" />	
		<jsp:include page="../order/detail.jsp" flush="true"/>
	</div>
</div>
</body>