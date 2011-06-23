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
		$("button").button();
	});
</script>
<div id="header" class="simple-box span-24">

		<sec:authorize ifAnyGranted="ROLE_USER">
			<div class="span-18">
				<img src="${imageUrl}/header_logo.png"/> 
			</div>
			<div class="padding-5 span-5 last">
				<div class="span-5"><sec:authentication property="principal.username" /></div>
				<div class="actions padding-5 span-5"><a href="<c:url value='/logout' />" class="button">logout</a>
					<a href="<c:url value='/home' />">home</a>
				</div>			
			</div>
		</sec:authorize>
		
		<sec:authorize  ifNotGranted="ROLE_USER">

		
		<div class="span-18"><img src="${imageUrl}/header_logo.png"/></div>
		<div class="actions padding-5 span-5 last">
			<a href="<c:url value='/home' />" class="button">member login</a>
		</div>
		</sec:authorize>	

</div>