<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Place List"/>
<jsp:include page="../head.jsp"/>
<script>
	$(function() {
		$( "a", ".actions" ).button();
	});
</script>
<body>

<div class="container">
	<div class="span-24">
		<h2><a href="<c:url value='/' />">home</a> : list of places</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/admin/place' />" class="button">create</a>
		</div>		
		<div class="span-24 last">
			<c:forEach var="place" items="${places}">
			<div class="span-24 last">
				<div class="strong-12 span-1">${place.id}</div>
				<div class="strong-12 span-6"><a href="<c:url value='/admin/place/${place.id}'/>">${place.name}</a></div>
				<div class="span-10">
					<div class="span-10"><a href="${place.url}">${place.url}</a></div>
					<div class="span-10">${place.description}</div>
				</div>
				<div class="span-2 last">${place.address}</div>
			</div>
			</c:forEach>
		</div>	
	</div>
</div>

</body>
</html>