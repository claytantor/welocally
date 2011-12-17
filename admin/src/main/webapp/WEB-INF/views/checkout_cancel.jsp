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
<c:set var="pageTitle" value="Awards"/>
<jsp:include page="head.jsp"/>
<link href='http://fonts.googleapis.com/css?family=Open+Sans:700|Muli' rel='stylesheet' type='text/css'>
<style>
	h1 { font-family: 'Open Sans', sans-serif; font-size: 110%; }
	h2 { font-family: 'Open Sans', sans-serif; font-size: 80%; }
	body { font-size: 200%; }
	.comments {font-family: 'Muli', sans-serif; font-size: 80%;}
</style> 
<body>

<div class="container">
	<div class="span-24 last">
		<div class="span-24 last">
			<img src="<c:url value="${imageUrl}/header_logo.png"/>">
		</div>
		<div class="span-24 last">
			<h1>Not Ready?</h1>
			<h2>What was it that made you change your mind?</h2>
		</div>
		
		<div class="span-24 last comments">
			So it looks like you decided to cancel the process of setting up your subscription. 
			This makes us sad, but there might be a good reason, and it may be something 
			that we can change. If there is something we can do better make sure to 
			<a href="http://www.welocally.com/wordpress/?page_id=139"><strong>contact us</strong></a> and tell us why.  
		</div>
		<div class="span-24 last">
			<div class="span-16 align-right"><a href="http://welocally.com"><img border="0" src="<c:url value="${imageUrl}/back-arrow.png"/>"></a></div>		
		</div>
	</div>		
</div>

</body>
</html>	
	