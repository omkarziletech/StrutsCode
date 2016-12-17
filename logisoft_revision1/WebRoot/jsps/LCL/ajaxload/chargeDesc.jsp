<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileNumberId" value="${param.fileNumberId}"/>
<c:set var="fileNumber" value="${param.fileNumber}"/>
<c:set var="fileNumberStatus" value="${lclBooking.lclFileNumber.status}"/>
<c:set var="bookingStatus" value="${lclBooking.bookingType}"/>
<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
<c:choose>
    <c:when test="${not empty lclBookingForm.moduleName}">
        <c:set var="moduleName" value="${lclBookingForm.moduleName}"/>
    </c:when>
    <c:otherwise>
        <c:set var="moduleName" value="${param.moduleName}"/>
    </c:otherwise>
</c:choose>

<input type="hidden" name="isArGlMappingFlag" id="isArGlMappingFlag" value="${lclSession.isArGlmappingFlag}"/>
<input type="hidden" name="glMappingBlueCode" id="glMappingBlueCode" value="${lclSession.glMappingBlueCode}"/>
<input type="hidden" id="validateImpRates" value="${validateImpRates}"/>
<input type="hidden" id="newIpiChargeStatus" value="${newIpiChargeStatus}"/>
<input type="hidden"   name="formChangeFlag"  id="formChangeFlag"  value="${lclBookingForm.formChangeFlag}"/>

