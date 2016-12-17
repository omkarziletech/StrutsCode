<%-- 
    Document   : voyageDetailsResult
    Created on : Jun 19, 2015, 5:22:43 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<table align="center" id="ssdetailTable"  cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc;">
    <tr>
        <td class="tableHeadingNew">Voyage Details
    <c:if test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
        <div class="button-style1" style="float: right" id="adddetailsline" onclick="addDetails('${path}');">Add Details Line</div>&nbsp;
    </c:if>
    <c:if test="${lclAddVoyageForm.filterByChanges eq 'lclDomestic' || filterValues eq 'lclDomestic'}">
        <div class="button-style1" id="stopoffs" style="float: right" onclick="addStopOff('${path}');">Stop-offs</div>
    </c:if>
</td>
</tr>
<tr>
    <td>
<c:if test="${not empty lclssdetailsList}">
    <div id="divtablesty1" style="width: 100%;">
        <display:table name="${lclssdetailsList}" class="dataTable" pagesize="20"  id="lclssDetail" sort="list" >
            <display:setProperty name="paging.banner.some_items_found">
                <span class="pagebanner"><font color="blue">{0}</font>LCL Voyage Details displayed,For more Records click on page numbers.</span>
            </display:setProperty>
            <display:setProperty name="paging.banner.one_item_found">
                <span class="pagebanner">One {0} displayed. Page Number</span>
            </display:setProperty>
            <display:setProperty name="paging.banner.all_items_found">
                <span class="pagebanner">{0} {1} Displayed, Page Number</span>
            </display:setProperty>
            <display:setProperty name="basic.msg.empty_list">
                <span class="pagebanner">No Records Found.</span>
            </display:setProperty>
            <display:setProperty name="paging.banner.placement" value="bottom"/>
            <display:setProperty name="paging.banner.item_name" value="Detail"/>
            <display:setProperty name="paging.banner.items_name" value="Details"/>
            <c:set var="count" value="0"/>
            <c:set var="detailId" value="${lclssDetail.id}"/>
            <c:set var="destination" value="${lclssDetail.departure.unLocationName}/${lclssDetail.departure.stateId.code}(${lclssDetail.departure.unLocationCode})"/>
            <c:set var="origin" value="${lclssDetail.arrival.unLocationName}/${lclssDetail.arrival.stateId.code}(${lclssDetail.arrival.unLocationCode})"/>
            <fmt:formatDate pattern="dd-MMM-yyyy" var="std" value="${lclssDetail.std}"></fmt:formatDate>
            <fmt:formatDate pattern="dd-MMM-yyyy" var="etaPod" value="${lclssDetail.sta}"></fmt:formatDate>
            <fmt:formatDate pattern="dd-MMM-yyyy" var="loadDeadlineDate" value="${lclssDetail.generalLoadingDeadline}"></fmt:formatDate>
            <fmt:formatDate pattern="dd-MMM-yyyy" var="hazloadDeadlineDate" value="${lclssDetail.hazmatLoadingDeadline}"></fmt:formatDate>
            <fmt:formatDate pattern="dd-MMM-yyyy" var="sslDocsCutoffDate" value="${lclssDetail.atd}"></fmt:formatDate>
            <display:column title="Carrier" property="spAcctNo.accountName" headerClass="sortable" />
            <display:column title="Vessel" property="spReferenceName" headerClass="sortable" />
            <display:column title="SS Voyage#" property="spReferenceNo" headerClass="sortable" />
            <c:if test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
                <display:column title="Agent Acct">
                    <span style="cursor:pointer" title="${lclssheader.lclSsExports.exportAgentAcctoNo.accountName} <br/>
                          ${lclssheader.lclSsExports.exportAgentAcctoNo.accountno}">
                        ${lclssheader.lclSsExports.exportAgentAcctoNo.accountno}
                    </span>
                </display:column>
            </c:if>
            <c:choose>
                <c:when test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
                    <display:column title="Departure<br>Pier" headerClass="sortable" style="width:10Px;">
                        <span style="cursor:pointer" title="${destination}">
                            ${lclssDetail.departure.unLocationCode}
                        </span>
                    </display:column>
                    <display:column title="Arrival<br>Pier/POD" headerClass="sortable">
                        <span style="cursor:pointer" title="${origin}">
                            ${lclssDetail.arrival.unLocationCode}
                        </span>
                    </display:column>
                    <display:column title="ETD Sailing<br>Date" headerClass="sortable" class="boldBlue"><span>${std}</span></display:column>
                    <display:column title="ETA at POD" headerClass="sortable"><span>${etaPod}</span></display:column>
                </c:when>
                <c:otherwise>
                    <display:column title="Departure<br>Pier" headerClass="sortable" style="width:10Px;">
                        <span style="cursor:pointer" title="${destination}">
                            ${lclssDetail.departure.unLocationCode} 
                        </span>
                    </display:column>
                    <display:column title="Arrival<br>Pier/POD" headerClass="sortable">
                        <span style="cursor:pointer" title="${origin}">
                            ${lclssDetail.arrival.unLocationCode} 
                        </span>
                    </display:column>
                    <display:column title="ETD" headerClass="sortable" class="boldBlue">${std}</display:column>
                    <display:column title="ETA" headerClass="sortable">${etaPod}</display:column>
                </c:otherwise>
            </c:choose>
            <display:column title="Loading Deadline<br> Date" headerClass="sortable">${loadDeadlineDate}</display:column>
            <display:column title="HazLoad Deadline<br> Date" headerClass="sortable">${hazloadDeadlineDate}</display:column>
            <c:if test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
                <display:column title="SSL Docs<br> Cutoff Date" headerClass="sortable">${sslDocsCutoffDate}</display:column>
            </c:if>
            <display:column title="Transit<br>Days Override" property="relayTtOverride" headerClass="sortable" />
            <display:column title="LRD<br>Override Days" property="relayLrdOverride" headerClass="sortable" />
            <display:column title="Transit<br> Mode" property="transMode" headerClass="sortable" />
            <display:column title="Action">
                <div  class="button-style1" id="notes" onclick="openHeaderNotes('${path}', '${lclssheader.id}', '${lclssheader.scheduleNo}')">
                    Notes
                </div>              
                <c:if test="${lclAddVoyageForm.filterByChanges=='lclDomestic'}" >
                    <c:choose>
                        <c:when test="${lclssDetail.detailStatus eq null || lclssDetail.detailStatus eq 'Open'}">
                            <c:set var="openButtonValue" value="Open"/>
                            <c:set var="openButtonColor" value="background:#8DB7D6"/>
                            <c:set var="loadButtonvisibleMode" value="display:none;"/>
                        </c:when>
                        <c:when test="${lclssDetail.detailStatus eq 'Close'}">
                            <c:set var="openButtonValue" value="${lclssDetail.detailStatus}"/>
                            <c:set var="openButtonColor" value="background:red"/>
                            <c:set var="loadButtonvisibleMode" value="display:block;"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="openButtonValue" value="${lclssDetail.detailStatus}"/>
                            <c:set var="openButtonColor" value="background:green"/>
                            <c:set var="loadButtonvisibleMode" value="display:none;"/>
                        </c:otherwise>
                    </c:choose>
                    <div class="button-style1 openbutton" style="${openButtonColor}"  id="detailOpenButton${index}" onclick="openButton('${path}', '${index}', '${lclssDetail.id}');">${openButtonValue}</div>
                    <div class="button-style1" style="${loadButtonvisibleMode}" id="detailLoadButton${index}"
                         onclick="detailLoad('${path}', '${lclssDetail.departure.id}', '${lclssDetail.arrival.id}', '${lclssheader.id}', '${lclAddVoyageForm.filterByChanges}', '${lclssheader.scheduleNo}', '${index}', '${lclssDetail.id}');">Load</div>
                </c:if>
                <span title="Edit Voyage Details">
                    <a style="cursor: pointer;" onclick="editDetails('${path}', '${lclssDetail.id}')">
                        <img src="${path}/images/edit.png" width="16" height="16"/>
                    </a>
                </span>
                <c:if test="${roleDuty.roleName eq 'Admin' && lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
                    <span style="cursor:pointer" title="Delete Voyage Details">
                        <img src="${path}/images/error.png" width="16" height="16" onclick="deleteSSDetail('${lclssDetail.id}')"/>
                    </span>
                </c:if>
            </display:column>
            <c:set var="ediCompanyName" value="${not empty lclssDetail.spAcctNo.generalInfo ? lclssDetail.spAcctNo.generalInfo.shippingCode :''}"/>
            <c:set var="index" value="${index+1}"/>
        </display:table>
        <input type="hidden" id="ediCompanyName" value="${ediCompanyName}"/>
        <input type="hidden" id="carrierSpAccountno" value="${carrierSpAccountno}"/>
    </div>
</c:if>
</td>
</tr>
</table>
<br/>
