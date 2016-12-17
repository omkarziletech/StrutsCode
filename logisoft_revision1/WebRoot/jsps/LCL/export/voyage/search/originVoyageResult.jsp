<%-- 
    Document   : originVoyageResult
    Created on : Jun 19, 2015, 11:39:01 AM
    Author     : Mei
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<div id="result-header" class="table-banner green">
    <div class="float-left">
        <c:choose>
            <c:when test="${fn:length(viewAllVoyageList)>1}">
                ${fn:length(viewAllVoyageList)} files found.
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
            <th>Size</th>
            <th>Origin</th>
            <th>Destination</th>
            <th>Trucking Info</th>
            <th>Dispo</th>
            <th>Inbond</th>
            <th>Haz</th>
            <th>POL LRD</th>
            <th>ETD Sailing Date</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="voyage" items="${viewAllVoyageList}">
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
                              class="${goBackVoyNo eq voyage.scheduleNo ? "highlight-saffron" :''}"
                              onclick="editViewAll('${path}', '${voyage.ssHeaderId}')">
                            ${voyage.scheduleNo}
                        </span>
                    </u>
                </td>
                <td>${voyage.unitNo}</td>
                <td>${voyage.unitSize}</td>
                <td>${voyage.pooName}</td>
                <td>${voyage.fdName}</td>

                <td> <span title="${voyage.unitTrackingNotes}">${fn:substring(voyage.unitTrackingNotes,0,40)}</span></td>
                <td title="${voyage.dispoDesc}">${voyage.dispoCode}</td>
                <td>
                    <c:if test="${voyage.isInbond!='' && voyage.isInbond!= null}">
                        <img src="${path}/img/icons/yellow.gif" width="12" height="12" title="InBond"/>
                    </c:if>
                </td>
                <td>
                    <c:if test="${voyage.isHazmat!='' && voyage.isHazmat!= null}">
                        <img src="${path}/img/icons/danger..png" width="12" height="12" title="Hazardous"/>
                    </c:if>
                </td>
                <td>${voyage.polLrdDate}</td>
                <td>${voyage.etaSailDate}</td>
                <td>
                    <input type="button" class="buttonStyleNew" id="notes" value='Notes' onclick=""/>
                    <input type="button" class="buttonStyleNew" id="PrintFaxEmail" value='Print/Fax/Email' onclick=""/>
                    <input type="button" class="buttonStyleNew" id="scanAttach" value='Scan/Attach' onclick=""/>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>