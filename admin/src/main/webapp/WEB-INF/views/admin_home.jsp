<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<h1>${member.name}</h1>
		<hr/>
		<div class="bottom-10 frame span-24">		
			<div class="fill-frame">
				<h2>admin activities</h2>
				<hr/>
			</div>
			<div class="padding-5">
				<ul class="list-none actions">
					<li class="inline-block" style="width:100px">place</li>
					<li class="inline-block"><a href="<c:url value='/publisher/place/finder'/>">search</a></li>
				</ul>
			</div>
			<div class="padding-5">
				<ul class="list-none actions">
					<li class="inline-block" style="width:100px">user</li>
					<li class="inline-block"><a href="<c:url value='/admin/user/list'/>">list all</a></li>
					<li class="inline-block"><a href="<c:url value='/admin/user'/>">create</a></li>
				</ul>
			</div>	
			<div class="padding-5">
				<ul class="list-none actions">
					<li class="inline-block" style="width:100px">publisher</li>
					<li class="inline-block"><a href="<c:url value='/publisher'/>">create</a></li>
				</ul>
			</div>				
					
			<div >
				<img src="<c:url value='/images/spacer.gif'/>" height="10"/>
			</div>
		</div>
		
		<div class="bottom-10 frame span-24">
			<span class="text-16">${member.name} orders:${fn:length(orders)}</span><br/>
			<span class="text-10">Last 5 days</span><br/>
			<c:set var="hideOrderLines" value="true" scope="request"/>
			<c:forEach var="order" items="${orders}" varStatus="status">
				<c:set var="order" scope="request" value="${order}" />	
				<jsp:include page="order/detail.jsp" flush="true"/>
			</c:forEach>
		</div>
		<!-- member publishers -->
		<div class="bottom-10 frame span-24">	
			<h2>${member.name} publishers</h2>
			<hr/>			
			<div class="fill-frame">
				<h3>publishers:${fn:length(member.publishers)}</h3>		
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
									${publisher.id}&nbsp;<a href="<c:url value='/publisher/${publisher.id}'/>">${publisher.name}</a>
								</div>
								<%--<div class="span-10">
									<a href="${publisher.url}" target="_new">${publisher.url}</a>
								</div> --%>
							</div>							
							<div class="align-right span-2">${publisher.subscriptionStatus}</div>	
						</div>
						</c:forEach>
					</div>					
				</div>								
			</c:if>
			</div>
		</div>