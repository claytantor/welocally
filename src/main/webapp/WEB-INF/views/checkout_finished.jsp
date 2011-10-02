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
			<h1>Yipeee!</h1>
			<h2>We are super psyched that you are a subscriber!</h2>
		</div>
		
		<div class="span-24 last comments" style="margin-bottom: 10px">
			Although you are firmly entrenched in our hearts we still hope you will to take 
			the relationship the next step and get involved 
			in our community. Its super easy, you meet other people who share your 
			interests, and you can help us make the product better!  
		</div>
		<div class="span-24 last comments">
			<ul>
				<li><a href="http://eepurl.com/fBdKI" target="_new">Welocally Mailing List</a></li>
				<li><a href="http://www.welocally.com/wordpress/?page_id=104" target="_new">Welocally Places User Reference</a></li>
				<li><a href="https://groups.google.com/group/welocally-places-wpplugin" target="_new">Welocally Places Group</a></li>
			</ul>
		</div>
		
		<div class="span-24 last">
			<div class="span-16 align-right"><a href="http://welocally.com"><img border="0" src="<c:url value="${imageUrl}/back-arrow.png"/>"></a></div>		
		</div>
	</div>		
</div>

</body>
</html>	
	