<body style="background:#ffffff">
    <table width="100%">
        <tr  class="tableHeadingNew">
            <td width="15%">Cost and Charges</td>
            <c:if test="${moduleName eq 'Imports'}">
                <td>Commodity:<span style="color:red"><c:out value="${commodityNumber}"/></span></td>
                </c:if>
            <td width="13%">Origin:
                <span class="ChargeOrigin" style="color:red">
                    <c:choose>
                        <c:when test="${not empty lclBooking.portOfOrigin}">
                            <c:out value="${fn:toUpperCase(lclBooking.portOfOrigin.unLocationName)}"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${fn:toUpperCase(lclBooking.portOfLoading.unLocationName)}"/>
                        </c:otherwise>
                    </c:choose>
                </span>
            </td>
            <td width="13%">Destination:
                <span class="ChargeDest" style="color:red"><c:out value="${fn:toUpperCase(lclBooking.finalDestination.unLocationName)}"/></span>
            </td>
            <td width="11%">Cube:
                <span style="color:red"> <c:out value="${cubeValues}"/></span>
            </td>
            <td width="5%">Weight:
                <span style="color:red"> <c:out value="${weightValues}"/></span>
            </td>
            <td  width="58%">
                <c:if test="${not empty fileNumberId}">
                    <c:choose>
                        <c:when test="${(not empty lclssheader.auditedBy || not empty lclssheader.closedBy) && moduleName eq 'Imports' }">
                            <div class="gray-background floatRight costAndCharge" id="costCharge" disabled="true">Add Cost</div>
                        </c:when>
                        <c:otherwise>
                            <div class="button-style1 floatRight costAndCharge" id="costCharge"
                                 onclick="addCharge('${path}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', 'addCost', 'costCharge');">Add Cost</div>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="${moduleName eq 'Imports' && fileNumberStatus eq 'M' && bookingStatus ne 'T'}">
                            <c:choose>
                                <c:when test="${not empty lclssheader.auditedBy || not empty lclssheader.closedBy}">
                                    <div class="gray-background floatRight quickCN" id="quickCN" disabled="true">Quick CN</div>
                                    <div class="gray-background floatRight costAndCharge" id="invoiceCharge" disabled="true">Add Invoice Charge</div>
                                    <div class="button-style1 floatRight costAndCharge" id="cfsDevCharge" disabled="true">CFS Dev Whse chgs</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="button-style1 floatRight quickCN" id="quickCN"
                                         onclick="quickCN('${path}', '${fileNumberId}', '${fileNumber}', '${moduleName}');">Quick CN</div>
                                    <div class="button-style1 floatRight costAndCharge" id="invoiceCharge"
                                         onclick="addInvoiceCharge('${path}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', 'addCharge');">Add Invoice Charge</div>
                                    <c:if test="${lclBooking.finalDestination.id eq lclBooking.portOfDestination.id && unitSsCollectType eq 'C'}">
                                        <div class="button-style1 floatRight costAndCharge" id="cfsDevCharge"
                                             onclick="addCfsDevCharges('${fileNumberId}');">CFS Dev Whse chgs</div>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${(not empty lclssheader.auditedBy || not empty lclssheader.closedBy) && moduleName eq 'Exports'}">
                                    <div id="calculate" class="gray-background floatRight" disabled="true">Calculate</div>  
                                </c:when> 
                                <c:otherwise>
                                    <div id="calculate" class="button-style1 floatRight"
                                         onclick="calculateCharge('', '#doorOriginCityZip', '#pickupReadyDate');">Calculate</div>  
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${not empty lclssheader.auditedBy || not empty lclssheader.closedBy}">
                                    <div class="gray-background floatRight costAndCharge" id="costandCharge" disabled="true">Add charge</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="button-style1 floatRight costAndCharge" id="costandCharge"
                                         onclick="addCharge('${path}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', 'addCharge', 'costandCharge');">Add charge</div>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${moduleName eq 'Imports'}">
                        <div class="${paymentStatus} floatRight costAndCharge" id="impPayments"
                             onclick="impPayments('${path}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', '${lclssheader.id}');">Payments</div>
                    </c:if>
                </c:if>
            </td>
        </tr>
    </table>
    <table class="dataTable" border="0" id="chargesTable">
        <thead>
            <tr>
                <th></th>
                <th></th>
                <th>Charge/Cost Code</th>
                <th>Charge Desc</th>
                <th>Charge Amount</th>
                <th>Bill to Party</th>
                    <c:choose>
                        <c:when test="${moduleName eq 'Imports'}">
                            <c:set var="column7" value="Amt Paid"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="column7" value="Bundle Into OFR"/>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${moduleName ne 'Imports'}">
                    <th>Adj Amt</th>
                    <th>Total Adj Amt</th>
                    </c:if>
                <th>${column7}</th>
                    <c:if test="${moduleName eq 'Imports'}">
                    <th>Rels to Inv</th>
                    </c:if>
                <th>Cost/Vendor</th>
                <th>Invoice#</th>
                <th>Rate</th>
                    <c:if test="${moduleName ne 'Imports'}">
                    <th>Trans Date</th>
                    </c:if>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <c:if test="${not empty chargeList}">
            <tbody>
                <c:forEach var="charges" items="${chargeList}">
                    <c:choose>
                        <c:when test="${rowStyle eq 'oddStyle'}">
                            <c:set var="rowStyle" value="evenStyle"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" value="oddStyle"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowStyle}">
                        <td>${charges.label1}</td><%--CHARGE VOLUME --%>
                        <td><%--MANUAL ENTRY CHARGE --%>
                            <c:choose>
                                <c:when test="${charges.manualEntry}">
                                    <img alt="Manual Charge" title="Manual Charge"style="vertical-align: middle" src="${path}/img/icons/asterikPurple.png" width="10" height="10" />
                                </c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty charges.apAmount && charges.apAmount ne '0.00' && charges.arAmount eq '0.00'}">
                                    ${charges.apglMapping.chargeCode}
                                </c:when>
                                <c:when test="${(empty charges.apAmount || charges.apAmount eq '0.00') && charges.arAmount ne '0.00'}">
                                    ${charges.arglMapping.chargeCode}
                                </c:when>
                                <c:when test="${charges.apAmount ne '0.00'}">
                                    ${charges.arglMapping.chargeCode}
                                </c:when>
                                <c:when test="${not empty charges.apAmount && charges.apAmount ne '0.00' && charges.arAmount ne '0.00'}">
                                    ${charges.arglMapping.chargeCode} ${not empty charges.apglMapping.chargeCode and charges.arglMapping.chargeCode ne charges.apglMapping.chargeCode
                                      ? '/ '.concat(charges.apglMapping.chargeCode): ''}
                                </c:when>
                            </c:choose>
                        </td><%--CHARGE CODE --%>
                        <td>${charges.arglMapping.chargeDescriptions}</td><%--CHARGE DESCRIPTION --%>
                        <td><%--CHARGE AMOUNT --%>
                            <c:set var="totaladjustmentamount" value="0.00"/>
                            <c:set var="containCharge" value="N"/>
                            <c:choose>
                                <c:when test="${not empty charges.rolledupCharges && charges.rolledupCharges ne '0.00'}">
                                    <p class="chargeAmount">${charges.rolledupCharges}</p>
                                    <c:set value="${totaladjustmentamount+charges.rolledupCharges}" var="totaladjustmentamount"/>
                                    <input type="hidden" id="chargeAmount${charges.id}" value="${charges.rolledupCharges}"  />
                                    <c:set value="Y" var="containCharge"/>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${charges.arAmount ne '0.00'}">
                                        <p class="chargeAmount">${charges.arAmount}</p>
                                        <input type="hidden" id="chargeAmount${charges.id}" value="${charges.arAmount}"  />
                                        <c:set value="${totaladjustmentamount+charges.arAmount}" var="totaladjustmentamount"/>
                                        <c:set value="Y" var="containCharge"/>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="billToPartyCharges billToCharge${containCharge}">
                            <c:choose>
                                <c:when test="${not empty charges.tempArBillToParty && bookingStatus eq 'T'}">
                                    <c:if test="${charges.tempArBillToParty eq 'A'}">Agent</c:if>
                                    <c:if test="${charges.tempArBillToParty eq 'S'}">Shipper</c:if>
                                    <c:if test="${charges.tempArBillToParty eq 'F'}">Forwarder</c:if>
                                    <c:if test="${charges.tempArBillToParty eq 'T'}">Third Party</c:if>
                                    <c:if test="${charges.tempArBillToParty eq 'C'}">Consignee</c:if>
                                    <c:if test="${charges.tempArBillToParty eq 'N'}">Notify Party</c:if>
                                    <c:if test="${charges.tempArBillToParty eq 'W'}">Warehouse</c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${charges.arBillToParty eq 'A'}">Agent</c:if>
                                    <c:if test="${charges.arBillToParty eq 'S'}">Shipper</c:if>
                                    <c:if test="${charges.arBillToParty eq 'F'}">Forwarder</c:if>
                                    <c:if test="${charges.arBillToParty eq 'T'}">Third Party</c:if>
                                    <c:if test="${charges.arBillToParty eq 'C'}">Consignee</c:if>
                                    <c:if test="${charges.arBillToParty eq 'N'}">Notify Party</c:if>
                                    <c:if test="${charges.arBillToParty eq 'W'}">Warehouse</c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <c:if test="${moduleName ne 'Imports'}">
                            <td>
                                <c:if test="${!charges.manualEntry}"><%--Adjustment Amount for Exports --%>
                                    <input type="text" name="adjustmentAmount" id="adjustmentAmount${charges.id}" class="adjustmentAmount textlabelsBoldForTextBox" style="width:76px"
                                           value="${empty charges.adjustmentAmount ? "0.00" : charges.adjustmentAmount}"
                                           onchange="allowOnlyWholeNumbers(this);
                                                   calculateAdjustmentAmount('${charges.id}', '${charges.adjustmentAmount}');"
                                           ${charges.lclFileNumber.status eq "M" ? "disabled" :""}/>=
                                </c:if>
                            </td>
                        </c:if>
                        <c:if test="${moduleName ne 'Imports'}">
                            <td id="tdTotalAdjAmt${charges.id}">${totaladjustmentamount+charges.adjustmentAmount}
                                <c:if test="${!charges.manualEntry && charges.lclFileNumber.status ne 'M'}">
                                    <img src="${path}/img/icons/edit.gif"  title="TotalAdjustmentCharge" style="vertical-align: middle"  width="12" height="12" onclick="calculateAdjustmentTotal('${charges.id}', '${charges.rolledupCharges}');"/>
                                </c:if>
                            </td>
                        </c:if>
                        <td>
                            <c:choose>
                                <c:when test="${moduleName eq 'Imports'}">
                                    <c:if test="${not empty charges.lclBookingAcTaList && charges.lclBookingAcTaList[0].lclBookingAc.id eq charges.id}"><%--PAID AMT --%>
                                        <c:set var="paidAmt" value="0"/>
                                        <c:forEach items="${charges.lclBookingAcTaList}" var="lclBkgTa">
                                            <c:set var="paidAmt" value="${paidAmt+lclBkgTa.amount}"/>
                                        </c:forEach>
                                        <c:if test="${not empty paidAmt && paidAmt ne '0.00'}">${paidAmt}</c:if>
                                    </c:if>
                                </c:when>
                                <c:otherwise><%--Bundle Into OFR for Exports --%>
                                    <c:if test="${charges.arglMapping.chargeCode ne 'OCNFRT'}">
                                        <c:choose>
                                            <c:when test="${!charges.bundleIntoOf}">
                                                <input type="checkBox" name="bundleToOf" id="bundleToOf${charges.id}" onclick="updateCharges('${charges.id}', 'B');"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkBox" name="bundleToOf" id="bundleToOf${charges.id}" checked onclick="updateCharges('${charges.id}', 'B');"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <c:set var="invoiceStatus"/>
                        <c:if test="${moduleName eq 'Imports'}">
                            <td><%--RELS TO INV --%>
                                <c:if test="${charges.arBillToParty eq 'A'}">
                                    <c:choose> <c:when test="${empty charges.spReferenceNo}">
                                            <c:set var="invoiceStatus" value=""/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="query" value="SELECT STATUS FROM ar_red_invoice WHERE "/>
                                            <c:set var="query" value="${query} invoice_number='${charges.spReferenceNo}' and screen_name='IMP VOYAGE' limit 1"/>
                                            <c:set var="invoiceStatus"><c:out value="${dao:getUniqueResult(query)}" /></c:set>
                                        </c:otherwise></c:choose>
                                    <c:choose>
                                        <c:when test="${not empty invoiceStatus}">
                                            <span class="${invoiceStatus eq 'AR' ? "greenBold":"redBold11px"}  ${charges.manualEntry ? '':' spReferenceNo'}">${charges.spReferenceNo}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${lclBooking.bookingType ne 'T'}"><input type="checkBox" name="relsToInv" class="relsToInv" 
                                                   id="relsToInv${charges.id}" onclick="updateCharges('${charges.id}', 'R');"
                                                   <c:if test="${charges.relsToInv}"> checked </c:if>/></c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                        </c:if>
                        <td><%-- COST VENDOR --%>
                            <c:if test="${ not empty charges.apAmount && charges.apAmount ne '0.00'}">

                                <c:if test="${ not empty charges.apAmount && charges.apAmount ne '0.00'}">
                                    <span title="
                                          <c:choose>
                                              <c:when test="${ not empty charges.costWeight && charges.costWeight ne '0.00'}">
                                                  <c:choose>
                                                      <c:when test="${charges.rateUom=='M'}">
                                                          ${charges.costMeasure} CBM, ${charges.costWeight}/1000.00 KGS,
                                                      </c:when>
                                                      <c:otherwise>
                                                          ${charges.costMeasure} CFT, ${charges.costWeight}/100.00 LBS,
                                                      </c:otherwise>
                                                  </c:choose>
                                                  (${charges.costMinimum} MINIMUM)
                                              </c:when>
                                          </c:choose>
                                          ">${charges.apAmount}</span></c:if>

                            </c:if>
                            <c:if test="${not empty charges.supAcct.accountno}"> / <span title="${charges.supAcct.accountName}">${charges.supAcct.accountno}</span></c:if>
                            </td>
                            <td>
                                <span class="purpleBold11px">${charges.invoiceNumber}</span>
                        </td>
                        <td><%-- CHARGE RATE --%>
                            <c:if test="${charges.arglMapping.chargeCode eq 'OCNFRT'}">
                                <b style="color: red;font-size: small">${highVolumeMessage}</b>
                            </c:if>
                            ${charges.label2}
                        </td>
                        <c:if test="${moduleName ne 'Imports'}">
                            <td><%-- Trans Date for Exports --%>
                                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="transDatetime" value="${charges.transDatetime}"/>
                                <c:out value="${transDatetime}">

                                </c:out>
                            </td>
                        </c:if>
                        <c:set var="costStatusQuery" value="SELECT lbt.`trans_type` FROM `lcl_booking_ac_trans` lbt JOIN `lcl_booking_ac_ta` ta ON (lbt.`id` = ta.`lcl_booking_ac_trans_id`) WHERE ta.`lcl_booking_ac_id`= '${charges.id}' AND lbt.`payment_type` IS NULL LIMIT 1"/>    
                        <c:set var="costStatus"><c:out value="${dao:getUniqueResult(costStatusQuery)}" /></c:set>
                        <td class="costStatus ${charges.manualEntry ? '':'accountStatus'}"><%-- CHARGE STATUS --%>
                            <c:if test="${not empty costStatus}">${costStatus}
                            </c:if>
                        </td>
                        <td><%-- ACTION --%>
                            <c:if test="${not empty charges.apAmount && charges.apAmount >0.00}">
                                <img alt="Scan/Attach" title="Scan View" class="scanView" src="${path}/img/icons/preview.gif"
                                     onclick="showScanOrAttach('${path}', '${charges.supAcct.accountno}', '${charges.invoiceNumber}', '${fileNumber}');"/>
                            </c:if>
                            <%--  <c:if test="${(not empty charges.apAmount && not empty charges.lclBookingAcTaList && costStatus ne 'AP')
                                            || (not empty charges.arAmount && charges.arAmount>0.00)}">
                              </c:if> --%>
                            <c:if test="${not empty charges.adjustmentComments && moduleName eq 'Exports'}">
                                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="adjustment_enter_date" value="${charges.modifiedDatetime}"/>
                                <c:set var="comments" value="${fn:substring(charges.adjustmentComments,0,50)}<br/>${fn:substring(charges.adjustmentComments,50,100)}"/>
                                <span title="<table><tr><td>${comments}</td></tr>
                                      <tr><td>${charges.modifiedBy.loginName} ${adjustment_enter_date}</tr></td></table>">
                                    <img id="adjustmentChargeCommentViewGif" src="${path}/img/icons/view.gif" width="16" height="16" alt="info"/>
                                </span>
                            </c:if>    
                            <c:choose>
                                <c:when test="${moduleName eq 'Exports'}">
                                    <c:choose>   
                                        <c:when test="${costStatus eq 'AC'}">
                                            <c:if test="${!charges.arglMapping.destinationServices}">
                                                <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge" width="13" height="13" alt="edit"
                                                     onclick="editCharge('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', '${charges.manualEntry}', '${costStatus}', '${charges.spReferenceNo}');"
                                                     title="Edit Charge"/>
                                            </c:if>
                                            <c:if test="${charges.arglMapping.destinationServices}">
                                                <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge" width="13" height="13" alt="edit"
                                                     onclick="editdestinationServices('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}', 'editService');"
                                                     title="Edit Charge"/>
                                            </c:if>
                                            <c:if test="${empty lclssheader.auditedBy && empty lclssheader.closedBy}">
                                                <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                                     onclick="deleteCharge('Are you sure you want to delete?', '${charges.id}', '${fileNumberId}');"
                                                     title="Delete Charge"/>  
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>          
                                            <c:if test="${charges.lclFileNumber.status ne 'M' && charges.manualEntry && empty lclssheader.auditedBy && empty lclssheader.closedBy}">
                                                <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge" width="13" height="13" alt="edit"
                                                     onclick="editCharge('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', '${charges.manualEntry}', '${costStatus}', '${charges.spReferenceNo}');"
                                                     title="Edit Charge"/>
                                                <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                                     onclick="deleteCharge('Are you sure you want to delete?', '${charges.id}', '${fileNumberId}');"
                                                     title="Delete Charge"/>  
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>

                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${charges.arglMapping.blueScreenChargeCode eq '1612' && fileNumberStatus eq 'M' && charges.arBillToParty eq 'A'
                                                        && (empty lclssheader.auditedBy && empty lclssheader.closedBy)}">
                                            <%--<c:if test="${costStatus ne 'IP'}">--%>
                                            <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge" width="13" height="13" alt="edit"
                                                 onclick="editCharge('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', '${charges.manualEntry}', '${costStatus}', '${charges.spReferenceNo}');"
                                                 title="Edit Charge"/>
                                            <%--</c:if>--%>
                                        </c:when><c:when test="${empty lclssheader.auditedBy && empty lclssheader.closedBy}">
                                            <c:if test="${(costStatus eq 'AC' && not empty charges.arAmount && not empty charges.apAmount)
                                                          || (charges.arBillToParty eq 'A' && empty invoiceStatus)
                                                          || (fileNumberStatus eq 'B' && costStatus ne 'AP' && empty charges.spReferenceNo)
                                                          || (fileNumberStatus eq 'B' && costStatus eq 'AP' && empty charges.spReferenceNo && not empty charges.arAmount && charges.arAmount ne '0.00')
                                                          || (lclBooking.bookingType eq 'T')
                                                          || (charges.arBillToParty eq 'W' && !charges.postAr)}">
                                                <%--<c:if test="${costStatus ne 'IP'}">--%>
                                                <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge" width="13" height="13" alt="edit"
                                                     onclick="editCharge('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', '${charges.manualEntry}', '${costStatus}', '${charges.spReferenceNo}');"
                                                     title="Edit Charge"/>
                                                <%--</c:if>--%>
                                                <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                                     onclick="deleteCharge('Are you sure you want to delete?', '${charges.id}', '${fileNumberId}', '${costStatus}', '${fileNumberStatus}');"
                                                     title="Delete Charge"/>
                                            </c:if>
                                            <c:if test="${(fileNumberStatus eq 'M' && costStatus eq 'DS')}">
                                                <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge" width="13" height="13" alt="edit"
                                                     onclick="editCharge('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}', '${fileNumberStatus}', '${charges.manualEntry}', '${costStatus}', '${charges.spReferenceNo}');"
                                                     title="Edit Charge"/>
                                            </c:if>
                                        </c:when>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </c:if>
        <tfoot>
            <c:if test="${not empty totalPrepaidChargesAmt && totalPrepaidChargesAmt ne '0.00' && moduleName eq 'Imports'}" >
                <tr>
                    <td colspan="4"></td>
                    <td>-----------</td>
                    <td colspan="8"></td>
                </tr>
                <tr>
                    <td colspan="3"></td>
                    <td><b id="blueBold">COL</b></td>
                    <%-- <td>
                        <c:if test="${moduleName eq 'Imports'}">
                            <b id="blueBold">
                                <c:if test="${lclBooking.billToParty eq 'C'}">CONSIGNEE</c:if>
                                <c:if test="${lclBooking.billToParty eq 'T'}">THIRD PARTY</c:if>
                                <c:if test="${lclBooking.billToParty eq 'N'}">NOTIFY</c:if></b>
                            </c:if>
                    </td> --%>
                    <td><c:choose>
                            <c:when test="${not empty totalCollectChargesAmtC && totalCollectChargesAmtC ne '0.00'}">
                                <b id="greenBold">${totalCollectChargesAmtC}</b>
                                <c:set var="billToPartyStatus" value="C"></c:set>
                            </c:when>
                            <c:when test="${not empty totalCollectChargesAmtT && totalCollectChargesAmtT ne '0.00'}">
                                <b id="greenBold">${totalCollectChargesAmtT}</b>
                                <c:set var="billToPartyStatus" value="T"></c:set>
                            </c:when>
                            <c:when test="${not empty totalCollectChargesAmtN && totalCollectChargesAmtN ne '0.00'}">
                                <b id="greenBold">${totalCollectChargesAmtN}</b>
                                <c:set var="billToPartyStatus" value="N"></c:set>
                            </c:when>
                            <c:when test="${not empty totalCollectChargesAmtW && totalCollectChargesAmtW ne '0.00'}">
                                <b id="greenBold">${totalCollectChargesAmtW}</b>
                                <c:set var="billToPartyStatus" value="W"></c:set>
                            </c:when>
                        </c:choose></td>
                    <td><c:choose>
                            <c:when test="${billToPartyStatus eq 'C'}">
                                <b id="blueBold">CONSIGNEE</b>
                            </c:when>
                            <c:when test="${billToPartyStatus eq 'T'}">
                                <b id="blueBold">THIRD PARTY</b>
                            </c:when>
                            <c:when test="${billToPartyStatus eq 'N'}">
                                <b id="blueBold">NOTIFY</b>
                            </c:when>
                            <c:when test="${billToPartyStatus eq 'W'}">
                                <b id="blueBold">WAREHOUSE</b>
                            </c:when>
                        </c:choose></td>
                    <td colspan="7"></td>
                </tr>
                <c:if test="${not empty totalCollectChargesAmtC && totalCollectChargesAmtC ne '0.00' && billToPartyStatus ne 'C'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtC}</b></td>
                        <td><b id="blueBold">CONSIGNEE</b></td>
                    </tr>
                </c:if>
                <c:if test="${not empty totalCollectChargesAmtT && totalCollectChargesAmtT ne '0.00' && billToPartyStatus ne 'T'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtT}</b></td>
                        <td><b id="blueBold">THIRD PARTY</b></td>
                    </tr>
                </c:if>
                <c:if test="${not empty totalCollectChargesAmtN && totalCollectChargesAmtN ne '0.00' && billToPartyStatus ne 'N'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtN}</b></td>
                        <td><b id="blueBold">NOTIFY</b></td>
                    </tr>
                </c:if>
                <c:if test="${not empty totalCollectChargesAmtW && totalCollectChargesAmtW ne '0.00' && billToPartyStatus ne 'W'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtW}</b></td>
                        <td><b id="blueBold">WAREHOUSE</b></td>
                    </tr>
                </c:if>
                <tr>
                    <td colspan="3"></td>
                    <td><b id="blueBold">PPD</b></td>
                    <td><b id="greenBold">${totalPrepaidChargesAmt}</b></td>
                    <td><b id="blueBold">AGENT</b></td>
                    <td colspan="7"></td>
                </tr>
            </c:if>
            <tr>
                <td colspan="4"></td>
                <td>-----------</td>
                <td colspan="8"></td>
            </tr>
            <c:if test="${(empty totalPrepaidChargesAmt || totalPrepaidChargesAmt eq '0.00') && moduleName eq 'Imports'}">
                <c:if test="${not empty totalCollectChargesAmtC && totalCollectChargesAmtC ne '0.00'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtC}</b></td>
                        <td><b id="blueBold">CONSIGNEE</b></td>
                    </tr>
                </c:if>
                <c:if test="${not empty totalCollectChargesAmtT && totalCollectChargesAmtT ne '0.00'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtT}</b></td>
                        <td><b id="blueBold">THIRD PARTY</b></td>
                    </tr>
                </c:if>
                <c:if test="${not empty totalCollectChargesAmtA && totalCollectChargesAmtA ne '0.00'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtA}</b></td>
                        <td><b id="blueBold">AGENT</b></td>
                    </tr>
                </c:if>
                <c:if test="${not empty totalCollectChargesAmtN && totalCollectChargesAmtN ne '0.00'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtN}</b></td>
                        <td><b id="blueBold">NOTIFY</b></td>
                    </tr>
                </c:if>
                <c:if test="${not empty totalCollectChargesAmtW && totalCollectChargesAmtW ne '0.00'}">
                    <tr>
                        <td colspan="4"></td>
                        <td><b id="greenBold">${totalCollectChargesAmtW}</b></td>
                        <td><b id="blueBold">WAREHOUSE</b></td>
                    </tr>
                </c:if>
            </c:if>
            <tr>
                <td colspan="3">
                    <c:if test="${moduleName eq 'Imports' && not empty fileNumberId}">
                        <span style="color: green;font-size: medium;" class="headerlabel">Total:</span>
                        <span style="color: red;font-size: medium;font-weight:bolder">${totalStorageAmt}&nbsp;Weekly</span>
                        <div class="button-style1  costAndCharge disabledButton" id="storageCharge" onclick="storageCharge('${path}', '${fileNumberId}', '${fileNumber}');">Storage
                        </div> </c:if>
                    </td>
                    <td><b id="blueBold">TOTAL($-USD)</b></td>
                    <td id="totalchargestd"><b id="greenBold">${totalCharges}</b></td>
                <td>
                    <%-- <c:choose>
                        <c:when test="${moduleName eq 'Imports'}">
                            <c:if test="${empty totalPrepaidChargesAmt || totalPrepaidChargesAmt eq 0.00 ||
                                          empty totalCollectChargesAmt || totalCollectChargesAmt eq 0.00}">
                                  <b id="blueBold"> <c:if test="${lclBooking.billToParty eq 'C'}">CONSIGNEE</c:if>
                                      <c:if test="${lclBooking.billToParty eq 'T'}">THIRD PARTY</c:if>
                                      <c:if test="${lclBooking.billToParty eq 'A'}">AGENT</c:if>
                                      <c:if test="${lclBooking.billToParty eq 'N'}">NOTIFY</c:if></b></c:if>
                            </c:when>
                            <c:otherwise>
                            <b style="color: red;font-size: medium" class="headerlabel">${billingMethod}</b>
                        </c:otherwise>
                    </c:choose>--%>
                    <c:if test="${moduleName ne 'Imports'}">
                        <b style="color: red;font-size: medium" class="headerlabel">${billingMethod}</b>
                    </c:if>
                </td>
                <td>
                    <c:if test="${moduleName eq 'Imports'}">
                        <b id="blueBold">PD=</b>
                        <c:set var="totalpaidAmt" value="0"/>
                        <c:forEach items="${chargeList}" var="paidCharges">
                            <c:forEach items="${paidCharges.lclBookingAcTaList}" var="bkgAc">
                                <c:set var="totalpaidAmt" value="${totalpaidAmt+bkgAc.amount}"/>
                            </c:forEach>
                        </c:forEach>
                        <b id="orangeBold">${totalpaidAmt}</b>
                    </c:if>
                </td>
                <td>
                    <c:if test="${moduleName eq 'Imports'}">
                        <b id="blueBold">BAL=</b>
                        <b id="greenBold">${balanceAmt}</b>
                    </c:if>
                </td>
                <td>
                    <c:if test="${moduleName eq 'Imports'}">
                        <b id="blueBold">COST=</b>
                        <b style="color: red;font-size: medium;font-weight:bolder">${totalCostAmt}</b>
                    </c:if>
                </td>
                <td colspan="4"></td>
            </tr>
            <c:if test="${moduleName eq 'Imports'}">
                <tr>
                    <td colspan="3">
                        <b id="blueBold">A/R Balance=</b><b id="greenBold">${totalArBalanceAmount}</b>
                        <img src="${path}/images/icons/currency_blue.png" title="Show Transactions History"
                             align="middle" onclick="showTransactions('${path}');"/>
                    </td>
                    <td colspan="7"></td>
                    <td colspan="2" align="right">
                        <b id="blueBold">Total Profit=</b><b id="greenBold">${totalCharges-totalCostAmt}</b>
                    </td>
                    <td align="right">
                        <c:if test="${not empty fileNumberId}">
                            <c:set var="query" value="select format(sum(invoice_amount), 2) from ar_red_invoice where screen_name = 'LCLI DR' and file_no = '${fileNumberId}'"/>
                            <c:set var="invoiceAmount"><c:out value="${dao:getUniqueResult(query)}" /></c:set>
                            <c:if test="${not empty invoiceAmount}">
                                <b id="blueBold">Invoice Amount=</b><b id="greenBold">${invoiceAmount}</b>
                            </c:if>
                        </c:if>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td colspan="13" align="center">
                    <span style="color: red;font-size: medium;">${rateErrorMessage}</span></td>
            </tr>
        </tfoot>
    </table>
    <div id="add-Comments-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
        <c:import url="/jsps/LCL/ajaxload/adjustmentChargeBkgBlDiv.jsp"/>
    </div>
     <div id="add-Comments-containerTotal" class="static-popup" style="display: none;width: 550px;height: 130px;">
         <c:import url="/jsps/LCL/ajaxload/adjustmentTotalChgBkgBlDiv.jsp"/>
    </div>
    <input type="hidden" id="cifHidValue" name="cifHidValue" value="${lclBooking.cifValue}"/>
    <input type="hidden" name="totalCostAmt" id="totalCostAmt" value="${totalCostAmt}"/>
    <input type="hidden" name="oldFdUnlocationCode" id="oldFdUnlocationCode" value="${not empty oldFdUnlocationCode ? oldFdUnlocationCode : lclBooking.finalDestination.unLocationCode}"/>
</body>
<script type="text/javascript">
    jQuery(document).ready(function () {
        if ($('#moduleName').val() !== 'Imports' && $('#isArGlMappingFlag').val() == 'true' && $("#glMappingBlueCode").val() !== '') {
            $.prompt("BlueScreen Charge Codes <span style='color:red;font-weight:bold;'>#"
                    + $("#glMappingBlueCode").val() + "</span> not Associated with GlMapping Code");
        }
        if ($("#cifHidValue").val() != '') {
            $("#cifValue").val($("#cifHidValue").val());
        }
        $("[title != '']").not("link").tooltip();
    });
</script>
