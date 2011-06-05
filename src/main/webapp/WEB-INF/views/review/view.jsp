<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Review View"/>
<jsp:include page="../head.jsp"/>
<script>
	$(function() {
		$( "a", ".actions" ).button();
	});
</script>
<body>

<div class="container">
	<div class="span-24">
		<h2><a href="<c:url value='/admin/review/list' />">all reviews</a> : ${review.url}</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/admin/review/edit/${review.id}' />" class="button">edit</a>
			<a href="<c:url value='/admin/review/delete/${review.id}' />" class="button">delete</a>
		</div>
		<div class="span-24 last">
			<div class="strong-12 span-4">${review.id}</div>
			<div class="span-10">
				<div class="span-10"><a href="${review.url}">${review.url}</a></div>
				<div class="span-10">${review.description}</div>
			</div>
			<div class="span-2 last">${review.summary}</div>
		</div>
	</div>
</div>

</body>