<%-- 
    Document   : unitsResult
    Created on : Jun 19, 2015, 5:23:36 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<table align="center" id="unitTable" cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc">
    <tr class="tableHeadingYellow">
        <td>Units
            <div class="button-style1 units" style="float: right" id="addunit" onclick="addUnits('${path}');">Add Unit</div>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <c:if test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
        <div class="button-style1" id="stuffing_dr" style="float: right"
             onclick="load('${path}', '${lclssheader.origin.id}', '${lclssheader.destination.id}', '${lclssheader.id}', '${lclAddVoyageForm.filterByChanges}', '${lclssheader.scheduleNo}', '${detailId}', '${lclssheader.serviceType}');">Load
        </div>
    </c:if>
</td>
</tr>
<tr>
    <td>
<c:if test="${not empty lclUnitSSList}">
    <c:set var="index" value="1"/>
    <display:table name="${lclUnitSSList}" class="dataTable" pagesize="20"  id="lclunitSS" sort="list" >
        <display:setProperty name="paging.banner.some_items_found">
            <span class="pagebanner"><font color="blue">{0}</font>LCL Unit Details displayed,For more Records click on page numbers.</span>
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
        <display:setProperty name="paging.banner.item_name" value="Unit"/>
        <display:setProperty name="paging.banner.items_name" value="Units"/>
        <display:column title="Unit#" property="lclUnit.unitNo" headerClass="sortable" class="boldPurple"/>
        <display:column title="Haz">
            <c:if test="${lclunitSS.hazStatus}">
                <img src="${path}/images/icons/danger.png" width="12" height="12" alt="HazardousIcon" title="Hazardous"/>
            </c:if>
        </display:column>
        <display:column title="Inbond">
            <c:if test="${lclunitSS.inBondStatus}">
                <img src="${path}/img/icons/yellow.gif" width="12" height="12" alt="inbondIcon" title="InBond"/>
            </c:if>
        </display:column>
        <display:column title="Dispo">
            <span class="link" style="color:blue" title="${lclunitSS.dispoDesc}"
                  onclick="unitsEdit('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclSsHeader.id}', '${lclunitSS.id}', '${lclunitSS.hazStatus}')">
                ${lclunitSS.dispoCode}</span>
        </display:column>
        <display:column title="Status" headerClass="sortable">
            <c:if test="${not empty lclunitSS.status}">
                <c:if test="${lclunitSS.status eq 'E'}">Active</c:if>
                <c:if test="${lclunitSS.status eq 'C'}">Completed</c:if>
                <c:if test="${lclunitSS.status eq 'M'}"><span class="boldGreen text-case-none">Manifested</span></c:if>
            </c:if>
        </display:column>
        <display:column title="Size" headerClass="sortable">
            <span title="${lclunitSS.lclUnit.unitType.description}" style="text-transform: uppercase">
                <c:choose>
                    <c:when test="${lclunitSS.lclUnit.unitType.shortDesc ne null && lclunitSS.lclUnit.unitType.shortDesc ne ''}">
                        ${lclunitSS.lclUnit.unitType.shortDesc}
                    </c:when><c:otherwise>
                        ${fn:substring(lclunitSS.lclUnit.unitType.description,0,5)} 
                    </c:otherwise>
                </c:choose>         
            </span>
        </display:column>
        <display:column title="Seal#" headerClass="sortable">
            ${lclunitSS.SUHeadingNote}
        </display:column>
        <display:column title="NumDR" property="numDR"/>
        <display:column title="CFT" property="CFT" />
        <display:column title="CBM" property="CBM" />
        <display:column title="LBS" property="LBS" />
        <display:column title="KGS" property="KGS" />
        <display:column title="Booking#" property="spBookingNo"  headerClass="sortable"/>
        <display:column title="Trucking<br>Information">
            <a style=" cursor:pointer" title="${fn:toUpperCase(lclunitSS.lclUnit.remarks)}">${fn:substring(lclunitSS.lclUnit.remarks,0,20)}</a>
        </display:column>
        <display:column title="Loader">
            ${lclunitSS.loadedByUserId.firstName}<br>${lclunitSS.loadedByUserId.lastName}
        </display:column>
        <fmt:formatDate pattern="dd-MMM-yyyy" var="verifiedEta" value="${lclunitSS.cobDatetime}"></fmt:formatDate>
        <display:column title="Verified<br>ETA">
            ${verifiedEta}
        </display:column>
        <display:column title="Action">
            <div style="width: 500px">
                <div id="stuffing_dr"
                     <c:choose>
                        <c:when test="${lclunitSS.status=='C' || lclunitSS.status=='M'}">
                            class="green-background"
                            <c:if test="${lclAddVoyageForm.openLCLUnit=='Y' && lclunitSS.status!='M'}">
                                onclick="unLoad('${path}','${lclunitSS.lclUnit.id}','${lclunitSS.id}','${lclunitSS.lclSsHeader.id}','${index}','${lclunitSS.lclSsHeader.origin.id}','${lclunitSS.lclSsHeader.destination.id}')"
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            class="button-style1" onclick="loadComplete('${path}','${lclunitSS.lclUnit.id}','${lclunitSS.id}','${lclunitSS.lclSsHeader.id}','${index}','${lclunitSS.lclSsHeader.origin.id}','${lclunitSS.lclSsHeader.destination.id}')"
                        </c:otherwise>
                    </c:choose>
                    >Completed
                </div>
                <input type="button"  id="addCost" class="${lclunitSS.isCheckedRates ? "green-background" :"button-style1"}"
                       value="Costs" onclick="costPopUp('${path}', '${lclunitSS.id}', '${lclunitSS.lclUnit.id}',
                                       '${lclunitSS.lclUnit.unitNo}', '${lclunitSS.cob}', '${closeddatetime}', '${auditeddatetime}'
                                       , '${lclunitSS.hazStatus}', '${lclunitSS.lclUnit.hazmatPermitted}');"/>

                <!-- to validate cob in unit cost -->
                <input type="hidden" value="${lclunitSS.cob}" class="cob${lclunitSS.id}"/>
                <c:if test="${lclunitSS.status eq 'C' || lclunitSS.status eq 'M'}">
                    <div id="cobId" class="${lclunitSS.cob ? 'green-background':'button-style1'}"
                         onclick="openCOBPopUp('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.id}',
                                         '${lclunitSS.lclUnit.unitNo}', '${lclunitSS.lclSsHeader.id}',
                                         '${lclunitSS.lclSsHeader.scheduleNo}', '${lclunitSS.status}',
                                         '${lclAddVoyageForm.filterByChanges}', '${lclunitSS.hazStatus}', '${lclunitSS.lclUnit.hazmatPermitted}')">
                        COB
                    </div>

                </c:if>
                <c:choose>
                    <c:when test="${lclunitSS.status eq 'M'}">
                        <c:if test="${lclAddVoyageForm.unmanifestLCLUnit eq 'Y'}">
                            <div  class="button-style1" id="manifest"
                                  onclick="unmanifest('${lclunitSS.lclUnit.id}', '${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', '${lclunitSS.cob}', '${lclAddVoyageForm.filterByChanges}')">
                                Unmanifest
                            </div>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${lclunitSS.status eq 'C'}">
                            <div  class="button-style1" id="manifest"
                                  onclick="openManifestPopup('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', '${lclunitSS.lclSsHeader.scheduleNo}')">
                                Manifest
                            </div>
                        </c:if>
                    </c:otherwise>
                </c:choose>
                <c:if test="${lclunitSS.status=='C' || lclunitSS.status=='M'}">
                    <c:choose>
                        <c:when test="${lclunitSS.unitExceptionFlag=='false'}">
                            <c:set var="exClass" value="button-style1"/>
                        </c:when><c:otherwise>
                            <c:set var="exClass" value="green-background"/>
                        </c:otherwise>
                    </c:choose>
                    <div  class="${exClass}" id="addException${lclunitSS.id}"
                          onclick="addExceptionPopup('${path}', '${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', '${lclunitSS.lclSsHeader.scheduleNo}', '${lclunitSS.lclUnit.id}', '${lclssheader.id}')">
                        Exception List
                    </div>
                </c:if>
                <%--   <c:if test="${lclunitSS.status=='M'}">
                       <div  class="button-style1" id="viewmanifest"
                             onclick="viewManifestPopup('${path}','${lclunitSS.id}','${lclunitSS.lclUnit.unitNo}','${lclunitSS.lclSsHeader.scheduleNo}')">View Manifest</div>
                       <div  class="button-style1" id="addbltomanifest"
                             onclick="addBLToManifest('${path}','${lclunitSS.lclUnit.id}','${lclunitSS.id}','${lclunitSS.lclUnit.unitNo}','${lclunitSS.lclSsHeader.scheduleNo}')"
                             >Add B/L to Manifest</div>
                   </c:if>
                    <c:if test="${roleDuty.changeVoyage}">
                       <div  class="button-style1" id="changeVoyageId"
                             onclick="changeVoyagePopUp('${path}','${lclunitSS.lclSsHeader.scheduleNo}','${lclunitSS.lclSsHeader.origin.id}','${lclunitSS.lclSsHeader.destination.id}','${lclunitSS.id}','${lclssheader.id}','${lclunitSS.lclUnit.id}','${lclunitSS.lclUnit.unitNo}')"
                             >Change Voyage</div>
                   </c:if> --%>
                <div  class="button-style1" id="changeOptionId" onclick="changeOptionPopUp('${path}', '${lclunitSS.id}', '${lclunitSS.lclUnit.id}')">
                    Change Options
                </div>

                <div  class="button-style1" id="PrintFaxEmail" onclick="printreport('${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', 'LCLUnits')">
                    Print/Fax/Email
                </div>
                <c:set var="scanCss" value="${lclunitSS.scanAttachStatus ? 'green-background' :'button-style1'}"/>
                <input type="button" value="Scan/Attach"  class="${scanCss}" id="scan-Attach${lclunitSS.lclUnit.unitNo}-${lclunitSS.id}"
                       onclick="scanAttach('${lclunitSS.lclUnit.unitNo}', 'LCL EXPORTS UNIT', '${lclunitSS.id}', '')">
                <img src="${path}/img/icons/e_contents_view.gif" width="16" height="16" id="notes" title="Notes" alt="notesIcon"
                     onclick="unitNotes('${path}', '${lclunitSS.lclUnit.id}', '${lclssheader.id}', '${lclssheader.scheduleNo}');"/>
                <img src="${path}/img/icons/view.gif" width="16" height="16" id="viewdrs" alt="viewDrIcon" title="Click here to View D/Rs"
                     onclick="viewDRSPopup('${path}', '${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', '${lclunitSS.lclSsHeader.scheduleNo}')">
                <span style="cursor:pointer" title="Edit Unit">
                    <img src="${path}/images/edit.png" id="editUnits"  width="16" height="16" alt="editUnitIcon"
                         onclick="unitsEdit('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclSsHeader.id}', '${lclunitSS.id}', '${lclunitSS.hazStatus}')"/>
                </span>              
                <c:if test="${roleDuty.deleteUnits}">
                    <img src="${path}/images/error.png" width="16" height="16" title="Delete Unit" alt="deleteUnitIcon"
                         onclick="customDltMove('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.cob}', '${lclunitSS.status}', '${lclunitSS.SUHeadingNote}', '${lclunitSS.chassisNo}', '${lclunitSS.loadedByUserId.userId}')"/>
                </c:if>
                <input  type="hidden" id="hazStatus" value="${lclunitSS.hazStatus}"/>
            </div>
        </display:column>
        <c:set var="index" value="${index+1}"/>
    </display:table>
</c:if>
</td>
</tr>
</table>
