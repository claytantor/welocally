<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:url value="/index/detail.json" var="indexDetail"/>
<html>
<body>
<table cellpadding="0" cellspacing="0" border="1" class="display" id="publisher_list" width="100%">
		<thead>
			<tr>
				<th>name</th>
				<th>primaryKey</th>
				<th>searchFields</th>
				<th>lat</th>
				<th>lng</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="index" items="${indexes}">
    		<tr>
    			<td class="wl_row"><a href="${indexDetail}?id=${index.worksheetFeed}">${index.name}</a></td>
    			<td class="wl_row">${index.primaryKey}</td>
    			<td class="wl_row">${index.searchFields}</td>
    			<td class="wl_row">${index.lat}</td>
    			<td class="wl_row">${index.lng}</td>
    		</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
