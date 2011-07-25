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
		<div class="actions span-24 last">
			<a href="<c:url value='/publisher/publisher/edit/${publisher.id}' />" class="button">edit</a>
			<a href="<c:url value='/publisher/publisher/delete/${publisher.id}' />" class="button">delete</a>
			<a href="<c:url value='/widget/verify?publisherId=${publisher.id}' />" class="button">verify publishing</a>
		</div>

		<div class="padding-5 span-24 last">
			<div class="strong-12 span-1">${publisher.id}</div>
			<div class="span-23">
				<div class="span-23"><img src="${publisher.iconUrl}"/></div>
				<div class="span-23"><img src="${publisher.mapIconUrl}"/></div>
				<div class="span-23"><a href="${publisher.url}">${publisher.url}</a></div>
				<div class="span-23">${publisher.description}</div>
							
			</div>	
		</div>
		<div class="bottom-10 frame span-24">
			<h3>event</h3>
			<div class="actions span-24 last"><a href="<c:url value='/widget/generator/event?publisherId=${publisher.id}' />" class="button">make event widget</a></div>
			<div><a href="<c:url value='/publisher/event/list?publisherId=${publisher.id}'/>">list all</a></div>
			<div><a href="<c:url value='/publisher/event?publisherId=${publisher.id}'/>">create</a></div>
		</div>
		
		<div class="bottom-10 frame  span-24">
			<h3>article</h3>
			<div class="actions span-24 last"><a href="<c:url value='/widget/generator/article?publisherId=${publisher.id}' />" class="button">make article widget</a></div>
			<div><a href="<c:url value='/publisher/article/list?publisherId=${publisher.id}'/>">list all</a></div>
			<div><a href="<c:url value='/publisher/article?publisherId=${publisher.id}'/>">create</a></div>
		</div>			
		
		
	</div>
</div>

</body>