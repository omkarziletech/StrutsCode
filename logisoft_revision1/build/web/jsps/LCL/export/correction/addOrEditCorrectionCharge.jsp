<%-- 
    Document   : addOrEditCorrectionCharge
    Created on : Jan 11, 2016, 11:35:46 AM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="../../colorBox.jsp" %>
<%@include file="../../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add/Edit Correction Charge</title>
    </head>
    <body>
        <cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="/blCorrection.do">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="correctionId" name="correctionId"  value="${lclCorrectionForm.correctionId}"/>
            <cong:hidden id="correctionChargeId" name="correctionChargeId" value="${lclCorrectionForm.correctionChargeId}"/>
            <cong:hidden id="lclBookingAcId" name="lclBookingAcId"/>
            <cong:hidden id="chargeId" name="chargeId"/>
            <cong:hidden id="previousNewAmount" name="previousNewAmount" value="${lclCorrectionForm.newAmount}"/>
            <input type ="hidden" name="correctionType" id="correctionType" value="${correctionType}"/>
            <c:set var="shipmentType" value="LCLE"/>
            <table width="100%" border="0" style="border:1px solid #dcdcdc;">
                <tr class="tableHeadingNew">
                    <td colspan="6">${not empty lclCorrectionForm.correctionId ? "Edit" :"Add"} Correction Charge</td></tr>
                <tr class="textlabelsBold">
                    <td class="textlabelsBoldforlcl">Charge Code Desc</td>
                    <td>
                        <cong:text  name="chargeDescriptions" maxlength="60" styleClass="textlabelsBoldForTextBoxDisabledLook"
                                    readOnly="true"  id="chargeDescriptions"/>
                    </td>
                    <td class="textlabelsBoldforlcl">Charge Code</td>
                    <td>
                        <c:choose>
                            <c:when test="${lclCorrectionForm.chargeStatus=='edit'}">
                                <cong:text name="chargeCode"  id="chargeCode" readOnly="true"
                                           styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                            </c:when>
                            <c:otherwise>
                                <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="CHARGE_CODE" fields="chargeDescriptions,chargeId"
                                                    container="NULL" styleClass="text mandatory require" width="250" shouldMatch="true" scrollHeight="150"  params="${shipmentType}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="textlabelsBoldforlcl">Bill To</td>
                    <td>
                        <html:select property="billToParty" styleId="billToParty" style="width:120px;" 
                                     styleClass="dropdown_accounting" onchange="checkValidAmount();">
                            <html:optionsCollection name="billingList"/>
                        </html:select>
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
    </body>
