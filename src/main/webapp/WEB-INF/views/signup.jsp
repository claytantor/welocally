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
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="header.jsp"/>
	</div>
	<div class="span-24">
		<h1>sign up!</h1>
		<hr/>
        <div class="span-12">
        <h2>Subscribe to WeLocally today.</h2>
        <form:form commandName="signup">
            <form:errors path="" element="div" cssClass="error"/>
            <fieldset>
                <legend>Required Information</legend>
                <p>
                    <form:label path="email">Email:</form:label><br>
                    <form:errors path="email" element="div" cssClass="error"/>
                    <form:input path="email" cssClass="text"/>
                </p>
                <p>
                    <form:label path="username">Username:</form:label><br>
                    <form:errors path="username" element="div" cssClass="error"/>
                    <form:input path="username" cssClass="text"/>
                </p>
                <p>
                    <form:label path="password">Password:</form:label><br>
                    <form:errors path="password" element="div" cssClass="error"/>
                    <form:password path="password" cssClass="text"/>
                </p>
                <p>
                    <input type="submit" name="submitBtn" value="Subscribe!">
                </p>
            </fieldset>
        </form:form>
        </div>
	</div>
</div>

</body>
</html>