<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Place View"/>
<jsp:include page="../head.jsp"/>
<script>
	$(function() {
		$( "a", ".actions" ).button();
	});
</script>
<body>

<div class="container">
	<div class="span-24">
        <h2><a href="<c:url value='/publisher/place/list' />">all places</a> : ${place.url}</h2>
        <hr/>
        <div class="actions span-24 last">
            <a href="<c:url value='/publisher/place/edit/${place.id}' />" class="button">edit</a>
            <a href="<c:url value='/publisher/place/delete/${place.id}' />" class="button">delete</a>
        </div>
        <div class="span-24 last">
            <div class="strong-12 span-4">${place.id}</div>
            <div class="span-10">
                <div class="span-10"><a href="${place.url}">${place.name}</a></div>
                <div class="span-10">${place.description}</div>
            </div>
            <div class="span-2 last">${place.address}</div>
            <div class="span-2 last">lat:${place.latitude}, lng:${place.longitude}</div>
        </div>
	</div>		
</div>

</body>
</html>