<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Event View"/>
<jsp:include page="../head.jsp"/>
<script>
	$(function() {
		$( "a", ".actions" ).button();
	});
</script>
<body>

<div class="container">
	<div class="span-24">
		<h2><a href="<c:url value='/home' />">home</a> : <a href="<c:url value='/publisher/event/list' />">all events</a> : ${event.name}</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/publisher/event/edit/${event.id}' />" class="button">edit</a>
			<a href="<c:url value='/publisher/event/delete/${event.id}' />" class="button">delete</a>
		</div>
		<div class="span-24 last">
			<div class="strong-12 span-4">${event.id}</div>
			<div class="span-10">
				<div class="span-10"><a href="${event.url}">${event.url}</a></div>
				<div class="span-10">${event.description}</div>
			</div>
			<div class="span-2 last">${event.whenText}</div>
		</div>
	</div>
</div>

</body>