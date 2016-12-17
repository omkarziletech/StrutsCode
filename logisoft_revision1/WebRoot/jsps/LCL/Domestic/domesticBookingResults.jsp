<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@include file="../../includes/baseResources.jsp"%>
<c:choose>
    <c:when test="${not empty domesticBookingList}">
	<div class="result-container">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
                        <th>Q</th>
                        <th>BK</th>
                        <th>BL</th>
			<th>Booking Number</th>
			<th>Origin</th>
			<th>Destination</th>
			<th>Carrier Name</th>
			<th>Pro Number</th>
			<th>Direct/Interline</th>
			<th>Estimated Days</th>
			<th>Type</th>
			<th>Line Haul</th>
			<th>Fuel Charge</th>
			<th>Extra Charge</th>
			<th>Final Charge</th>
                        <th>Booked By</th>

			<th>Action</th>
		    </tr>
		</thead>
		<tbody>
                    <c:set var="notesPath" value="${path}/notes.do"/>
		    <c:set var="notesPath" value="${notesPath}?moduleId=DOMESTIC"/>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="booking" items="${domesticBookingList}">
			<tr class="${zebra}">
                            <td>
                                <c:if test="${empty booking.bookedOn}">
                                    <img src="${path}/img/icons/yellow.gif" border="0" />
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${not empty booking.bookedOn}">
                                    <img src="${path}/img/icons/yellow.gif" border="0" />
                                </c:if>
                            </td>
                            <td></td>
			    <td>${booking.bookingNumber}</td>
			    <td>${booking.origin}</td>
			    <td>${booking.destination}</td>
			    <td>${booking.carrierName}</td>
			    <td>${booking.proNumber}</td>
			    <td>${booking.directInterline}</td>
			    <td>${booking.estimatedDays}</td>
			    <td>${booking.type}</td>
			    <td>${booking.lineHual}</td>
			    <td>${booking.fuelCharge}</td>
			    <td>${booking.extraCharge}</td>
			    <td>${booking.finalCharge}</td>
			    <td>${booking.bookedBy.firstName}</td>
                            <c:set var="bolButton" value="buttonStyleNew"/>
                            
                            <td class="align-center">
				<img src="${path}/images/icons/edit.gif" title="Edit" onclick="editBooking('${booking.id}')"/>
                                <img title="Notes" class="image-16x16" src="${path}/images/icons/contents-view.gif"
				     onclick="showNotes('${notesPath}&itemName=Domestic Booking&moduleRefId=${booking.id}')"/>
                                <img src="${path}/images/icons/preview.png" title="Preview" onclick="createPreview('${booking.id}')"/>
                                <c:if test="${booking.bolStatus == 'E'}">
                                    <c:if test="${not empty booking.statusMessage}">
                                            <span onmouseover="tooltip.showSmall('<strong>${booking.statusMessage}</strong>');" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                <img src="${path}/images/icons/dots/red-cross.png"  />
                                            </span>
                                       </c:if>
                                </c:if>
                                <c:if test="${not empty booking.bolStatus && not empty booking.proNumber && not empty booking.carrierNemonic}">
                                    <img src="${path}/images/update.png" title="Track" onclick="trackBol('http://connect.track-trace.com/for/primary/${booking.carrierNemonic}/${booking.proNumber}')"/>
                                </c:if>
			    </td>
			</tr>
			<c:choose>
			    <c:when test="${zebra=='odd'}">
				<c:set var="zebra" value="even"/>
			    </c:when>
			    <c:otherwise>
				<c:set var="zebra" value="odd"/>
			    </c:otherwise>
			</c:choose>
		    </c:forEach>
		</tbody>
	    </table>
	</div>
    </c:when>
    <c:otherwise>
	<div class="table-banner green" style="background-color: #D1E6EE;">No Booking found</div>
    </c:otherwise>
</c:choose>
