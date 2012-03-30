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
	<p>Place ID: <input id="place_id"> </input></p>
	<p><textarea id="json">
        </textarea>
        </p>
</div>
<script>
    var geodb = "http://localhost:8082/geodb/place/1_0/";
    function fetchGeoData(id) {
      $.ajax({
	  type: 'POST',
          url: geodb + id + ".json",
          data: {foo: "bar"},
          dataType: 'jsonp',
	  error: function(jqXHR, textStatus, errorThrown) {
	      console.error(textStatus);
	  },
	  success: function(data, textStatus, jqXHR){
	      obj = data[0];
	      delete obj._id;
	      json.value = JSON.stringify(obj, null, 4);
	  }
      });
    }
   fetchGeoData("WL_2vj8Ym5inDULSLFQ7K1kg6_32.553827_-82.873494@1294087602");
    </script>
  </body>
</html>
