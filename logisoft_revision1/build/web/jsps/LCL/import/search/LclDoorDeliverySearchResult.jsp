<%-- 
    Document   : LclDoorDeliverySearchResult
    Created on : Jun 8, 2016, 3:59:52 PM
    Author     : PALRAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center" id="result-table">
    <tr>
        <td>
<div id="result-header" class="table-banner green">
    <c:choose>
        <c:when test="${fn:length(lclDoorDeliverySearchResult)>=1}">
            ${fn:length(lclDoorDeliverySearchResult)} files found.
        </c:when>
        <c:otherwise>0 file found.</c:otherwise>
    </c:choose>
</div>

<table class="display-table">
    <thead>
        <tr>
            <th>Voyage</th>
            <th id="pol" class="${lclDoorDeliverySearchForm.orderBy eq 'polUnCode' ? lclDoorDeliverySearchForm.sortBy : ''}">
                <a href="javascript:doSort('polUnCode')">POL</a></th>
            <th id="pod" class="${lclDoorDeliverySearchForm.orderBy eq 'podUnCode' ? lclDoorDeliverySearchForm.sortBy : ''}">
                <a href="javascript:doSort('podUnCode')">POD</a></th>
            <th id="etaDate" class="${lclDoorDeliverySearchForm.orderBy eq 'etaDate' ? lclDoorDeliverySearchForm.sortBy : ''}">
                <a href="javascript:doSort('etaDate')">ETA</a></th>
            <th id="dispoCode" class="${lclDoorDeliverySearchForm.orderBy eq 'dispoCode' ? lclDoorDeliverySearchForm.sortBy : ''}">
                <a href="javascript:doSort('dispoCode')">DISP</a></th>
            <th>LFD</th>
            <th>DR#</th>
            <th>HBL#</th>
            <th>CCR</th>
            <th>DO</th>
            <th>COM</th>
            <th>Lift Gate</th>
            <th>HAZ</th>
            <th>EPD</th>
            <th>APD</th>
            <th>EDD</th>
            <th>ADD</th>
            <th>Sell</th>
            <th>Buy</th>
            <th>Profit</th>
            <th>Status</th>
            <th>Carrier</th>
            <th>PRO#</th>
            <th>Need POD</th>
            <th>ZipCode</th>
            <th>City</th>
            <th>Action</th>
        </tr>  
    </thead>
    <tbody>
    <c:forEach items="${lclDoorDeliverySearchResult}" var="doorDelivery">
        <c:choose>
            <c:when test="${rowStyle eq 'odd'}">
                <c:set var="rowStyle" value="even"/>
            </c:when>
            <c:otherwise>
                <c:set var="rowStyle" value="odd"/>
            </c:otherwise>
        </c:choose>
        <tr class="${rowStyle}">
            <td>${doorDelivery.voyageNo}</td>
            <td><span title="${doorDelivery.polUnCode}/${doorDelivery.pol}">${doorDelivery.polUnCode}</span></td>
            <td><span title="${doorDelivery.podUnCode}/${doorDelivery.pod}">${doorDelivery.podUnCode}</span></td>
            <td>${doorDelivery.etaDate}</td>
            <td><span style="cursor:pointer" title="${doorDelivery.dispoDesc}">${doorDelivery.dispoCode}</span></td>
            <td>${doorDelivery.lastFreeDate}</td>
            <td>
                <u>
                    <span class="link ${doorDelivery.fileNumber eq fileNumber ? 'highlight-saffron' : ''}"
                     onclick="checkLock('${path}', '${doorDelivery.fileNumber}', '${doorDelivery.fileNumberId}', '${doorDelivery.state}', 'Imports', '${doorDelivery.transshipment}', 'LCLIMPDDP');">
                                    IMP-${doorDelivery.fileNumber}   
                    </span>
                </u>  
            </td>
            <td>${doorDelivery.houseBl}</td>
            <td>${doorDelivery.customsClearanceReceived}</td>
            <td>${doorDelivery.deliveryOrderReceived}</td>
            <td>${doorDelivery.deliveryCommercial}</td>
            <td>${doorDelivery.liftGate}</td>
            <td>
            <c:if test="${doorDelivery.hazmat eq true}">
            <img src="${path}/img/icons/danger..png" style="cursor:pointer" width="12" height="12" title="Hazardous Information"/>
            </c:if>
            </td>
            <td>${doorDelivery.pickupEstDate}</td>
            <td>${doorDelivery.pickedUpDateTime}</td>
            <td>${doorDelivery.deliveryEstDate}</td>
            <td>${doorDelivery.deliveredDateTime}</td>
            <td>${doorDelivery.sell}</td>
            <td>${doorDelivery.buy}</td>
            <td><fmt:formatNumber value="${doorDelivery.sell - doorDelivery.buy}" maxFractionDigits="2"/></td>
            <td><span title="${doorDelivery.doorDeliveryDesc}">${fn:substring(doorDelivery.doorDeliveryDesc,0,15)}</span></td>
            <td><span title="${not empty doorDelivery.carrierName ? doorDelivery.carrierName : doorDelivery.scacCode}">${fn:substring(doorDelivery.scacCode, 0, 4)}</span></td>
            <td>${doorDelivery.pickupReferenceNo}</td>
            <td>${doorDelivery.needPod}</td>
            <td>${doorDelivery.zipCode}</td>
            <td>${doorDelivery.city}</td>
            <td><img title="Notes" src="${path}/images/icons/notes.png" class="notes"
                                     onclick="displayNotesPopUp('${path}', '${doorDelivery.fileNumberId}', '${doorDelivery.fileNumber}', 'Imports');"/></td>
        </tr> 
    </c:forEach>
</tbody>
</table>
        </tr>
        </td>
</table>
