<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="Rater Awards"/>
<jsp:include page="head.jsp"/>

<body>
<div class="container">
	<div class="span-24 last">
		<h2>awards for rater ${rater.userName}</h2>
		<div class="span-24 last">
			<c:forEach var="award" items="${raterAwards.awards}">
			<div class="grey-box span-4">
				<div class="span-4">	
					<img width="64" height="64" src="http://media.ratecred.com.s3.amazonaws.com/dev/images/awards/award_${award.awardType.keyname}.png" title="${award.notes}" />
	            </div>
				<div class="span-4">${award.awardType.name}</div>
			</div>
			</c:forEach>
		</div>
	</div>	
</div>

</body>
</html>