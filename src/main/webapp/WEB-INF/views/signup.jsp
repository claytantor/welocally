<%@ page
	contentType="text/html; charset=iso-8859-1"
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="WeLocally Sign Up"/>
<jsp:include page="head.jsp"/>
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="header.jsp"/>
	</div>
	<div class="span-24">
		<h1>sign up!</h1>
		<hr/>
        <h2>Become part of the we!</h2>

		<div class="span-24 last comments" style="margin-bottom: 10px">
			You are firmly entrenched in our hearts and we hope you will 
			to take  the relationship the next step and get involved 
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