<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Merchant List"/>
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
        <h2><a href="<c:url value="/association/merchant/list"/>">${member.name}</a> : list of merchants</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/association/merchant'/>" class="button">create</a>
		</div>		
		<div class="span-24 last">
			<c:forEach var="merchant" items="${merchants}">
			<div class="span-24 last">
				<div class="strong-12 span-1">${merchant.id}</div>
				<div class="strong-12 span-6"><a href="<c:url value='/association/merchant/${merchant.id}'/>">${merchant.name}</a></div>
				<div class="span-10">
					<div class="span-10"><a href="${merchant.url}">${merchant.url}</a></div>
					<div class="span-10">${merchant.description}</div>
				</div>
			</div>
			</c:forEach>
		</div>	
	</div>
</div>

</body>
</html>