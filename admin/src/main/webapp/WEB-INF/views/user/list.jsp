<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<table cellpadding="0" cellspacing="0" border="0" class="display" id="user_list" width="100%">
		<thead>
			<tr>
				<th>Id</th>
				<th>Username</th>
				<th>Expired</th>
				<th>Enabled</th>
				<th>Locked</th>
				<th>Roles</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="user" items="${users}">
    		<tr>
    			<td>${user.id}</td>
 				<td>${user.username}</td>
				<td>${user.credentialsExpired}</td>
				<td>${user.enabled}</td>
				<td>${user.locked}</td>
				<td><c:forEach var="role" items="${user.roles}">${role.role}, </c:forEach></td>
    		</tr>
		</c:forEach>
		</tbody>
	</table>

