<%-- 
    Document   : results
    Created on : Sep 25, 2015, 8:16:24 PM
    Author     : Mei
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center" id="voyagetable">
    <tr>
        <td>
    <c:if test="${not empty voyageList}">
        <div id="result-header" class="table-banner green">
            <div class="float-left">
                <c:choose>
                    <c:when test="${fn:length(voyageList)>1}">
                        ${fn:length(voyageList)} files found.
                    </c:when>
                    <c:otherwise>1 file found.</c:otherwise>
                </c:choose>
            </div>
        </div>
        <table class="display-table">
            <thead>
                <tr>
                    <th>Agent Acct</th>
                    <th>Terminal Location</th>
                    <th>UnitNo#</th>
                    <th>Disp</th>
                    <th>CL / AU</th>
                    <th><a href="javascript:doSort('voyageNo')">File#</a></th>
                    <th>EDI</th>
                    <th>Carrier</th>
                    <th>Vessel</th>
                    <th>SS Voyage</th>
                    <th>POL</th>
                    <th>Dep Pier</th>
                    <th>Arr Pier</th>
                    <th>POD</th>
                    <th>Sailing Date</th>
                    <th><a href="javascript:doSort('etaAtPod')">ETA at POD</a></th>

                    <th>Stripped Date</th>

                    <th>TT</th>
                    <th>Creator/Owner</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody style="text-transform: uppercase;">
            <c:forEach var="voyage" items="${voyageList}">
                <c:choose>
                    <c:when test="${rowStyle eq 'odd'}">
                        <c:set var="rowStyle" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="rowStyle" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <tr class="${rowStyle}">
                    <td>
                        <%-- Agent Acct --%>
                        <span title="<strong>${voyage.agentName}<br>${voyage.agentAcct}</strong>">
                            ${voyage.agentAcct}
                        </span>
                    </td>
                    <td>
                        <%-- Billing terminal --%>
                        ${voyage.terminal}
                    </td>
                    <td>
                        <%-- Unit No --%>
                        ${voyage.unitNo}
                <c:if test="${voyage.hazmatPermitted eq '1'}">
                    <img src="${path}/img/icons/danger..png" style="cursor:pointer" width="12" height="12" title="Hazardous Information"/>
                </c:if>

                </td>
                <td>
                    <%-- Disposition --%>
                <c:choose>
                    <c:when test="${voyage.dispoCode eq 'DATA'|| voyage.dispoCode eq 'WATR'}">
                        <span style="cursor: pointer;" class="blueBold" title="<strong>${voyage.dispoDesc}</strong>">
                            ${voyage.dispoCode}
                        </span>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${voyage.dispoCode eq 'AVAL'}">
                                <span style="cursor: pointer;" class="greenBold" title="<strong>${voyage.dispoDesc}</strong>">
                                    ${voyage.dispoCode}
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span style="cursor: pointer;" id="orangeBold12px" title="<strong>${voyage.dispoDesc}</strong>">
                                    ${voyage.dispoCode}
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </td><td>
        <c:if test="${voyage.closedStatus ne ''}">
            <span style="cursor: pointer;" class="blueBold"
                  title="Closed By: ${voyage.closedBy}<br/>On: ${voyage.closedOn}<br/>Remarks: ${voyage.closedRemarks}">
                ${voyage.closedStatus}
            </span>
        </c:if>
        <c:if test="${voyage.auditedStatus ne ''}">
            <span style="cursor: pointer; color: purple;font-weight: bold"
                  title="Audited By: ${voyage.auditedBy}<br/>On: ${voyage.auditedOn}<br/>Remarks: ${voyage.auditedRemarks}">
                &nbsp;${voyage.auditedStatus}
            </span>
        </c:if>
        </td>
        <td>
            <%-- Voyage No --%>
        <c:choose>
            <c:when test="${voyage.dataSource eq 'L'}">
                <u>
                    <span style="height: 10px;cursor: pointer;" class="link ${goBackVoyNo eq voyage.scheduleNo ? "highlight-saffron" :''}"
                          onclick="editVoyage('${path}', '${voyage.ssHeaderId}')">
                        ${voyage.scheduleNo}
                    </span>
                </u>
            </c:when>
            <c:otherwise>
                ${voyage.scheduleNo}
            </c:otherwise>
        </c:choose>
        </td>
        <td>
        <c:if test="${not empty voyage.eculineVoyage}">
            <img src="${path}/img/icons/edistar.png" height="10px" width="10px" title="Eculine EDI" alt="eculineEDIIcon"/>
        </c:if>
        </td>
        <td>
            <%--Carrier --%>
            <span title="<strong>${voyage.carrierName}<br>${voyage.carrierAcctNo}</strong>">
                ${fn:substring(voyage.carrierName,0,20)}
            </span>
        </td>
        <td>
            <%--Vessel --%>
            ${voyage.vesselName}
        </td>
        <td>
            <%--SS Voyage# --%>
            ${voyage.ssVoyage}
        </td>
        <td>
            <%-- POL --%>
            <span title="${voyage.originName}">
                ${voyage.originUnCode}
            </span>
        </td>
        <td>
            <%-- Departure Pier --%>
            <span title="${voyage.departPier}">
                ${voyage.departPierUnloc}
            </span>
        </td>
        <td>
            <%-- Arrival Pier --%>
            <span title="${voyage.arrivalPier}">
                ${voyage.arrivalPierUnloc}
            </span>
        </td>
        <td>
            <%-- POD --%>
            <span title="${voyage.fdName}">
                ${voyage.fdUnCode}
            </span>
        </td>
        <td>
            <%-- ETD Sailing DATE --%>
            ${voyage.etaSailDate}
        </td>
        <td>
            <%-- ETA at POD --%>
            ${voyage.etaPodDate}
        </td>
        <td>     
            <%-- Stripped Date --%>
            ${voyage.strippeddate}
        </td>
        <td>
            <%-- Transit Days --%>
            ${voyage.totaltransPod}
        </td>
        <td>
            <%-- Creator/Owner --%>
            ${voyage.createdBy} / ${voyage.voyOwner}
        </td>
        <td>
            <%-- Action --%>
            <img alt="Copy" id="copy" title="Copy Voyage" src="${path}/img/copy_icon.gif" style="vertical-align: middle"
                 onclick="copyVoyage('${path}', '${voyage.ssHeaderId}', '${voyage.ssDetailId}', '${voyage.unitId}');"/>
        <c:if test="${voyage.dataSource eq 'L' && roleDuty.deleteVoyage}">
            <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="14" width="14" style="vertical-align: middle"
                 onclick="deleteVoyageList('${voyage.ssHeaderId}');" title="Delete Voyage"/>
        </c:if>
        </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</c:if>
</td>
</tr>
</table>
