<%-- 
    Document   : resultsHeader
    Created on : Mar 6, 2013, 4:06:36 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${searchForm.action == 'search'}">
	<div class="search-fields float-left" style="width: 40%">
	    <c:if test="${not empty searchForm.fileNumber}">
		File Number->${searchForm.fileNumber},
	    </c:if>
	    <c:if test="${not empty searchForm.origin}">
		Origin->${searchForm.origin},
	    </c:if>
	    <c:if test="${not empty searchForm.pol}">
		POL->${searchForm.pol},
	    </c:if>
	    <c:if test="${not empty searchForm.pod}">
		POD->${searchForm.pod},
	    </c:if>
	    <c:if test="${not empty searchForm.destination}">
		Destination->${searchForm.destination},
	    </c:if>
	    <c:if test="${not empty searchForm.originRegionDesc}">
		Origin Region->${searchForm.originRegionDesc},
	    </c:if>
	    <c:if test="${not empty searchForm.destinationRegionDesc}">
		Destination Region->${searchForm.destinationRegionDesc},
	    </c:if>
	    <c:choose>
		<c:when test="${searchForm.disableClient && not empty searchForm.manualClientName}">
		    Client->${searchForm.manualClientName},
		</c:when>
		<c:when test="${not empty searchForm.clientName}">
		    Client->${searchForm.clientName},
		</c:when>
	    </c:choose>
	    <c:if test="${not empty searchForm.shipperName}">
		Shipper->${searchForm.shipperName},
	    </c:if>
	    <c:if test="${not empty searchForm.forwarderName}">
		Forwarder->${searchForm.forwarderName},
	    </c:if>
	    <c:if test="${not empty searchForm.consigneeName}">
		Consignee->${searchForm.consigneeName},
	    </c:if>
	    <c:if test="${not empty searchForm.sslName}">
		SSL->${searchForm.sslName},
	    </c:if>
	    <c:if test="${not empty searchForm.filterBy}">
		Filter By->${searchForm.filterBy},
	    </c:if>
	    <c:if test="${not empty searchForm.issuingTerminal}">
		Issuing Terminal->${searchForm.issuingTerminal},
	    </c:if>
	    <c:if test="${not empty searchForm.containerNumber}">
		Container->${searchForm.containerNumber},
	    </c:if>
	    <c:if test="${not empty searchForm.createdBy}">
		Created By->${searchForm.createdBy},
	    </c:if>
	    <c:if test="${not empty searchForm.bookedBy}">
		Booked By->${searchForm.bookedBy},
	    </c:if>
	    <c:if test="${not empty searchForm.fromDate}">
		Start Date->${searchForm.fromDate},
	    </c:if>
	    <c:if test="${not empty searchForm.toDate}">
		To Date->${searchForm.toDate},
	    </c:if>
	    <c:if test="${not empty searchForm.sslBookingNumber}">
		SSL Booking #->${searchForm.sslBookingNumber},
	    </c:if>
	    <c:if test="${not empty searchForm.sortByDate}">
		Sort By->${searchForm.sortByDate}
	    </c:if>
	    Limit->${searchForm.limit}
	</div>
	<div class="float-left">
	    <input type="button" value="Quick Rates" class="button" onclick="showQuickRates('${sessionScope.importNavigation}');"/>
	    <c:if test="${sessionScope.quoteAccessMode}">
		<input type="button" value="New Quote" class="button" onclick="newQuote();"/>
	    </c:if>
	    <c:if test="${sessionScope.bookingAccessMode}">
		<input type="button" value="New Booking" class="button" onclick="newBooking();"/>
	    </c:if>
	    <c:if test="${sessionScope.blAccessMode}">
		<input type="button" value="New Arrival Notice" class="button" onclick="newArrivalNotice();"/>
	    </c:if>
	    <input type="button" value="Go Back" class="button" onclick="gotoSearch();"/>
            <c:if test="${empty sessionScope.importNavigation}">
             <input type="button" value="Multi Quote" class="button" onclick="multiQuote();"> 
            </c:if>	  
	</div>
	<div class="label float-right align-right">
	    File Search:&nbsp;&nbsp;&nbsp;
	    <input type="text" name="fileNumber" value="" class="textbox" id="fileNumber" value="${searchForm.fileNumber}"/>
	    <img src="${path}/images/icons/magnifier.png" onclick="searchByFileNumber()"/>
	</div>
    </c:when>
    <c:otherwise>
	&nbsp;
    </c:otherwise>
</c:choose>