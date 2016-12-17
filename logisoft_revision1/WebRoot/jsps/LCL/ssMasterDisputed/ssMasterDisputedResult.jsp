<%-- 
    Document   : ssMasterDisputedResult
    Created on : Nov 10, 2015, 3:43:15 PM
    Author     : aravindhan.v
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="documentsPath" value="${path}/document.do"/>
<c:set var="documentsPath" value="${documentsPath}?action=showDocuments"/>
<c:set var="documentsPath" value="${documentsPath}&screenName=LCL SS MASTER BL"/>
<c:set var="documentsPath" value="${documentsPath}&documentName=SS LINE MASTER BL"/>
<div class="table-banner green">
    <table>
        <tr><td>
        <c:choose>
            <c:when test="${fn:length(ssMasterDisputeList)>=1}">
                ${fn:length(ssMasterDisputeList)}   files found.
            </c:when>
            <c:otherwise>No file found.</c:otherwise>
        </c:choose>
        </td></tr>
    </table>
</div>
<c:if test="${not empty ssMasterDisputeList}">            
    <table class="dataTable" id="masterDisputeResult">      
        <thead>
            <tr>
                <th>Voyage#</th>
                <th>Booking#</th>
                <th>Origin</th>
                <th width="7%">Destination</th>
                <th>POL</th>
                <th>POD</th>
                <th>SSL</th>
                <th>SSL BL</th>
                <th>Status</th>
                <th>Disputed Date</th>
                <th>ETA</th>
                <th>ETD</th>
                <th>ACK</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="dispute" items="${ssMasterDisputeList}">
            <c:choose>
                <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>

            <tr class="${zebra}">
                <td><span class="link ${dispute.voyage eq '' ? 'highlight-saffron' : ''}" 
                          onclick="goToVoyageFromDispute('${path}','${dispute.headerId}','${dispute.serviceType}');">${dispute.voyage}</span></td> 
                <td>${dispute.bookingNo}</td> 
                <td>${dispute.voyageOrigin}</td> 
                <td>${dispute.voyageDest}</td> 
                <td>${dispute.voyageOrigin}</td> 
                <td>${dispute.voyageDest}</td> 
                <td>${dispute.carrierName}</td> 
                <td>${dispute.sslBlPrepaid}</td> 
                <td>Disputed</td> 
                <td>${dispute.disputedDate}</td>
                <td>${dispute.sta}</td> 
                <td>${dispute.std}</td> 
                <td class="align-center">
            <c:choose>
                <c:when test="${not empty dispute.acknowledge}">
                    <img title="${dispute.acknowledge}" width="10" height="10" src="${path}/images/icons/dots/dark-green.gif"/>
                </c:when>
                <c:when test="${roleDuty.takeOwnershipOfDisputedBL}">
                    <img title="Click here to take ownership" width="10" height="10" id="acknowledge${dispute.documentMasterId}"
                         src="${path}/images/icons/dots/red.gif" onclick="acknowledge('${dispute.documentMasterId}','${path}')"/>
                </c:when>
                <c:otherwise>
                    <img title="Click here to take ownership" width="10" height="10" src="${path}/images/icons/dots/red.gif"/>
                </c:otherwise>
            </c:choose>
            </td>
            <td class="align-center">
                <img title="Notes" class="image-16x16" src="${path}/images/icons/contents-view.gif"
                     onclick="openNotes('${path}','${dispute.headerId}','${dispute.voyage}')"/>
                <img title="Preview" class="image-16x16" src="${path}/images/icons/preview.png"
                     onclick="showDocuments('${documentsPath}&documentId=${dispute.documentMasterId}')"/>
            </td>
            </tr>

        </c:forEach>
        </tbody>
    </table>
</c:if>
