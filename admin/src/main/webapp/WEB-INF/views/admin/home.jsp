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
<c:url value='/images' var="imagePath" />
<c:url value='/publisher/list' var="allPublishersEndpoint" />
<c:url value='/user/list' var="allUsersEndpoint" />
<c:url value='/images/ajax-loader.gif' var="loadingImage" />
<c:url value='/user' var="userForm" />

<script>
	jQuery(function() {
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

	});	
</script>
<div id="wl-admin-tables-dialog"></div>
<div class="span-24 last">
		<div class="wl_admin_main">	
			<div id="wl_admin_tabs">
				<ul>
					<li><a href="#tabs-1">Publishers</a></li>					
					<li><a href="#tabs-2">Users</a></li>
					<li><a href="<c:url value='/actions'/>">Actions</a></li>
				</ul>
				<div id="tabs-1">
					<div class="wl_table_buttons"><a href="#" style="margin-right: 20px;">New</a><a id="wl_publisher_view" href="#">View</a><a id="wl_publisher_edit" href="#">Edit</a><a id="wl_publisher_delete" href="#">Delete</a></div>										
					<div id="wl-admin-publishers"></div>
										
				</div>
				<div id="tabs-2">	
					<div class="wl_table_buttons"><a href="#" id="wl_user_new" style="margin-right: 20px;">New</a><a id="wl_user_view" href="#">View</a><a id="wl_user_edit" href="#">Edit</a><a id="wl_user_delete" href="#">Delete</a></div>					
					<div id="wl-admin-users"></div>
					<div id="wl-admin-users-dialog"></div>
				</div> 								
			</div>	
		</div>
</div>	



<%
/*

<h1>${member.name}</h1>
		<hr/>
		<div class="bottom-10 frame span-24">		
			<div class="fill-frame">
				<h2>admin activities</h2>
				<hr/>
			</div>
			<div class="padding-5">
				<ul class="list-none actions">
					<li class="inline-block" style="width:100px">place</li>
					<li class="inline-block"><a href="<c:url value='/place/1_0/finder'/>">finder</a></li>
					<li class="inline-block"><a href="<c:url value='/place/1_0/add'/>">add</a></li>
					<li class="inline-block"><a href="<c:url value='/publisher/place/edit'/>">edit</a></li>
				</ul>
			</div>
			<div class="padding-5">
				<ul class="list-none actions">
					<li class="inline-block" style="width:100px">user</li>
					<li class="inline-block"><a href="<c:url value='/admin/user/list'/>">list all</a></li>
					<li class="inline-block"><a href="<c:url value='/admin/user'/>">create</a></li>
				</ul>
			</div>	
			<div class="padding-5">
				<ul class="list-none actions">
					<li class="inline-block" style="width:100px">publisher</li>
					<li class="inline-block"><a href="<c:url value='/publisher'/>">create</a></li>
				</ul>
			</div>				
					
			<div >
				<img src="<c:url value='/images/spacer.gif'/>" height="10"/>
			</div>
		</div>
		
		<div class="bottom-10 frame span-24">
			<span class="text-16">${member.name} orders:${fn:length(orders)}</span><br/>
			<span class="text-10">Last 5 days</span><br/>
			<c:set var="hideOrderLines" value="true" scope="request"/>
			<c:forEach var="order" items="${orders}" varStatus="status">
				<c:set var="order" scope="request" value="${order}" />	
				<jsp:include page="order/detail.jsp" flush="true"/>
			</c:forEach>
		</div>
		<!-- member publishers -->
		<div class="bottom-10 frame span-24">	
			<h2>${member.name} publishers</h2>
			<hr/>			
			<div class="fill-frame">
				<h3>publishers:${fn:length(member.publishers)}</h3>		
			</div>			
			<div class="fill-frame">
			<c:if test="${not empty member.publishers}">
				<c:set var="publishers" value="${member.publishers}" scope="request"/>
				<div class="simple-box span-24">	
					<div class="span-24 last">
						<c:forEach var="publisher" items="${publishers}" varStatus="status">
						<c:choose>
						    <c:when test='${(status.index)%2 eq 0}'>
						      <c:set var="rowColor" value="even-row" scope="page"/>
						    </c:when>
						    <c:otherwise>
						      <c:set var="rowColor" value="odd-row" scope="page"/>
						    </c:otherwise>
					  	</c:choose>
											
						<div class="${rowColor} span-24 last">
							<div class="span-21">
								<div class="strong-12 span-10">
									${publisher.id}&nbsp;<a href="<c:url value='/publisher/${publisher.id}'/>">${publisher.name}</a>
								</div>
							</div>							
							<div class="align-right span-2">${publisher.subscriptionStatus}</div>	
						</div>
						</c:forEach>
					</div>					
				</div>								
			</c:if>
			</div>
		</div>


*/


%>