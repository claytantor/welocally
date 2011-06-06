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
<c:set var="pageTitle" value="Home"/>
<jsp:include page="head.jsp"/>
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="header.jsp"/>
	</div>
	<div class="span-24">
		<h1>home</h1>
		<hr/>
		<sec:authorize ifAllGranted="ROLE_ADMIN">
		<div class="span-24">
			<h2>admin activities</h2>
			<hr/>
		</div>
		<div class="span-24">
			<h3>user</h3>
			<div><a href="<c:url value='/admin/user/list'/>">list all</a></div>
			<div><a href="<c:url value='/admin/user'/>">create</a></div>
		</div>		
		<div class="span-24">
			<h3>network member</h3>
			<div><a href="<c:url value='/admin/member/list'/>">list all</a></div>
			<div><a href="<c:url value='/admin/member'/>">create</a></div>
		</div>		
		</sec:authorize>
		
		<sec:authorize ifAllGranted="ROLE_MEMBER">
		<div class="padding-5 span-24">
			<h2>member activities</h2>
			<hr/>
			<c:if test="${not empty member.publishers}">
				<h3>publisher activities</h3>
				<hr/>
				<div class="padding-5 simple-box span-24">
					<h3>event</h3>
					<div><a href="<c:url value='/publisher/event/list'/>">list all</a></div>
					<div><a href="<c:url value='/publisher/event'/>">create</a></div>
				</div>
				<div class="padding-5 simple-box span-24">
					<h3>article</h3>
					<div><a href="<c:url value='/publisher/article/list'/>">list all</a></div>
					<div><a href="<c:url value='/publisher/article'/>">create</a></div>
				</div>			
				<div class="padding-5 simple-box span-24">
					<h3>review</h3>
					<div><a href="<c:url value='/publisher/review/list'/>">list all</a></div>
					<div><a href="<c:url value='/publisher/review'/>">create</a></div>
				</div>
				<div class="padding-5 simple-box span-24">
					<h3>place</h3>
					<div><a href="<c:url value='/publisher/place/list'/>">list all</a></div>
					<div><a href="<c:url value='/publisher/place'/>">create</a></div>
				</div>								
			</c:if>
		</div>
		</sec:authorize>

	</div>
</div>

</body>
</html>