<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<c:set var="pageTitle" value="Publisher View"/>
<jsp:include page="../head.jsp"/>

<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<div class="span-24 last">
			<h2><a href="<c:url value='/publisher/${site.publisher.id}' />">${site.publisher.name}</a> : ${site.name}</h2>
		</div>
		<div class="bottom-10 frame span-24 last">
			<div class="actions span-24 last" >
				<div class="actions span-12" style="text-align:left">
					<h3>Site</h3>
				</div>
				<sec:authorize ifAllGranted="ROLE_ADMIN">
				<div class="actions span-12 last" style="text-align:right">
					<a href="<c:url value='/site/edit/${site.id}' />" class="button">edit</a>
					<a href="<c:url value='/site/delete/${site.id}' />" class="button">delete</a>
				</div>
				</sec:authorize>
							
			</div>
			<div class="span-24 last" >
				<div style="font-size: 1.4em;">${site.name}</div>
				<div><a href="${site.url}" target="_new">${site.url}</a></div>
			</div>
			
		</div>
	</div>
</div>
</body>