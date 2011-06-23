<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java" 
	import="javax.servlet.jsp.*" 
	errorPage="" %>
<%@ page import="org.springframework.security.ui.AbstractProcessingFilter" %>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.AuthenticationException" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta name="description" content="Login to Service Tattler" />

<link rel="icon" href="http://www.servicetattler.com/favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="http://www.servicetattler.com/favicon.ico" type="image/x-icon"> 
<link rel="stylesheet" type="text/css" href="<c:url value='/css/ratecred.css'/>" />


</head>

<body onLoad="document.f.j_username.focus();">
	<div style="width:100%; text-align:center;">
    	<div style="width:100%;height:80px" class="hd">
             <div style="text-align:left; width: 800px; margin: 0 auto">
            	<img src="<c:url value='/images/header_logo.png'/>" border="0"/>
            </div>
        </div>
                  
        <div style="text-align:left; width: 800px; margin: 0 auto">

           
            <div id="login" style=" position:relative; top:30px;" >
                <c:if test="${not empty param.login_error}">
                  <font color="red">
                    Your login attempt was not successful, try again.<br/><br/>
                    Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
                  </font>
                </c:if>
            
                <form name="f" action="<c:url value='j_spring_security_check'/>" method="POST">
                  <table>
                    <tr><td align="right">User:</td><td><input type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/></td></tr>
                    <tr><td align="right">Password:</td><td><input type='password' name='j_password' ></td></tr>
                    <tr><td></td><td><input name="submit" type="submit" value="Submit"><input name="reset" type="reset"></td></tr>
                  </table>
                </form>	
                
                
            </div> <%-- login --%>
        </div> <%-- end 800 --%>              
             
    </div> <%-- end 100% center--%>

</body>
</html>
