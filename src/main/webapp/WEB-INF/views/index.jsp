<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Admin Home"/>
<jsp:include page="head.jsp"/>
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="header.jsp"/>
	</div>
	<div class="span-24">
		<h1>welcome!</h1>
		<hr/>
	</div>
</div>

</body>
</html>