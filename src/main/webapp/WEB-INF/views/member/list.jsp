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
<c:set var="pageTitle" value="Publisher List"/>
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
		<h2><a href="<c:url value='/home' />">home</a> : list of members</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/admin/member' />" class="button">create</a>
		</div>		
		<div class="span-24 last">
			<c:forEach var="member" items="${members}">

			<div class="span-24 last">
				<div class="strong-12 span-1">${member.id}</div>
				<div class="strong-12 span-4"><a href="<c:url value='/admin/member/${member.id}'/>">${member.name}</a></div>
				<div class="span-8">
					<div class="span-8">${member.description}</div>	
					<div class="span-8"><a href="${member.iconUrl}">${member.iconUrl}</a></div>
					<div class="span-8"><a href="${member.mapIconUrl}">${member.mapIconUrl}</a></div>
				</div>			
				<div class="span-4 last">
					<div class="span-4 last">${member.primaryEmail}</div>
					<div class="span-4 last">${member.paypalEmail}</div>	
				</div>
			</div>
			</c:forEach>
		</div>	
	</div>
</div>

</body>
</html>