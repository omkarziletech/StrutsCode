<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib prefix="fmt" uri="/WEB-INF/fmt-1_0-rt.tld" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"  %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileNumberId" value="${param.fileNumberId}"/>
<c:set var="fileNumber" value="${param.fileNumber}"/>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left;">
        <table width="100%">
            <tr class="tableHeadingNew">
                <td width="15%" colspan="2">Cost and Charges</td>
                <td width="10%">Rate Basis:</td>
                <td width="16%">O/F:
                    <span style="color:red"> <c:out value="${ofratebasis}"/></span>
                </td>
                <td>Std Chgs:
                    <span style="color:red"> <c:out value="${stdchgratebasis}"/></span></td>
                <td>Cube:
                    <span style="color:red"> <c:out value="${actualVolume}"/></span>
                </td>
                <td>Weight:
                    <span style="color:red"> <c:out value="${actualWeight}"/></span>
                </td>
                <td style="float: right">
                    <div id="calculate" class="button-style1 calculateCharge" onclick="calculateBlCharge()">Calculate</div>
                    <div class="button-style1 costAndCharge" id="costCharge" 
                         onclick="addBlCharge('${path}', '${fileNumber}', '${fileNumberId}')">Add charge</div></td>
            </tr>
        </table>
        <table width="100%" border="0" class="dataTable" id="chargesTable">
            <thead>
                <tr>
                    <th></th>
                    <th></th>
                    <th>Charge Code</th>
                    <th>Charge Desc</th>
                    <th>Charge Amount</th>
                    <th>Adj Amt</th>
                    <th>Total Adj Amt</th>
                    <th>Bill To Party</th>
                    <th>Bundle Into OFR</th>
                    <th>Print On BL</th>
                    <th>Rate</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="forwarderTotalAmount" value="${0}"/>
                <c:set var="shipperTotalAmount" value="${0}"/>
                <c:set var="thirtyPartyTotalAmount" value="${0}"/>
                <c:set var="agentTotalAmount" value="${0}"/>
                <c:if test="${not empty chargeList}">
                    <fmt:formatNumber var="profit" value=" ${totalCharges-totalCost}" pattern="###,###,##0.00"/>
                    <c:forEach items="${chargeList}" var="charge" varStatus="count">
                        <c:choose>
                            <c:when test="${zebra eq 'odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${zebra}" >
                            <td>${charge.label1}</td>
                            <td>
                                <c:if test="${charge.manualEntry}">
                                    <img alt="Manual Charge" title="Manual Charge"style="vertical-align: middle" src="${path}/img/icons/asterikPurple.png" width="10" height="10" />
                                </c:if>
                            </td>
                            <td class="chargeCode">${charge.arglMapping.chargeCode}
                                <c:if test="${charge.manualEntry && isConsolidate}">
                                    <span title="${charge.consolidateCharges}">
                                        <img alt="Total Charge" style="vertical-align: middle" src="${path}/img/icons/greenasteriskIcon.png" width="10" height="10" />
                                    </span>
                                </c:if>
                            </td>
                            <td>${charge.arglMapping.chargeDescriptions}</td>
                            <td class="chargeAmount">${charge.rolledupCharges}</td>
                            <td> 
                                <c:if test="${!charge.manualEntry}">
                                    <input type="text" name="adjustmentAmount" id="adjustmentAmount${charge.id}" class="adjustmentAmount textlabelsBoldForTextBox" 
                                           style="width:70px" value="${empty charge.adjustmentAmount ? "0.00" : charge.adjustmentAmount}"
                                           onchange="allowOnlyWholeNumbers(this);
                                                   calculateAdjustmentAmount('${charge.id}', '${charge.adjustmentAmount}');"
                                           ${charge.lclFileNumber.status eq "M" && lclBlForm.blUnitCob eq true ? "disabled" :""}/>=
                                </c:if>
                            </td>
                            <td>
                                ${charge.adjustmentAmount+charge.rolledupCharges}
                                <c:if test="${not empty charge.adjustmentComment}">
                                    <fmt:formatDate pattern="yyyy-MM-dd hh:mm" var="adjustment_enter_date" value="${charge.modifiedDatetime}"/>
                                    <c:set var="comments" value="${fn:substring(charge.adjustmentComment,0,50)}<br/>${fn:substring(charge.adjustmentComment,50,100)}"/>
                                    <span title="<table><tr><td>${comments}</td>
                                          <td>${charge.modifiedBy.loginName} ${adjustment_enter_date}</td></tr></table>">
                                        <img src="${path}/img/icons/iicon.png" style="vertical-align: middle"  width="12" height="12"/>
                                    </span>
                                </c:if>
                                <c:if test="${!charge.manualEntry}">
                                    <img src="${path}/img/icons/edit.gif" title="TotalAdjustmentCharge" style="vertical-align: middle"  width="12" height="12" onclick="calculateAdjustmentTotal('${charge.id}', '${charge.rolledupCharges}');"/>
                                </c:if>
                            </td>
                            <td class="billToParty billingCharge${count.index}">
                                <c:if test="${not empty charge.arBillToParty}">
                                    <c:choose>
                                        <c:when test="${charge.arBillToParty eq 'A'}">
                                            <c:set value="${charge.arBillToParty}" var="Agent"/>
                                            <c:set var="agentTotalAmount" value="${agentTotalAmount + charge.rolledupCharges + charge.adjustmentAmount}" />
                                            Agent
                                        </c:when>
                                        <c:when test="${charge.arBillToParty eq 'T'}">
                                            <c:set value="${charge.arBillToParty}" var="ThirdParty"/>
                                            <c:set var="thirtyPartyTotalAmount"
                                                   value="${thirtyPartyTotalAmount + charge.rolledupCharges + charge.adjustmentAmount}" />
                                            Third Party
                                        </c:when>
                                        <c:when test="${charge.arBillToParty eq 'F'}">
                                            <c:set value="${charge.arBillToParty}" var="Forwarder"/>
                                            <c:set var="forwarderTotalAmount" value="${forwarderTotalAmount + charge.rolledupCharges + charge.adjustmentAmount}" />
                                            Forwarder
                                            <input type="hidden" id="fwdValidate" value="${charge.arBillToParty}">
                                        </c:when>
                                        <c:when test="${charge.arBillToParty eq 'S'}">
                                            <c:set value="${charge.arBillToParty}" var="Shipper"/>
                                            <c:set var="shipperTotalAmount" value="${shipperTotalAmount + charge.rolledupCharges + charge.adjustmentAmount}" />
                                            Shipper
                                        </c:when>
                                    </c:choose>
                                </c:if>
                            </td>
                            <td>
                                <span class="isBundle${count.index}" style="display:none;">${charge.bundleIntoOf}</span>
                                <span class="chargeId${count.index}" style="display:none;">${charge.id}</span>
                                <c:if test="${charge.arglMapping.chargeCode ne 'OCNFRT'}">
                                    <input type="checkBox" name="bundleToOf" id="bundleToOf${charge.id}" class="bundle${count.index}"
                                           ${charge.bundleIntoOf ? "checked" :""} onclick="updateBlCharges('${charge.id}', 'B')"/>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${corrected ne 'Y'}">
                                        <input type="checkBox" name="printOnBL" id="printOnBL${charge.id}" class="printOnBL"
                                               ${charge.printOnBl ? "checked" :""} onclick="updateBlCharges('${charge.id}', 'P')"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkBox" name="correctedPrintOnBL" id="correctedPrintOnBL${charge.id}"
                                               class="correctedPrintOnBL" ${charge.printOnBl ? "checked" :""} 
                                               onclick="updateBlCharges('${charge.id}', 'C')"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${charge.label2}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty charge && charge.manualEntry && charge.arglMapping.chargeCode ne ffcommchargecode
                                                    &&  charge.arglMapping.chargeCode ne pbachargecode}">
                                        <c:if test="${lclBl.billingType eq 'B'}">
                                            <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge hideImg" width="15" height="15" alt="edit"
                                                 onclick="editBlCharge('${path}', '${charge.id}', '${fileNumberId}', '${fileNumber}',${charge.manualEntry});"
                                                 title="Edit Charge"/>
                                        </c:if>
                                        <c:if test="${lclBl.billingType ne 'B'}">
                                            <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge hideImg" width="15" height="15" alt="edit"
                                                 onclick="editBlCharge('${path}', '${charge.id}', '${fileNumberId}', '${fileNumber}',${charge.manualEntry});"
                                                 title="Edit Charge"/>
                                        </c:if>
                                        <img src="${path}/images/error.png" style="cursor:pointer" width="15" height="15" alt="delete" class="hideImg"
                                             onclick="deleteBlCharge('Are you sure you want to delete?', '${charge.id}', '${fileNumberId}', '${fileNumber}', '${charge.arglMapping.chargeCode}');"
                                             title="Delete Charge"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/images/edit.png"  style="cursor:pointer" class="costAndCharge hideImg" width="15" height="15" alt="edit"
                                             onclick="editBlCharge('${path}', '${charge.id}', '${fileNumberId}', '${fileNumber}',${charge.manualEntry});"
                                             title="Edit Charge"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
            </tbody>
            <c:if test="${not empty chargeList}">
                <tfoot>
                    <c:if test="${Agent eq 'A'}">
                        <tr>
                            <td class="greenBold">BILL TO PARTY(Agent)</td><td></td>
                            <%--for Freight Agent Acct--%>
                            <td class="blueBold" id="showFreightNo" style="display: none;">
                                <span  id="freightAgentNo"></span>
                            </td> 
                            <td id="showFreightName" style="display: none;">
                                <img src="${path}/img/icons/iicon.png"  style="vertical-align: middle"
                                     width="16" height="16" id="frtAgt" alt="FreightAgent" />
                            </td>
                            <c:choose>
                                <%--for Freight Agent Acct--%>
                                <c:when test="${lclBooking.agentAcct.accountno ne null}">
                                    <td class="blueBold" id="showPickFreightNo">${lclBooking.agentAcct.accountno}</td> 
                                    <td id="showPickFreightName">
                                        <img src="${path}/img/icons/iicon.png"  style="vertical-align: middle"  width="16" height="16" title="${lclBooking.agentAcct.accountName}"/>
                                    </td>
                                </c:when>
                                <%--for Pickup Agent Acct--%>
                                <c:otherwise>
                                    <td class="blueBold" id="showPickFreightNo">${lclBl.agentAcct.accountno}</td> 
                                    <td id="showPickFreightName">
                                        <img src="${path}/img/icons/iicon.png"  style="vertical-align: middle"  width="16" height="16" title="${lclBl.agentAcct.accountName}"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td class="redBold">${agentTotalAmount}</td>
                        </tr>
                    </c:if>
                    <c:if test="${Shipper eq 'S'}">
                        <tr>
                            <td class="greenBold">BILL TO PARTY(Shipper)</td><td></td>
                            <td class="blueBold">${lclBl.shipAcct.accountno}</td>
                            <td>
                                <img src="${path}/img/icons/iicon.png" style="vertical-align: middle"  width="16" height="16" title="${lclBl.shipAcct.accountName}"/>
                            </td>
                            <td class="redBold">${shipperTotalAmount}</td>
                        </tr>
                    </c:if>
                    <c:if test="${ThirdParty eq 'T'}">
                        <tr>
                            <td class="greenBold">BILL TO PARTY(Third Party)</td><td></td>
                            <td class="blueBold">${lclBl.thirdPartyAcct.accountno}</td>
                            <td>
                                <img src="${path}/img/icons/iicon.png" style="vertical-align: middle"  width="16" height="16" title="${lclBl.thirdPartyAcct.accountName}"/>
                            </td>
                            <td class="redBold">${thirtyPartyTotalAmount}</td>
                        </c:if>
                        <c:if test="${Forwarder eq 'F'}">
                        <tr>
                            <td class="greenBold">BILL TO PARTY(Forwarder)</td><td></td>
                            <td class="blueBold">${lclBl.fwdAcct.accountno}</td>
                            <td>
                                <img src="${path}/img/icons/iicon.png" style="vertical-align: middle"  width="16" height="16" title="${lclBl.fwdAcct.accountName}"/>
                            </td>
                            <td class="redBold">${forwarderTotalAmount}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td></td>
                        <td ></td>
                        <td></td>
                        <td></td>
                        <td>-------------------</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="greenBold" align="right">A/R Balance: </td>
                        <td  ></td>
                        <td class="redBold">${totalArBalanceAmount}</td>
                        <td align="center" class="greenBold">
                            TOTAL($-USD)
                        </td>
                        <td  id="totalchargestd" style="color: red;font-size: medium;font-weight:bolder" align="right">
                            ${corrected eq 'Y' ? totalArBalanceAmount : totalCharges}              </td>
                        <td  class="redBold blBillingType" align="center" colspan="2">
                            <c:choose>
                                <c:when test="${lclBl.billingType eq 'P'}">PREPAID</c:when>
                                <c:when test="${lclBl.billingType eq 'C'}">COLLECT</c:when>
                                <c:otherwise>BOTH</c:otherwise>
                            </c:choose>
                            ${corrected eq 'Y' ? '(Corrected)':''}
                        </td>
                    </tr>
                    <tr>
                        <td class="greenBold" align="right"> </td>
                        <td  ></td>
                        <td class="redBold"></td>
                        <td align="center" class="greenBold">
                            Cost Total:
                        </td>
                        <td  style="color: red;font-size: medium;font-weight:bolder;" align="right">
                            ${totalCost}
                        </td>
                        <td ></td>
                    </tr>
                    <tr>
                        <td class="greenBold" align="right"> </td>
                        <td  ></td>
                        <td class="redBold"></td>
                        <td align="center" class="greenBold">
                            Profit:
                        </td>
                        <td  style="color: red;font-size: medium;font-weight:bolder" align="right">
                            <c:if test="${corrected eq 'Y'}">
                                <fmt:formatNumber var="profit" value=" ${totalArAmount-totalCost}" pattern="###,###,##0.00"/>
                            </c:if>
                            ${profit}
                        </td>
                    </tr>
                </tfoot>
            </c:if>
        </table>
        <table width="100%" border="0">
            <tr>
                <td width="100%" align="center" class="redBold">${rateErrorMessage}</td>
            </tr>
        </table>
    </cong:div>

    <div id="add-Comments-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
        <c:import url="/jsps/LCL/ajaxload/adjustmentChargeBkgBlDiv.jsp"/>
    </div>
    <div id="add-Comments-containerTotal" class="static-popup" style="display: none;width: 550px;height: 130px;">
        <c:import url="/jsps/LCL/ajaxload/adjustmentTotalChgBkgBlDiv.jsp"/>
    </div>
    <input type="hidden" id="cifHidValue" name="cifHidValue" value="${lclBl.cifValue}"/>
</body>
<script type="text/javascript">
    jQuery(document).ready(function () {
        if ($("#cifHidValue").val() != '') {
            $("#cifValue").val($("#cifHidValue").val());
        }
    });
</script>
