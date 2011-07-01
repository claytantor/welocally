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
		<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_MEMBER"><h1>${member.name}</h1></sec:authorize>
		<hr/>
		<sec:authorize ifAllGranted="ROLE_ADMIN">
		<div class="bottom-10 frame span-24">		
			<div class="fill-frame">
				<h2>admin activities</h2>
				<hr/>
			</div>
			<div class="bottom-10 fill-frame">
				<h3>user</h3>
				<div><a href="<c:url value='/admin/user/list'/>">list all</a></div>
				<div><a href="<c:url value='/admin/user'/>">create</a></div>
			</div>		
			<div class="fill-frame">
				<h3>network member</h3>
				<div><a href="<c:url value='/admin/member/list'/>">list all</a></div>
				<div><a href="<c:url value='/admin/member'/>">create</a></div>
			</div>		
			<div >
				<img src="<c:url value='/images/spacer.gif'/>" height="10"/>
			</div>
		</div>
		</sec:authorize>	

		<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_MEMBER">
		<div class="bottom-10 frame span-24">	
			<h2>association activities</h2>
			<hr/>		
			<div class="fill-frame">
				<h3>association key: ${member.memberKey}</h3>		
			</div>	
			
			<div class="fill-frame">
				<h3>merchants</h3>
				<div class="actions span-24 last">
					<a href="<c:url value='/association/merchant' />" class="button">create member merchant</a>
				</div>			
			</div>	
															
		</div>
		</sec:authorize>		
			
		<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_PUBLISHER">
		<div class="bottom-10 frame span-24">	
			<h2>publisher activities</h2>
			<hr/>			
			<div class="fill-frame">
				<h3>sites</h3>
				<div class="actions span-24 last">
					<a href="<c:url value='/publisher/publisher' />" class="button">create site</a>
				</div>			
			</div>			
			<div class="fill-frame">
			<c:if test="${not empty member.publishers}">
				<c:set var="publishers" value="${member.publishers}" scope="request"/>
				<div class="simple-box span-24">	
					<div class="span-24 last">
						<c:forEach var="publisher" items="${publishers}">
						<div class="span-24 last">
							<div class="span-2"><img src="${publisher.mapIconUrl}"/></div>
							<div class="strong-12 span-4"><a href="<c:url value='/publisher/publisher/${publisher.id}'/>">${publisher.siteName}</a></div>							
							<div class="span-10">
								<div class="span-10"><a href="${publisher.url}">${publisher.url}</a></div>
								<div class="span-10">${publisher.summary}</div>				
							</div>
							<div class="span-2 last">${publisher.monthlyPageviews}</div>	
						</div>
						</c:forEach>
					</div>					
				</div>								
			</c:if>
			</div>
		</div>
		</sec:authorize>
		
		<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_AFILLIATE">
		<div class="bottom-10 frame span-24">	
			<h2>member activities</h2>
			<hr/>						
		</div>
		</sec:authorize>		

		<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_MERCHANT">
		<div class="bottom-10 frame span-24">	
			<h2>merchant activities</h2>
			<hr/>						
		</div>
		</sec:authorize>		

	</div>
</div>

</body>
</html>