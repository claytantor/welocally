<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="Article Edit"/>
<jsp:include page="../head.jsp"/>
<script>
    /*var placesUrl = "<c:url value="/publisher/place/search?name="/>";
    var placeSource = function(req, add) {
        $.getJSON(placesUrl + req.term, function(data) {
            var suggestions = [];
            for (var i = 0; i < data.places.length; i++) {
                suggestions.push({'label':data.places[i].name,'value':data.places[i].id});
            }
            add(suggestions);
        });
    };*/
    
    var publishersUrl = "<c:url value="/publisher/publisher/search?siteName="/>";
    var publisherSource = function(req, add) {
        $.getJSON(publishersUrl + req.term, function(data) {
            var suggestions = [];
            for (var i = 0; i < data.publishers.length; i++) {
                suggestions.push({'label':data.publishers[i].siteName,'value':data.publishers[i].id});
            }
            add(suggestions);
        });
    };

    $(function() {
		
		$("#publisher").autocomplete({
            minLength:3,
            source:publisherSource,
            select:function(event,ui) {
                $('#publisher').val(ui.item.label);
                $('#publisher_id').val(ui.item.value);
                return false;
            }
        });
	});
</script>
<body>
<jsp:include page="../place/chooser.jsp"/>
<div class="container">

	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2>
		<a href="<c:url value='/publisher/publisher/${publisher.id}' />">${publisher.siteName}</a> :
		<c:if test="${not empty(articleForm.id)}">edit article</c:if>
		<c:if test="${empty(articleForm.id)}">create article</c:if>
		</h2>

		<c:url value='/publisher/article' var="articleAction"/>
		<form:form modelAttribute="articleForm" action="${articleAction}" method="post">
		  	<fieldset>
				<legend>Article Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
                <form:hidden id="place-id" path="place.id" />
                <form:hidden id="publisher_id" path="publisher.id" />
                  <p>
                      <label for="place">Place:</label><br/>
                      <input id="place-name" value="${articleForm.place.name}" class="textinput"/>
                  </p>                
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Name:</form:label><br/>
					<form:input path="name" id="name" class="textinput"/> <form:errors path="name" class="error" />
				</p>
                  <p>
                      <form:label for="url" path="url" cssErrorClass="error">URL:</form:label><br/>
                      <form:input path="url" id="url" class="textinput"/> <form:errors path="url" class="error" />
                  </p>

                  <p>
                      <form:label for="description" path="description" cssErrorClass="error">Description</form:label><br/>
                      <form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />
                  </p>
                  <p>
                      <form:label for="summary" path="summary" cssErrorClass="error">Summary</form:label><br/>
                      <form:textarea path="summary" rows="1" cols="10" /> <form:errors path="summary" class="error" />
                  </p>

                  <%--<p>
                      <label for="publisher">Publisher:</label><br/>
                      <input id="publisher" value="${articleForm.publisher.siteName}" />
                  </p> --%>

				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>

	</div>
</div>

</body>