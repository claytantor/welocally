<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery('a','.actions').button();
});
</script>
<div id="header" class="simple-box span-24" style="margin-top:10px;">
		<div class="span-18"><img src="<c:url value="${imageUrl}/header_logo.png"/>"></div>		
		
		<sec:authorize ifAnyGranted="ROLE_USER">
			<div class="padding-5 span-5 last">
				<div class="span-5"><sec:authentication property="principal.username" /></div>
				<div class="actions padding-5 span-5"><a href="<c:url value='/logout' />" class="button">logout</a>
					<a href="<c:url value='/home' />">home</a>
				</div>			
			</div>
		</sec:authorize>
		
		<sec:authorize  ifNotGranted="ROLE_USER">
		<c:if test="${empty hideLogin}">
		
				
        <div class="span-6 last">
        	<div><a href="${facebookRedirect}">login with facebook</a></div>
        	<div>

		        Please enter your Google Apps domain to log in.<br/>
		
		        <form action="<c:url value="/openid"/>" method="get">
		            <div class="field">
		                <label for="domain_field">Google Apps Domain</label><input id="domain_field" type="text" name="hd"/>
		            </div>
		            <div class="field">
		                <input type="submit" value="Log in">
		            </div>
		        </form>
     	
        	</div>
    		<div class="actions span-6">
	    		<a href="<c:url value='/home' />" class="button">login</a><a href="<c:url value='/signup/4_0' />" class="button">register</a>
		    </div>
		    
        </div>
        
               
        </c:if>
		</sec:authorize>	

</div>