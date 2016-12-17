<%-- 
    Document   : unitResults
    Created on : Sep 26, 2015, 12:18:55 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<table align="center" id="unitTable" cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc">
    <div id="lclUnitHeadingDiv" style="visibility: hidden;position: absolute">
        <tr class="tableHeadingYellow">
            <td width="95%">Units</td>
        </tr>
    </div>
</table>


<cong:table align="center" id="unitTable" cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc">
    <cong:tr><cong:td>
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
                    <display:column title="Unit#" property="lclUnit.unitNo" headerClass="sortable" class="boldGreen"/>
                    <display:column title="Edi">
                        <c:choose>
                            <c:when test="${lclunitSS.edi eq 'true'}">
                                Y
                            </c:when>
                            <c:otherwise>
                                N
                            </c:otherwise>
                        </c:choose>
                    </display:column>
                    <display:column title="Status" headerClass="sortable">
                        <c:if test="${not empty lclunitSS.status}">
                            <c:if test="${lclunitSS.status eq 'E'}">Active</c:if>
                            <c:if test="${lclunitSS.status eq 'C'}">Completed</c:if>
                            <c:if test="${lclunitSS.status eq 'M'}">Manifest</c:if>
                        </c:if>
                    </display:column>
                    <display:column title="Dispo" style="cursor:pointer;">
                        <%--  <c:forEach items="${lclunitSS.lclUnit.lclUnitSsDispoList}" var="dispo">
                              <c:set var="dispoCode" value="${dispo.disposition.eliteCode}"/>
                              <c:set var="dispoDesc" value="${dispo.disposition.description}"/>
                          </c:forEach>--%>
                        <c:choose>
                            <c:when test="${lclunitSS.dispoCode eq 'DATA'|| lclunitSS.dispoCode eq 'WATR'}">
                                <a href="#">
                                    <span class="blueBold" title="${lclunitSS.dispoDesc}" onclick="unitsEdit('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclSsHeader.id}', '${lclunitSS.id}', '${polUnlocationCode}');">${lclunitSS.dispoCode}</span></a>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${lclunitSS.dispoCode eq 'AVAL'}">
                                        <a href="#">
                                            <span class="greenBold"  title="${lclunitSS.dispoDesc}" onclick="unitsEdit('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclSsHeader.id}', '${lclunitSS.id}', '${polUnlocationCode}');">${lclunitSS.dispoCode}</span></a>
                                        </c:when>
                                        <c:otherwise>
                                        <a href="#">
                                            <span class="orangeBold11px" onclick="unitsEdit('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclSsHeader.id}', '${lclunitSS.id}', '${polUnlocationCode}');" title="${lclunitSS.dispoDesc}">${lclunitSS.dispoCode}</span></a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </display:column>
                        <display:column title="Size" headerClass="sortable">
                        <span title="${lclunitSS.lclUnit.unitType.description}">
                            ${fn:substring(lclunitSS.lclUnit.unitType.description,0,10)}</span>
                        </display:column>
                        <display:column title="Pieces" headerClass="sortable">
                            ${lclunitSS.lclSsHeader.lclUnitSsManifestList[0].calculatedTotalPieces}
                        </display:column>
                        <display:column title="CBM" headerClass="sortable">
                            ${lclunitSS.lclSsHeader.lclUnitSsManifestList[0].calculatedVolumeMetric}
                        </display:column>
                        <display:column title="KGS" headerClass="sortable">
                            ${lclunitSS.lclSsHeader.lclUnitSsManifestList[0].calculatedWeightMetric}
                        </display:column>
                        <display:column title="CFT" headerClass="sortable">
                            ${lclunitSS.lclSsHeader.lclUnitSsManifestList[0].calculatedVolumeImperial}
                        </display:column>
                        <display:column title="LBS" headerClass="sortable">
                            ${lclunitSS.lclSsHeader.lclUnitSsManifestList[0].calculatedWeightImperial}
                        </display:column>
                        <display:column title="MasterBL" headerClass="sortable">
                            ${fn:toUpperCase(lclunitSS.lclSsHeader.lclUnitSsManifestList[0].masterbl)}
                        </display:column>
                        <display:column title="Coloader">
                            <c:set var="coloaderName" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.accountName}"/>
                            <c:set var="coloaderNo" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.accountno}"/>
                            <c:set var="coloaderCoName" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.primaryCustAddr.coName}" />
                            <c:set var="coloaderAddress" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.primaryCustAddr.address1}" />
                            <c:set var="coloaderCity" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.primaryCustAddr.city2}" />
                            <c:set var="coloaderState" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.primaryCustAddr.state}" />
                            <c:set var="coloaderZip" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.primaryCustAddr.zip}" />
                            <c:set var="coloaderPhone" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.primaryCustAddr.phone}" />
                            <c:set var="coloaderFax" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.primaryCustAddr.fax}" />
                        <span title="<c:import url="/jsps/LCL/booking/addressTooltip.jsp">
                                  <c:param name="whName" value="${coloaderName}"/>
                                  <c:param name="cfsWhNo" value="${coloaderNo}"/>
                                  <c:param name="whCoName" value="${coloaderCoName}"/>
                                  <c:param name="whAddress" value="${coloaderAddress}"/>
                                  <c:param name="whCity" value="${coloaderCity}"/>
                                  <c:param name="whState" value="${coloaderState}"/>
                                  <c:param name="whZip" value="${coloaderZip}"/>
                                  <c:param name="whPhone" value="${coloaderPhone}"/>
                                  <c:param name="whFax" value="${coloaderFax}"/>
                              </c:import>">
                            ${fn:substring(lclunitSS.lclSsHeader.lclUnitSsImportsList[0].coloaderAcctNo.accountName,0,15)}
                        </span>
                    </display:column>
                    <display:column title="CFS Dev" headerClass="sortable">
                        <c:set var="cfsWhName" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.warehouseName}"/>
                        <c:set var="cfsWhNo" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.warehouseNo}"/>
                        <c:set var="cfsWhVenorNo" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.vendorNo}"/>
                        <c:set var="cfsAddress" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.address}" />
                        <c:set var="city" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.city}" />
                        <c:set var="state" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.state}" />
                        <c:set var="zip" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.zipCode}" />
                        <c:set var="phone" value="${phone}${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.phone}" />
                        <c:set var="fax" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.fax}" />
                        <c:set var="query" value="SELECT co_name FROM cust_address WHERE acct_no='${cfsWhVenorNo}'"/>
                        <c:set var="cfsWhCoName"><c:out value="${dao:getUniqueResult(query)}" /></c:set>
                        <span title="<c:import url="/jsps/LCL/booking/addressTooltip.jsp">
                                  <c:param name="whName" value="${cfsWhName}"/>
                                  <c:param name="whCoName" value="${cfsWhCoName}"/>
                                  <c:param name="whAddress" value="${cfsAddress}"/>
                                  <c:param name="whCity" value="${city}"/>
                                  <c:param name="whState" value="${state}"/>
                                  <c:param name="whZip" value="${zip}"/>
                                  <c:param name="whPhone" value="${phone}"/>
                                  <c:param name="whFax" value="${fax}"/>
                              </c:import>">
                            ${fn:substring(lclunitSS.lclSsHeader.lclUnitSsImportsList[0].cfsWarehouseId.warehouseName,0,15)}
                        </span>
                        <img alt="Copy" title="copy data to clipboard" src="${path}/img/copy_icon.gif" style="vertical-align: middle"
                             onclick="copyToClipboard('Name: ${cfsWhName}/${cfsWhNo}\nAddress : ${cfsAddress}\nCity : ${city}\nState : ${state}\nZip : ${zip}\nPhone : ${phone}\nFax : ${fax}');"/>
                    </display:column>
                    <display:column title="Unit Tm" headerClass="sortable">
                        <c:set var="unitWhName" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].unitWareHouseId.warehouseName}"/>
                        <c:set var="unitWhNo" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].unitWareHouseId.warehouseNo}"/>
                        <c:set var="unitWhVendorNo" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].unitWareHouseId.vendorNo}" />
                        <c:set var="unitWhAddress" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].unitWareHouseId.address}" />
                        <c:set var="unitWhCity" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].unitWareHouseId.city}" />
                        <c:set var="unitWhState" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].unitWareHouseId.state}" />
                        <c:set var="unitWhZip" value="${lclunitSS.lclSsHeader.lclUnitSsImportsList[0].unitWareHouseId.zipCode}" />
                        <c:set var="coNameQuery" value="SELECT co_name FROM cust_address WHERE acct_no='${unitWhVendorNo}'"/>
                        <c:set var="uniotWhCoName"><c:out value="${dao:getUniqueResult(coNameQuery)}" /></c:set>
                        <span title="<c:import url="/jsps/LCL/booking/addressTooltip.jsp">
                                  <c:param name="whName" value="${unitWhName}"/>
                                  <c:param name="whCoName" value="${uniotWhCoName}"/>
                                  <c:param name="whAddress" value="${unitWhAddress}"/>
                                  <c:param name="whCity" value="${unitWhCity}"/>
                                  <c:param name="whState" value="${unitWhState}"/>
                                  <c:param name="whZip" value="${unitWhZip}"/>
                              </c:import>">
                            ${fn:substring(lclunitSS.lclSsHeader.lclUnitSsImportsList[0].unitWareHouseId.warehouseName,0,15)}
                        </span>
                    </display:column>
                    <display:column title="IT#" headerClass="sortable">
                        <span title="${fn:toUpperCase(lclunitSS.lclSsHeader.lclUnitSsImportsList[0].itNo)}">
                            ${fn:toUpperCase(fn:substring(lclunitSS.lclSsHeader.lclUnitSsImportsList[0].itNo,0,15))}</span>
                        </display:column>
                        <display:column title="Action">
                        <div style="white-space: normal">
                            <c:choose>
                                <c:when test="${closed ne 'Reopen'}">
                                    <input type="button" value="New DR" class="button-style1" onclick="importBooking('${path}', '${lclunitSS.lclSsHeader.id}', '${lclunitSS.id}', '${lclunitSS.lclUnit.id}');"/>
                                    <input type="button" value="Link DR" class="button-style1" onclick="linkDR('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', '${lclunitSS.lclSsHeader.scheduleNo}', '${lclunitSS.lclSsHeader.id}');"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="button" value="New DR" class="gray-background"/>
                                    <input type="button" value="Link DR" class="gray-background"/>
                                </c:otherwise>
                            </c:choose>
                            <div id="arInvoice"
                                 <c:if test="${lclunitSS.statusSendEdi eq 'Posted'}">
                                     class="green-background"
                                 </c:if>
                                 <c:if test="${lclunitSS.statusSendEdi eq 'Open'}">
                                     class="red-background"
                                 </c:if>
                                 <c:if test="${lclunitSS.statusSendEdi eq 'Empty'}">
                                     class="button-style1"
                                 </c:if>
                                 onclick="openLclVoyageArInvoice('${path}', '${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', true, '${lclunitSS.lclSsHeader.id}');">AR Invoice</div>
                            <input type="button"  id="addCost" class="${not empty lclunitSS.lclSsAcList ? "green-background" :"button-style1"}"
                                   value="Costs" onclick="lclImpCost('${path}', '${lclunitSS.id}', '${lclssheader.id}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclUnit.unitNo}', '${closeddatetime}', '${auditeddatetime}');"/>
                            <c:choose><c:when test="${not empty lclssheader.auditedBy || not empty lclssheader.closedBy}">
                                    <input type="button"  id="chargeDist" class="gray-background" disabled="true" value="Charges/Dist"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="button"  id="chargeDist" class="button-style1" value="Charges/Dist"
                                           onclick="lclImpCharge('${path}', '${lclunitSS.id}', '${lclssheader.id}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclUnit.unitNo}', '${lclunitSS.lclSsHeader.destination.unLocationCode}');"/>
                                </c:otherwise>
                            </c:choose>
                            <c:set var="concatemailSubValues" value="${lclunitSS.lclSsHeader.origin.unLocationCode}-${lclunitSS.lclSsHeader.destination.unLocationCode}-${lclunitSS.lclSsHeader.scheduleNo}"/>
                            <div  class="button-style1" id="profitLoss" onclick="printreport('${path}', '${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', 'LclImpUnitReports', '', '${concatemailSubValues}');">
                                Reports
                            </div>
                            <div  class="button-style1" id="PrintFaxEmail" onclick="printreport('${path}', '${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', 'LCLImpUnits', '', '${concatemailSubValues}');">
                                Print/Fax/Email
                            </div>
                            <div  class="${lclunitSS.scanAttachStatus eq 'true' ? "green-background" :"button-style1"}" id="scan-attach"
                                  onclick="scan('${lclunitSS.lclUnit.unitNo}--${lclunitSS.lclSsHeader.scheduleNo}', 'LCL IMPORTS UNIT', 'BOOKING', 'false');">
                                Scan/Attach
                            </div>
                            <input type="button" id="outsource${lclunitSS.lclUnit.id}" class="${outSourceRemarks ? "green-background" :"button-style1"}"
                                   value="Outsource" onclick="openOutsourceEmail('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclSsHeader.id}', '${loginuser.userId}');"/>
                            <c:choose>
                                <c:when test="${not empty lclunitSS.lclUnit.lclUnitWhseList[0].osdDatetime && not empty lclunitSS.lclUnit.lclUnitWhseList[0].osdUser.loginName}">
                                    <c:set var="osdClass" value="green-background"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="osdClass" value="button-style1"/>
                                </c:otherwise>
                            </c:choose>
                            <div class="${osdClass}" style="width: 20px;" onclick="osdReceived('${path}', '${lclunitSS.id}', '${lclunitSS.lclUnit.id}', '${lclssheader.id}', '${lclunitSS.lclUnit.unitNo}');" title="OSD Details">OSD</div>
                            <c:if test="${roleDuty.unmanifestLclUnit && lclunitSS.status eq 'M' }">
                                <div class="green-background" style="width: 12px" onclick="unManifest('${lclunitSS.id}', '${lclunitSS.lclUnit.id}');" title="Reverse Acct">AR</div>
                            </c:if>
                            <a href="${path}/lclImportUnitNotes.do?methodName=displayNotes&unitSsId=${lclunitSS.id}&headerId=${lclssheader.id}&unitId=${lclunitSS.lclUnit.id}&unitNo=${lclunitSS.lclUnit.unitNo}&actions=4" class="unitSsRemark">
                                <img alt="notes" title="Unit Notes" width="16" height="16" src="${path}/img/icons/e_contents_view.gif" class="notes"/></a>
                            <img src="${path}/images/edit.png"  style="cursor:pointer" width="16" height="16" alt="edit" title="Edit Unit"
                                 onclick="unitsEdit('${path}', '${lclunitSS.lclUnit.id}', '${lclunitSS.lclSsHeader.id}', '${lclunitSS.id}', '${polUnlocationCode}', '${etaPod}');"/>
                            <c:if test="${roleDuty.deleteImportsUnit }">
                                <img src="${path}/images/error.png"  alt="delete" title="Delete Unit" style="cursor:pointer" width="16" height="16" onclick="deleteUnits('${lclunitSS.lclUnit.id}');"/>
                            </c:if>
                            <img  alt="preview" style="cursor:pointer;" width="18" height="18" src="${path}/img/icons/search_over.gif"
                                  border="0" title="Preview" onclick="viewPreviewReport('${lclunitSS.id}', '${lclunitSS.lclUnit.unitNo}', '${path}');"/>
                            <c:if test="${lclUnitSs.dispoCode ne 'DATA'}">
                                <input type="button" class="button-style1" value="Resend All S/U" onclick="resendEmails('${lclunitSS.id}')"/>
                            </c:if>
                        </div>
                    </display:column>
                    <c:set var="index" value="${index+1}"/>
                </display:table>
            </c:if>
        </cong:td></cong:tr>
</cong:table>
