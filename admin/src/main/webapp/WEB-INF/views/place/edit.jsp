<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<c:set var="pageTitle" value="Place Edit"/>
<jsp:include page="../head.jsp"/>
<script type="text/javascript">
var placeEditor;
jQuery(document).ready(function() {
	 placeEditor = 
		new WELOCALLY_EditPlaceWidget({  endpoint:'http://gaudi-vb',statusArea: jQuery('#wl_editplace_status'),selectedPlaceArea: jQuery('#wl_place_textarea') }).init();
});

</script>
<body>

<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<div class="span-24 actions" id="wl_editplace_status" style="display:none"></div>
		<div class="span-24 actions">
			<input name="id" class="wl_field" id="wl_place_id" value="WL_ccb7ts57mct4bdeq95e917_47.810331_-122.371257@1333125750"/><a href="#" onclick="javascript:placeEditor.findPlaceById(jQuery('#wl_place_id').val()); return false;">get</a>
		</div>
		<div class="span-24 actions">
			<textarea name="place" class="wl_field" rows="32" id="wl_place_textarea"/></textarea>
			<a href="#" onclick="javascript:placeEditor.savePlace(jQuery('#wl_place_textarea').val()); return false;">save</a>
		</div>
	</div>	
</div>

</body>
</html>