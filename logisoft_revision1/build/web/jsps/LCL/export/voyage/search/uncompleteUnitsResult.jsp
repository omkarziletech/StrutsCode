<%-- 
    Document   : uncompleteUnitsResult
    Created on : Sep 5, 2015, 9:11:41 PM
    Author     : aravindhan.v
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<div id="result-header" class="table-banner green">
    <div class="float-left">
        <c:choose>
            <c:when test="${fn:length(uncompleteunitsList)>1}">
                ${fn:length(uncompleteunitsList)} files found.
            </c:when>
            <c:otherwise>1 file found.</c:otherwise>
        </c:choose>
    </div>
</div>
<table class="display-table">
    <thead>
        <tr>
            <th>Voyage#</th>
            <th>Unit#</th>
            <th>Dispo</th>
            <th>Loaded By</th>
            <th>Loading Deadline Date</th>
            <th>Size</th>
            <th>Seal</th>
            <th>NumDRs</th>
            <th>Inbond</th>
            <th>Haz</th>
            <th>Door</th>
            <th>POD</th>
            <th>Carrier</th>
            <th>Vessel</th>
            <th>SS Voyage#</th>
            <th>DepPier</th>
            <th>ArrPier</th>
            <th>ETD Sailing Date</th>
            <th>ETA at POD</th>
            <th>TT-PP</th>
            <th>TT-OF</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="voyage" items="${uncompleteunitsList}">
            <c:choose>
                <c:when test="${rowStyle eq 'oddStyle'}">
                    <c:set var="rowStyle" value="evenStyle"/>
                </c:when>
                <c:otherwise>
                    <c:set var="rowStyle" value="oddStyle"/>
                </c:otherwise>
            </c:choose>
            <tr class="${rowStyle}">
                <td>
                    <u>
                        <span style="height: 10px;cursor: pointer;"
                              class="link ${goBackVoyNo eq voyage.scheduleNo ? "highlight-saffron" :''}"
                              onclick="editVoyage('${path}','${voyage.ssHeaderId}','${voyage.serviceType}')">
                            ${voyage.scheduleNo}
                        </span>
                    </u>
                </td>
                <td>${voyage.unitNo}</td>
                <td><span title="${voyage.dispoDesc}">${voyage.dispoCode}</span></td>
                <td>${voyage.loadedBy}</td>
                <td>${voyage.loadingDeadLineDate}</td>
                <td><span title="${voyage.unitSize}">${fn:substring(voyage.unitSize,0,5)}</span></td>
                <td>${voyage.sealNo}</td>
                <td align="center">${voyage.numberDrs ne 0 ? voyage.numberDrs:''}</td>
                <td>
                    <c:if test="${voyage.isInbond!='' && voyage.isInbond!= null}">
                        <img src="${path}/img/icons/yellow.gif" width="12" height="12" alt="Inbond" title="InBond"/>
                    </c:if>
                </td>
                <td>
                    <c:if test="${voyage.isHazmat!='' && voyage.isHazmat!= null}">
                        <img src="${path}/img/icons/danger..png" width="12" height="12" alt="haz" title="Hazardous"/>
                    </c:if>
                </td>
                
                <td>${voyage.doorLocation}</td>
                <td><span title="${voyage.fdName}">${voyage.fdUnLocCode}</span></td>
                <td><span title="${voyage.carrierName}<br/>${voyage.carrierAcctNo}">${fn:substring(voyage.carrierName,0,15)}</span></td>
                <td>${voyage.vesselName}</td>
                <td><span title="${voyage.ssVoyage}">${fn:substring(voyage.ssVoyage,0,10)}</span></td>
                <td><span title="${voyage.departPier}">${voyage.departPierUnloc}</span></td>
                <td><span title="${voyage.arrivalPier}">${voyage.arrivalPierUnloc}</span></td>
                <td>${voyage.etaSailDate}</td>
                <td>${voyage.etaPodDate}</td>
                <td align="center">${voyage.totaltransPod}</td>
                <td align="center">${voyage.totaltransFd}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>


