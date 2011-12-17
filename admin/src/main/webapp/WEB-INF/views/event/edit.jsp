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
	var sgPlacesClient = new simplegeo.PlacesClient('bb8HCTrBtUZs78EwUVJvXG6ugWkrjNvM');
	var jsonObjFeatures = []; //declare features array
	var selectedFeatureIndex = 0;
	
/*    var placesUrl = "<c:url value="/publisher/place/search?name="/>";
    
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

    function parseAll(str) {
    	var allparts = str.split(' ')
		var ddate = parseDate(allparts[0]);
		var dtime = parseTime(allparts[1]);
        return ddate+dtime;
    }
    
    function parseDate(str) {
        var mdy = str.split('/')
        var d = new Date(mdy[2], mdy[0]-1, mdy[1]);
		return d.getTime();
    }

    function parseTime(str) {
        var parts = str.split(':')
        var hours = 3600000*parts[0];
        var mins = 60000*parts[1];
        return hours+mins;
    }

        
	$(function() {						
		$( "#timeStartsTime" ).datetimepicker({
			showSecond: false,
			timeFormat: 'hh:mm',
			onSelect: function(time, inst) {
	        	$("#timeStarts").val(parseAll(time));
	    	}
		});
		$( "#timeStartsTime" ).datetimepicker('setDate', (new Date(${eventForm.timeStarts})) );

		$( "#timeEndsTime" ).datetimepicker({
			showSecond: false,
			timeFormat: 'hh:mm',
			onSelect: function(time, inst) {
				$("#timeEnds").val(parseAll(time));
	        	
	    	}
		});
		$( "#timeEndsTime" ).datetimepicker('setDate', (new Date(${eventForm.timeEnds})) );
		
        
		$("#publisher-name").autocomplete({
            minLength:3,
            source:publisherSource,
            select:function(event,ui) {
                $('#publisher-name').val(ui.item.label);
                $('#publisher-id').val(ui.item.value);
                return false;
            }
        });
	});
</script>
<style>		

		.ui-timepicker-div .ui-widget-header{ margin-bottom: 8px; }
		.ui-timepicker-div dl{ text-align: left; }
		.ui-timepicker-div dl dt{ height: 25px; }
		.ui-timepicker-div dl dd{ margin: -25px 0 10px 65px; }
		.ui-timepicker-div td { font-size: 90%; }	

		#feedback { font-size: 1.0em; }
</style>

<body>
<%--<div id="dialog-modal" title="Choose Place">
	<div>Find where the event will occur.</div>
	<div>	
		place address:</br>
		<input type="text" id="place-address" class="search-field" value="Oakland, CA"></br>	
		search term: (optional)</br>
		<input type="text" id="place-search" class="search-field"></br>
		<button id="search-places-action">find places</button>
	</div>
	<div class="padding-5" id="results">
	<div id="scoller-places">
		<ol id="selectable">
		</ol>	
	</div>
	<div><img src="<c:url value='/images/spacer.gif' />" height="5"/></div>
	<div id="selection">
		<button id="choose-place-action">choose place</button>
	</div>
	</div>
</div> --%>
<jsp:include page="../place/chooser.jsp"/>
<div class="container">

	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<h2>
		<a href="<c:url value='/publisher/publisher/${publisher.id}' />">${publisher.siteName}</a> :
        <a href="<c:url value='/publisher/event/list?publisherId=${publisher.id}' />">All Events</a> :
		<c:if test="${not empty(eventForm.id)}">edit event</c:if>
		<c:if test="${empty(eventForm.id)}">create event</c:if>
		</h2>

		<c:url value='/publisher/event' var="eventAction"/>
		<form:form modelAttribute="eventForm" action="${eventAction}" method="post">
		  	<fieldset>		
				<legend>Event Info</legend>
				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden id="place-id" path="place.id" />
				<form:hidden id="publisher-id" path="publisher.id" />			
				<form:hidden path="timeStarts" id="timeStarts" />
				<form:hidden path="timeEnds" id="timeEnds" />
                  <p>
                      <label for="place">Place:</label><br/>
                      <input id="place-name" value="${eventForm.place.name}" />
                  </p>                  
                                				
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
                <%--<p>
                    <form:label for="whenText" path="whenText" cssErrorClass="error">When:</form:label><br/>
                    <input type="text" class="datepicker" id="whenDate"> <form:errors path="whenText" class="error" />
                </p> --%>
                  <p>
                      <form:label for="timeStarts" path="timeStarts" cssErrorClass="error">Time Starts:</form:label><br/>
                      <input type="text" class="timepicker" id="timeStartsTime" value="<fmt:formatDate value="${timeStarts}" pattern="yyyy-MM-dd"/>"/> 
                      <form:errors path="timeStarts" class="error" />
                  </p>
                  <p>
                      <form:label for="timeEnds" path="timeEnds" cssErrorClass="error">Time Ends:</form:label><br/>
                      <input type="text" class="timepicker" id="timeEndsTime"> <form:errors path="timeEnds" class="error" />
                  </p>  

				<p>	
					<input type="submit" value="Save Event"/>
				</p>
			</fieldset>
		</form:form>

	</div>
</div>

</body>