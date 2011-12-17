<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body style="background-color: white; font-family: sans-serif; font-size: 12px; font-weight: bold; text-align: center; margin: 0; padding: 0">
<div style="width:100px;height:25px;white-space:nowrap;padding-top:6px">WeLocally -
<c:choose>
    <c:when test="${status ne null}"><span style="color:red;cursor:pointer" title="${status}">XX</span></c:when>
    <c:otherwise><span style="color:green">OK</span></c:otherwise>
</c:choose></div>
</body>
</html>
