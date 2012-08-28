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
<c:set var="pageTitle" value="Publisher View"/>
<jsp:include page="../head.jsp"/>

<body>

<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<div class="span-24 last">
			<h2><a href="<c:url value='/home' />">${publisher.networkMember.name}</a> : ${publisher.name}</h2>
		</div>
		<div class="bottom-10 frame span-24 last">
			<div class="actions span-24 last" >
				<div class="actions span-12" style="text-align:left">
					<h3>Publisher Info</h3>
				</div>
				<sec:authorize ifAnyGranted="ROLE_ADMIN">
				<div class="actions span-12 last" style="text-align:right">
					provisionStatus: ${provisionStatus}
					<a href="<c:url value='/publisher/edit/${publisher.id}' />" class="button">edit</a>
					<a href="<c:url value='/publisher/delete/${publisher.id}' />" class="button">delete</a>
					<a href="<c:url value='/administrator/publisher/provision/${publisher.id}' />" class="button">provision</a>
				</div>
				</sec:authorize>				
			</div>
			<div class="span-24 last">
				<div class="span-3">status</div><div class="strong-12 span-20">${publisher.subscriptionStatus}</div>
			</div>
			<div class="span-24 last">
				<div class="span-3">key</div><div class="strong-12 span-20">${publisher.key}</div>
			</div>
				
			<c:if test="${not empty publisher.jsonToken}">
			<div class="span-24 last">
				<div class="span-3">token</div><div class="strong-12 span-20">${publisher.jsonToken}</div>
			</div>
			</c:if>
				
		</div>
	</div>
	<div class="bottom-10 frame span-24 last">
		<div class="span-24 last">
			<div class="span-12" style="text-align:left">
				<h3>Contacts</h3>
			</div>
			<sec:authorize ifAllGranted="ROLE_ADMIN">
			<div class="actions span-12 last" style="text-align:right">
				<a href="<c:url value='/contact' />" class="button">create</a>
			</div>
			</sec:authorize>
		</div>
		<div class="span-24 last">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<c:forEach var="contact" items="${publisher.contacts}">		
				<tr><td>${contact.firstName}&nbsp;${contact.lastName}</td><td>${contact.email}</td></tr>	
			</c:forEach>
			</table>
		</div>
	</div>
	<div class="bottom-10 frame span-24 last">
		<div class="span-24 last">
			<div class="span-12" style="text-align:left">
				<h3>Sites</h3>
			</div>
		</div>	
		<sec:authorize ifAllGranted="ROLE_ADMIN">
		<div class="span-24 last">
			<div class="actions span-12 last" style="text-align:right">
				<a href="<c:url value='/site?publisherId=${publisher.id}' />" class="button">create</a>
			</div>
		</div>
		</sec:authorize>
		<div class="span-24 last">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<c:forEach var="site" items="${publisher.sites}">		
				<c:set var="site" scope="request" value="${site}" />	
				<jsp:include page="../site/detail.jsp" flush="true"/>
			</c:forEach>
			</table>
		</div>
	</div>			
	<div class="bottom-10 frame span-24 last">
		<div class="span-24 last">
			<div class="span-12" style="text-align:left">
				<h3>Orders</h3>
			</div>
			<sec:authorize ifAllGranted="ROLE_ADMIN">
			<div class="actions span-12 last" style="text-align:right">
				<a href="<c:url value='/order?publisherId=${publisher.id}' />" class="button">create</a>
			</div>
			</sec:authorize>
		</div>
		<div class="span-24 last">			
			<c:forEach var="order" items="${publisher.orders}"  >
				<c:set var="order" scope="request" value="${order}" />	
				<jsp:include page="../order/detail.jsp" flush="true"/>
				<div style="margin-bottom: 10px">&nbsp;</div>
			</c:forEach>
			
		</div>
	</div>	
			
		
		
	</div>

</body>