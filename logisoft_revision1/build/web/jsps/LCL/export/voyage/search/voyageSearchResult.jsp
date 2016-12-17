<%-- 
    Document   : voyageSearchResult
    Created on : Jun 19, 2015, 10:08:36 AM
    Author     : Mei
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<style>
    .unitTh{
        font-size: 15px;
        font-weight: bold;
    }
</style>
<div id="result-header" class="table-banner green">
    <div class="float-left">
        <c:choose>
            <c:when test="${fn:length(lclVoyageList)>1}">
                ${fn:length(lclVoyageList)} files found.
            </c:when>
            <c:otherwise>1 file found.</c:otherwise>
        </c:choose>
    </div>
</div>
<table class="display-table">
    <thead>
        <tr>
            <th id="scheduleNo" class="${lclUnitsScheduleForm.columnName eq 'scheduleNo' ? lclUnitsScheduleForm.sortBy : ''}">
                <a href="javascript:doSort('scheduleNo')" class="space">Voyage#</a>
            </th>
            <th>MNF</th>
            <th>COB</th>
    <c:if test="${lclUnitsScheduleForm.filterByChanges ne 'lclDomestic'}">
        <th>CL</th>
        <th>AU</th>
    </c:if>
    <th width="10px">Carrier</th>
    <th>Units</th>
    <th>Vessel</th>
    <th class="${lclUnitsScheduleForm.columnName eq 'ssVoyage' ? lclUnitsScheduleForm.sortBy : ''}">
        <a href="javascript:doSort('ssVoyage')" class="space">
            SS Voyage#</a></th>
    <th class="${lclUnitsScheduleForm.columnName eq 'departPierUnloc' ? lclUnitsScheduleForm.sortBy : ''}">
        <a href="javascript:doSort('departPierUnloc')" class="space">
            Departure Pier</a>
    </th>
    <th class="${lclUnitsScheduleForm.columnName eq 'arrivalPierUnloc' ? lclUnitsScheduleForm.sortBy : ''}">
        <a href="javascript:doSort('arrivalPierUnloc')" class="space">
            Arrival Pier</a>
    </th>
    <th>LRD-OVR</th>
    <c:if test="${lclUnitsScheduleForm.filterByChanges ne 'lclDomestic'}">
        <th class="${lclUnitsScheduleForm.columnName eq 'polLrdDates' ? lclUnitsScheduleForm.sortBy : ''}">
            <a href="javascript:doSort('polLrdDates')" class="space">
                POL LRD</a></th>
    </c:if>
    <th class="${lclUnitsScheduleForm.columnName eq 'etaSailDates' ? lclUnitsScheduleForm.sortBy : ''}">
        <a href="javascript:doSort('etaSailDates')" class="space">
            ETD Sailing Date</a></th>
    <th class="${lclUnitsScheduleForm.columnName eq 'etaPodDates' ? lclUnitsScheduleForm.sortBy : ''}">
        <a href="javascript:doSort('etaPodDates')" class="space">
            ETA at POD</a></th>
    <c:if test="${lclUnitsScheduleForm.filterByChanges ne 'lclDomestic'}">
        <th>ETA at FD</th>
        <th>TT-PP</th>
        <th>TT-OF</th>
    </c:if>
    <th>Creator/Owner</th>
    <th>Action</th>
