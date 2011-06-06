<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script>
	$(function() {
		$( "a", ".actions" ).button();
	});
</script>
<div id="header" class="grey-box span-24">
		<div class="span-18">HEADER</div>
		<div class="actions padding-5 span-5 last">
		<sec:authorize ifAnyGranted="ROLE_USER">
		<sec:authentication property="principal.username" />
		<a href="<c:url value='/logout' />" class="button">logout</a><a href="<c:url value='/' />">home</a>
		</sec:authorize>
		<sec:authorize  ifNotGranted="ROLE_USER">
		<a href="<c:url value='/home' />" class="button">member login</a>
		</sec:authorize>		
		</div>	 
</div>