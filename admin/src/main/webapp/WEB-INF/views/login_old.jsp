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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta name="description" content="Login to Welocally" />

<link rel="shortcut icon" href="<c:url value='/images/we_16.png' />"/> 
<link rel="stylesheet" type="text/css" href="<c:url value='/css/welocally.css'/>" />
<script type="text/javascript">

</script>
</head>

<body onLoad="document.f.j_username.focus();">
	<div style="width:100%; text-align:center;">
    	<div >
             <div style="text-align:center; width: 400px; margin: 0 auto">
            	<img src="<c:url value='/images/header_logo.png'/>" border="0"/>
            </div>
        </div>


        <div id="login-area" style="text-align:center; width: 400px; margin: 0 auto">
             <c:if test="${not empty param.login_error}">
                  <font color="red">
                    Your login attempt was not successful, try again.<br/><br/>
                    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
                  </font>
                </c:if>
            	<div class="margin-10">Don't have an account yet? <a href="<c:url value='/signup/4_0'/>">Register Now</a></div>
            	<div class="margin-10">
                <form name="f" action="<c:url value='j_spring_security_check'/>" method="POST">
                  <input type="hidden" id="_spring_security_remember_me" name="_spring_security_remember_me" value="true">
                  <table>
                    <tr><td align="right">User </td><td><input type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/></td></tr>
                    <tr><td align="right">Password </td><td><input type='password' name='j_password'></td></tr>
                    <tr><td></td><td><input name="submit" type="submit" value="Submit"><input name="reset" type="reset"></td></tr>
                  </table>
                </form>	
                </div>
                <div class="margin-10">Forgot password? <a href="/reset.html">Password Reset</a></div>
        </div>
                  
                  
             
    </div> <%-- end 100% center--%>

</body>
</html>
