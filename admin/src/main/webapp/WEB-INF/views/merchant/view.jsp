<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Merchant View"/>
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
		<h2><a href="<c:url value="/association/merchant/list"/>">${member.name}</a> : ${merchant.name}</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/association/merchant/edit/${merchant.id}' />" class="button">edit</a>
			<a href="<c:url value='/association/merchant/delete/${merchant.id}' />" class="button">delete</a>
		</div>
		<div class="span-24 last">
			<div class="strong-12 span-4">${merchant.id}</div>
			<div class="span-19">
				<div class="span-19"><a href="${merchant.url}">${merchant.url}</a></div>
				<div class="span-19">${merchant.description}</div>
				<%--<div class="span-19">${merchant.summary}</div>--%>
			</div>
		</div>
		<div class="span-24 last">
			<h2>place</h2>
			</hr>
		</div>
		<c:set var="place" value="${merchant.place}" scope="request"/>
		<div class="span-24 last">
            <jsp:include page="../place/detail.jsp"/>
        </div>		
	</div>
</div>

</body>