<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileNumberId" value="${param.fileNumberId}"/>
<c:set var="fileNumber" value="${param.fileNumber}"/>
<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
<c:choose>
    <c:when test="${not empty lclQuoteForm.moduleName}">
        <c:set var="moduleName" value="${lclQuoteForm.moduleName}"/>
    </c:when>
    <c:otherwise>
        <c:set var="moduleName" value="${param.moduleName}"/>
    </c:otherwise>
</c:choose>
<input type="hidden" name="isArGlMappingFlag" id="isArGlMappingFlag" value="${lclSession.isArGlmappingFlag}"/>
<input type="hidden" name="glMappingBlueCode" id="glMappingBlueCode" value="${lclSession.glMappingBlueCode}"/>
<input type="hidden" id="validateImpRates" value="${validateImpRates}"/>
<body style="background:#ffffff">
    <table width="100%">
        <tr class="tableHeadingNew">
            <td width="15%">Cost and Charges</td>
            <c:if test="${moduleName eq 'Imports'}">
                <td>Commodity:
                    <span style="color:red"> <c:out value="${commodityNumber}"/></span>
                </td>
            </c:if>
            <td width="16%">Origin:
                <span class="quoteChargeOrigin" style="color:red">
                    <c:choose>
                        <c:when test="${not empty lclQuote.portOfOrigin}">
                            <c:out value="${lclQuote.portOfOrigin.unLocationName}"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${lclQuote.portOfLoading.unLocationName}"/>
                        </c:otherwise>
                    </c:choose></span>
            </td>
            <td>Destination:
                <span class="quoteChargeDest" style="color:red"> <c:out value="${lclQuote.finalDestination.unLocationName}"/></span>
            </td>

            <td>Cube:
                <span style="color:red"> <c:out value="${measure}"/></span>
            </td>
            <td>Weight:
                <span style="color:red"> <c:out value="${weight}"/></span>
            </td>
            <td>
                <c:if test="${not empty fileNumberId}">
                    <div id="Qcalculate" class="button-style1 floatRight calculateCharge ${hideShow}" 
                         onclick="calculateCharge('', '#doorOriginCityZip', '#pickupReadyDate');">Calculate</div>
                    <div class="button-style1 floatRight quoteCostAndCharge ${hideShow}" id="QcostCharge" 
                         onclick="addQuoteCharge('${path}', '${fileNumberId}', '${fileNumber}');">Add charge</div>
                </c:if>
            </td>
        </tr>
    </table>
    <table class="dataTable" border="0" id="chargesTable">
        <thead>
            <tr>
                <th></th>
                <th></th>
                <th>Charge Code</th>
                <th>Charge Desc</th>
                <th>Charge Amount</th>
                <c:choose>
                    <c:when test="${moduleName eq 'Imports'}">
                        <c:set var="column6" value="Bill to Party"/>
                        <c:set var="column7" value="Amt Paid"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="column6" value="Adj Amt"/>
                        <c:set var="column7" value="Bundle Into OFR"/>
                    </c:otherwise>
                </c:choose>
                <th>${column6}</th>
                <c:if test="${moduleName ne 'Imports'}">
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
                        <td>${charges.arglMapping.chargeCode}</td><%--CHARGE CODE --%>
                        <td>${charges.arglMapping.chargeDescriptions}</td><%--CHARGE DESCRIPTION --%>
                        <td><%--CHARGE AMOUNT --%>
                            <c:set var="totaladjustmentamount" value="0.00"/>
                            <c:choose>
                                <c:when test="${not empty charges.rolledupCharges && charges.rolledupCharges ne '0.00'}">
                                    <p class="chargeAmount">${charges.rolledupCharges}</p>
                                    <c:set value="${totaladjustmentamount+charges.rolledupCharges}" var="totaladjustmentamount"/>
                                    <input type="hidden" id="chargeAmount${charges.id}" value="${charges.rolledupCharges}"  />
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${charges.arAmount ne '0.00'}"><p class="chargeAmount">${charges.arAmount}</p>
                                        <input type="hidden" id="chargeAmount${charges.id}" value="${charges.arAmount}"  />
                                        <c:set value="${totaladjustmentamount+charges.arAmount}" var="totaladjustmentamount"/>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${moduleName eq 'Imports'}">
                                    <c:if test="${not empty charges.arBillToParty}"><%--BILL TO PARTY For Imports --%>
                                        <c:if test="${charges.arBillToParty eq 'A'}">Agent</c:if>
                                        <c:if test="${charges.arBillToParty eq 'T'}">Third Party</c:if>
                                        <c:if test="${charges.arBillToParty eq 'C'}">Consignee</c:if>
                                        <c:if test="${charges.arBillToParty eq 'N'}">Notify Party</c:if>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${!charges.manualEntry}"><%--Adjustment Amount for Exports --%>
                                        <input type="text" name="adjustmentAmount" id="adjustmentAmount${charges.id}" class="adjustmentAmount textlabelsBoldForTextBox" style="width:76px"
                                               value="${empty charges.adjustmentAmount ? "0.00" : charges.adjustmentAmount}" onchange="allowOnlyWholeNumbers(this);
                                                   calculateAdjustmentAmount('${charges.id}','${charges.adjustmentAmount}');"/>=
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <c:if test="${moduleName ne 'Imports'}">
                            <td id="tdTotalAdjAmt${charges.id}">${totaladjustmentamount+charges.adjustmentAmount}</td>
                        </c:if>
                        <td>
                            <c:choose>
                                <c:when test="${moduleName eq 'Imports'}">

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
                        <c:if test="${moduleName eq 'Imports'}">
                            <td><%--RELS TO INV --%>
                                <c:if test="${charges.arBillToParty eq 'A'}">

                                </c:if>
                            </td>
                        </c:if>
                        <td><%-- COST VENDOR --%>
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
                                <c:out value="${transDatetime}"></c:out>
                            </td>
                        </c:if>

                        <td><%-- CHARGE STATUS --%>
                        </td>
                        <td><%-- ACTION --%>
                            <c:choose>
                                <c:when test="${moduleName eq 'Imports'}">
                                    <img src="${path}/images/edit.png"  style="cursor:pointer" class="quoteCostAndCharge" width="13" height="13" alt="edit"
                                         onclick="editQuoteCharge('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}','${charges.manualEntry}');"
                                         title="Edit Charge"/>
                                    <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                         onclick="deleteQuoteCharge('Are you sure you want to delete?', '${charges.id}', '${fileNumberId}');"
                                         title="Delete Charge"/>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${charges.manualEntry}">
                                        <c:if test="${!charges.arglMapping.destinationServices}">
                                        <img src="${path}/images/edit.png"  style="cursor:pointer" class="quoteCostAndCharge" width="13" height="13" alt="edit"
                                             onclick="editQuoteCharge('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}','${charges.manualEntry}');"
                                             title="Edit Charge"/>
                                        </c:if>
                                        <c:if test="${charges.arglMapping.destinationServices}">
                                             <img src="${path}/images/edit.png"  style="cursor:pointer" class="quoteCostAndCharge" width="13" height="13" alt="edit"
                                             onclick="editQuotedestinationServices('${path}', '${charges.id}', '${fileNumberId}', '${fileNumber}','${charges.manualEntry}','editService');"
                                             title="Edit Charge"/>
                                        </c:if>
                                        <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                             onclick="deleteQuoteCharge('Are you sure you want to delete?', '${charges.id}', '${fileNumberId}');"
                                             title="Delete Charge"/>
                                        
                                    </c:if>
                                        <c:if test="${not empty charges.adjustmentComments}">
                                            <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="adjustment_enter_date" value="${charges.modifiedDatetime}"/>
                                            <c:set var="comments" value="${fn:substring(charges.adjustmentComments,0,50)}<br/>${fn:substring(charges.adjustmentComments,50,100)}"/>
                                            <span title="<table><tr><td>${comments}</td></tr>
                                                  <tr><td>${charges.modifiedBy.loginName} ${adjustment_enter_date}</tr></td></table>">
                                                <img id="adjustmentChargeCommentViewGif" src="${path}/img/icons/view.gif" width="16" height="16" alt="info"/>
                                            </span>
                                        </c:if>
                                </c:otherwise>
                            </c:choose>

                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </c:if>
        <tfoot>
            <c:if test="${not empty totalPrepaidChargesAmt && totalPrepaidChargesAmt ne 0.00 &&
                          not empty totalCollectChargesAmt && totalCollectChargesAmt ne 0.00 && moduleName eq 'Imports'}" >
                  <tr>
                      <td colspan="4"></td>
                      <td>-----------</td>
                      <td colspan="8"></td>
                  </tr>
                  <tr>
                      <td colspan="3"></td>
                      <td><b id="blueBold">COL</b></td>
                      <td><b id="greenBold">${totalCollectChargesAmt}</b></td>
                      <td>
                          <c:if test="${moduleName eq 'Imports'}">
                              <b id="blueBold">
                                  <c:if test="${lclQuote.billToParty eq 'C'}">CONSIGNEE</c:if>
                                  <c:if test="${lclQuote.billToParty eq 'T'}">THIRD PARTY</c:if>
                                  <c:if test="${lclQuote.billToParty eq 'A'}">AGENT</c:if>
                                  <c:if test="${lclQuote.billToParty eq 'N'}">NOTIFY</c:if></b>
                              </c:if>
                      </td>
                      <td colspan="7"></td>
                  </tr>
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
            <tr>
                <td colspan="3"></td>
                <td><b id="blueBold">TOTAL($-USD)</b></td>
                <td id="totalchargestd"><b id="greenBold">${totalCharges}</b></td>
                <td>
                    <c:choose>
                        <c:when test="${moduleName eq 'Imports'}">
                            <c:if test="${empty totalPrepaidChargesAmt || totalPrepaidChargesAmt eq 0.00 ||
                                          empty totalCollectChargesAmt || totalCollectChargesAmt eq 0.00}">
                                  <b id="blueBold">
                                      <c:if test="${lclQuote.billToParty eq 'C'}">CONSIGNEE</c:if>
                                      <c:if test="${lclQuote.billToParty eq 'T'}">THIRD PARTY</c:if>
                                      <c:if test="${lclQuote.billToParty eq 'A'}">AGENT</c:if>
                                      <c:if test="${lclQuote.billToParty eq 'N'}">NOTIFY</c:if></b></c:if>
                            </c:when>
                            <c:otherwise>
                            <b style="color: red;font-size: medium" class="headerlabel">${billingMethod}</b>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td colspan="2"></td>
                <td>
                    <c:if test="${moduleName eq 'Imports'}">
                        <b id="blueBold">COST=</b>
                        <b style="color: red;font-size: medium;font-weight:bolder">${totalCostAmt}</b>
                    </c:if>
                </td>
                <td colspan="4">
                    <c:if test="${moduleName ne 'Imports'}">
                        <b style="color: red;font-size: medium" class="headerlabel">${rateErrorMessage}</b>
                    </c:if>
                </td>
            </tr>
        </tfoot>
    </table>