</html>
<script type="text/javascript">
    jQuery(document).ready(function () {
        var corrCode = parent.$("#correctionCodeId option:selected").text();
        var correctionCode = corrCode.split("-");
//        if ($("#correctionType").val() !== 'Y') {
//            $("#billToParty").addClass("textlabelsBoldForTextBoxDisabledLook");
//            $("#billToParty").attr("disabled", true);
//        }
    });
    function allowOnlyWholeNumbers(obj) {
        if (!/^\d+\.?\d{0,2}$/.test(obj.value)) {
            $.prompt("This field should be Numeric", {
                callback: function () {
                    $(obj).val("").focus();
                }
            });
        }
    }

    function computeDifference() {
        if ($("#newAmount").val() !== "") {
            $("#newAmount").val(Number($("#newAmount").val()).toFixed(2));
            $("#differenceAmount").val(Number(Number($("#newAmount").val()) - Number($("#oldAmount").val())).toFixed(2));
        }
    }
    function saveCorrectionCharges() {
        if (validateCorrectionCharges()) {
            showLoading();
            var creditEmail = [];
            var debitEmail = [];
            parent.jQuery(".creditEmailId").each(function () {
                if (jQuery(this).is(":checked") === true) {
                    creditEmail.push(jQuery(this).val());
                }
            });
            parent.jQuery(".debitEmailId").each(function () {
                if (jQuery(this).is(":checked") === true) {
                    debitEmail.push(jQuery(this).val());
                }
            });
            parent.$('#chargeId').val(document.lclCorrectionForm.chargeId.value);
            parent.$('#oldAmount').val($('#oldAmount').val());
            parent.$('#newAmount').val($('#newAmount').val());
            parent.$('#billToParty').val(document.lclCorrectionForm.billToParty.value);
            parent.$('#correctionChargeId').val(document.lclCorrectionForm.correctionChargeId.value);
            parent.$('#lclBookingAcId').val(document.lclCorrectionForm.lclBookingAcId.value);
            parent.$('#currentProfit').val(parent.$("#lblCurrentProfit").text().trim().trim());
            parent.$('#profitAfterCN').val(parent.$("#lblProfitAfterCn").text().trim().trim());
            parent.$('#consigneeNo').val(parent.$('#constAcctNo').val());
            parent.$('#notifyNo').val(parent.$('#notyAcctNo').val());
            parent.$('#thirdPartyAcctNo').val(parent.$('#thirdpartyaccountNo').val());
            parent.$('#previousNewAmount').val($('#previousNewAmount').val());
            parent.$('#creditMemoEmail').val(creditEmail);
            parent.$('#debitMemoEmail').val(debitEmail);
            parent.$("#methodName").val('saveCorrectionCharges');
            parent.$("#formChangedVal").val(true);
            parent.$("#lclCorrectionForm").submit();
        }
    }
    function validateCorrectionCharges() {
        var currentBillToParty = $("#billToParty").val();
        var existBillToParty = $("#existBillToParty").val();
        var currentBillToPartyAccount = "";
        var exitBillToPartyAccount = "";
        var consigneeAcctNameAndConsigneeAcctNo = $("#constAcctName", parent.document).val() + " " + $("#constAcctNo", parent.document).val();
        var thirdPartyAcctNameAndthirdPartyAcctNo = $("#thirdPartyname", parent.document).val() + " " + $("#thirdpartyaccountNo", parent.document).val();
        var notifyAcctNameAndnotifyAcctNo = $("#notyAcctName", parent.document).val() + " " + $("#notyAcctNo", parent.document).val();
        var corrCode = $("#correctionCodeId option:selected", parent.document).text();
        var correctionCode = corrCode.split("-");
        if (document.lclCorrectionForm.chargeId.value == "" || document.lclCorrectionForm.chargeId.value == "0") {
            $.prompt("Please enter ChargeCode");
            document.lclCorrectionForm.chargesCode.style.borderColor = "red";
            document.lclCorrectionForm.chargesCode.value = "";
            $("#warning").parent.show();
            return false;
        }
        if (document.lclCorrectionForm.newAmount.value === "") {
            $.prompt("Please enter New Amount");
            document.lclCorrectionForm.newAmount.style.borderColor = "red";
            $("#warning").parent.show();
            return false;
        }
        if (correctionCode[0] === "001" && Number(document.lclCorrectionForm.oldAmount.value).toFixed(2) !== 0.00) {
            var billToParty = document.lclCorrectionForm.billToParty.value;
            if (currentBillToParty !== existBillToParty) {
                currentBillToPartyAccount = currentBillToParty === 'C' ?
                        consigneeAcctNameAndConsigneeAcctNo : currentBillToParty === 'N' ? notifyAcctNameAndnotifyAcctNo :
                        currentBillToParty === 'T' ? thirdPartyAcctNameAndthirdPartyAcctNo : '';
                exitBillToPartyAccount = existBillToParty === 'C' ? consigneeAcctNameAndConsigneeAcctNo :
                        existBillToParty === 'N' ? notifyAcctNameAndnotifyAcctNo : existBillToParty === 'T' ?
                        thirdPartyAcctNameAndthirdPartyAcctNo : '';
                if (currentBillToPartyAccount === exitBillToPartyAccount && (currentBillToPartyAccount !== ''
                        && exitBillToPartyAccount !== '')) {
                    $.prompt("New Bill to Party account cannot be same as the Old Bill to Party account. Please change the " + currentBillToParty + " account.");
                    return false;
                }
            } else if (document.lclCorrectionForm.differenceAmount.value === "0.00") {
                $.prompt("Old Amount and New Amount cannot be same.");
                return false;
            }
        } else if (document.lclCorrectionForm.differenceAmount.value === "0.00") {
            $.prompt("Old Amount and New Amount cannot be same.");
            return false;
        }
        if (parent.document.getElementById('chargesTable') != null && $("#chargesCode").length > 0) {
            var chargestableobj = parent.document.getElementById('chargesTable');
            var row = chargestableobj.rows.item(0);
            var chargeCode = $("#chargesCode").val();
            var originId = parent.parent.$("#portOfOriginId").val();
            if (!validateCostGLAccount(chargeCode, originId)) {
                return false;
            }
            for (var i = 1; i <= chargestableobj.rows.length - 1; i++) {
                var chargeCodeFound = false, billToPartyFound = false;
                for (var j = 0; j < row.cells.length; j++) {
                    var col = row.cells.item(j);
                    if (col.innerHTML.toString().trim().indexOf("Charge Code") != -1) {
                        if (chargestableobj.rows[i].cells[j].innerHTML == chargeCode) {
                            chargeCodeFound = true;
                        }
                    }
                    if (col.innerHTML.toString().trim().indexOf("Bill To Party") != -1) {
                        if (chargestableobj.rows[i].cells[j].innerHTML == document.lclCorrectionForm.billToParty.value) {
                            billToPartyFound = true;
                        }
                    }
                    if (chargeCodeFound && billToPartyFound) {
                        $.prompt("Charge Code " + chargeCode + " with BillToParty " + document.lclCorrectionForm.billToParty.value + " Already Exists.");
                        return false;
                    }
                }
            }
        }
        if (currentBillToParty == "F" && (parent.$("#expForwarderNo").val() == null
                || parent.$("#expForwarderNo").val() == "")) {
            $.prompt("Forwarder Account Number is required");
            return false;
        } else if (currentBillToParty == "F" && filterNoFreightForwarderAc()) {
            $.prompt("Please Select Valid Forwarder Account");
            $("#billToParty").val(existBillToParty);
            return false;
        } else if (currentBillToParty == "S" && (parent.$("#expShipperNo").val() == null
                || parent.$("#expShipperNo").val() == "")) {
            $.prompt("Shipper Account Number is required");
            return false;
        }
        else if (currentBillToParty == "A" && (parent.$("#expAgentNo").val() == null
                || parent.$("#expAgentNo").val() == "")) {
            $.prompt("Agent Account Number is required");
            return false;
        }
        if (currentBillToParty === "T" && (parent.$("#expThirdPartyNo").val() === null
                || parent.$("#expThirdPartyNo").val() === "")) {
            $.prompt("Third Party Account Number is required");
            return false;
        }
        return true;
    }

    function filterNoFreightForwarderAc() {
        var forwardAcctNo = parent.$("#expForwarderNo").val();
        var flag;
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
                methodName: "getFreightForwardAcctStatus",
                param1: forwardAcctNo,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                flag = data;
            }
        });
        return flag;
    }
    function checkValidAmount() {
        if ($("#newAmount").val() === '' && $("#oldAmount").val() !== '') {
            $("#newAmount").val($("#oldAmount").val());
        }
    }

    function validateCostGLAccount(chargeId, originId) {
        var flag = true;
        var bookedHeader = parent.parent.$("#eciVoyage").val();
        var pickedHeader = parent.parent.$("#pickedOnVoyageNo").val();
        var voyScheduleNo = pickedHeader === '' ? bookedHeader : pickedHeader;
        var voyOriginId = "";
        if (voyScheduleNo !== '') {
            voyOriginId = parent.parent.getVoyageOriginId(voyScheduleNo);
        } else {
            voyOriginId = parent.parent.$("#portOfLoadingId").val();
        }
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO",
                methodName: "validateLclExportGlAccount",
                param1: chargeId,
                param2: originId,
                param3: parent.parent.$("#trmnum").val(),
                param4: voyOriginId,
                param5: "AR",
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data !== '') {
                    flag = false;
                    $.prompt(data);
                }
            }
        });
        return flag;
    }
</script>