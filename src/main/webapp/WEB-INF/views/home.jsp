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
			<div>
				<h3>place</h3>
				<div><a href="<c:url value='/publisher/place/finder'/>">place finder</a></div>
			</div>
			<div class="bottom-10 fill-frame">
				<h3>user</h3>
				<div><a href="<c:url value='/admin/user/list'/>">list all</a></div>
				<div><a href="<c:url value='/admin/user'/>">create</a></div>
			</div>			
			<div >
				<img src="<c:url value='/images/spacer.gif'/>" height="10"/>
			</div>
		</div>
		<!-- member publishers -->
		<div class="bottom-10 frame span-24">	
			<h2>welocally publishers</h2>
			<hr/>			
			<div class="fill-frame">
				<h3>sites:${fn:length(member.publishers)}</h3>		
			</div>			
			<div class="fill-frame">
			<c:if test="${not empty member.publishers}">
				<c:set var="publishers" value="${member.publishers}" scope="request"/>
				<div class="simple-box span-24">	
					<div class="span-24 last">
						<c:forEach var="publisher" items="${publishers}" varStatus="status">
						<c:choose>
						    <c:when test='${(status.index)%2 eq 0}'>
						      <c:set var="rowColor" value="even-row" scope="page"/>
						    </c:when>
						    <c:otherwise>
						      <c:set var="rowColor" value="odd-row" scope="page"/>
						    </c:otherwise>
					  	</c:choose>
											
						<div class="${rowColor} span-24 last">
							<div class="span-21">
								<div class="strong-12 span-10">
									<a href="<c:url value='/publisher/publisher/${publisher.id}'/>">${publisher.siteName}</a>
								</div>
								<div class="span-10">
									<a href="${publisher.url}" target="_new">${publisher.url}</a>
								</div>
							</div>							
							<div class="align-right span-2">${publisher.subscriptionStatus}</div>	
						</div>
						</c:forEach>
					</div>					
				</div>								
			</c:if>
			</div>
		</div>
		</sec:authorize>	
		<sec:authorize ifAnyGranted="ROLE_PUBLISHER">
		
		
		<div class="area sidebar span-5">
			
			<!--  navbar -->
			<script type="text/javascript">
			$(function() {
				$( "a", "#navbar" ).button();
				$( "#button-addnote" ).click(function() { return false; });
			});		
			</script>

			<div id="navbar" class="widget">
				<ul  >
					<li><a href="http://welocally.com" target="_blank"/>Welocally Home</a></li>
				</ul>
			</div>
		
		</div>
		<div class="area main span-18 last">
			<div class="border-bottom bottom-10 span-18 last">
				<h3>Publisher Info For ${publisher.siteName}</h3>
				<div>Subscription Status:${publisher.subscriptionStatus}</div>
				<div>Publisher Key:${publisher.key}</div>
				<div>Publisher Token:${publisher.jsonToken}</div>
				<div>Site Url:${publisher.url}</div>
				<div>Site Summary:${publisher.summary}</div>
				<div>Subscription Status:${publisher.subscriptionStatus}</div>
			</div>
			<div class="bottom-10 span-18 last">
				<h3>Orders</h3>
				<div>
					<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<c:forEach var="order" items="${publisher.orders}">			
						<tr>
							<td>${order.buyerEmail}</td><td>${order.title}</td><td>${order.price}</td>
						</tr>
					</c:forEach>
					</table>
				</div>
			</div>
		</div>
		
		</sec:authorize>
	</div>
</div>

</body>
</html>