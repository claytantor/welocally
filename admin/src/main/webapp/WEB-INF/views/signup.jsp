<%@ page
	contentType="text/html; charset=iso-8859-1"
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="WeLocally Sign Up"/>
<jsp:include page="head.jsp"/>
<script type="text/javascript">

function passwordHandler(result){
	jQuery('#passwordStatus').val(result);	
	if(result != 'match' ){
		jQuery('#pw-match-status').html('FAIL:'+result); 
		jQuery('#pw-match-status').show(); 
	} else if(result == 'match' ) {
		jQuery('#pw-match-status').html('OK'); 
		jQuery('#pw-match-status').show(); 
	} else {
		jQuery('#pw-match-status').hide(); 
	}
}


jQuery(document).ready(function(){	
	jQuery('#password').change(function(){
		var result = WELOCALLY.util.passwordTest(jQuery('#password').val(), jQuery('#passwordAgain').val());
		WELOCALLY.util.log(result);		
		passwordHandler(result);
		
	});

	jQuery('#passwordAgain').change(function(){
		var result = WELOCALLY.util.passwordTest(jQuery('#password').val(), jQuery('#passwordAgain').val());
		WELOCALLY.util.log(result);		
		passwordHandler(result);
	
	});	

	jQuery('#submit-button').click(function() {
		jQuery('#publisherSignupForm').submit();
	});

	
	
});
</script>
<body>
<div class="container">
	<div class="span-24">
		<c:set var="hideLogin" value="true" scope="request"/>
		<jsp:include page="header.jsp"/>
	</div>
	<div class="span-24">
		<c:if test="${not empty errors}">errors</c:if>
		<c:url value='/signup/4_0' var="userAction"/>
		<div class="span-2">&nbsp;</div>	
		<div class="span-15">
		<form:form id="publisherSignupForm" modelAttribute="publisherSignupForm" action="${userAction}" method="post" class="wl_form">
		  	<fieldset>		
				<legend>New Publisher Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="passwordStatus" id="passwordStatus"/>
				<div class="bottom-10 span-4 last">
					<form:label	for="username" path="username" cssErrorClass="wl_error_title">User Name</form:label><br/>
					<form:input path="username" id="username"/> <form:errors path="username" class="wl_error_message" />			
				</div>
				<div class="bottom-10 span-14 last">
					<form:label	for="siteName" path="siteName" cssErrorClass="wl_error_title">Site Name</form:label><br/>
					<form:input path="siteName" id="siteName"/> <form:errors path="siteName" class="wl_error_message" />			
				</div>
				<div class="bottom-10 span-14 last">
					<form:label	for="siteUrl" path="siteUrl" cssErrorClass="wl_error_title">Site URL</form:label><br/>
					<form:input path="siteUrl" id="siteUrl"/> <form:errors path="siteUrl" class="wl_error_message" />			
				</div>								
				<div class="bottom-10 span-14 last">
					<form:label	for="email" path="email" cssErrorClass="wl_error_title">Email</form:label><br/>
					<form:input path="email" id="email"/> <form:errors path="email" class="wl_error_message" />			
				</div>					
				<div class="bottom-10 span-4">
					<form:label	for="password" path="password" cssErrorClass="wl_error_title">Password</form:label><br/>
					<form:password path="password" id="password" class="password"/> <form:errors path="password" class="wl_error_message"/>			
				</div>	
				<div class="bottom-10 span-4">
					<form:label	for="passwordAgain" path="passwordAgain" cssErrorClass="wl_error_title">Password Again</form:label><br/>
					<form:password path="passwordAgain" id="passwordAgain"/> <form:errors path="passwordAgain" class="wl_error_message"/>			
				</div>
				<div class="span-4" id="pw-match-status" style="display:none"></div>
				<div class="span-10">
				<div class="span-4">
					<form:label for="termsAgree" path="termsAgree" cssErrorClass="wl_error_title">Agree to Welocally Terms of Service?</form:label>
					<form:checkbox path="termsAgree" class="span-1" id="termsAgree"/><form:errors path="termsAgree" class="wl_error_message" style="display:inline-block" /><div>I have read <a href="<c:url value='/view/tos'/>">the terms</a> and agree.</div>
			    </div>	
			    <div class="span-4 last">
					<form:label for="subscribe" path="subscribe" cssErrorClass="wl_error_title">Recieve Updates and Offers?</form:label>
					<form:checkbox path="subscribe" class="span-1" /><form:errors path="subscribe" class="wl_error_message" style="display:inline-block" /><div>Keep me updated about Welocally.</div>
			    </div>			
				</div>
				<div class="span-14 margin-10 align-center"><img id="submit-button" src="<c:url value="${imageUrl}/button-do-it.png"/>"></div>
																		
			</fieldset>
		</form:form>
		</div>	
		<div class="signup-info span-4 last">
		<div class="align-center"><img src="<c:url value="${imageUrl}/badge_offer_basic.png"/>"></div>
		</div>

	</div>
</div>

</body>
</html>