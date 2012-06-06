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
<script type="text/javascript">
jQuery(document).ready(function() {

	if (!window.userPlacesUI) {
		window.userPlacesUI = new WELOCALLY_UserPlacesUI();
		window.userPlacesUI.initCfg({
			endpoint: '${config.ajaxServerEndpoint}',
			searchPlacesRequestPath: '/geodb/place/3_0/search.json',
			imagePath:'<c:url value='/css/welocally-places-developer/images/marker_all_base.png' />',							
			key: '${publisher.key}',
			token: '${publisher.jsonToken}'
		});		
	}
	
	jQuery( "#tabs" ).tabs({
		selected: 0,
		select: function(event, ui) { 
			WELOCALLY.util.log(ui.tab.innerHTML);

			if(ui.index==1){
				if(!window.userPlacesUI.getUserPlacesManager().wrapper){
					jQuery( "#places-wrapper-1" ).html(window.userPlacesUI.getUserPlacesManager().makeWrapper());
				} else {
					window.userPlacesUI.getUserPlacesManager().placesSearch.search();
				}
				
			} else if(ui.index==2){
				if(!window.userPlacesUI.getSearchAdd().wrapper){
					jQuery( "#places-wrapper-2" ).html(window.userPlacesUI.getSearchAdd().makeWrapper());
					jQuery( "#places-wrapper-2" ).find('#wl_finder_search_button').button();
				}
			} else if(ui.index==3){
				//add as an edit widget
				
				if(!window.userPlacesUI.getAddPlace().wrapper){
					jQuery( "#places-wrapper-3" ).html(window.userPlacesUI.getAddPlace().makeWrapper());
				}
			}
			
		},
		ajaxOptions: {
			error: function( xhr, status, index, anchor ) {
				jQuery( anchor.hash ).html(
					"Couldn't load this tab. We'll try to fix this as soon as possible." );
			}
		}
	});
	
});
</script>
<div class="area sidebar span-4">				

	<div id="navbar">
		<ul>
			<li><a href="http://welocally.com" target="_blank"/>Welocally Home</a></li>
		</ul>
		<sec:authorize ifAnyGranted="WL_PLACES_PRO,WL_PLACES_PREMIUM">
		<ul>
			<li><a href="http://welocally.com" target="_blank"/>Download Customize Plugin</a></li>
		</ul>
		<ul>
			<li><a href="http://welocally.com" target="_blank"/>Download Deals Plugin</a></li>
		</ul>
		</sec:authorize>	
		
	</div>
</div>
<div class="area main span-19 last">
	<div id="tabs">
		<ul>
			<li><a href="#tabs-overview">Overview</a></li>
			<li><a href="#tabs-myplaces">My Places</a></li>
			<li><a href="#tabs-search">Search</a></li>
			<li><a href="#tabs-create">Create</a></li>
			<li><a href="#tabs-upload">Upload</a></li>
		</ul>
		<div id="tabs-overview">
			<div class="foo-18 bottom-10 ">
				<h3>Publisher Info For ${publisher.name}</h3>
				<div>Subscription Status: ${publisher.subscriptionStatus}</div>
				<div>Publisher Key: ${publisher.key}</div>
				<div>Publisher Token: ${publisher.jsonToken}</div>
			</div>
			<div class="foo-18 bottom-10">
				<h3>Orders</h3>
				<div class="last">
					<c:forEach var="order" items="${publisher.orders}" varStatus="status">
						<c:set var="order" scope="request" value="${order}" />	
						<jsp:include page="order/detail.jsp" flush="true"/>
					</c:forEach>		
				</div>
			</div>
			<div class="foo-19 bottom-10" style="margin-top: 10px;">
				<div>
					<div style="text-align:left">
						<h3>Sites</h3>
					</div>
				</div>	
				<div>
					<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<c:forEach var="site" items="${publisher.sites}">		
						<c:set var="site" scope="request" value="${site}" />	
						<jsp:include page="site/detail.jsp" flush="true"/>
					</c:forEach>
					</table>
				</div>		
			</div>			
							
		</div>
		<div id="tabs-myplaces">
			<div class="bottom-10 ">
				<div >
					<div style="text-align:left">
						<h3>My Places</h3>
					</div>
				</div>	
				<div>
						<div id="places-wrapper-1"></div>
						<% /* <table id="list4"></table>	 */ %>				
				</div>
			</div>
		</div>
		<div id="tabs-search">
				<div >
					<div style="text-align:left">
						<h3>Search and Save</h3>
					</div>
				</div>	
				<div>
						<div id="places-wrapper-2"></div>					
				</div>
		</div>
		<div id="tabs-create">
				<div >
					<div style="text-align:left">
						<h3>Create A New Place</h3>
					</div>
				</div>	
				<div>
					<div id="places-wrapper-3"></div>					
				</div>
		</div>	
		<div id="tabs-upload">
				<div >
					<div style="text-align:left">
						<h3>Upload Places</h3>
					</div>
				</div>	
				<div>
					<div id="places-wrapper-4">
					<h1>Please upload a file</h1>
					
			        <form method="post" action="<c:url value='/place/1_0/user/upload' />" enctype="multipart/form-data">
			            <input type="file" name="file"/>
			            <input type="submit"/>
			        </form>
					
					</div>					
				</div>
		</div>						
	</div>
</div>