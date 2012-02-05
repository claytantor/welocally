<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<c:set var="pageTitle" value="Site Edit"/>
<jsp:include page="../head.jsp"/>
<script type="text/javascript">
$(document).ready(function() {

	$('#txgen').click(function(){
		$('#externalTxId').val('WL_TX_'+WELOCALLY.util.keyGenerator());
		return false;
	});	
	
});
</script>
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2>
		<c:if test="${not empty(orderForm.id)}">edit order for ${orderForm.owner.name}</c:if>
		<c:if test="${empty(orderForm.id)}">create order for ${orderForm.owner.name}</c:if>
		</h2>
		<c:url value='/order' var="orderAction"/>		
		<form:form modelAttribute="orderForm" action="${orderAction}" method="post">
		  	<fieldset>		
				<legend>Order Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="owner.id" />
				<p>
					<form:label	for="externalTxId" path="externalTxId" cssErrorClass="error">External Tx Id</form:label><br/>
					<form:input path="externalTxId" id="externalTxId" class="textinput"/> <form:errors path="externalTxId" class="error" />	
					<span class="actions"><a id="txgen" href="">generate</a></span>		
				</p>	
				<p>
					<form:label	for="buyerEmail" path="buyerEmail" cssErrorClass="error">Buyer Email</form:label><br/>
					<form:input path="buyerEmail" id="buyerEmail" class="textinput"/> <form:errors path="buyerEmail" class="error" />		
				</p>								
				<p>
					<form:label	for="channel" path="channel" cssErrorClass="error">Sales Channel</form:label><br/>
					<form:input path="channel" id="channel" class="textinput"/> <form:errors path="channel" class="error" />		
				</p>				
				<p>
					<form:label	for="product" path="product" cssErrorClass="error">Product</form:label><br/>
					<form:select path="product.id">					
						<form:option value="-" label="--Please Select"/>
            			<form:options items="${products}" itemValue="id" itemLabel="name"/>
					</form:select>	
				</p>															
							
																																																		
				<div class="actions">	
					<input type="submit" />
				</div>
			</fieldset>
		</form:form>

	</div>
</div>

</body>