<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<table cellpadding="0" cellspacing="0" border="0" class="display" id="publisher_list" width="100%">
		<thead>
			<tr>
				<th>Id</th>
				<th>Name</th>
				<th>Status</th>
				<th>Created</th>
			</tr>
		</thead>
		<tbody>

		<c:forEach var="publisher" items="${publishers}">
    		<tr>
    			<td class="wl_row">${publisher.id}</td>
    			<td class="wl_row">${publisher.name}</td>
    			<td class="wl_row">${publisher.subscriptionStatus}</td>
    			<td class="wl_row">${publisher.timeCreated}</td>
    		</tr>
		</c:forEach>
		</tbody>
	</table>

<%
/*
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2><a href="<c:url value='/' />">home</a> : list of publishers</h2>
		<hr/>
		<div class="actions span-24 last">
			<a href="<c:url value='/publisher/publisher' />" class="button">create</a>
		</div>		
		<div class="span-24 last">
			<c:forEach var="publisher" items="${publishers}">
			<div class="span-24 last">
				<div class="strong-12 span-1">${publisher.id}</div>
				<div class="strong-12 span-6"><a href="<c:url value='/publisher/${publisher.id}'/>">${publisher.siteName}</a></div>
				<div class="span-10">
					<div class="span-10"><a href="${publisher.url}">${publisher.url}</a></div>
					<div class="span-10">${publisher.description}</div>				
				</div>
				<div class="span-2 last">${publisher.monthlyPageviews}</div>	
			</div>
			</c:forEach>
		</div>	
	</div>
</div> */
%>
