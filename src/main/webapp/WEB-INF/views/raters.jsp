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
<html>
<c:set var="pageTitle" value="Awards"/>
<jsp:include page="head.jsp"/>
<body>

<div class="container">
	<div class="span-12 last">
		<h2>users</h2>	
		<c:if test="${not empty raters}">
		<div class="padding-5 span-12">
			<c:forEach items="${raters}" var="rater" varStatus="status" begin="0">
				<div class="green-box padding-5 span-10">
					<div class="span-1">
						${rater.score}
					</div>	
					<div class="span-1">
							<c:choose>
				                    <c:when test="${not empty rater.raterImage && rater.raterImage.type == 'S3REPO' }">
				                        <img src="${applicationProperties.imageRepostoryUrl}/${rater.raterImage.filename}" width="32" height="32">
				                    </c:when>
				                    <c:when test="${not empty rater.raterImage && rater.raterImage.type == 'TWITTER' }">
				                        <img src="${rater.raterImage.filename}" width="32" height="32">
				                    </c:when>                
				                    <c:otherwise>
				                        <img src="/images/generic_32.png" width="32" height="32"/>
				                    </c:otherwise>
								</c:choose>
					</div>
					<div class="span-3">
						<span><a href="http://ratecred.com/rater/profile/${rater.id}">${rater.userName}</a></span>
					</div>
					<div class="span-3 last">
						<c:url var="awardsUrl" value="/do/admin/rules/target/${rater.userName}" />
						<span><a class="button" href="${awardsUrl}"/>proc awards</a></span>
					</div>						
				</div>
			</c:forEach>
		</div>


</c:if>		
	</div>		
</div>

</body>
</html>