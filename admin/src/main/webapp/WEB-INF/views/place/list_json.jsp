<%@ page
	contentType="application/json; charset=iso-8859-1"
	language="java"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>{"places":[<c:forEach varStatus="status"
    var="place" items="${places}">{"name":"${place.name}","id":"${place.id}"}<c:if test="${!status.last}">,</c:if></c:forEach>]}
