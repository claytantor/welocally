<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<c:set var="pageTitle" value="Home"/>
<jsp:include page="head.jsp"/>

<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="header.jsp"/>
	</div>
	<div class="span-24">
		<sec:authorize ifAllGranted="ROLE_ADMIN,ROLE_MEMBER">
			<c:set var="member" scope="request" value="${member}" />	
			<c:set var="orders" scope="request" value="${orders}" />	
			<jsp:include page="admin_home.jsp" flush="true"/>
		</sec:authorize>	
		
		<sec:authorize ifAnyGranted="ROLE_PUBLISHER">
			<c:set var="publisher" scope="request" value="${publisher}" />	
			<jsp:include page="publisher_home.jsp" flush="true"/>
		</sec:authorize>
	</div>
	
	<div class="span-24">
		<jsp:include page="footer.jsp"/>
	</div>
	
</div>

</body>
</html>