</body>
<div id="add-Comments-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
    <table class="table" style="margin: 2px;width: 598px;">
        <tr>
            <th>
                <div class="float-left">
                    <label id="headingAdjustmentComments"></label>
                </div>
            </th>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
        <input type="hidden" name="chargeId" id="qtChargeId" /><%-- chargeId --%>
        <input type="hidden" name="oldAdjusmentVal" id="oldAdjusmentVal" /> <%-- oldAdjustmentValue --%>
        <td class="label">
            <textarea id="adjustmentcomments" name="adjustmentcomments" cols="85" rows="5" class="textBoldforlcl"
                      style="resize:none;text-transform: uppercase"></textarea>
        </td>
        </tr>
        <tr>
            <td align="center">
                <input type="button"  value="Save" id="saveComments"
                       align="center" class="button" onclick="addAdjustmentComments();"/>
                <input type="button"  value="Cancel" id="cancelHotCode"
                       align="center" class="button" onclick="cancelAdjustmentComments();"/>
            </td>
        </tr>
    </table>
        <input type="hidden" id="cifHidValue" name="cifHidValue" value="${lclQuote.cifValue}"/>
</div>
<script type="text/javascript">
    jQuery(document).ready(function () {
        if($('#isArGlMappingFlag').val()!='' && $('#isArGlMappingFlag').val()=='true' && $("#glMappingBlueCode").val()!==''){
            $.prompt("BlueScreen Charge Codes <span style='color:red;font-weight:bold;'>#"+$("#glMappingBlueCode").val()
                    +"</span> not Associated with GlMapping Code");
        }
         if($("#cifHidValue").val()!=''){
           $("#cifValue").val($("#cifHidValue").val());
        }
        $("[title != '']").not("link").tooltip();
    });
</script>
