<%@ page
	contentType="application/json; charset=iso-8859-1"
	language="java"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>{"publishers":[<c:forEach varStatus="status"
    var="publisher" items="${publishers}">{"siteName":"${publisher.siteName}","id":"${publisher.id}"}<c:if test="${!status.last}">,</c:if></c:forEach>]}
