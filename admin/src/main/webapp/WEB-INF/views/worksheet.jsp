<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head><title>Worksheet</title></head>
  <body>
   	<h2>${worksheet.name}</h2>
   	<div>${worksheet.id}</div>
   	<div>
   		<c:url value="/index/publish" var="publish"/>
   		<form action="${publish}" method="post">
   			<input type="hidden" value="${worksheet.name}" name="name">
   			<input type="hidden" value="${worksheet.id}" name="feedUrl">
   			primary key field:<input type="text" name="primaryKey"  value="place_name">
   			search fields:<input type="text" name="searchFields"  value="place_category">
   			lat:<input type="text" name="lat"  value="place_lat">
   			lng:<input type="text" name="lng" value="place_lng">
   			<input type="submit">
   		</form>
   	</div>
    <div>
    <table border="1" cellpadding="5">
    <c:forEach var="row" items="${worksheet.rows}">
    	<tr>
    		<c:forEach var="column" items="${row.columns}">
    			<td>${column.cell.value}</td>   	
			</c:forEach>
    	</tr>
    </c:forEach> 
    </table>   
    </div>
    
  </body>
</html>