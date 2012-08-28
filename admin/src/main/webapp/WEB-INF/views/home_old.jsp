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
<c:set var="pageTitle" value="Home"/>
<jsp:include page="head.jsp"/>
<c:url value='/images' var="imagePath" />
<c:url value='/publisher/list' var="allPublishersEndpoint" />
<c:url value='/user/list' var="allUsersEndpoint" />
<c:url value='/images/ajax-loader.gif' var="loadingImage" />
<c:url value='/user' var="userForm" />
<c:set var="publisher" scope="request" value="${publisher}" />
<script>
	jQuery(function() {

<sec:authorize ifAllGranted="ROLE_ADMIN,ROLE_MEMBER">
		if(!window.WELOCALLY_AdminEventHandler){
			window.WELOCALLY_AdminEventHandler = new WELOCALLY_AdminEventHandler({
			}).init(); 
		}

		jQuery( ".wl_table_buttons a" ).button();
		
		//init dialogs
		jQuery( "#wl-admin-publishers-dialog" ).dialog({
			title: 'Publisher Details',
			autoOpen: false,
			position: 'top',
			width: '800px',
			height: 'auto',
			modal: true
		});

		jQuery( "#wl-admin-tables-dialog" ).dialog({
			autoOpen: false,
			position: 'top',
			width: '800px',
			height: 'auto',
			modal: true
		});
		
		jQuery( "#wl_admin_tabs" ).tabs();
		
		jQuery('#wl-admin-publishers').getTable({
			endpoint: '${allPublishersEndpoint}',
			table: '#publisher_list',
			linkQuery: '.wl_publisher_detail',
			buttonView: jQuery('#wl_publisher_view'),
			buttonEdit: jQuery('#wl_publisher_edit'),			
			buttonDelete: jQuery('#wl_publisher_delete'),
			deleteHandler: window.WELOCALLY_AdminEventHandler.prototype.publisherDeleteHandler,
			viewHandler: window.WELOCALLY_AdminEventHandler.prototype.publisherViewHandler,
			editHandler: window.WELOCALLY_AdminEventHandler.prototype.publisherEditHandler,
			data: { 
				dialog: jQuery('#wl-admin-tables-dialog'),
				viewEndpoint: 'http://localhost:8082/geodb/publisher/1_0/'
			}
		});
		
		jQuery('#wl-admin-users').getTable({
			endpoint: '${allUsersEndpoint}',
			table: '#user_list',
			buttonView: jQuery('#wl_user_view'),
			buttonEdit: jQuery('#wl_user_edit'),					
			buttonDelete: jQuery('#wl_user_delete'),
			deleteHandler: window.WELOCALLY_AdminEventHandler.prototype.userDeleteHandler,
			viewHandler: window.WELOCALLY_AdminEventHandler.prototype.userViewHandler,
			editHandler: window.WELOCALLY_AdminEventHandler.prototype.userEditHandler,
			data: { 
				dialog: jQuery('#wl-admin-tables-dialog'),
				loadingImage: '${loadingImage}',
				geodbUserEndpoint: 'http://localhost:8082/geodb/user/1_0'
			}
		});

		//new handlers
		jQuery('#wl_user_new').bind(
				'click', 
				{ 
					endpoint: '${userForm}',
					dialog: jQuery('#wl-admin-tables-dialog') 
				}, 
				window.WELOCALLY_AdminEventHandler.prototype.userNewHandler);
</sec:authorize>

<sec:authorize ifAnyGranted="ROLE_PUBLISHER">
		jQuery('#wl-publisher-places').getPlaces({
			key:'${publisher.key}',
			token:'${publisher.jsonToken}',
			endpoint: 'http://localhost:8082',
			data: {
				dialog: jQuery('#wl-admin-tables-dialog')
			}		
		});	
</sec:authorize>

	});	
</script>

<body>
<div class="container">
	<div class="span-24">
		<jsp:include page="header.jsp"/>
	</div>	
	<div id="wl-admin-tables-dialog"></div>
	<div class="span-24 last">
			<div class="wl_admin_main">	
				<div id="wl_admin_tabs">
					<ul>
						<sec:authorize ifAllGranted="ROLE_ADMIN,ROLE_MEMBER">
							<li><a href="#admin-tabs-publishers">Publishers</a></li>					
							<li><a href="#admin-tabs-users">Users</a></li>
							<li><a href="<c:url value='/actions'/>">Actions</a></li>
						</sec:authorize>
						<sec:authorize ifAnyGranted="ROLE_PUBLISHER">
							<li><a href="#publisher-tabs-places">Places</a></li>
							<li><a href="#publisher-tabs-searches">Searches</a></li>
							<li><a href="#publisher-tabs-maps">Maps</a></li>
						</sec:authorize>
					</ul>
					<sec:authorize ifAllGranted="ROLE_ADMIN,ROLE_MEMBER">
						<div id="admin-tabs-publishers">
							<div class="wl_table_buttons"><a href="#" style="margin-right: 20px;">New</a><a id="wl_publisher_view" href="#">View</a><a id="wl_publisher_edit" href="#">Edit</a><a id="wl_publisher_delete" href="#">Delete</a></div>										
							<div id="wl-admin-publishers"></div>												
						</div>
						<div id="admin-tabs-users">	
							<div class="wl_table_buttons"><a href="#" id="wl_user_new" style="margin-right: 20px;">New</a><a id="wl_user_view" href="#">View</a><a id="wl_user_edit" href="#">Edit</a><a id="wl_user_delete" href="#">Delete</a></div>					
							<div id="wl-admin-users"></div>
							<div id="wl-admin-users-dialog"></div>
						</div> 	
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_PUBLISHER">
						<div id="publisher-tabs-places">	
							<div id="wl-publisher-places">
								
							</div>
						</div> 
						<div id="publisher-tabs-searches">	
							<h1>Searches</h1>
						</div>
						<div id="publisher-tabs-maps">	
							<h1>Maps</h1>
						</div>
						 						
					</sec:authorize>
												
				</div>	
			</div>
	</div>



<% 
/*
<div class="container">
	<div class="span-24">
		<jsp:include page="header.jsp"/>
	</div>
	<div class="span-24">
		<sec:authorize ifAllGranted="ROLE_ADMIN,ROLE_MEMBER">
			<c:set var="member" scope="request" value="${member}" />	
			<c:set var="orders" scope="request" value="${orders}" />	
			<jsp:include page="admin/home.jsp" flush="true"/>
		</sec:authorize>	
		
		<sec:authorize ifAnyGranted="ROLE_PUBLISHER">
			<c:set var="publisher" scope="request" value="${publisher}" />	
			<jsp:include page="publisher/home.jsp" flush="true"/>
		</sec:authorize>
	</div>
	
	<div class="span-24">
		<jsp:include page="footer.jsp"/>
	</div>
	
</div>
*/
%>
</div>
</body>
</html>