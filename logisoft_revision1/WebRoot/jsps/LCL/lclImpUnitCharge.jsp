<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left; ">
        <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
            Charges for Unit#  <span class="fileNo">${lclUnitCostChargeForm.unitNo}</span>
        </cong:div><br><br>
        <cong:form name="lclUnitCostChargeForm" id="lclUnitCostChargeForm" action="/lclImpUnitCostCharge">
            <input type="hidden" id="headerId" name="headerId" value="${lclUnitCostChargeForm.headerId}"/>
            <input type="hidden" id="unitSsId" name="unitSsId" value="${lclUnitCostChargeForm.unitSsId}"/>
            <input type="hidden" id="unitId" name="unitId" value="${lclUnitCostChargeForm.unitId}"/>
            <input type="hidden" id="unitNo" name="unitNo" value="${lclUnitCostChargeForm.unitNo}"/>
            <input type="hidden" id="drTotal" name="drTotal" value="${lclUnitCostChargeForm.costAmount}"/>
            <input type="hidden" id="podUnlocCode" name="podUnlocCode" value="${lclUnitCostChargeForm.podUnlocCode}"/>
            <input type="hidden" id="hiddenChargeId" name="hiddenChargeId" value=""/>
            <input type="hidden" id="orginalCostAmount"  value="${lclUnitCostChargeForm.costAmount}"/>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden  id="unitStatus" name="unitStatus"/>

            <c:set var="index" value="1"/>
            <table class="dataTable" border="0" id="lclUnitSSAc">
                <thead>
                    <tr>
                        <th></th>
                        <th></th>
                        <th>Cost Code</th>
                        <th>Cost Desc</th>
                        <th>Auto Cost</th>
                        <th>Cost Amount</th>
                        <th>Vendor</th>
                        <th>Invoice#</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>User</th>
                    </tr>
                </thead>
                <c:if test="${not empty lclUnitSSAcList}">
                    <tbody>
                        <c:forEach var="lclUnitSSAc" items="${lclUnitSSAcList}">
                            <c:choose>
                                <c:when test="${rowStyle eq 'oddStyle'}">
                                    <c:set var="rowStyle" value="evenStyle"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="rowStyle" value="oddStyle"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${rowStyle}">
                                <td class="distribute">
                                    <c:choose><c:when test="${lclUnitSSAc.distributed eq false}">
                                            <input type="checkbox" id="distributeChecked" class="chkDistribute" id="${index}" value="${lclUnitSSAc.id}"
                                                   onclick="setChargeCode();" onchange="calculateFinalAmount()"/>
                                        </c:when><c:otherwise>
                                            <input type="checkbox" id="distributeChecked" class="chkDistribute" id="${index}" value="${lclUnitSSAc.id}"
                                                   disabled="true"/>
                                        </c:otherwise></c:choose>
                                </td><%--CHARGE VOLUME --%>
                                <td>
                                    <c:if test="${lclUnitSSAc.manualEntry}">
                                        <img alt="Manual Charge" title="Manual Charge" src="${path}/img/icons/asterikPurple.png" width="10" height="10" />
                                    </c:if>
                                </td>
                                <td id="cod${index}">${lclUnitSSAc.apGlMappingId.chargeCode}
                                </td>
                                <td>${lclUnitSSAc.apGlMappingId.chargeDescriptions}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${lclUnitSSAc.manualEntry}">No</c:when>
                                        <c:otherwise>Yes</c:otherwise>
                                    </c:choose>

                                </td>
                                <td>${lclUnitSSAc.apAmount}</td>
                                <td>${lclUnitSSAc.apAcctNo.accountno}</td>
                                <td>${lclUnitSSAc.apReferenceNo}</td>
                                <td>${lclUnitSSAc.apTransType}</td>
                                <td>
                                    <fmt:formatDate value="${lclUnitSSAc.modifiedDatetime}" pattern="dd-MMM-yyyy hh:mm" var="modfieddate"/>
                                    ${fn:toUpperCase(modfieddate)}
                                </td>
                                <td>${fn:toUpperCase(lclUnitSSAc.modifiedByUserId.loginName)}</td>
                            </tr>
                            <c:set var="index" value="${index+1}"/>

                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="5"></td>
                            <td >-----------</td>
                            <td colspan="5"></td>
                        </tr>
                        <tr>
                            <td colspan="3"></td>
                            <td class="greenFontBold" align="center" colspan="2">TOTAL($-USD)</td>
                            <td  class="fileNo" id="tdTotal">${lclUnitCostChargeForm.costAmount}</td>
                            <td colspan="5"></td>
                        </tr>
                    </tfoot>
                </c:if>
            </table>
            <br/>
            <cong:table width="100%" border="0">
                <cong:tr>
                    <cong:td styleClass="td" width="4%">Code</cong:td>
                    <cong:td width="15%"><cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="CHARGE_CODE" fields="NULL,chargesCodeId"
                                                             shouldMatch="true"  params="LCLI"  container="NULL" styleClass="text mandatory require" width="350" scrollHeight="150"/>
                        <cong:hidden name="chargesCodeId" id="chargesCodeId"/>
                    </cong:td>
                    <cong:td width="20%" align="right">Percentage to Add
                        <cong:text styleClass="text twoDigitDecFormat" style="width:76px" name="percentage" id="percentage" onkeyup="checkForNumberAndDecimal(this);"
                                   onchange="calculateFinalAmount();"/>
                    </cong:td>
                    <cong:td styleClass="td" width="1%"></cong:td>
                    <cong:td width="10%" align="right">Final Amount
                        <cong:text styleClass="text twoDigitDecFormat" style="width:76px" name="finalAmount" id="finalAmount" onkeyup="checkForNumberAndDecimal(this);"/>
                    </cong:td>
                    <cong:td>
                        <div class="button-style1" onclick="validateDistributeDr('${path}', 'distributeDr',$('#hiddenChargeId').val());" id="distributeToDrs" >
                            Charges/Dist
                        </div>
                        <div class="button-style1" onclick="clear();">Clear</div>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function submitForm(methodName) {
            $("#methodName").val(methodName);
            parent.$.colorbox.close();
            $("#lclUnitCostChargeForm").submit();
        }
        function sampleAlert(txt) {
            $.prompt(txt);
        }
        $(document).ready(function() {
            $(document).keydown(function(e) {
                if ($(e.target).attr("readonly")) {
                    if (e.keyCode === 8) {
                        return false;
                    }
                }
            });
        });
        function clear()
        {
            $('#finalAmount').val('');
            $('#percentage').val('');
            $('#chargesCode').val('');
            $('#chargesCodeId').val('');
        }
    </script>
</body>
