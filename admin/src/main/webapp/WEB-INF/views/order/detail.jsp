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
<div>

	<div class="inline">
		<sec:authorize ifAllGranted="ROLE_ADMIN"><div class="strong-16 foo-1" style="font-size:1.4em"><strong>${order.id}</strong></div></sec:authorize>
		<div class="inline-100">
			<jsp:useBean id="now" class="java.util.Date" />
	        <fmt:setLocale value="en_US" /><fmt:formatDate value="${order.orderCreationDate}" />
        </div>
        <c:url var="publisherUrl" value="/publisher/${order.owner.id}"/>
        <div class="inline-100"><a href="${publisherUrl}">${order.owner.name}</a></div>
		<div class="inline-200"><a href="mailto:${order.buyerEmail}">${order.buyerEmail}</a></div>
		<div class="inline-200">${order.product.name}</div>
		<sec:authorize ifAllGranted="ROLE_ADMIN">
		<div class="inline-100" style="text-align: right;"><c:url value="/order/email/${order.id}" var="emailUrl"/><a style="margin-right: 5px;" href="${emailUrl}">email order</a>&nbsp;</div>
		<div class="inline-100 last"><a href="<c:url value="/order/edit/${order.id}" />">change order</a></div>
		</sec:authorize>	
	</div>
<div style="display:block;">&nbsp;</div>
<c:if test="${empty hideOrderLines}">
<c:forEach var="orderLine" items="${order.orderLines}">
	<div class="inline last">
		<div class="inline-200">${orderLine.itemSku.name}</div>							
		<div class="inline-100 align-center">${orderLine.quantityOrig}</div>
		<div class="inline-100 align-right text-12">$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2"
            value="${orderLine.itemSku.price}" /></div>   
   </div> 
</c:forEach>
</c:if>	
</div>

	