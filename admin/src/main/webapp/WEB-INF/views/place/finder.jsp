<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<c:set var="pageTitle" value="Place Finder"/>
<jsp:include page="../head.jsp"/>
<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="../header.jsp"/>
	</div>
	<div class="span-24">
		<sec:authorize ifAllGranted="ROLE_ADMIN">
		Authorized
		<div>
		<script type="text/javascript">
		var placeSelected = new WELOCALLY_PlaceWidget({}).init();
	    var cfg = { 
				id:'finder_1',
				placehoundPath: 'http://placehound.com',	
				showLetters: true,
				showShare: true,
				zoom:4, 
				imagePath:'http://placehound.com/images/marker_all_base.png',
    			endpoint:'https://api.welocally.com',
		    	showSelection: true,
		    	observers:[placeSelected],				
	    };
	    
	    var placesFinder = 
			  new WELOCALLY_PlaceFinderWidget(cfg)
		  		.init();
  		
  		//now register the display for the place
	    placeSelected.setWrapper(cfg, jQuery(placesFinder.getSelectedSection()));	
	    
	    jQuery('#wl-place-finder-meta-1').find('.handlediv').click(function() {
	    	WELOCALLY.util.log('finder toggle');
	    	jQuery(placesFinder._locationField).trigger('change' , {instance: placesFinder}, placesFinder.locationFieldInputHandler);	
	    });		
		</script>
		</div>
			
		</sec:authorize>
		
		<sec:authorize ifNotGranted="ROLE_ADMIN">
		Not Authorized.
		</sec:authorize>	
			
	</div>
</div>
</body>
</html>


