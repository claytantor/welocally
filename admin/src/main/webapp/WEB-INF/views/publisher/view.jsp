<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<c:set var="pageTitle" value="Publisher View"/>
<jsp:include page="../head.jsp"/>

<body>

<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2><a href="<c:url value='/home' />">${publisher.networkMember.name}</a> : ${publisher.siteName}</h2>
		<hr/>
		<h3>${publisher.networkMember.memberKey}.${publisher.key}</h3>
		<div class="actions span-24 last">
			<a href="<c:url value='/publisher/publisher/edit/${publisher.id}' />" class="button">edit</a>
		</div>

		<div class="padding-5 span-24 last">
			<div class="strong-12 span-1">id:${publisher.id}</div>
			<div class="span-4">${publisher.subscriptionStatus}</div>
			<div class="span-16 last">
				<div class="span-16"><a href="${publisher.url}">${publisher.url}</a></div>
				<div class="span-16">${publisher.description}</div>
							
			</div>	
		</div>
		<div class="bottom-10 frame span-24 last">
			<h3>orders</h3>
			<div class="span-24 last">
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<c:forEach var="order" items="${publisher.orders}">			
					<tr>
						<td>${order.buyerEmail}</td><td>${order.title}</td><td>${order.price}</td>
					</tr>
				</c:forEach>
				</table>
			</div>
		</div>	
		<div class="bottom-10 frame span-24 last">
			<h3>event</h3>
			<div><a href="<c:url value='/publisher/event/list?publisherId=${publisher.id}'/>">list all</a></div>
			<div><a href="<c:url value='/publisher/event?publisherId=${publisher.id}'/>">create</a></div>
		</div>
		
		<div class="bottom-10 frame  span-24 last">
			<h3>article</h3>
			<div><a href="<c:url value='/publisher/article/list?publisherId=${publisher.id}'/>">list all</a></div>
			<div><a href="<c:url value='/publisher/article?publisherId=${publisher.id}'/>">create</a></div>
		</div>			
		
		
	</div>
</div>

</body>