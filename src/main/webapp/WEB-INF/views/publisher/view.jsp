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
<c:set var="pageTitle" value="Publisher View"/>
<jsp:include page="../head.jsp"/>
<script>
	$(function() {
		$( "a", ".actions" ).button();
		$( "a", ".actions" ).click(function() { return false; });
	});
	</script>
<body>

<div class="container">
	<div class="span-24">
		<h2>${publisher.siteName}</h2>
		<hr/>
		<div class="actions span-24 last"><a href="" class="button">edit</a><a href="" class="button">delete</a></div>
		<div class="span-24 last">
			<div class="strong-12 span-4">${publisher.id}</div>
			<div class="span-10">
				<div class="span-10"><a href="${publisher.url}">${publisher.url}</a></div>
				<div class="span-10">${publisher.description}</div>				
			</div>
			<div class="span-2 last">${publisher.monthlyPageviews}</div>	
		</div>
	</div>
</div>

</body>