<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head><title>Spreadsheet</title></head>
  <body>
   	<h2>${spreadsheet.title.name}</h2>
    <div>
 	<table cellspacing="5">
 	<c:forEach var="entry" items="${spreadsheet.entry}">
    		
    		<c:forEach var="link" items="${entry.link}">
    			<c:url value="/user/worksheet" var="worsheet"/>
    			<c:if test='${link.rel eq "http://schemas.google.com/spreadsheets/2006#cellsfeed" }'>
	    		<tr>
	    			<td class="wl_row">${entry.title.name}</td>
	    			<td class="wl_row"><a href="${worsheet}?url=${link.href}"/>view feed</a></td>
	    			<td>create index</td>
	    		</tr> 
	    		</c:if>  		
    		</c:forEach>
		</c:forEach>
	</table>
    </div>
  </body>
</html>