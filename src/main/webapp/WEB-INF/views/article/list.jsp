<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Article List"/>
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
		<h2><a href="<c:url value='/publisher/publisher/${publisher.id}' />">${publisher.siteName}</a> : list of articles</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/publisher/article?publisherId=${publisher.id}' />" class="button">create</a>
		</div>		
		<div class="span-24 last">
			<c:forEach var="article" items="${articles}">
			<div class="span-24 last">
				<div class="strong-12 span-1">${article.id}</div>
				<div class="strong-12 span-6"><a href="<c:url value='/publisher/article/${article.id}?publisherId=${publisher.id}'/>">${article.name}</a></div>
				<div class="span-10">
					<div class="span-10"><a href="${article.url}">${article.url}</a></div>
					<div class="span-10">${article.summary}</div>
				</div>
			</div>
			</c:forEach>
		</div>	
	</div>
</div>

</body>
</html>