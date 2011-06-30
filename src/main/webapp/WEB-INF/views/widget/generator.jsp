<%@ page
        contentType="text/html; charset=iso-8859-1"
        language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="We Locally Publish Widget Generator"/>
<jsp:include page="../head.jsp"/>
<script>
    var code = [], j = 0;
    code[j++] = '<' + 'script type="text/javascript" src="http://www.welocally.com<c:url value="/js/widget/publish_widget.js"/>"' + '><' + '/script>';
    code[j++] = '<' + 'script type="text/javascript">';
    code[j++] = 'var cfg = {';
    code[j++] = 'url: "URL",';
    code[j++] = 'title:"TITLE",';
    code[j++] = 'publisher:PUBLISHER,';
    code[j++] = 'place:PLACE';
    code[j++] = 'summary:"DESCRIPTION",';
    code[j++] = '};';
    code[j++] = 'WELOCALLY.PublishWidget(cfg);';
    code[j++] = '<' + '/script' + '>';
    function generateWidget() {
        // validate input
        var f = document.forms.widget;
        var errors = [], i = 0;
        if (!$("#publisher-id").val().match(/^\d+$/)) {
            errors[i++] = "Invlid publisher, please check the URL you used to get to this page";
        }
        if (!$("#place-id").val().match(/^\d+$/)) {
            errors[i++] = "Please select a place";
        }
        if (!$("#article-title").val().match(/\S/)) {
            errors[i++] = "Please enter the article title";
        }
        if (!$("#url").val().match(/\S/)) {
            errors[i++] = "Please enter the article URL";
        } else if (!$("#url").val().match(new RegExp("^${publisher.url}"))) {
            errors[i++] = "URL must start with ${publisher.url}";
        }
        if (!$("#description").val().match(/\S/)) {
            errors[i++] = "Please enter the article description";
        }
        if (errors.length == 0) {
            // no errors, generate code
            $("#errors").hide();

            var joinedCode = code.join('\n');
            joinedCode = joinedCode.replace(/URL/, $("#url").val());
            joinedCode = joinedCode.replace(/TITLE/, $("#article-title").val().replace(/"/g, '\\"'));
            joinedCode = joinedCode.replace(/PUBLISHER/, $("#publisher-id").val());
            joinedCode = joinedCode.replace(/PLACE/, $("#place-id").val());
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
                <legend>Article Info</legend>

                <input type="hidden" id="publisher-id" value="${publisher.id}"/>
                <input type="hidden" id="place-id" name="place-id"/>

                <p>
                    <label for="publisher">Publisher:</label><br/>
                    <input id="publisher" class="textinput" value="${publisher.siteName}" disabled="true">
                </p>

                <p>
                    <label for="place-name">Place:</label><br/>
                    <input id="place-name" class="textinput">
                </p>

                <p>
                    <label for="article-title">Title:</label><br/>
                    <input type="text" id="article-title" class="textinput">
                </p>

                <p>
                    <label for="url">URL (must start with ${publisher.url}):</label><br/>
                    <input type="text" id="url" class="textinput">
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