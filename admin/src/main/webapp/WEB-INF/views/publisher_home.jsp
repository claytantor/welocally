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
<div class="area sidebar span-5">			
	<!--  navbar -->
	<script type="text/javascript">
	$(function() {
		$( "a", "#navbar" ).button();
		$( "#button-addnote" ).click(function() { return false; });
	});		
	</script>

	<div id="navbar" class="widget">
		<ul>
			<li><a href="http://welocally.com" target="_blank"/>Welocally Home</a></li>
		</ul>
		<sec:authorize ifAnyGranted="WL_PLACES_PRO,WL_PLACES_PREMIUM">
		<ul>
			<li><a href="http://welocally.com" target="_blank"/>Download Customize Plugin</a></li>
		</ul>
		<ul>
			<li><a href="http://welocally.com" target="_blank"/>Download Deals Plugin</a></li>
		</ul>
		</sec:authorize>	
		
	</div>

</div>
<div class="area main span-18 last">
	<div class="border-bottom bottom-10 span-18 last">
		<h3>Publisher Info For ${publisher.name}</h3>
		<div>Subscription Status: ${publisher.subscriptionStatus}</div>
		<div>Publisher Key: ${publisher.key}</div>
		<div>Publisher Token: ${publisher.jsonToken}</div>
	</div>
	<div class="bottom-10 span-18 last">
		<h3>Orders</h3>
		<div class="span-24 last">
			<c:forEach var="order" items="${publisher.orders}" varStatus="status">
				<c:set var="order" scope="request" value="${order}" />	
				<jsp:include page="order/detail.jsp" flush="true"/>
			</c:forEach>

		</div>
	</div>
	<div class="bottom-10 span-18 last">
		<div class="span-24 last">
			<div class="span-12" style="text-align:left">
				<h3>Sites</h3>
			</div>
		</div>	
		<div class="span-24 last">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<c:forEach var="site" items="${publisher.sites}">		
				<c:set var="site" scope="request" value="${site}" />	
				<jsp:include page="site/detail.jsp" flush="true"/>
			</c:forEach>
			</table>
		</div>

	</div>	
</div>