</tr>
</thead>
<tbody>
<c:forEach var="voyage" items="${lclVoyageList}">
    <fmt:parseNumber var="unitCount" integerOnly="true" type="number" value="${voyage.unitcount}" />
    <fmt:parseNumber var="cobUnitCount" integerOnly="true" type="number" value="${voyage.cobUnitCount}" />
    <fmt:parseNumber var="manifestUnitCount" integerOnly="true"  type="number" value="${voyage.manifestUnitCount}" />

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
                      onclick="editVoyage('${path}', '${voyage.ssHeaderId}', '${voyage.serviceType}')">
                    ${voyage.scheduleNo}
                </span>
            </u>
        </td>
        <td align='center'>
            <span class="${manifestUnitCount lt unitCount ? 'red' : manifestUnitCount eq unitCount ? 'green' :''}">
                <b>${manifestUnitCount ne 0 ? manifestUnitCount : ''}</b>
            </span>
        </td>
        <td align='center'>
            <span  class="${cobUnitCount lt unitCount ? 'red' : cobUnitCount eq unitCount ? 'green' :''}">
                <b >${cobUnitCount ne 0 ? cobUnitCount : ''}</b>
            </span>
        </td>
    <c:if test="${lclUnitsScheduleForm.filterByChanges ne 'lclDomestic'}">
        <td >
        <c:if test="${voyage.closedStatus ne ''}">
            <span style="cursor: pointer;" class="blueBold"
                  title="Closed By: ${voyage.closedBy}<br/>On: ${voyage.closedOn}<br/>Remarks: ${voyage.closedRemarks}">
                ${voyage.closedStatus}
            </span>
        </c:if>
        </td>
        <td>
        <c:if test="${voyage.auditedStatus ne ''}">
            <span style="cursor: pointer; color: purple;font-weight: bold"
                  title="Audited By: ${voyage.auditedBy}<br/>On: ${voyage.auditedOn}<br/>Remarks: ${voyage.auditedRemarks}">
                ${voyage.auditedStatus}
            </span>
        </c:if>
        </td>
    </c:if>
    <td><span title="${voyage.carrierAcctNo}">${voyage.carrierName}</span></td>
    <td align='center'>

    <c:if test="${voyage.unitcount ne 0}">
        <span title="<table><tr><td class='unitTh'>Unit #</td><td class='unitTh'>Size</td><td class='unitTh'>Status</td><td class='unitTh'>ETA</td><td class='unitTh'>Verified ETA</td></tr><tr><td>${voyage.unitNo}</td></tr></table>">
            <b>${voyage.unitcount}</b>
        </span>
    </c:if>

    </td>
    <td>${voyage.vesselName}</td>
    <td class="bold">${voyage.ssVoyage}</td>
    <td><span title="${voyage.departPier}">${voyage.departPierUnloc}</span></td>
    <td><span title="${voyage.arrivalPier}">${voyage.arrivalPierUnloc}</span></td>
    <td align='center'>${voyage.lrdOverrideDays}</td>
    <c:if test="${lclUnitsScheduleForm.filterByChanges ne 'lclDomestic'}">
        <td>${voyage.polLrdDate}</td>
    </c:if>
    <td>${voyage.etaSailDate}</td>
    <td>${voyage.etaPodDate}
    <c:if test="${not empty voyage.verifiedEta && voyage.verifiedEta ne 0&& voyage.verifiedEta ne 2}">
        <span style="font-size: 11px;font-weight: bold" class="${voyage.verifiedEta eq 1 ?'green':voyage.verifiedEta eq 4 ?'red':'red'}">(VER)</span>
    </c:if>
    </td>
    <c:if test="${lclUnitsScheduleForm.filterByChanges ne 'lclDomestic'}">
        <fmt:formatDate pattern="dd-MMM-yyyy" var="etaFd" value="${voyage.etafdDate}"></fmt:formatDate>
        <td>${etaFd}</td>
        <td align='center'>${voyage.totaltransPod}</td>
        <td align='center'>${voyage.totaltransFd}</td>
    </c:if>
    <td style="text-transform: uppercase">${voyage.createdBy} / ${voyage.voyOwner}</td>
    <td>
        <img alt="Copy" id="copy" title="Copy Voyage" src="${path}/img/copy_icon.gif" style="vertical-align: middle"
             onclick="copyVoyage('${path}', '${voyage.ssDetailId}')"/>
    <c:if test="${voyage.dataSource eq 'L' && roleDuty.expDeleteVoyage}">
        <img src="${path}/jsps/LCL/images/close1.png" width="14" height="14" alt="Delete" title="Delete Voyage"
             style="vertical-align: middle" onclick="deleteVoyage('${voyage.ssHeaderId}')"/>
    </c:if>
    </td>
    </tr>
</c:forEach>
</tbody>
</table>
