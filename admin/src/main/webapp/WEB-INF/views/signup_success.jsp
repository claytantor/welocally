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
		<div class="span-6"><img src="<c:url value="${imageUrl}/yay1.png"/>"></div>
        <div class="span-18">
            <h2>You have signed up for WeLocally!</h2>
            <div class="span-18">
                Thank you for signing up. You should receive an email with your account confirmation soon.
            </div>
            <div class="span-18">
                <a href="<c:url value="/home"/>">Click here</a> to log in to your account.
            </div>
        </div>
	</div>
</div>

</body>
</html>