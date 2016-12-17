<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<body style="background:#ffffff">
    <cong:form name="lclUnitCostChargeForm" id="lclUnitCostChargeForm" action="/lclImpUnitCostCharge">
        <input type="hidden" id="methodName" name="methodName"/>
        <input type="hidden" id="fileId" name="fileId"/>
        <input type="hidden" id="chargesAmount" name="chargesAmount"/>
        <input type="hidden" id="minimumAmt" name="minimumAmt"/>
        <input type="hidden" id="headerId" name="headerId" value="${lclUnitCostChargeForm.headerId}"/>
        <input type="hidden" id="unitSsId" name="unitSsId" value="${lclUnitCostChargeForm.unitSsId}"/>
        <input type="hidden" id="unitId" name="unitId" value="${lclUnitCostChargeForm.unitId}"/>
        <input type="hidden" id="finalAmount" name="finalAmount" value="${lclUnitCostChargeForm.finalAmount}"/>
        <input type="hidden" id="chargesCode" name="chargesCode" value="${lclUnitCostChargeForm.chargesCode}"/>
        <input type="hidden" id="invoiceNumber" name="invoiceNumber" value="${lclUnitCostChargeForm.invoiceNumber}"/>
        <input type="hidden" id="unitNo" name="unitNo" value="${lclUnitCostChargeForm.unitNo}"/>
        <input type="hidden" id="message" name="message" value="${lclUnitCostChargeForm.message}"/>
        <input type="hidden" id="podUnlocCode" name="podUnlocCode" value="${lclUnitCostChargeForm.podUnlocCode}"/>
        <cong:hidden id="lclSsAcId" name="lclSsAcId" value="${lclSsAcId}"/>
        <cong:hidden  id="unitStatus" name="unitStatus"/>
        <cong:table align="center" width="100%" border="0">
            <cong:tr>
                <cong:td width="10%" styleClass="greenFontBold">Distributed Amount = </cong:td>
                <cong:td width="15%" styleClass="fileNo">${lclUnitCostChargeForm.finalAmount}</cong:td>
                <cong:td width="10%" styleClass="greenFontBold">Original Cost Amount = </cong:td>
                <cong:td width="90%" styleClass="fileNo">${orginalCostAmount}</cong:td>
            </cong:tr>
        </cong:table>
        <table class="dataTable" border="0" id="charges">
            <thead>
                <tr>
                    <th>DR#</th>
                    <th>Destination</th>
                    <th>Basis</th>
                    <th>Charge Code</th>
                    <th>Charge Amt</th>
                    <th>Bill To Party</th>
                </tr>
            </thead>
            <c:if test="${not empty drList}">
                <tbody>
                    <c:forEach var="charges" items="${drList}">
                        <c:choose>
                            <c:when test="${rowStyle eq 'oddStyle'}">
                                <c:set var="rowStyle" value="evenStyle"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="rowStyle" value="oddStyle"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${rowStyle}">
                            <td>${charges.fileNo}</td>
                            <td>${charges.finalDestination}</td>
                            <td>${charges.basis}</td>
                            <td>${charges.chargeCode}</td>
                            <td>
                                <input type="text" class="chgAmt text twoDigitDecFormat" id="chargeAmount${charges.fileId}"
                                       name="chargeAmount"
                                       onkeyup="checkForNumberAndDecimal(this);" onchange="calculateDistributeChargeamt();"
                                       value="${charges.strTotalCharges}"/>
                                <input type="hidden" id="${charges.fileId}" class="hiddenFileId" value="${charges.fileId}"/>
                                <span class="greenBold">${charges.minimumAmount}</span>
                            </td>
                            <td>Consignee</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>-----------</td>
                        <td></td>
                        <td>-----------</td>
                    </tr>
                    <tr>
                        <td></td>
                        <td id="blueBold">TOTAL BASIS</td>
                        <td id="greenBold">${lclUnitCostChargeForm.costAmount}</td>
                        <td id="blueBold">TOTAL($-USD)</td>
                        <td id="tdChargeTotal" class="greenBold">${lclUnitCostChargeForm.chargesAmount}</td>
                    </tr>
                </tfoot>
            </c:if>
        </table>
        <br/>



        <cong:table align="center" width="100%" border="0">
            <cong:tr>
                <cong:td width="42%" > </cong:td>
                <cong:td width="50%" >
                    <input type="button" class="button-style1" value="Distribute" id="saveCode" onclick="distributeAllDrs($('#lclSsAcId').val());" />
                    <input type="button" class="button-style1" value="Abort" id="abort" onclick="abortCurrentPopup();" />
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:form></body>
<script>
    jQuery(document).ready(function () {
        if (document.lclUnitCostChargeForm.message.value != null && document.lclUnitCostChargeForm.message.value != "")
        {
            document.lclUnitCostChargeForm.message.value = "";
            parent.$.colorbox.close();
            parent.parent.$.colorbox.close();
        }
    });
</script>