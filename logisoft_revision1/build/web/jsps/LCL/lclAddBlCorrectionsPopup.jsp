<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclCorrection.js"/>
<cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="lclCorrection.do">
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="correctionId" name="correctionId"/>
    <cong:hidden id="correctionChargeId" name="correctionChargeId"/>
    <cong:hidden id="lclBookingAcId" name="lclBookingAcId"/>
    <cong:hidden id="chargeId" name="chargeId"/>
    <c:choose><c:when test="${lclCorrectionForm.selectedMenu eq 'Imports'}">
            <c:set var="shipmentType" value="LCLI"/>
        </c:when>
        <c:otherwise> 
            <c:set var="shipmentType" value="LCLE"/></c:otherwise>
    </c:choose>
    <table width="100%" border="0" style="border:1px solid #dcdcdc;">
        <tr class="tableHeadingNew">
            <td colspan="6">Add/Edit Correction Charge</td></tr>
        <tr class="textlabelsBold">
            <td class="textlabelsBoldforlcl">Charge Code Desc</td>
            <td><cong:text  name="chargeDescriptions" maxlength="60" styleClass="textlabelsBoldForTextBoxDisabledLook"
                        readOnly="true"  id="chargeDescriptions"/>
            </td>
            <td class="textlabelsBoldforlcl">Charge Code</td>
            <td>
                <c:choose>
                    <c:when test="${lclCorrectionForm.chargeStatus=='edit'}">
                        <cong:text name="chargeCode"  id="chargeCode" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                    </c:when>
                    <c:otherwise>
                        <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="CHARGE_CODE" fields="chargeDescriptions,chargeId"
                                            container="NULL" styleClass="text mandatory require" width="250" scrollHeight="150"  params="${shipmentType}"/>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="textlabelsBoldforlcl">Bill To</td>
            <td>
                <html:select property="billToParty" styleId="billToParty" style="width:120px;" styleClass="dropdown_accounting" disabled="true" onchange="changeBillToParty();">
                    <html:optionsCollection name="billToVendor"/></html:select>
                <input type="hidden" name="existBillToParty" id="existBillToParty" value="${lclCorrectionForm.billToParty}"/>
                </td>
            </tr>
            <tr class="textlabelsBold">
                <td class="textlabelsBoldforlcl">Old Amount</td>
                <td><cong:text name="oldAmount" id="oldAmount" styleClass="textlabelsBoldForTextBoxDisabledLook"
                       readOnly="true"  maxlength="8"/></td>

            <td class="textlabelsBoldforlcl">New Amount</td>
            <td><html:text  property="newAmount" styleClass="textlabelsBoldForTextBox" maxlength="8"
                        onkeyup="allowOnlyWholeNumbers(this)" styleId="newAmount" onchange="computeDifference()"/></td>
            <td class="textlabelsBoldforlcl">Difference</td>
            <td><cong:text name="differenceAmount"  id="differenceAmount" styleClass="textlabelsBoldForTextBoxDisabledLook"
                       readOnly="true" maxlength="13"/></td>
        </tr>
        <tr><td colspan="6">&nbsp;</td></tr>
        <tr>
            <td colspan="6" align="center">
                <input type="button" class="buttonStyleNew" value="Save"  onclick="saveCorrectionCharges();"/>
            </td>
        </tr>
    </table>
</cong:form>
<script>
    jQuery(document).ready(function() {
        var corrCode = parent.$("#correctionCodeId option:selected").text();
        var correctionCode = corrCode.split("-");
        if (parent.document.getElementById("correctionTypeIdY").value === parent.document.lclCorrectionForm.correctionType.value || correctionCode[0] === '001') {
            document.lclCorrectionForm.billToParty.disabled = false;
        }
    });
    function allowOnlyWholeNumbers(obj) {
        if (!/^\d+\.?\d{0,2}$/.test(obj.value)) {
            $.prompt("This field should be Numeric", {
                callback: function(){
                    $(obj).val("").focus();
                }
            });
        }
    }
    
    function computeDifference(){
        if($("#newAmount").val() !== ""){
            $("#newAmount").val(Number($("#newAmount").val()).toFixed(2));
            $("#differenceAmount").val(Number(Number($("#newAmount").val()) - Number($("#oldAmount").val())).toFixed(2));
        }
    }
</script>
