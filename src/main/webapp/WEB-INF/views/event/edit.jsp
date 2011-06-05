<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="Event Edit"/>
<jsp:include page="../head.jsp"/>
<script>
    var placesUrl = "<c:url value="/admin/place/search?name="/>";
    var placeSource = function(req, add) {
        $.getJSON(placesUrl + req.term, function(data) {
            var suggestions = [];
            for (var i = 0; i < data.places.length; i++) {
                suggestions.push({'label':data.places[i].name,'value':data.places[i].id});
            }
            add(suggestions);
        });
    };
    var publishersUrl = "<c:url value="/admin/publisher/search?siteName="/>";
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
		$("#place").autocomplete({
            minLength:3,
            source:placeSource,
            select:function(event,ui) {
                $('#place').val(ui.item.label);
                $('#place_id').val(ui.item.value);
                return false;
            }
        });
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

<div class="container">
	<div class="span-24">
		<h2>
		<c:if test="${not empty(eventForm.id)}">edit event</c:if>
		<c:if test="${empty(eventForm.id)}">create event</c:if>
		</h2>

		<c:url value='/admin/event' var="eventAction"/>
		<form:form modelAttribute="eventForm" action="${eventAction}" method="post">
		  	<fieldset>		
				<legend>Event Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden id="place_id" path="place.id" />
				<form:hidden id="publisher_id" path="publisher.id" />
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Name:</form:label><br/>
					<form:input path="name" id="name"/> <form:errors path="name" class="error" />
				</p>	
				<p>
					<form:label	for="url" path="url" cssErrorClass="error">URL:</form:label><br/>
					<form:input path="url" id="url"/> <form:errors path="url" class="error"/>			
				</p>							
				<p>
					<form:label	for="cost" path="cost" cssErrorClass="error">Cost</form:label><br/>
					<form:input path="cost" id="cost" /> <form:errors path="cost" class="error" />
				</p>	
				<p>
					<form:label	for="description" path="description" cssErrorClass="error">Description:</form:label><br/>
					<form:textarea path="description" rows="1" cols="10" /> <form:errors path="description" class="error" />
				</p>
                <p>
                    <form:label for="whenText" path="whenText" cssErrorClass="error">When:</form:label><br/>
                    <form:input path="whenText" id="whenText" /> <form:errors path="whenText" class="error" />
                </p>
                  <p>
                      <form:label for="categoryAttachmentKey" path="categoryAttachmentKey" cssErrorClass="error">Category Attachment Key:</form:label><br/>
                      <form:input path="categoryAttachmentKey" id="categoryAttachmentKey" /> <form:errors path="categoryAttachmentKey" class="error" />
                  </p>
                  <p>
                      <form:label for="imageAttachmentKey" path="imageAttachmentKey" cssErrorClass="error">Image Attachment Key:</form:label><br/>
                      <form:input path="imageAttachmentKey" id="imageAttachmentKey" /> <form:errors path="imageAttachmentKey" class="error" />
                  </p>
                  <p>
                      <form:label for="timeStarts" path="timeStarts" cssErrorClass="error">Time Starts:</form:label><br/>
                      <form:input path="timeStarts" id="timeStarts" /> <form:errors path="timeStarts" class="error" />
                  </p>
                  <p>
                      <form:label for="timeEnds" path="timeEnds" cssErrorClass="error">Time Ends:</form:label><br/>
                      <form:input path="timeEnds" id="timeEnds" /> <form:errors path="timeEnds" class="error" />
                  </p>
                  <p>
                      <form:label for="recurrenceInterval" path="recurrenceInterval" cssErrorClass="error">Recurrence Interval:</form:label><br/>
                      <form:input path="recurrenceInterval" id="recurrenceInterval" /> <form:errors path="recurrenceInterval" class="error" />
                  </p>
                  <p>
                      <form:label for="recurrenceData" path="recurrenceData" cssErrorClass="error">Recurrence Data:</form:label><br/>
                      <form:input path="recurrenceData" id="recurrenceData" /> <form:errors path="recurrenceData" class="error" />
                  </p>
                  <p>
                      <form:label for="recurrenceEnd" path="recurrenceEnd" cssErrorClass="error">Recurrence End:</form:label><br/>
                      <form:input path="recurrenceEnd" id="recurrenceEnd" /> <form:errors path="recurrenceEnd" class="error" />
                  </p>
                  <p>
                      <label for="place">Place:</label><br/>
                      <input id="place" />
                  </p>
                  <p>
                      <label for="publisher">Publisher:</label><br/>
                      <input id="publisher" />
                  </p>

				<p>	
					<input type="submit" />
				</p>
			</fieldset>
		</form:form>

	</div>
</div>

</body>