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
<c:set var="pageTitle" value="Expiring Publishers"/>
<jsp:include page="../head.jsp"/>
<body>

<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2><a href="<c:url value='/' />">home</a> : list of expiring publishers</h2>
		<hr/>
<%--
		<div class="actions span-24 last">
			<a href="<c:url value='/publisher/publisher' />" class="button">create</a>
		</div>		
--%>
		<div class="span-24 last">
            <div class="span-24 last" style="margin-bottom:10px">
                <div class="strong-12 span-1">Id</div>
                <div class="strong-12 span-6">Name</div>
                <div class="span-14">
                    <div class="span-10">URL</div>
                    <div class="span-2">Key</div>
                    <div class="span-2">SimpleGeo Token</div>
                </div>
                <div class="span-2 last">Service End Date</div>
            </div>
			<c:forEach var="publisher" items="${publishers}">
			<div class="span-24 last">
				<div class="strong-12 span-1">${publisher.id}</div>
				<div class="strong-12 span-6"><a href="<c:url value='/publisher/publisher/${publisher.id}'/>">${publisher.siteName}</a></div>
				<div class="span-14">
					<div class="span-10"><a href="${publisher.url}">${publisher.url}</a></div>
					<div class="span-2">${publisher.key}</div>
					<div class="span-2">${publisher.simpleGeoJsonToken}</div>
				</div>
				<div class="span-2 last"<c:if test="${publiser.serviceExpired}"> style="color:red"</c:if>><fmt:formatDate value="${publisher.serviceEndDate}" pattern="MM/dd/yyyy"/></div>
			</div>
			</c:forEach>
		</div>	
	</div>
</div>

</body>
</html>