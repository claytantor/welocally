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

<html>
<c:set var="pageTitle" value="Edit"/>
<jsp:include page="head.jsp"/>

<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="header.jsp"/>
	</div>
	<h1>EDIT</h1>
</div>
<script>
    var urlValue = "http://localhost:8082/geodb/place/1_0/edit/123.json";
//    var urlValue = _instance.cfg.endpoint + "/geodb/place/1_0/edit/123.json";
        $.ajax({
          type: 'POST',
          url: urlValue,
          data: {foo: "bar"},
          dataType: 'jsonp',
          error: function(jqXHR, textStatus, errorThrown) {
                        console.error(textStatus);
          },
   	      success: function(data, textStatus, jqXHR){
    		$(this).addClass("done");
    	  }
  		});
 </script>
</body>
</html>
