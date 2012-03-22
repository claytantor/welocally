<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tr>
<td><a href="<c:url value='/site/${site.id}' />">${site.name}</a></td>
<td><a href="${site.url}" target="_new">${site.url}</a></td>
</tr>	
	