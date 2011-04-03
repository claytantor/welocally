<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ratecred" uri="/WEB-INF/ratecred.tld" %>
<html>
<jsp:include page="head.jsp"/>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script src="http://platform.twitter.com/widgets.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	
});
</script>

<body>
<div class="span-18 margin-10 grey-text last">
	
	<%-- the offer --%>
	<div class="white-bg span-18">
		<c:if test="${not empty offer.illustrationUrl}">
			<div class="span-7 padding-5">
					<img src="${offer.illustrationUrl}" width="245px"/>
					<div class="padding-5 last">&nbsp;</div>	       		
			</div>
			<c:set var="offerspan" value="span-10"/>
		</c:if>	
		<c:if test="${empty offer.illustrationUrl}">
			<c:set var="offerspan" value="span-18 last"/>
		</c:if>
		
		<%-- the offer --%>							
		<div class="${offerspan} last">
			<div class="text-20">${offer.programName}</div>
       		<div class="text-14">${offer.name}</div>
       		<div><span class="text-10">type:</span><span class="text-14">${offer.type}</span></div>
       		<div class="margin-5 last">
				&nbsp;
			</div>
			<div class="align-center ${offerspan} last">
       			<div class="align-center span-3">you pay</div>
       			<div class="align-center span-3">valued at</div>
       			<div class="align-center span-3">you save</div>
       		</div>
       		<div class="align-center ${offerspan} last">
       			<div class="strong-16 span-3">$<fmt:formatNumber type="number" value="${offer.price}" pattern="0.00"/></div>
       			<div class="strong-16 span-3">$<fmt:formatNumber type="number" value="${offer.value}" pattern="0.00"/></div>
       			<div class="strong-16 span-3">%<fmt:formatNumber type="number" value="${(1.0 - (offer.price/offer.value))*100}" pattern="0"/>					        			
       			</div>
       		</div>
       		<div class="align-center">
        			<ratecred:azcart 
						itemTitle="${offer.advertiser.name}, ${offer.name}" 
						itemSku="${rater.id}-${offer.id}" 
						itemDescription="${offer.description}" 
						itemPrice="${offer.price}" 
						environment="SANDBOX"  />
        		</div>
 
       		<div class="margin-5 last">
				&nbsp;
			</div>				
		
			
		</div>
		
		
	</div>
	
	
							
</body>         		
</div>
</html>
