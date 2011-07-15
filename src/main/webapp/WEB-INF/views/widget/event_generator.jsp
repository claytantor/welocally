<%@ page
        contentType="text/html; charset=iso-8859-1"
        language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="We Locally Event Publishing Widget Generator"/>
<jsp:include page="../head.jsp"/>
<script>
    var code = [], j = 0;
    code[j++] = '<' + 'script type="text/javascript" src="http://${hostname}<c:url value="/js/widget/event_widget.js"/>"' + '><' + '/script>';
    code[j++] = '<' + 'script type="text/javascript">';
    code[j++] = 'var cfg = {';
    code[j++] = 'url: "URL",';
    code[j++] = 'title:"TITLE",';
    code[j++] = 'hostname:"${hostname}",';
    code[j++] = 'publisher:PUBLISHER,';
    code[j++] = 'place:PLACE,';
    code[j++] = 'timeStarts:TIMESTARTS,';
    code[j++] = 'timeEnds:TIMEENDS,';
    code[j++] = 'summary:"DESCRIPTION"';
    code[j++] = '};';
    code[j++] = 'WELOCALLY.PublishWidget(cfg);';
    code[j++] = '<' + '/script' + '>';
    function generateWidget() {
        // validate input
        var f = document.forms.widget;
        var errors = [], i = 0;
        if (!$("#publisher-id").val().match(/^\d+$/)) {
            errors[i++] = "Invalid publisher, please check the URL you used to get to this page";
        }
        if (!$("#place-id").val().match(/^\d+$/)) {
            errors[i++] = "Please select a place";
        }
        if (!$("#event-title").val().match(/\S/)) {
            errors[i++] = "Please enter the event title";
        }
        if (!$("#url").val().match(/\S/)) {
            errors[i++] = "Please enter the event URL";
        } else if (!$("#url").val().match(new RegExp("^${publisher.url}"))) {
            errors[i++] = "URL must start with ${publisher.url}";
        }
        if (!$("#timeStarts").val().match(/\S/)) {
            errors[i++] = "Please enter the time the event starts";
        }
        if (!$("#timeEnds").val().match(/\S/)) {
            errors[i++] = "Please enter the time the event ends";
        }
        if (!$("#description").val().match(/\S/)) {
            errors[i++] = "Please enter the event description";
        }
        if (errors.length == 0) {
            // no errors, generate code
            $("#errors").hide();

            var joinedCode = code.join('\n');
            joinedCode = joinedCode.replace(/URL/, $("#url").val());
            joinedCode = joinedCode.replace(/TITLE/, $("#event-title").val().replace(/"/g, '\\"'));
            joinedCode = joinedCode.replace(/PUBLISHER/, $("#publisher-id").val());
            joinedCode = joinedCode.replace(/PLACE/, $("#place-id").val());
            joinedCode = joinedCode.replace(/TIMESTARTS/, $("#timeStarts").val());
            joinedCode = joinedCode.replace(/TIMEENDS/, $("#timeEnds").val());
            joinedCode = joinedCode.replace(/DESCRIPTION/, $("#description").val().replace(/"/g, '\\"').replace(/[\r\n]/g, ''));

            $("#widgetCode").val(joinedCode);
            $("#widgetCode").focus();
            $("#widgetCode").select();

            window.scrollTo(0, $("#widgetCode").offset().top);
        } else {
            // show errors
            var errorHtml = "Please fix the following errors:<br><ul><li>" + errors.join("</li><li>") + "</li></ul>";
            $("#errors").html(errorHtml);
            $("#errors").show();
            window.scrollTo(0, $("#errors").offset().top);
        }
        return false;
    }

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
		$( "#timeStartsTime" ).datetimepicker('setDate');

		$( "#timeEndsTime" ).datetimepicker({
			showSecond: false,
			timeFormat: 'hh:mm',
			onSelect: function(time, inst) {
				$("#timeEnds").val(parseAll(time));

	    	}
		});
		$( "#timeEndsTime" ).datetimepicker('setDate');
	});
</script>
<body>
<jsp:include page="../place/chooser.jsp"/>
<div class="container">

    <div class="span-24">
        <jsp:include page="../header.jsp"/>
    </div>
    <div class="span-24">

        <div id="errors" style="border:2px red solid;background-color:#fbc2c4;display:none"></div>

        <form name="widget" action="generator.jsp" onsubmit="return generateWidget()">
            <fieldset>
                <legend>Event Info</legend>

                <input type="hidden" id="publisher-id" value="${publisher.id}"/>
                <input type="hidden" id="place-id" name="place-id"/>
                <input type="hidden" id="timeStarts"/>
                <input type="hidden" id="timeEnds"/>

                <p>
                    <label for="publisher">Publisher:</label><br/>
                    <input id="publisher" class="textinput" value="${publisher.siteName}" disabled="true">
                </p>

                <p>
                    <label for="place-name">Place:</label><br/>
                    <input id="place-name" class="textinput">
                </p>

                <p>
                    <label for="event-title">Title:</label><br/>
                    <input type="text" id="event-title" class="textinput">
                </p>

                <p>
                    <label for="url">URL (must start with ${publisher.url}):</label><br/>
                    <input type="text" id="url" class="textinput">
                </p>

                <p>
                    <label for="timeStartsTime">Time Starts:</label><br/>
                    <input type="text" id="timeStartsTime" class="timepicker">
                </p>
                <p>
                    <label for="timeEnds" path="timeEndsTime">Time Ends:</label><br/>
                    <input type="text" id="timeEndsTime" class="timepicker">
                </p>

                <p>
                    <label for="description">Description</label><br/>
                    <textarea id="description"></textarea>
                </p>

                <p>
                    <input type="submit" value="Generate Widget Code"/>
                </p>
            </fieldset>

            <fieldset>
                <legend>Widget Embed Code:</legend>

                <p>
                    <label for="widgetCode">Copy all the code below and paste it into your blog post:</label><br/>
                    <textarea id="widgetCode"></textarea>
                </p>
            </fieldset>
        </form>

    </div>
</div>

</body>